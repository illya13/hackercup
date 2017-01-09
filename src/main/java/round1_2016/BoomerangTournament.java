package round1_2016;

import java.io.*;
import java.util.*;

public class BoomerangTournament {
    public static final String INPUTDIR = "src/main/resources";
    public static final String OUTPUTDIR = "target/output";
    public static final String ROUND = "round1_2016";

    /*
     * Input definition
     */

    private enum Input {
        SAMPLE("D-sample.in", true),
        INPUT("D-input.in", false);
        
        private String fileName;
        private boolean useConsole;
        
        Input(String fileName, boolean useConsole) {
            this.fileName = fileName;
            this.useConsole = useConsole;
        }

        public String getFileName() {
            return fileName;
        }

        public boolean useConsole() {
            return useConsole;
        }
    }
    
    /*
     * IO Utils interface
     */

    private interface IOUtils {
        public void init(Input input) throws Exception;
        public void close() throws Exception;

        public Scanner scanner();
        public PrintWriter writer();
    }

    /*
     * IO Utils implementation
     */

    private static class IOUtilsImpl implements IOUtils {
        private boolean useConsole;
        private InputStream is;
        private OutputStream os;

        private Scanner scanner;
        private PrintWriter writer;

        @Override
        public void init(Input input) throws Exception {
            this.useConsole = input.useConsole();
            is = initInputStream(input.getFileName());
            os = initOutputStream(input.getFileName(), useConsole);

            scanner = new Scanner(is);
            writer = new PrintWriter(os);
        }

        @Override
        public void close() throws Exception {
            scanner.close();
            writer.flush();

            doneStreams();
        }

        @Override
        public Scanner scanner() {
            return scanner;
        }

        @Override
        public PrintWriter writer() {
            return writer;
        }

        private InputStream initInputStream(String fileName) throws FileNotFoundException {
            File inputDir = new File(INPUTDIR + File.separator + ROUND);
            File inputFile = new File(inputDir, fileName);
            return new FileInputStream(inputFile);
        }

        private OutputStream initOutputStream(String fileName, boolean useConsole) throws FileNotFoundException {
            OutputStream os = System.out;
            if (useConsole) {
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

        private void doneStreams() throws IOException {
            is.close();
            if (useConsole) {
                System.out.println("          ---] cut [---");
                System.out.println("");
            } else {
                os.close();
            }
        }
    }

    /*
     * Common part
     */

    public static void main(String[] args) {
        try {
            runTest(Input.SAMPLE);
            // runTest(Input.INPUT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void runTest(Input input) throws Exception {
        BoomerangTournament problem = new BoomerangTournament();
        IOUtils ioUtils = new IOUtilsImpl();

        ioUtils.init(input);
        problem.solve(ioUtils);
        ioUtils.close();
    }

    /*
     * Problem part
     */

    int n;
    int[][] w;

    public void solve(IOUtils ioUtils) {
        // 1 ≤ T ≤ 250
        int t = ioUtils.scanner().nextInt();

        for (int i = 1; i <= t; i++) {
            // N = 2^K, where K is an integer and 0 ≤ K ≤ 4
            n = ioUtils.scanner().nextInt();

            w = new int[n][n];
            for (int a=0; a<n; a++) {
                for (int b=0; b<n; b++) {
                    w[a][b] = ioUtils.scanner().nextInt();
                }
            }
            ioUtils.writer().printf("Case #%1$d:\n", i);

            solveInternal();
        }
    }

    private void solveInternal() {
        for (int i=0; i<n; i++) {
            Game game = new Game(i);
            solveBrute(game);
        }
    }

    private void solveBrute(Game game) {
        for (int i=0; i<n; i++) {
            if (game.played.contains(i))
                continue;

            for (int j: game.played) {
                game.results.add(new Result(i, j, w[i][j]));
            }

            game.played.add(i);
            solveBrute(game);
        }
    }

    private static class Result {
        int i, j, w;

        public Result(int i, int j, int w) {
            this.i = i;
            this.j = j;
            this.w = w;
        }
    }

    private static class Game {
        Set<Integer> played;
        List<Result> results = new LinkedList<>();

        public Game(int started) {
            played = new HashSet<>();
            played.add(started);
        }
    }
}

