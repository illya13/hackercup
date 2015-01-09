package round1b2011;

import java.io.*;
import java.util.*;

public class DiminishingCircle {
    // general part
    private static final String INPUTDIR = "src/main/resources";
    private static final String OUTPUTDIR = "target/output";
    private static final String ROUND = "round1b2011";

    private static final String SAMPLE = "C-sample.in";
    private static final String INPUT = "C-input.in";

    private Scanner scanner;
    private PrintWriter writer;

    public DiminishingCircle(InputStream is, OutputStream os) {
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

        DiminishingCircle problem = new DiminishingCircle(is, os);
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
            // runTest(INPUT, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // problem part

    /**
     * Solve the problem
     */
    public void solve() {
        // t = 20
        int t = scanner.nextInt();

        for (int i = 1; i <= t; i++) {
            long n = scanner.nextLong();
            long k = scanner.nextLong();
            writer.printf("%1$d\n", solve(n, k));
        }
    }

    private int solve(long n, long k) {
        Set<Long> set = new LinkedHashSet<Long>();
        long index = 0;
        long size = n;
        while (size != 1) {
            while (index+k < size) {
                index += k+1;
                while (set.contains(index)) {
                    index++;
                }
                set.add(index);
            }
            index -= size;
            size -= size / (k+1);
        }
        return (int)index;
    }
}
