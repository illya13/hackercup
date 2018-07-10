package qualification;

import java.io.*;
import java.util.Scanner;

public class Interception {
    // general part
    private static final String INPUTDIR = "src/main/resources";
    private static final String OUTPUTDIR = "target/output";
    private static final String ROUND = "qualification";

    private static final String SAMPLE = "B-sample.in";
    private static final String INPUT = "B-input.in";

    private Scanner scanner;
    private PrintWriter writer;

    public Interception(InputStream is, OutputStream os) {
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

        Interception problem = new Interception(is, os);
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
        // 1 ≤ T ≤ 1000
        int t = scanner.nextInt();
        scanner.nextLine();

        for (int i = 1; i <= t; i++) {
            // 0 ≤ P, X, Y ≤ 100
            int p = scanner.nextInt();
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            writer.printf("Case #%1$d: %2$s\n", i, solve(p, x, y));
        }
    }

    private String solve(int p, int x, int y) {
        // int pp = 45;
        int a = 50; int b = 50; int r = 50;
        boolean[][] points = new boolean[101][101];

/*
        for (p=0; p<=pp; p++) {
            double radians = (-2 * Math.PI * p / 100) + Math.PI / 2;
            for (r = 0; r <= 50; r++) {
                int pieX = a + (int) Math.round(r * Math.cos(radians));
                int pieY = b + (int) Math.round(r * Math.sin(radians));
                for (int j = 100; j >= pieY; j--) {
                    for (int i = 50; i <= pieX; i++) {
                        points[i][j] |= circle(a, b, 50, i, j);
                    }
                }
            }
        }
*/

        for (int j=100; j>=0; j--) {
            for (int i=0; i<=100; i++) {
                points[i][j] |= circle(a, b, 50, i, j);
            }
        }

        for (int j=100; j>=0; j--) {
            for (int i=0; i<=100; i++) {
                System.out.print((points[i][j] ? "*":"."));
                if (points[i][j] && (j < 10) && (i < 45)) {
                    double dy = j - 50;
                    double dx = i - 50;
                    double rad = Math.PI/2 - Math.atan(dy/dx);
                    double d = Math.toDegrees(rad);
                    double k = d;
                }
            }
            System.out.println();
        }
        return "white";
    }

    private boolean circle(int a, int b, int r, int x, int y) {
        return (x-a)*(x-a) + (y-b)*(y-b) <= r*r;
    }
}

