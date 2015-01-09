package round1b2011;

import java.io.*;
import java.util.Scanner;

public class SlotMachineHacker {
    // general part
    private static final String INPUTDIR = "src/main/resources";
    private static final String OUTPUTDIR = "target/output";
    private static final String ROUND = "round1b2011";

    private static final String SAMPLE = "A-sample.in";
    private static final String INPUT = "A-input.in";

    private Scanner scanner;
    private PrintWriter writer;

    public SlotMachineHacker(InputStream is, OutputStream os) {
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

        SlotMachineHacker problem = new SlotMachineHacker(is, os);
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
    private static final int MAX_SLOT_VALUES = 100000000;

    private long secret;
    private int[] values;

    /**
     * Solve the problem
     */
    public void solve() {
        initDp();

        // 20
        int t = scanner.nextInt();

        for (int i = 1; i <= t; i++) {
            // 1 ? N ? 100
            int n = scanner.nextInt();

            int[] observations = new int[n];
            for (int j = 0; j < n; j++) {
                observations[j] = scanner.nextInt();
            }

            writer.printf("%1$s\n", solve(observations));
        }
    }

    private void initDp() {
        values = new int[MAX_SLOT_VALUES];
        for (int i=0; i< MAX_SLOT_VALUES; i++) {
            secret = i;
            values[i] = getRandomNumber();
        }
    }

    private String solve(int[] observations) {
        if (observations.length < 2)
            return "Not enough observations";

        for (int i=0; i< MAX_SLOT_VALUES; i++) {
            if ((values[i] == observations[0]) && (observe(i, observations))) {
                return betNext(i, observations.length);
            }
        }

        return "Wrong machine";
    }

    private String betNext(int i, int length) {
        secret = i;
        for(int j=0; j<length; j++)
            getRandomNumber();

        StringBuilder sb = new StringBuilder();
        for(int j=0; j<10; j++) {
            sb.append(getRandomNumber());
            if (j != 9)
                sb.append(" ");
        }
        return sb.toString();
    }

    private boolean observe(int i, int[] observations) {
        secret = i;
        for (int j=0; j<observations.length; j++) {
            if (observations[j] != getRandomNumber())
                return false;
        }
        return true;
    }

    private int getRandomNumber() {
        secret = (secret * 5402147 + 54321) % 10000001;
        return (int)(secret % 1000);
    }
}
