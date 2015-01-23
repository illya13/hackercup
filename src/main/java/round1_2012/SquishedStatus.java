package round1_2012;

import java.io.*;
import java.util.*;

public class SquishedStatus {
    // general part
    private static final String INPUTDIR = "src/main/resources";
    private static final String OUTPUTDIR = "target/output";
    private static final String ROUND = "round1_2012";

    private static final String SAMPLE = "C-sample.in";
    private static final String INPUT = "C-input.in";

    private Scanner scanner;
    private PrintWriter writer;

    public SquishedStatus(InputStream is, OutputStream os) {
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

        SquishedStatus problem = new SquishedStatus(is, os);
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
    private static long FACEBOOK = 4207849484l;

    /**
     * Solve the problem
     */
    public void solve() {
        // 5 <= N <= 25
        int n = scanner.nextInt();

        for (int i = 1; i <= n; i++) {
            //2 <= M <= 255
            int m = scanner.nextInt();
            String status = scanner.next();
            writer.printf("Case #%1$d: %2$d\n", i, solve(m, status));
        }
    }

    private long solve(int m, String status) {
        Map<String, Long> hash = new HashMap<String, Long>();
        return getCountOfValidCombinations(m, status, hash);
    }
    
    private long getCountOfValidCombinations(int m, String status, Map<String, Long> hash) {
        if (status.length() == 0)
            return 1;

        if (status.startsWith("0"))
            return 0;
        
        if (hash.containsKey(status))
            return hash.get(status);

        long count = 0;
        for (int i=1; i<Math.min(status.length()+1, 4); i++) {
            if (Integer.parseInt(status.substring(0,i)) <= m)
                count += getCountOfValidCombinations(m, status.substring(i), hash);
            if (count >= FACEBOOK)
                count -= FACEBOOK;
        }
        
        hash.put(status, count);
        return count;
    }
}
