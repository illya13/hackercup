package qualification2017;

import java.io.*;
import java.util.*;

public class LaserMaze {
    // general part
    private static final String INPUTDIR = "src/main/resources";
    private static final String OUTPUTDIR = "target/output";
    private static final String ROUND = "qualification";

    private static final String SAMPLE = "C-sample.in";
    private static final String INPUT = "C-input.in";

    private Scanner scanner;
    private PrintWriter writer;

    public LaserMaze(InputStream is, OutputStream os) {
        scanner = new Scanner(is);
        writer = new PrintWriter(os);
    }

    public void close() {
        scanner.close();
        writer.flush();
    }

    private static void runTest(String fileName, boolean isConsole) throws Exception {
        InputStream is = initInputStream(fileName);
        OutputStream os = initOutputStream(fileName, isConsole);

        LaserMaze problem = new LaserMaze(is, os);
        problem.solve();
        problem.close();

        doneStreams(isConsole, is, os);
    }

    private static InputStream initInputStream(String fileName) throws FileNotFoundException {
        File inputDir = new File(INPUTDIR + File.separator + ROUND);
        File inputFile = new File(inputDir, fileName);
        InputStream is = new FileInputStream(inputFile);
        return is;
    }

    private static OutputStream initOutputStream(String fileName, boolean isConsole) throws FileNotFoundException {
        OutputStream os = System.out;
        if (isConsole) {
            System.out.println(fileName);
            System.out.println("          ---] cut [---");
        } else {
            File outputDir = new File(OUTPUTDIR + File.separator + ROUND);
            outputDir.mkdirs();

            File outputFile = new File(outputDir, fileName.replace(".in", ".out"));
            os = new PrintStream(new FileOutputStream(outputFile));
        }
        return os;
    }

    private static void doneStreams(boolean isConsole, InputStream is, OutputStream os) throws IOException {
        is.close();
        if (isConsole) {
            System.out.println("          ---] cut [---");
            System.out.println("");
        } else {
            os.close();
        }
    }

    public static void main(String[] args) {
        try {
            runTest(SAMPLE, true);
            runTest(INPUT, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // problem part

    /**
     * Solve the problem
     */
    public void solve() {
        // 1 <= T <= 100
        int t = scanner.nextInt();

        for (int i = 1; i <= t; i++) {

            Maze maze = new Maze(scanner);
            writer.printf("Case #%1$d: %2$s\n", i, maze.solve());
        }
    }

    private static class Maze {
        // 1 <= M, N <= 100
        private int m, n;
        private char[][] maze;

        private Point start;
        private Point goal;
        private Integer minPath = Integer.MAX_VALUE;

        private Set<Point> walls;
        private Turrets turrets = new Turrets();

        public Maze(Scanner scanner) {
            m = scanner.nextInt();
            n = scanner.nextInt();

            maze = new char[m][n];
            walls = new HashSet<>(m * n);

            initCashes();

            // ignore end of line
            scanner.nextLine();
            for (int i = 0; i < m; i++) {
                String line = scanner.nextLine();
                for (int j = 0; j < n; j++) {
                    fillMaze(i, j, line.charAt(j));
                }
            }
        }

        // caches to avoid unnecessary instances of the same logical entities
        // also Sets and Maps work with object references
        private Point[][] pointCache;
        private BFSNode[][][] bfsNodeCache;

        private void initCashes() {
            pointCache = new Point[m][n];
            for (int i=0; i<m; i++)
                for (int j=0; j<n; j++)
                    pointCache[i][j] = new Point(i, j);

            bfsNodeCache = new BFSNode[m][n][4];
            for (int i=0; i<m; i++)
                for (int j=0; j<n; j++)
                    for (TurretRotation turretRotation: TurretRotation.values())
                        bfsNodeCache[i][j][turretRotation.ordinal()] = new BFSNode(pointCache[i][j], turretRotation);
        }

        private Point getInstanceOfPoint(int x, int y) {
            return pointCache[x][y];
        }

        private BFSNode getInstanceOfBFSNode(Point point, TurretRotation turretRotation) {
            return bfsNodeCache[point.x][point.y][turretRotation.ordinal()];
        }

        private void fillMaze(int i, int j, char c) {
            maze[i][j] = c;
            switch (c) {
                case 'S':
                    start = getInstanceOfPoint(i, j);
                    break;

                case 'G':
                    goal = getInstanceOfPoint(i, j);
                    break;

                case '#':
                    walls.add(getInstanceOfPoint(i, j));
                    break;

                case '<':
                    walls.add(getInstanceOfPoint(i, j));
                    turrets.add(new Turret(getInstanceOfPoint(i, j), TurretDirection.LEFT));
                    break;

                case '>':
                    walls.add(getInstanceOfPoint(i, j));
                    turrets.add(new Turret(getInstanceOfPoint(i, j), TurretDirection.RIGHT));
                    break;

                case '^':
                    walls.add(getInstanceOfPoint(i, j));
                    turrets.add(new Turret(getInstanceOfPoint(i, j), TurretDirection.UP));
                    break;

                case 'v':
                    walls.add(getInstanceOfPoint(i, j));
                    turrets.add(new Turret(getInstanceOfPoint(i, j), TurretDirection.DOWN));
                    break;
            }
        }

        public String solve() {
            turrets.traceLasers();
            bfs(start);
            return (minPath == Integer.MAX_VALUE) ? "impossible" : minPath.toString();
        }

        private void bfs(Point start) {
            Map<BFSNode, Integer> visited = new HashMap<>(m * n);
            BFSNode s = getInstanceOfBFSNode(start, TurretRotation.ONE);
            visited.put(s, 0);

            Queue<BFSNode> queue = new LinkedList<>();
            queue.add(s);

            bfs(visited, queue);
        }

        private void bfs(Map<BFSNode, Integer> visited, Queue<BFSNode> queue) {
            while (!queue.isEmpty()) {
                BFSNode current = queue.poll();

                if (current.point.equals(goal)) {
                    int path = visited.get(current);
                    if (minPath > path)
                        minPath = path;
                    // System.out.println(current + "[" + path + "]");
                }
                else {
                    // System.out.println(current);
                }

                for (BFSNode s : current.point.adj(current.turretRotation)) {
                    if (!visited.containsKey(s)) {
                        queue.add(s);
                        visited.put(s, visited.get(current) + 1);
                    }
                }
            }
        }

        // bfs node
        private class BFSNode {
            private Point point;
            private TurretRotation turretRotation;

            public BFSNode(Point point, TurretRotation turretRotation) {
                this.point = point;
                this.turretRotation = turretRotation;
            }

            @Override
            public String toString() {
                return point.toString() + ", " + turretRotation;
            }
        }

        // point in maze
        private class Point {
            int x, y;

            private Point(int x, int y) {
                this.x = x;
                this.y = y;
            }

            public Iterable<BFSNode> adj(TurretRotation turretRotation) {
                TurretRotation next = turretRotation.next();
                List<BFSNode> result = new LinkedList<>();

                if (x - 1 >= 0)
                    checkAndAdd(next, result, getInstanceOfPoint(x - 1, y));
                if (y - 1 >= 0)
                    checkAndAdd(next, result, getInstanceOfPoint(x, y - 1));
                if (x + 1 < m)
                    checkAndAdd(next, result, getInstanceOfPoint(x + 1, y));
                if (y + 1 < n)
                    checkAndAdd(next, result, getInstanceOfPoint(x, y + 1));
                return result;
            }

            private void checkAndAdd(TurretRotation next, List<BFSNode> result, Point p) {
                if ((!walls.contains(p)) && (!turrets.fire(p, next)))
                    result.add(getInstanceOfBFSNode(p, next));
            }

            @Override
            public String toString() {
                return maze[x][y] + " {" +
                        "x=" + x +
                        ", y=" + y +
                        '}';
            }
        }

        // turret direction
        private enum TurretDirection {
            UP, RIGHT, DOWN, LEFT;

            public TurretDirection next() {
                switch (this) {
                    case UP:
                        return RIGHT;
                    case RIGHT:
                        return DOWN;
                    case DOWN:
                        return LEFT;
                    case LEFT:
                        return UP;
                }
                throw new IllegalStateException();
            }

            @Override
            public String toString() {
                switch (this) {
                    case UP:
                        return "^";
                    case RIGHT:
                        return ">";
                    case DOWN:
                        return "v";
                    case LEFT:
                        return "<";
                }
                throw new IllegalStateException();
            }
        }

        // turret
        private class Turret {
            private Point point;
            private TurretDirection turretDirection;

            public Turret(Point point, TurretDirection turretDirection) {
                this.point = point;
                this.turretDirection = turretDirection;
            }

            public Turret next() {
                return new Turret(point, turretDirection.next());
            }

            public Set<Point> fired() {
                Set<Point> result = new HashSet<>();
                switch (turretDirection) {
                    case UP:
                        for (int x = point.x-1; x >= 0; x--) {
                            if (walls.contains(getInstanceOfPoint(x, point.y)))
                                break;
                            result.add(getInstanceOfPoint(x, point.y));
                        }
                        break;

                    case RIGHT:
                        for (int y = point.y+1; y < n; y++) {
                            if (walls.contains(getInstanceOfPoint(point.x, y)))
                                break;
                            result.add(getInstanceOfPoint(point.x, y));
                        }
                        break;

                    case DOWN:
                        for (int x = point.x+1; x < m; x++) {
                            if (walls.contains(getInstanceOfPoint(x, point.y)))
                                break;
                            result.add(getInstanceOfPoint(x, point.y));
                        }
                        break;

                    case LEFT:
                        for (int y = point.y-1; y >= 0; y--) {
                            if (walls.contains(getInstanceOfPoint(point.x, y)))
                                break;
                            result.add(getInstanceOfPoint(point.x, y));
                        }
                        break;
                }
                return result;
            }

            @Override
            public String toString() {
                return turretDirection.toString() + " {" +
                        "x=" + point.x +
                        ", y=" + point.y +
                        '}';
            }
        }

        // turret rotation step
        private enum TurretRotation {
            ONE, TWO, THREE, FOUR;

            public TurretRotation next() {
                switch (this) {
                    case ONE:
                        return TWO;
                    case TWO:
                        return THREE;
                    case THREE:
                        return FOUR;
                    case FOUR:
                        return ONE;
                }
                throw new IllegalStateException();
            }
        }

        // turrets in all possible rotation steps
        private class Turrets {
            private Map<TurretRotation, List<Turret>> turretRotationMap;
            private Map<TurretRotation, Set<Point>> turretFireMap;

            public Turrets() {
                turretRotationMap = new HashMap<>(4);

                turretRotationMap.put(TurretRotation.ONE, new LinkedList<>());
                turretRotationMap.put(TurretRotation.TWO, new LinkedList<>());
                turretRotationMap.put(TurretRotation.THREE, new LinkedList<>());
                turretRotationMap.put(TurretRotation.FOUR, new LinkedList<>());
            }

            public void add(Turret turret) {
                turretRotationMap.get(TurretRotation.ONE).add(turret);
                turretRotationMap.get(TurretRotation.TWO).add(turret.next());
                turretRotationMap.get(TurretRotation.THREE).add(turret.next().next());
                turretRotationMap.get(TurretRotation.FOUR).add(turret.next().next().next());
            }

            public void traceLasers() {
                turretFireMap = new HashMap<>(4);

                turretFireMap.put(TurretRotation.ONE, trace(turretRotationMap.get(TurretRotation.ONE)));
                turretFireMap.put(TurretRotation.TWO, trace(turretRotationMap.get(TurretRotation.TWO)));
                turretFireMap.put(TurretRotation.THREE, trace(turretRotationMap.get(TurretRotation.THREE)));
                turretFireMap.put(TurretRotation.FOUR, trace(turretRotationMap.get(TurretRotation.FOUR)));
            }

            private Set<Point> trace(List<Turret> turrets) {
                Set<Point> result = new HashSet<>();
                for (Turret t: turrets)
                    result.addAll(t.fired());
                return result;
            }

            public boolean fire(Point point, TurretRotation turretRotation) {
                return turretFireMap.get(turretRotation).contains(point);
            }
        }
    }
}
