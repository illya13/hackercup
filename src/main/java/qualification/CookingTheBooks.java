package qualification;

import java.io.*;
import java.util.*;

public class CookingTheBooks {
    // general part
    private static final String INPUTDIR = "src/main/resources";
    private static final String OUTPUTDIR = "target/output";
    private static final String ROUND = "qualification";

    private static final String SAMPLE = "A-sample.in";
    private static final String INPUT = "A-input.in";

    private Scanner scanner;
    private PrintWriter writer;

    public CookingTheBooks(InputStream is, OutputStream os) {
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

        CookingTheBooks problem = new CookingTheBooks(is, os);
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
        // 1 ≤ T ≤ 100
        int t = scanner.nextInt();
        scanner.nextLine();

        for (int i = 1; i <= t; i++) {
            // 0 <= N <= 999999999
            // N will never begin with a leading 0 unless N = 0
            // Integer.MAX_VALUE = 2147483647
            int n = scanner.nextInt();
            writer.printf("Case #%1$d: %2$s\n", i, solve(n));
        }
    }

    private String solve(Integer n) {
        int max = n, min = n;

        char[] chars = n.toString().toCharArray();
        for (int i=0; i<chars.length; i++) {
            for (int j=i+1; j<chars.length; j++) {
                if ((i ==0) && (chars[j] == '0')) continue;

                int candidate = copySwapAndConvert(chars, i, j);
                max = (candidate > max) ? candidate : max;
                min = (candidate < min) ? candidate : min;
            }
        }
        return String.format("%1$d %2$d", min, max);
    }

    private int copySwapAndConvert(char[] array, int i, int j) {
        char[] swapped = Arrays.copyOf(array, array.length);

        char swap = swapped[i];
        swapped[i] = swapped[j];
        swapped[j] = swap;

        return new Integer(new String(swapped));
    }
}

