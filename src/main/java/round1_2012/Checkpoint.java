package round1_2012;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Checkpoint {
    // general part
    private static final String INPUTDIR = "src/main/resources";
    private static final String OUTPUTDIR = "target/output";
    private static final String ROUND = "round1_2012";

    private static final String SAMPLE = "A-sample.in";
    private static final String INPUT = "A-input.in";

    private Scanner scanner;
    private PrintWriter writer;

    public Checkpoint(InputStream is, OutputStream os) {
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

        Checkpoint problem = new Checkpoint(is, os);
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
    private int[][] cDp;
    protected final int MAGIC = 4500;
    protected final int MAX_S = 10000000;

    private Map<Integer, Integer> sDp;

    /**
     * Solve the problem
     */
    public void solve() {
        initDp();

        // 5 <= R <= 20
        int r = scanner.nextInt();

        for (int i = 1; i <= r; i++) {
            //1 <= S <= 10000000
            int s = scanner.nextInt();
            writer.printf("Case #%1$d: %2$d\n", i, solve(s));
        }
    }

    private long solve(int s) {
        long min = Long.MAX_VALUE;

        // i*i<=s
        // because if we checked A*B == S
        // no sense test B*A == S
        for (int i=1; i*i<=s; i++) {
            if (s % i == 0) {
                if (min > s(i)+s(s/i))
                    min = s(i)+s(s/i);
            }
        }
        return min;
    }

    protected void initDp() {
        sDp = new HashMap<Integer, Integer>(MAGIC);

        cDp = new int[MAGIC][MAGIC];
        for (int i=0; i<MAGIC; i++) {
            Arrays.fill(cDp[i], MAX_S+1);
            cDp[0][i] = 1;
            cDp[i][0] = 1;
        }

        for (int i=1; i<MAGIC; i++) {
            loop:
            for (int j=1; j<MAGIC; j++) {
                cDp[i][j] = cDp[i-1][j] + cDp[i][j-1];
                if (cDp[i][j] > MAX_S) {
                    cDp[i][j] = MAX_S+1;
                    break loop;
                }

                if (sDp.containsKey(cDp[i][j])) {
                    if (sDp.get(cDp[i][j])> i+j)
                        sDp.put(cDp[i][j], i+j);
                } else {
                    sDp.put(cDp[i][j], i+j);
                }
            }
        }
    }

    protected int c(int n, int k) {
        if ((n == 0) || (k == 0))
            return 1;
        if (n == 1)
            return k+1;
        if (k == 1)
            return n+1;

        if ((n >= MAGIC) || (k >= MAGIC))
            return MAX_S+1;

        return cDp[n][k];
    }

    protected long s(int s) {
        if (sDp.containsKey(s))
            return sDp.get(s);
        return s;
    }
}
