package round1a2011;

import java.io.*;
import java.math.BigInteger;
import java.util.Scanner;

public class WineTasting {
    // general part
    private static final String INPUTDIR = "src/main/resources";
    private static final String OUTPUTDIR = "target/output";
    private static final String ROUND = "round1a2011";

    private static final String SAMPLE = "A-sample.in";
    private static final String INPUT = "A-input.in";

    private Scanner scanner;
    private PrintWriter writer;

    public WineTasting(InputStream is, OutputStream os) {
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

        WineTasting problem = new WineTasting(is, os);
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
            //runTest(INPUT, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // problem part

    // see Integer.MAX_VALUE = 2147483647
    private BigInteger[] factorial;

    private static int MODULO = 1051962371;
    private static BigInteger BI_MODULO = new BigInteger(((Integer)MODULO).toString());
    private static int MAX_G = 100;


    /**
     * Solve the problem
     */
    public void solve() {
        initDp_();
        initFCache();

        BigInteger max = aNK(100, 50);

        // 20
        int n = scanner.nextInt();

        for (int i = 1; i <= n; i++) {
            // 1 ? G ? 100
            // 1 ? C ? G
            int g = scanner.nextInt();
            int c = scanner.nextInt();

            writer.printf("%1$d\n", solve(g, c));
        }
    }

    private int solve(int g, int c) {
        BigInteger result = BigInteger.ZERO;
        for (int i=g; i>=c; i--)
            result = result.add(aNK(i, 1));
        return (int)result.mod(BI_MODULO).longValue();
    }


    private long[][] f;


    /**
     * f(g, c) = f(g-1, c-1) + (g-1)*f(g-1, c)
     */
    private void initFCache() {
        f = new long[5][5];

        f[1][1] = 1;

        f[2][1] = 1;
        f[2][2] = 1;

        for (int g = 3; g < 5; g++) {
            f[g][g] = 1;
            f[g][g-1] = 1;
            for (int c = 1; c < g; c++) {
                f[g][c] = f[g-1][c] + f[g-1][c-1];
            }

        }
    }





    private void initDp_() {
        factorial = new BigInteger[MAX_G+1];
        factorial[0] = new BigInteger("1");
        for (int i=1; i<factorial.length; i++)
            factorial[i] = factorial[i-1].multiply(
                    new BigInteger(((Integer)i).toString()));
    }

    private BigInteger cNK(int n, int k) {
        return factorial[n].divide(factorial[k]).divide(factorial[n-k]);
    }

    private BigInteger aNK(int n, int k) {
        return factorial[n].divide(factorial[n - k]);
    }
}
