package qualification2014;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AlphabetSoup {
    // general part
    private static final String INPUTDIR = "src/main/resources";
    private static final String OUTPUTDIR = "target/output";
    private static final String ROUND = "qualification2014";

    private static final String SAMPLE = "C-sample.in";
    private static final String INPUT = "C-input.in";

    private Scanner scanner;
    private PrintWriter writer;

    public AlphabetSoup(InputStream is, OutputStream os) {
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

        AlphabetSoup problem = new AlphabetSoup(is, os);
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
    private static final String PATTERN = "HACKERCUP";

    /**
     * Solve the problem
     */
    public void solve() {
        // 1 ≤ T ≤ 20
        int t = scanner.nextInt();
        scanner.nextLine();

        for (int i = 1; i <= t; i++) {
            // S
            String s = scanner.nextLine();
            writer.printf("Case #%1$d: %2$d\n", i, solve(s));
        }
    }

    private int solve(String s) {
        Map<Character, Integer> map = new HashMap<Character, Integer>();
        
        // init map
        for (int i=0; i<PATTERN.length(); i++) {
            map.put(PATTERN.charAt(i), 0);
        }
        for (int i=0; i<s.length(); i++) {
            if (map.containsKey(s.charAt(i))) {
                int entry = map.get(s.charAt(i)) + 1;
                map.put(s.charAt(i), entry);
            }
        }
        
        // calc pattern
        int count = 0;
        while (true) {
            for (int i=0; i<PATTERN.length(); i++) {
                int entry = map.get(PATTERN.charAt(i)) - 1;
                map.put(PATTERN.charAt(i), entry);
                if (entry < 0)
                    return count;
            }
            count++;
        }
    }
}

