package round1;

import java.io.*;
import java.util.*;

public class Yachtzee {
    public static final String INPUTDIR = "src/main/resources";
    public static final String OUTPUTDIR = "target/output";
    public static final String ROUND = "round1";

    /*
     * Input definition
     */

    private enum Input {
        SAMPLE("C-sample.in", true),
        INPUT("C-input.in", false);
        
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
        Yachtzee problem = new Yachtzee();
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

            // 0 ≤ A < B ≤ 1,000,000,000
            int a = ioUtils.scanner().nextInt();
            int b = ioUtils.scanner().nextInt();

            int[] c = new int[n];
            for (int j=0; j<n; j++) {
                // 1 ≤ Ci ≤ 1,000,000,000
                c[j] = ioUtils.scanner().nextInt();
            }
            ioUtils.writer().printf("Case #%1$d: %2$.9f\n", i, solve(n, a, b, c));
        }
    }

    private double solve(int n, int a, int b, int[] c) {
        boolean first = false;

        Map<Interval, Integer> intervals = new HashMap<>(n);

        int dollars = 0;
        int index = 0;

        int prev = -1;
        while (dollars <= b) {
            if (dollars > a) {
                if (!first) {
                    add(intervals, new Interval(a - (dollars - prev), prev));
                    first = true;
                } else {
                    add(intervals, new Interval(0, prev));
                }
            }

            dollars += c[index];
            prev = c[index];

            index++;
            if (index == n) {
                index = 0;
            }
        }

        if (dollars > b) {
            if (b < c[0]) {
                add(intervals, new Interval(a, b - (dollars - prev)));
            } else {
                add(intervals, new Interval(0, b - (dollars - prev)));
            }
        }

        double weight = 0;
        double length = 0;
        for (Map.Entry<Interval, Integer> entry : intervals.entrySet()) {
            weight += entry.getKey().getWeight() * entry.getValue();
            length += entry.getKey().getLength() * entry.getValue();
        }
        return weight / length;
    }

    private void add(Map<Interval, Integer> intervals, Interval interval) {
        if (interval.getLength() == 0)
            return;

        Integer integer = intervals.get(interval);
        if (integer == null) {
            integer = 0;
        }
        integer++;
        intervals.put(interval, integer);
    }

    private static class Interval {
        int start, end;

        public Interval(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public double getWeight() {
            return getAvgValue() * getLength();
        }

        public double getAvgValue() {
            return (0.0 + start + end) / 2;
        }

        public double getLength() {
            return end - start;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Interval)) return false;

            Interval interval = (Interval) o;

            if (start != interval.start) return false;
            return end == interval.end;
        }

        @Override
        public int hashCode() {
            int result = start;
            result = 31 * result + end;
            return result;
        }

        @Override
        public String toString() {
            return "[" + start +
                    "," + end +
                    ") " + getWeight();
        }
    }
}

