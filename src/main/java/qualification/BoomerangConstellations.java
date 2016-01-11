package qualification;

import java.io.*;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.Callable;

public class BoomerangConstellations {
    // general part
    private static final String INPUTDIR = "src/main/resources";
    private static final String OUTPUTDIR = "target/output";
    private static final String ROUND = "qualification";

    private static final String SAMPLE = "A-sample.in";
    private static final String INPUT = "A-input.in";

    private Scanner scanner;
    private PrintWriter writer;

    public BoomerangConstellations(InputStream is, OutputStream os) {
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

        BoomerangConstellations problem = new BoomerangConstellations(is, os);
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
        // 1 ≤ T ≤ 50
        int t = scanner.nextInt();
        scanner.nextLine();

        for (int i = 1; i <= t; i++) {
            // 0 <= N <= 2000
            int n = scanner.nextInt();
            Star[] stars = new Star[n];
            for (int j = 0; j < n; j++) {
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                stars[j] = new Star(x, y);
            }
            writer.printf("Case #%1$d: %2$d\n", i, solve(stars));
        }
    }

    private long solve(Star[] stars) {
        // max value: 2000 * (max c_n2)
        long count = 0;

        // for each star
        for (int a = 0; a < stars.length; a++) {
            // collect map:
            //      segment length -> count
            Map<Long, Integer> lenghtMap = new HashMap<>();

            for (int b = 0; b < stars.length; b++) {
                if (a == b)
                    continue;

                long distance = stars[a].getSquareDistanceTo(stars[b]);
                Integer segments = lenghtMap.get(distance);
                if (segments == null) {
                    segments = 0;
                }
                lenghtMap.put(distance, ++segments);
            }

            for (Map.Entry<Long, Integer> entry: lenghtMap.entrySet()) {
                int segments = entry.getValue();
                count += c_n2(segments);
            }
        }
        return count;
    }

    // max n = 2000
    // mas c_n2 = 2000 * 1000
    private long c_n2(int n) {
        return n * (n-1) / 2;
    }

    private class Star {
        private int x, y;

        public Star(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "(" + x +
                    "," + y + ')';
        }

        public long getSquareDistanceTo(Star s) {
            return (x-s.x)*(x-s.x) + (y-s.y)*(y-s.y);
        }
    }
}

