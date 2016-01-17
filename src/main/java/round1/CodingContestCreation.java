package round1;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class CodingContestCreation {
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
        CodingContestCreation problem = new CodingContestCreation();
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
            // 1 ≤ N ≤ 100,000
            int n = ioUtils.scanner().nextInt();

            // 1 ≤ Di ≤ 100
            int[] d = new int[n];
            for (int j=0; j<n; j++) {
                d[j] = ioUtils.scanner().nextInt();
            }
            ioUtils.writer().printf("Case #%1$d: %2$d\n", i, solve(d));
        }
    }

    private int solve(int[] d) {
        int total = 0;
        int current = 1;
        for (int i=1; i<d.length; i++) {
            if (current != 0) {
                // start new series because of data
                if (d[i] <= d[i - 1]) {
                    total += 4 - current;
                    current = 0;
                }

                // insert element
                if (d[i] - d[i - 1] > 10) {
                    d = newD(d, i);
                    total++;
                }
            }

            current++;

            // start new series because current is full
            if (current == 4) {
                current = 0;
            }
        }

        if (current != 0) {
            total += 4 - current;
        }
        return total;
    }

    private int[] newD(int[] d, int i) {
        int[] result = new int[d.length+1];
        System.arraycopy(d, 0, result, 0, i);

        result[i] = d[i-1] + 10;

        System.arraycopy(d, i, result, i + 1, d.length - i);
        return result;
    }
}

