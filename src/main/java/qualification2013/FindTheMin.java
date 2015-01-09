package qualification2013;

import java.io.*;
import java.util.*;

public class FindTheMin {
    // general part
    private static final String INPUTDIR = "src/main/resources";
    private static final String OUTPUTDIR = "target/output";
    private static final String ROUND = "qualification2013";

    private static final String SAMPLE = "C-sample.in";
    private static final String INPUT = "C-input.in";

    private Scanner scanner;
    private PrintWriter writer;

    public FindTheMin(InputStream is, OutputStream os) {
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

        FindTheMin problem = new FindTheMin(is, os);
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
    private Problem problem;

    /**
     * Solve the problem
     */
    public void solve() {
        // t <= 20
        int t = scanner.nextInt();

        for (int i = 1; i <= t; i++) {
            problem = new Problem();
            // 1 <= k <= 10^5
            // k < n <= 10^9
            // Integer.MAX_VALUE = 0x7fffffff > 10^9
            problem.n = scanner.nextInt();
            problem.k = scanner.nextInt();

            // 0 <= a, b, c <= 10^9
            // 1 <= r <= 10^9
            problem.a = scanner.nextInt();
            problem.b = scanner.nextInt();
            problem.c = scanner.nextInt();
            problem.r = scanner.nextInt();

            writer.printf("Case #%1$d: %2$d\n",i, problem.solve());
        }
    }

    private class Problem {
        private int n, k;
        private int a, b, c, r;

        private int[] m;
        private TreeSet<Integer> sortedSet;
        private Map<Integer, Integer> map;

        private void initM() {
            m = new int[k];
            sortedSet = new TreeSet<Integer>();
            map = new HashMap<Integer, Integer>(k);

            // m[0] = a
            m[0] = a;
            add(m[0]);

            // m[i] = (b * m[i - 1] + c) % r, 0 < i < k
            for(int i=1; i<k; i++) {
                long bl = b;
                bl = bl * m[i-1] + c;
                bl %= r;
                m[i] = (int)bl;
                add(m[i]);
            }
        }

        private int solve() {
            initM();

            for(int i=k; i<n; i++) {
                int j = 0;
                for(int value: sortedSet) {
                    if (j != value)
                        break;
                    j++;
                }
                remove(m[0]);
                System.arraycopy(m,1,m,0,k-1);
                m[k-1] = j;
                add(m[k-1]);
            }
            return m[k-1];
        }

        private void add(int value) {
            sortedSet.add(value);
            Integer count = map.get(value);
            if (count == null)
                count = 0;
            map.put(value, ++count);
        }

        private void remove(int value) {
            Integer count = map.get(value);
            if (count > 1)
                map.put(value, --count);
            else {
                map.remove(value);
                sortedSet.remove(value);
            }
        }
    }
}


