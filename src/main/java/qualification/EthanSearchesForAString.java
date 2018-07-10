package qualification;

import java.io.*;
import java.util.Scanner;
import java.util.function.Function;

public class EthanSearchesForAString {
    // general part
    private static final String INPUTDIR = "src/main/resources";
    private static final String OUTPUTDIR = "target/output";
    private static final String ROUND = "qualification";

    private static final String SAMPLE = "C-sample.in";
    private static final String INPUT = "C-input.in";

    private Scanner scanner;
    private PrintWriter writer;

    public EthanSearchesForAString(InputStream is, OutputStream os) {
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

        EthanSearchesForAString problem = new EthanSearchesForAString(is, os);
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
        // 1 ≤ T ≤ 20
        int t = scanner.nextInt();

        for (int i = 1; i <= t; i++) {
            // protein, carbohydrates, and fat
            // 10 <= gP, gC, gF <= 1000
            int gP = scanner.nextInt();
            int gC = scanner.nextInt();
            int gF = scanner.nextInt();

            Foods foods = new Foods(scanner);
            writer.printf("Case #%1$d: %2$s\n", i, foods.solve(gP, gC, gF));
        }
    }

    private static class Foods {
        private static class Food {
            // 10 <= P, C, F <= 1000
            int p, c, f;

            public Food(Scanner scanner) {
                p = scanner.nextInt();
                c = scanner.nextInt();
                f = scanner.nextInt();
            }
        }

        private Food[] food;

        public Foods(Scanner scanner) {
            // 1 <= N <= 20
            int n = scanner.nextInt();
            food = new Food[n];
            for (int j = 0; j < n; j++) {
                food[j] = new Food(scanner);
            }
        }

        private Function<Integer, Integer> protein = x -> food[x].p;
        private Function<Integer, Integer> carbohydrates = x -> food[x].c;
        private Function<Integer, Integer> fat = x -> food[x].f;

        // food count permutation
        // 0     = [0 ... 0, 0]
        // 1     = [0 ... 0, 1]
        // 2     = [0 ... 1, 0]
        // 3     = [0 ... 1, 1]
        //   ...
        // 2^n-1 = [1 ... 1, 1]

        private int maxPermulation() {
            return (int)Math.pow(2, food.length) - 1;
        }

        private int sumOfMacronutrient(int permutation, Function<Integer, Integer> macronutrient) {
            int order = 1;
            int sum = 0;
            for (int i=0; i<food.length; i++) {
                sum += ((permutation & order) > 0) ? macronutrient.apply(i) : 0;
                order *= 2;
            }
            return sum;
        }

        public boolean matchProtein(int permulation, int gP) {
            return sumOfMacronutrient(permulation, protein) == gP;
        }

        public boolean matchCarbohydrates(int permulation, int gC) {
            return sumOfMacronutrient(permulation, carbohydrates) == gC;
        }

        public boolean matchFat(int permulation, int gF) {
            return sumOfMacronutrient(permulation, fat) == gF;
        }

        private String solve(int gP, int gC, int gF) {
            for (int p=0; p<=maxPermulation(); p++) {
                boolean possible = matchProtein(p, gP);
                possible &= matchCarbohydrates(p, gC);
                possible &= matchFat(p, gF);

                if (possible)
                    return "yes";
            }
            return "no";
        }
    }
}
