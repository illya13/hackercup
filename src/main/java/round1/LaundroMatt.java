package round1;

import java.io.*;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Scanner;

public class LaundroMatt {
    public static final String INPUTDIR = "src/main/resources";
    public static final String OUTPUTDIR = "target/output";
    public static final String ROUND = "round1";

    /*
     * Input definition
     */

    private enum Input {
        SAMPLE("B-sample.in", true),
        INPUT("B-input.in", false);

        private String fileName;
        private boolean useConsole;

        Input(String fileName, boolean useConsole) {
            this.fileName = fileName;
            this.useConsole = useConsole;
        }

        public String getFileName() {
            return fileName;
        }

        public boolean useConsole() {
            return useConsole;
        }
    }
    
    /*
     * IO Utils interface
     */

    private interface IOUtils {
        public void init(Input input) throws Exception;
        public void close() throws Exception;

        public Scanner scanner();
        public PrintWriter writer();
    }

    /*
     * IO Utils implementation
     */

    private static class IOUtilsImpl implements IOUtils {
        private boolean useConsole;
        private InputStream is;
        private OutputStream os;

        private Scanner scanner;
        private PrintWriter writer;

        @Override
        public void init(Input input) throws Exception {
            this.useConsole = input.useConsole();
            is = initInputStream(input.getFileName());
            os = initOutputStream(input.getFileName(), useConsole);

            scanner = new Scanner(is);
            writer = new PrintWriter(os);
        }

        @Override
        public void close() throws Exception {
            scanner.close();
            writer.flush();

            doneStreams();
        }

        @Override
        public Scanner scanner() {
            return scanner;
        }

        @Override
        public PrintWriter writer() {
            return writer;
        }

        private InputStream initInputStream(String fileName) throws FileNotFoundException {
            File inputDir = new File(INPUTDIR + File.separator + ROUND);
            File inputFile = new File(inputDir, fileName);
            return new FileInputStream(inputFile);
        }

        private OutputStream initOutputStream(String fileName, boolean useConsole) throws FileNotFoundException {
            OutputStream os = System.out;
            if (useConsole) {
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

        private void doneStreams() throws IOException {
            is.close();
            if (useConsole) {
                System.out.println("          ---] cut [---");
                System.out.println("");
            } else {
                os.close();
            }
        }
    }

    /*
     * Common part
     */

    public static void main(String[] args) {
        try {
            runTest(Input.SAMPLE);
            runTest(Input.INPUT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void runTest(Input input) throws Exception {
        LaundroMatt problem = new LaundroMatt();
        IOUtils ioUtils = new IOUtilsImpl();

        ioUtils.init(input);
        problem.solve(ioUtils);
        ioUtils.close();
    }

    /*
     * Problem part
     */

    public void solve(IOUtils ioUtils) {
        // 1 ≤ T ≤ 50

        int t = ioUtils.scanner().nextInt();

        for (int i = 1; i <= t; i++) {
            // L, N, M, and D

            // L - loads
            // N - washing machines
            // M - driers
            // D - minutes to dry

            // 1 ≤ L ≤ 1,000,000
            // 1 ≤ N ≤ 100,000
            // 1 ≤ M ≤ 1,000,000,000
            // 1 ≤ D ≤ 1,000,000,000

            // Integer.MAX_VALUE = ‭2,147,483,647‬
            int l = ioUtils.scanner().nextInt();
            int n = ioUtils.scanner().nextInt();
            int m = ioUtils.scanner().nextInt();
            int d = ioUtils.scanner().nextInt();

            int[] w = new int[n];
            for (int j=0; j<n; j++) {
                // 1 ≤ Wi ≤ 1,000,000,000
                w[j] = ioUtils.scanner().nextInt();
            }

            ioUtils.writer().printf("Case #%1$d: %2$d\n", i, greedy(l, n, m, d, w));
        }
    }

    private long greedy(int l, int n, int m, int d, int[] w) {
        PriorityQueue<Machine> washing = new PriorityQueue<>(n);
        for (int i=0; i<n; i++) {
            washing.add(new Machine(w[i], w[i]));
        }

        long[] finishTimes = new long[l];
        for (int i=0; i<l; i++) {
            Machine washingMachine = washing.poll();
            finishTimes[i] = washingMachine.finish;
            washingMachine.next();
            washing.add(washingMachine);
        }

        int i = 0;
        while (i < l) {
            for (int j = 0; j < m; i++, j++) {
                if (i == l) break;

                long start = finishTimes[i];
                if ( (i-m >= 0) && (start < finishTimes[i-m]) ) {
                    start = finishTimes[i-m];
                }
                finishTimes[i] = start + d;
            }
        }
        return finishTimes[l-1];
    }

    private static class Machine implements Comparable<Machine> {
        long minutes;
        long finish;

        public Machine(long minutes, long finish) {
            this.minutes = minutes;
            this.finish = finish;
        }

        public void next() {
            finish += minutes;
        }

        @Override
        public String toString() {
            return "" + finish;
        }

        @Override
        public int compareTo(Machine wm) {
            return Long.compare(finish, wm.finish);
        }
    }
}

