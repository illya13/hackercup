package round1b2011;

import java.io.*;
import java.util.Scanner;

public class Chess2 {
    // general part
    private static final String INPUTDIR = "src/main/resources";
    private static final String OUTPUTDIR = "target/output";
    private static final String ROUND = "round1b2011";

    private static final String SAMPLE = "B-sample.in";
    private static final String INPUT = "B-input.in";

    private Scanner scanner;
    private PrintWriter writer;

    public Chess2(InputStream is, OutputStream os) {
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

        Chess2 problem = new Chess2(is, os);
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
    private char[][] board;
    private boolean[][] threatened;

    /**
     * Solve the problem
     */
    public void solve() {
        // N = 20
        int n = scanner.nextInt();

        for (int i = 1; i <= n; i++) {
            initBoard();

            // 3 <= P <= 64
            int p = scanner.nextInt();
            for(int j=0; j<p; j++) {
                String c = scanner.next();
                int r = scanner.nextInt();
                int f = scanner.nextInt();
                board[r-1][f-1] = c.charAt(0);
            }

            writer.printf("%1$d\n", chess2());
        }
    }

    private int chess2() {
        for (int i=0; i<16; i++) {
            for (int j=0; j<16; j++) {
                if (board[i][j] != '\0') {
                    switch (board[i][j]) {
                        case 'K':
                            king(i, j);
                            break;

                        case 'Q':
                            queen(i, j);
                            break;

                        case 'R':
                            rook(i, j);
                            break;

                        case 'B':
                            bishop(i, j);
                            break;

                        case 'N':
                            knight(i, j);
                            break;

                        case 'S':
                            nightrider(i, j);
                            break;

                        case 'A':
                            archbishop(i, j);
                            break;

                        case 'E':
                            kraken(i, j);
                            break;
                    }
                }
            }
        }
        return result();
    }

    private void initBoard() {
        board = new char[16][16];
        threatened = new boolean[16][16];

        for (int i=0; i<16; i++)
            for (int j=0; j<16; j++) {
                board[i][j] = '\0';
                threatened[i][j] = false;
            }
    }

    private void king(int i, int j) {
    }

    private void queen(int i, int j) {
        rook(i, j);
        bishop(i, j);
    }

    private void rook(int i, int j) {
        for (int k=0; k<16; k++) {
            if (k!=j)
                threatened[i][k] = true;
            if (k!=i)
                threatened[k][j] = true;
        }
    }

    private void bishop(int i, int j) {
        for (int k=-15; k<16; k++) {
            if (k==0)
                continue;
            if ((i+k >= 0) && (j+k > 0) && (i+k < 16) && (j+k < 16))
                threatened[i+k][j+k] = true;
            if ((i+k >= 0) && (j-k >= 0) && (i+k < 16) && (j-k < 16))
                threatened[i+k][j-k] = true;
        }
    }

    private void knight(int i, int j) {
    }

    private void nightrider(int i, int j) {
    }

    private void archbishop(int i, int j) {
    }

    private void kraken(int ki, int kj) {
        for (int i=0; i<16; i++)
            for (int j=0; j<16; j++)
                if ((i != ki) || (j!=kj))
                    threatened[i][j] = true;
    }

    private int result() {
        int count = 0;
        for (int i=0; i<16; i++)
            for (int j=0; j<16; j++)
                if ((board[i][j] != '\0') && (threatened[i][j]))
                    count++;
        return count;
    }
}
