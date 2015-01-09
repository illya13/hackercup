package qualification2013;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class BeautifulStrings {
    // general part
    private static final String INPUTDIR = "src/main/resources";
    private static final String OUTPUTDIR = "target/output";
    private static final String ROUND = "qualification2013";

    private static final String SAMPLE = "A-sample.in";
    private static final String INPUT = "A-input.in";

    private Scanner scanner;
    private PrintWriter writer;

    public BeautifulStrings(InputStream is, OutputStream os) {
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

        BeautifulStrings problem = new BeautifulStrings(is, os);
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
        // 5 <= m <= 50
        int m = scanner.nextInt();
        scanner.nextLine();

        for (int i = 1; i <= m; i++) {
            // 2 <= length of s <= 500
            String s = scanner.nextLine();
            writer.printf("Case #%1$d: %2$d\n",i, solveInternal(s.toLowerCase()));
        }
    }

    private long solveInternal(String s) {
        final Map<Character, Integer> counts = new HashMap<Character, Integer>();
        for(int i=0; i<s.length(); i++) {
            char ch = s.charAt(i);
            if ((ch < 'a') || (ch > 'z'))
                continue;
            Integer count = counts.get(ch);
            if (count == null)
                count = 0;
            count++;
            counts.put(ch, count);
        }

        List<Character> list = new LinkedList<Character>();
        list.addAll(counts.keySet());
        Collections.sort(list, new Comparator<Character>(){
            public int compare(Character o1, Character o2) {
                return -counts.get(o1).compareTo(counts.get(o2));
            }
        });

        long sum = 0;
        int beauty = 26;
        for(Character character: list) {
            sum += counts.get(character) * beauty;
            beauty--;
        }
        return sum;
    }
}


