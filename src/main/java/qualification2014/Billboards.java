package qualification2014;

import java.io.*;
import java.util.Scanner;

public class Billboards {
    // general part
    private static final String INPUTDIR = "src/main/resources";
    private static final String OUTPUTDIR = "target/output";
    private static final String ROUND = "qualification2014";

    private static final String SAMPLE = "A-sample.in";
    private static final String INPUT = "A-input.in";

    private Scanner scanner;
    private PrintWriter writer;

    public Billboards(InputStream is, OutputStream os) {
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

        Billboards problem = new Billboards(is, os);
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
    
    private int w;
    private int h;
    private String s;

    /**
     * Solve the problem
     */
    public void solve() {
        // 1 ≤ T ≤ 20
        int t = scanner.nextInt();

        for (int i = 1; i <= t; i++) {
            // W H S
            // 1 ≤ W, H ≤ 1000
            w = scanner.nextInt();
            h = scanner.nextInt();
            s = scanner.nextLine();

            writer.printf("Case #%1$d: %2$d\n", i, solveInternal());
        }
    }

    private int solveInternal() {
        // The text will contain only lower-case letters a-z, upper-case letters A-Z, digits 0-9 and the space character
        // The text will not start or end with the space character, and will never contain two adjacent space characters

        // anyway fixing ...
        s = s.trim();
        s = s.replaceAll("\\s+", " ");

        String[] words = s.split(" ");

        int inches = Math.min(w, h);
        while (inches > 0) {
            if (tryToPrint(words, inches))
                break;
            inches--;
        }
        return inches;
    }

    private boolean tryToPrint(String[] words, int inches) {
        int maxLineSize = w / inches;
        int maxLines = h / inches;
        
        int wordsPrinted = 0;
        int lines = 0;
        int size = 0;
        while (wordsPrinted < words.length) {
            if (size != 0) {
                size++;
            }
            if (size + words[wordsPrinted].length() <= maxLineSize) {
                size += words[wordsPrinted].length();
                wordsPrinted++;
            } else {
                lines++;
                size = 0;
            }
            if (lines >= maxLines)
                return false;
        }
        return true;
    }
}
