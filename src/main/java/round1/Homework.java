package round1;

import java.io.*;
import java.util.Scanner;

public class Homework {
    public static final String INPUTDIR = "src/main/resources";
    public static final String OUTPUTDIR = "target/output";
    public static final String ROUND = "round1";

    /*
     * Input definition
     */

    private enum Input {
        SAMPLE("A-sample.in", true),
        INPUT("A-input.in", false);
        
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
        Homework problem = new Homework();
        IOUtils ioUtils = new IOUtilsImpl();

        ioUtils.init(input);
        problem.solve(ioUtils);
        ioUtils.close();
    }

    /*
     * Problem part
     */

    private int[] primacity;

    public void solve(IOUtils ioUtils) {
        initPrimacity(10000000);

        // 1 <= T <= 100
        int t = ioUtils.scanner().nextInt();

        for (int i = 1; i <= t; i++) {
            // 2 <= A <= B <= 10^7
            // 1 <= K <= 10^9
            // Integer.MAX_VALUE = 2147483647
            //                     1000000000
            int a = ioUtils.scanner().nextInt();
            int b = ioUtils.scanner().nextInt();
            int k = ioUtils.scanner().nextInt();
            ioUtils.writer().printf("Case #%1$d: %2$d\n", i, solve(a, b, k));
        }
    }

    private void initPrimacity(int N) {
        primacity = sieveOfEratosthenes(N);
    }

    private int[] sieveOfEratosthenes(int N) {
        // initially assume all integers are prime
        int[] primacity = new int[N + 1];
        boolean[] isPrime = new boolean[N + 1];
        for (int i = 2; i <= N; i++) {
            isPrime[i] = true;
            primacity[i] = 0;
        }

        // mark non-primes <= N using Sieve of Eratosthenes
        for (int i = 2; i*i <= N; i++) {

            // if i is prime, then mark multiples of i as nonprime
            // suffices to consider mutiples i, i+1, ..., N/i
            if (isPrime[i]) {
                for (int j = 1; i*j <= N; j++) {
                    isPrime[i*j] = false;
                    primacity[i*j]++;
                }
            }
        }
        return primacity;
    }

    private int solve(int a, int b, int k) {
        int count = 0;
        for(int i=a; i<=b; i++) {
            if (primacity[i] == k)
                count++;
        }
        return count;
    }
}

