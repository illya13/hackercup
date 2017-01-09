package qualification2016;

import java.io.*;
import java.util.*;

public class ThePriceIsCorrect {
    // general part
    private static final String INPUTDIR = "src/main/resources";
    private static final String OUTPUTDIR = "target/output";
    private static final String ROUND = "qualification";

    private static final String SAMPLE = "C-sample.in";
    private static final String INPUT = "C-input.in";

    private Scanner scanner;
    private PrintWriter writer;

    public ThePriceIsCorrect(InputStream is, OutputStream os) {
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

        ThePriceIsCorrect problem = new ThePriceIsCorrect(is, os);
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
        // 1 <= T <= 40
        int t = scanner.nextInt();

        for (int i = 1; i <= t; i++) {
            // 1 ≤ N ≤ 100,000
            int n = scanner.nextInt();

            // 1 ≤ P ≤ 1,000,000,000
            // Integer.MAX_VALUE == ‭2,147,483,647‬
            int p = scanner.nextInt();

            int[] b = new int[n];
            for (int j = 0; j < n; j++) {
                b[j] = scanner.nextInt();
            }
            writer.printf("Case #%1$d: %2$d\n", i, solve(b, n, p));
        }
    }

    private long solve(int[] b, int n, int p) {
        // max value: n*(n/2)
        long count = 0;

        for (int i=0; i<n; i++) {
            if (b[i] > p) {
                continue;
            }

            int[] sum = new int[n];
            sum[i] = b[i];

            count += n-i;
            for (int j=i+1; j<n; j++) {
                sum[j] = sum[j-1] + b[j];
                if (sum[j] > p) {
                    count -= n-j;
                    break;
                }
            }
        }
        return count;
    }
}
