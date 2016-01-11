package qualification;

import java.io.*;
import java.util.*;

public class HighSecurity {
    // general part
    private static final String INPUTDIR = "src/main/resources";
    private static final String OUTPUTDIR = "target/output";
    private static final String ROUND = "qualification";

    private static final String SAMPLE = "B-sample.in";
    private static final String INPUT = "B-input.in";

    private Scanner scanner;
    private PrintWriter writer;

    public HighSecurity(InputStream is, OutputStream os) {
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

        HighSecurity problem = new HighSecurity(is, os);
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

    private static final char EMPTY = '.';
    private static final char BUILDING = 'X';
    private static final char GUARD = 'G';

    private Map<Problem, Integer> globalProblemCache = new HashMap<>();

    /**
     * Solve the problem
     */
    public void solve() {
        // 1 ≤ T ≤ 200
        int t = scanner.nextInt();

        for (int i = 1; i <= t; i++) {
            // 1 ≤ N ≤ 1000
            int n = scanner.nextInt();
            char[][] grid = new char[2][n];

            scanner.nextLine();
            String row1 = scanner.next();
            String row2 = scanner.next();
            for (int j = 0; j < n; j++) {
                grid[0][j] = row1.charAt(j);
                grid[1][j] = row2.charAt(j);
            }
            writer.printf("Case #%1$d: %2$d\n", i, solve(grid, n));
        }
    }

    private int solve(char[][] grid, int n) {
        System.out.println();
        System.out.println();
        for (int i = 0; i < n; i++) {
            System.out.print(grid[0][i]);
        }
        System.out.println();
        for (int i = 0; i < n; i++) {
            System.out.print(grid[1][i]);
        }
        System.out.println();
        System.out.println(" => ");

        int total = 0;

        char[][] copy = reduce(grid, BUILDING, 1);
        List<Problem> problems = split(copy, copy[0].length);
        for (Problem problem: problems) {
            total += solveProblems(problem);
        }
        return total;
    }

    private List<Problem> split(char[][] grid, int n) {
        List<Problem> problems = new LinkedList<>();
        int lastSplit = -1;
        for (int i = 0; i < n; i++) {
            if ((grid[0][i] == BUILDING) && (grid[1][i] == BUILDING)) {
                problems.add(getProblemFromLastSplit(grid, n, i, lastSplit));
                lastSplit = i;
            }
        }
        problems.add(getProblemFromLastSplit(grid, n, n, lastSplit));
        return problems;
    }

    private Problem getProblemFromLastSplit(char[][] grid, int n, int i, int lastSplit) {
        if (lastSplit == -1) {
            char[][] copy = new char[2][];
            copy[0] = Arrays.copyOf(grid[0], i);
            copy[1] = Arrays.copyOf(grid[1], i);
            return new Problem(copy);
        } else {
            char[][] copy = new char[2][];
            copy[0] = Arrays.copyOfRange(grid[0], lastSplit+1, i);
            copy[1] = Arrays.copyOfRange(grid[1], lastSplit+1, i);
            return new Problem(copy);
        }
    }

    private int solveProblems(Problem problem) {

        for (int i = 0; i < problem.n(); i++) {
            System.out.print(problem.grid[0][i]);
        }
        System.out.println();
        for (int i = 0; i < problem.n(); i++) {
            System.out.print(problem.grid[1][i]);
        }
        System.out.println();

        if (globalProblemCache.containsKey(problem))
            return globalProblemCache.get(problem);

        Set<Guard> guards = new HashSet<>();
        Set<Guard> candidates = getPossibleGuards(problem.grid, problem.n());

        Map<Integer,Integer> cache = new HashMap<>();
        int result = addGuardBrute(problem.grid, problem.n(), guards, candidates, cache);

        globalProblemCache.put(problem, result);
        return result;
    }


    private int addGuardBrute(char[][] grid, int n, Set<Guard> guards, Set<Guard> candidates, Map<Integer,Integer> cache) {
        if (cache.containsKey(guards.hashCode()))
            return cache.get(guards.hashCode());

        for (Guard guard: candidates) {
            Set<Guard> guardsClone = new HashSet<>(guards.size()+1);
            guardsClone.addAll(guards);
            guardsClone.add(guard);
            if (applyGuardAndTrace(grid, n, guardsClone)) {
                cache.put(guards.hashCode(), guards.size()+1);
                return guards.size()+1;
            }
        }

        int min = Integer.MAX_VALUE;
        for (Guard guard: candidates) {
            Set<Guard> guardsClone = new HashSet<>(guards.size()+1);
            guardsClone.addAll(guards);
            guardsClone.add(guard);

            Set<Guard> candidatesClone = new HashSet<>(candidates.size());
            candidatesClone.addAll(candidates);
            candidatesClone.remove(guard);

            int current = addGuardBrute(grid, n, guardsClone, candidatesClone, cache);
            if (min > current) {
                min = current;
            }
        }
        cache.put(guards.hashCode(), min);
        return min;
    }

    private Set<Guard> getPossibleGuards(char[][] grid, int n) {
        Set<Guard> candidates = new HashSet<>();
        for (int i = 0; i < n; i++) {
            getPossibleGuardsCell(grid, candidates, 0, i);
            getPossibleGuardsCell(grid, candidates, 1, i);
        }
        return candidates;
    }

    private void getPossibleGuardsCell(char[][] grid, Set<Guard> candidates, int row, int col) {
        if (grid[row][col] == EMPTY) {
            Guard guard = new Guard(row, col);
            candidates.add(guard);
        }
    }

    private boolean applyGuardAndTrace(char[][] grid, int n, Set<Guard> guards) {
        char[][] copy = new char[2][];
        copy[0] = Arrays.copyOf(grid[0], n);
        copy[1] = Arrays.copyOf(grid[1], n);

        for (Guard g: guards) {
            copy[g.row][g.col] = GUARD;
        }
        return trace(copy, n);
    }

    private boolean trace(char[][] grid, int n) {
        boolean[][] visible = new boolean[2][n];
        Arrays.fill(visible[0], false);
        Arrays.fill(visible[1], false);

        traceLeft(grid, n, visible);
        traceRight(grid, n, visible);

        // check trace
        for (int i = 0; i < n; i++) {
            if (!visible[0][i])
                return false;
            if (!visible[1][i])
                return false;
        }
        return true;
    }

    private void traceLeft(char[][] grid, int n, boolean[][] visible) {
        boolean guardRow1 = false;
        boolean guardRow2 = false;
        for (int i = 0; i < n; i++) {
            guardRow1 = traceCell(grid, visible, 0, i, guardRow1);
            guardRow2 = traceCell(grid, visible, 1, i, guardRow2);
        }
    }

    private void traceRight(char[][] grid, int n, boolean[][] visible) {
        boolean guardRow1 = false;
        boolean guardRow2 = false;
        for (int i = n-1; i >= 0; i--) {
            guardRow1 = traceCell(grid, visible, 0, i, guardRow1);
            guardRow2 = traceCell(grid, visible, 1, i, guardRow2);
        }
    }

    private boolean traceCell(char[][] grid, boolean[][] visible, int col, int row, boolean guard) {
        visible[col][row] |= visibleCell(grid, col, row, guard);
        return guardCell(grid, col, row, guard);
    }

    private boolean visibleCell(char[][] grid, int row, int col, boolean guard) {
        int arow = anotherRow(row);
        switch(grid[row][col]) {
            case EMPTY:
                return guard || (grid[arow][col] == GUARD);
            default:
                return true;
        }
    }

    private boolean guardCell(char[][] grid, int row, int col, boolean guard) {
        switch(grid[row][col]) {
            case BUILDING:
                return false;
            case GUARD:
                return true;
            default:
                return guard;
        }
    }

    private int anotherRow(int row) {
        if (row == 0)
            return 1;
        else
            return 0;
    }

    private class Guard {
        private int row, col;

        public Guard(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Guard)) return false;

            Guard guard = (Guard) o;

            if (row != guard.row) return false;
            return col == guard.col;

        }

        @Override
        public int hashCode() {
            // (0,0) != 0
            // 10001 > 1000
            return 31 + row * 10001 + col;
        }

        @Override
        public String toString() {
            return row + "," + col;
        }
    }

    private class Problem {
        private char[][] grid;

        public Problem(char[][] grid) {
            this.grid = reduce(grid, EMPTY, 2);
        }

        public int n() {
            return grid[0].length;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Problem)) return false;

            Problem problem = (Problem) o;

            return Arrays.deepEquals(grid, problem.grid);

        }

        @Override
        public int hashCode() {
            return Arrays.deepHashCode(grid);
        }
    }

    private char[][] reduce(char[][] grid, char pattern, int size) {
        int n = grid[0].length;

        char[][] copy = new char[2][];
        copy[0] = Arrays.copyOf(grid[0], n);
        copy[1] = Arrays.copyOf(grid[1], n);

        int current = 0;
        for (int i = 0; i < n; i++) {
            if ((copy[0][i] == pattern) && (copy[1][i] == pattern)) {
                current++;
            } else {
                if (current > size) {
                    n = copyAndReduceSize(n, copy, current, i);
                    i = 2;
                }
                current = 0;
            }
        }
        if (current > size) {
            n = copyAndReduceSize(n, copy, current, n);
        }

        char[][] result = new char[2][];
        result[0] = Arrays.copyOf(copy[0], n);
        result[1] = Arrays.copyOf(copy[1], n);
        return result;
    }

    private int copyAndReduceSize(int n, char[][] copy, int current, int i) {
        for (int j=i; j<n; j++) {
            copy[0][j-current+1] = copy[0][j];
            copy[1][j-current+1] = copy[1][j];
        }
        return n-current+1;
    }

}
