package round1_2014;

import java.io.*;
import java.util.*;

public class CardGame {
    // general part
    private static final String INPUTDIR = "src/main/resources";
    private static final String OUTPUTDIR = "target/output";
    private static final String ROUND = "round1_2014";

    private static final String SAMPLE = "A-sample.in";
    private static final String INPUT = "A-input.in";

    private Scanner scanner;
    private PrintWriter writer;

    public CardGame(InputStream is, OutputStream os) {
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

        CardGame problem = new CardGame(is, os);
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

    private static final int N_MAX = 10000;
    private static final int MOD = 1000000007;

    private long c[][];

    /**
     * Solve the problem
     */
    public void solve() {
        initC();

        // 1 <= T <= 25
        int t = scanner.nextInt();

        for (int i = 1; i <= t; i++) {
            //  N <= 10 000
            int n = scanner.nextInt();
            int k = scanner.nextInt();
            int a[] = new int[n];
            for(int j=0; j<n; j++)
                a[j] = scanner.nextInt();
            writer.printf("Case #%1$d: %2$d\n",i, solveInternal(n, k, a));
        }
    }

    private long solveInternal(int n, int k, int[] a) {
        Arrays.sort(a);
        long sum = 0;

        for(int i=0; i<=n-k; i++) {
            sum += a[n-i-1] * c(n-i-1, k-1);
            sum %= MOD;
        }
        return sum;
    }

    private void initC() {
        c = new long[N_MAX+1][N_MAX+1];

        for(int n=0; n<c.length; n++)
            c[n][0] = 1;
        for(int k=1; k<c.length; k++)
            c[0][k] = 0;

        for(int n=1; n<c.length; n++)
            for(int k=1; k<c.length; k++) {
                c[n][k] = c[n-1][k-1] + c[n-1][k];
                c[n][k] %= MOD;
            }
    }

    private long c(int n, int k) {
        return c[n][k];
    }
}


