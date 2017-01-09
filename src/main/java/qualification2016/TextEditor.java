package qualification2016;

import java.io.*;
import java.util.*;

public class TextEditor {
    // general part
    private static final String INPUTDIR = "src/main/resources";
    private static final String OUTPUTDIR = "target/output";
    private static final String ROUND = "qualification";

    private static final String SAMPLE = "D-sample.in";
    private static final String INPUT = "D-input.in";

    private Scanner scanner;
    private PrintWriter writer;

    public TextEditor(InputStream is, OutputStream os) {
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

        TextEditor problem = new TextEditor(is, os);
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
        // 1 <= T <= 100
        int t = scanner.nextInt();

        for (int i = 1; i <= t; i++) {
            // 1 ≤ K ≤ N ≤ 300
            int n = scanner.nextInt();
            int k = scanner.nextInt();

            String[] words = new String[n];
            for (int j = 0; j < n; j++) {
                words[j] = scanner.next();
            }
            writer.printf("Case #%1$d: %2$d\n", i, solve(words, n, k));
        }
    }

    private long solve(String[] words, int n, int k) {
        int[][] distances = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    distances[i][j] = Integer.MAX_VALUE;
                } else {
                    distances[i][j] = distance(words[i], words[j]);
                }
            }
        }

        Map<Set<Integer>, Integer> cache = new HashMap<>();

        return bruteWithMemorization(words, n, k, distances, 0, -1, new HashSet<>(), 0, cache);
    }

    // brute: 300!, but possible is 14!
    // so some magic is required
    private int bruteWithMemorization(String[] words, int n, int k, int[][] distances, int totalWords, int lastIndex, Set<Integer> used, int operations, Map<Set<Integer>, Integer> cache) {
        if (cache.containsKey(used))
            return cache.get(used);

/*
        for (int i = 0; i < totalWords; i++)
            System.out.print("  ");
        System.out.println(lastIndex);
*/

        if (totalWords == k)
            return operations + words[lastIndex].length();

        int min = Integer.MAX_VALUE;
        for (int i: getSortedByDistance(n, k-totalWords, words, distances, lastIndex, used)) {
            int updatedOperations = (lastIndex == -1) ? words[i].length() : distances[lastIndex][i];
            updatedOperations += operations + 1;

            if (updatedOperations >= min)
                continue;

            used.add(i);
            updatedOperations = bruteWithMemorization(words, n, k, distances, totalWords+1, i, used, updatedOperations, cache);
            used.remove(i);

            if (min > updatedOperations)
                min = updatedOperations;
        }

        cache.put(used, min);
        return min;
    }


    private int distance(String str1, String str2) {
        int i = 0;
        for (; i<Math.min(str1.length(), str2.length()); i++) {
            if (str1.charAt(i) != str2.charAt(i))
                break;
        }
        return str1.length() + str2.length() - 2 * i;
    }

    private int[] getSortedByDistance(int n, int k, String[] words, int[][] distances, int index, Set<Integer> used) {
        class Pair implements Comparable<Pair> {
            int distance, index;

            public Pair(int distance, int index) {
                this.distance = distance;
                this.index = index;
            }

            @Override
            public int compareTo(Pair pair) {
                return Integer.compare(distance, pair.distance);
            }
        }
        
        Pair[] pairs = new Pair[n];
        for (int i=0; i<n; i++) {
            int distance;
            if (used.contains(i)) {
                distance = Integer.MAX_VALUE;
            } else {
                distance = (index == -1) ? words[i].length() : distances[index][i];
            }
            pairs[i] = new Pair(distance, i);
        }
        Arrays.sort(pairs);
        
        int[] indexes = new int[n];
        for (int i=0; i<n; i++) {
            indexes[i] = pairs[i].index;
        }

        return Arrays.copyOf(indexes, k);
    }
}
