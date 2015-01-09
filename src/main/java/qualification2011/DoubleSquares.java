package qualification2011;

import java.io.*;
import java.util.*;

public class DoubleSquares {
    // general part
    private static final String INPUTDIR = "src/main/resources";
    private static final String OUTPUTDIR = "target/output";
    private static final String ROUND = "qualification2011";

    private static final String SAMPLE = "A-sample.in";
    private static final String INPUT = "A-input.in";

    private Scanner scanner;
    private PrintWriter writer;

    public DoubleSquares(InputStream is, OutputStream os) {
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

        DoubleSquares problem = new DoubleSquares(is, os);
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
        //initDp();

        // 1 ? N ? 100
        int n = scanner.nextInt();

        for (int i = 1; i <= n; i++) {
            // 0 ? X ? 2147483647
            // see Integer.MAX_VALUE = 0x7fffffff = 2147483647
            int x = scanner.nextInt();
            writer.printf("%1$d\n", solve(x));
        }
    }

    private int solve(int x) {
        int cnt = 0;
        int min = (int)Math.sqrt(x/2);
        int max = (int)Math.sqrt(x);

        next_i:
        for (int i=min; i<=max; i++) {
            for (int j=0; j<=i; j++) {
                long value = i*i+j*j;

                if (value == x)
                    cnt++;
            }
        }
        return cnt;
    }

    public void initDp() {
        // 46340^2 = 2147395600
        int size = (int)Math.sqrt(Integer.MAX_VALUE);
        size = 5000;

        SortedMap<Integer, Integer> dp = new TreeMap<Integer, Integer>();

        for (int i=0; i<size; i++) {
            for (int j=0; j<=i; j++) {
                int key = i*i + j*j;
                int value = 0;
                if (dp.containsKey(key))
                    value = dp.get(key);
                value ++;
                dp.put(key, value);

                if(key == 325)
                    writer.printf("%1$d, %2$d\n", i, j);
            }
        }

        Iterator<Integer> iterator = dp.keySet().iterator();
        while (iterator.hasNext()) {
            int key = iterator.next();
            int value = 0;
            if (dp.containsKey(key))
                value = dp.get(key);
            if (value > 1)
                writer.printf("%1$d->%2$d\n", key, value);
        }
    }
}
