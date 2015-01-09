package qualification2011;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class PegGame {
    // general part
    private static final String INPUTDIR = "src/main/resources";
    private static final String OUTPUTDIR = "target/output";
    private static final String ROUND = "qualification2011";

    private static final String SAMPLE = "B-sample.in";
    private static final String INPUT = "B-input.in";

    private Scanner scanner;
    private PrintWriter writer;

    public PegGame(InputStream is, OutputStream os) {
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

        PegGame problem = new PegGame(is, os);
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

    // rows and columns
    private int r;
    private int c;

    // target column
    private int k;

    // is peg present
    private boolean peg[][];

    // probability[i] - probability after i-row
    // using BigDecimal for rounding up
    private BigDecimal probability[][];

    /**
     * Solve the problem
     */
    public void solve() {
        // 1 ? N ? 100
        int n = scanner.nextInt();

        for (int i = 1; i <= n; i++) {
            // rows and columns
            // 3 ? R,C ? 100
            r = scanner.nextInt();
            c = scanner.nextInt();

            // target column
            k = scanner.nextInt();

            // pairs
            int m = scanner.nextInt();

            peg = new boolean [r][c];
            for(int j=0; j<r; j++)
                Arrays.fill(peg[j], true);

            for(int j=0; j<m; j++) {
                int ri = scanner.nextInt();
                int ci = scanner.nextInt();
                peg[ri][ci] = false;
            }

            solveInternal();
        }
    }

    private void solveInternal() {
        int maxCol = -1;
        BigDecimal maxProbability = initBD(-1);

        for(int i=0; i<c; i++) {
            BigDecimal probability = calcProbability(i);
            if (probability.compareTo(maxProbability) == 1) {
                maxProbability = probability;
                maxCol = i;
            }
        }

        writer.printf("%1$s %2$.6f\n", maxCol, maxProbability.doubleValue());
    }


    private BigDecimal calcProbability(int start) {
        probability = new BigDecimal[r][c];

        for (int i=0; i<r; i++)
            initProbability(probability[i]);
        probability[r-1][start] = initBD(1);

        for (int i=r-2; i>=0; i--) {
            if (isOddRow(i))
                proceedOdd(i);
            else
                proceedEven(i);
        }
        return probability[0][k];
    }

    private void proceedOdd(int i){
        // columns: c total, c-1 last index
        // probabilities: ?-1 total, c-2 last index

        // left
        if (peg[i][0])
            probability[i][0] = probability[i][0].add(probability[i+1][0]);
        else if (i>0)
            probability[i-1][0] = probability[i-1][0].add(probability[i+1][0]);

        // right
        if (peg[i][c-1])
            probability[i][c-2] = probability[i][c-2].add(probability[i+1][c-1]);
        else if (i>0)
            probability[i-1][c-1] = probability[i-1][c-1].add(probability[i+1][c-1]);

        // other 1..c-2
        for (int j=1; j<c-1; j++) {
            if (peg[i][j]) {
                probability[i][j-1] = probability[i][j-1].add(probability[i+1][j].multiply(initBD(0.5)));
                probability[i][j] = probability[i][j].add(probability[i+1][j].multiply(initBD(0.5)));
            } else if (i>0) {
                probability[i-1][j] = probability[i-1][j].add(probability[i+1][j]);
            }
        }
    }

    private void proceedEven(int i){
        // columns: c-1 total, c-2 last index
        // probabilities: ? total, c-1 last index

        // left
        if (peg[i][0])
            probability[i][1] = probability[i][1].add(probability[i+1][0]);
        else if (i>0)
            probability[i-1][0] = probability[i-1][0].add(probability[i+1][0]);

        // right
        if (peg[i][c-2])
            probability[i][c-2] = probability[i][c-2].add(probability[i+1][c-2]);
        else if (i>0)
            probability[i-1][c-2] = probability[i-1][c-2].add(probability[i+1][c-2]);

        // other 1..c-3
        for (int j=1; j<c-2; j++) {
            if (peg[i][j]) {
                probability[i][j] = probability[i][j].add(probability[i+1][j].multiply(initBD(0.5)));
                probability[i][j+1] = probability[i][j+1].add(probability[i+1][j].multiply(initBD(0.5)));
            } else if (i>0) {
                probability[i-1][j] = probability[i-1][j].add(probability[i+1][j]);
            }
        }
    }

    private boolean isOddRow(int i) {
        return (i % 2) == 0;
    }

    private void initProbability(BigDecimal[] array) {
        for (int i=0; i<array.length; i++)
            array[i] = initBD(0);
    }

    private BigDecimal initBD(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd.setScale(6, RoundingMode.HALF_UP);
        return bd;
    }
}


