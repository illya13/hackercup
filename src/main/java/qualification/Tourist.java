package qualification;

import java.io.*;
import java.util.*;

public class Tourist {
    // general part
    private static final String INPUTDIR = "src/main/resources";
    private static final String OUTPUTDIR = "target/output";
    private static final String ROUND = "qualification";

    private static final String SAMPLE = "A-sample.in";
    private static final String INPUT = "A-input.in";

    private Scanner scanner;
    private PrintWriter writer;

    public Tourist(InputStream is, OutputStream os) {
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

        Tourist problem = new Tourist(is, os);
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
        // 1 <= T <= 80
        int t = scanner.nextInt();

        for (int i = 1; i <= t; i++) {
            // 1 <= K <= N <= 50
            int n = scanner.nextInt();
            int k = scanner.nextInt();

            // 1 <= V <= 10^12
            // see Long.MAX_VALUE = 0x7fffffffffffffffL = ‭9223372036854775807‬L
            long v = scanner.nextLong();

            String[] attractions = new String[n];
            for (int j = 0; j < n; j++) {
                attractions[j] = scanner.next();
            }

            writer.printf("Case #%1$d: %2$s\n", i, solve(k, n, v, attractions));
        }
    }

    private String solve(int k, int n, long v, String[] attractions) {
        Set<Integer> visit = new HashSet<>(k);

        long start = ((v-1) * k) % n;
        for (int i = 0; i < k; i++) {
            int next = getNext(n, start, i);
            visit.add(next);
        }

        return getAttractions(n, attractions, visit);
    }

    private int getNext(int n, long start, int i) {
        int next = i + (int)start;
        if (next >= n) {
            next -= n;
        }
        return next;
    }

    private String getAttractions(int n, String[] attractions, Set<Integer> visit) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            if (visit.contains(i)) {
                sb.append(attractions[i]);
                sb.append(' ');
            }
        }

        if (sb.charAt(sb.length()-1) == ' ') {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }
}
