package round1_2012;

import java.io.*;
import java.util.*;

public class RecoverTheSequence {
    // general part
    private static final String INPUTDIR = "src/main/resources";
    private static final String OUTPUTDIR = "target/output";
    private static final String ROUND = "round1";

    private static final String SAMPLE = "B-sample.in";
    private static final String INPUT = "B-input.in";

    private Scanner scanner;
    private PrintWriter writer;

    public RecoverTheSequence(InputStream is, OutputStream os) {
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

        RecoverTheSequence problem = new RecoverTheSequence(is, os);
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
        // 5 ≤ T ≤ 20
        int t = scanner.nextInt();

        for (int i = 1; i <= t; i++) {
            // 2 ≤ N ≤ 10000
            int n = scanner.nextInt();
            String debug = scanner.next();

            writer.printf("Case #%1$d: %2$d\n", i, solve(n, debug));
        }
    }

    private int solve(int n, String debug) {
        int[] sorted = initSorted(n);
        Stack<Queue<Character>> stack = new Stack<Queue<Character>>();
        calculateSequences(n, initQueue(debug), stack);
        restoreMergeSort(sorted, stack);
        return checksum(sorted);
    }

    private void restoreMergeSort(int[] sorted,  Stack<Queue<Character>> stack) {
        if (sorted.length <= 1)
            return;

        int mid = getMid(sorted.length);

        int[] firstHalf = new int[mid];
        int[] secondHalf = new int[sorted.length-mid];
        restoreMerge(sorted, stack.pop(), firstHalf, secondHalf);

        restoreMergeSort(secondHalf, stack);
        restoreMergeSort(firstHalf, stack);

        System.arraycopy(firstHalf, 0, sorted, 0, firstHalf.length);
        System.arraycopy(secondHalf, 0, sorted, mid, secondHalf.length);
    }

    protected void restoreMerge(int[] sorted, Queue<Character> queue, int[] arr1, int[] arr2) {
        int arr1Index = 0;
        int arr2Index = 0;
        int sortedIndex = 0;

        while ((arr1Index < arr1.length) && (arr2Index < arr2.length)) {
            char c = queue.remove();
            if (c == '1') {
                arr1[arr1Index++] = sorted[sortedIndex++];
            } else if (c == '2') {
                arr2[arr2Index++] = sorted[sortedIndex++];
            } else {
                throw new IllegalStateException("illegal characted");
            }
        }
        if (arr1Index != arr1.length) {
            System.arraycopy(sorted, sortedIndex, arr1, arr1Index, arr1.length-arr1Index);
        } else if (arr2Index != arr2.length) {
            System.arraycopy(sorted, sortedIndex, arr2, arr2Index, arr2.length-arr2Index);
        } else {
            throw new IllegalStateException("smth wrong with arrays");
        }
    }

    private int[] initSorted(int n) {
        int[] sorted = new int[n];
        for(int i=0; i<n; i++)
            sorted[i] = i+1;
        return sorted;
    }

    protected Queue<Character> initQueue(String s) {
        Queue<Character> Queue = new LinkedList<Character>();
        for (int i=0; i<s.length(); i++)
            Queue.add(s.charAt(i));
        return Queue;
    }

    protected int checksum(int[] array) {
        int result = 1;
        for (int i=0; i<array.length; i++)
            result = (31 * result + array[i]) % 1000003;
        return result;
    }
    
    protected int getMid(int n) {
        // could be just
        // return n/2
        // but anyway let's use floor :)

        double d = n;
        return (int)Math.floor(d/2);
    }

    protected void calculateSequences(int n, Queue<Character> queue, Stack<Queue<Character>> stack) {
        if (n <= 1)
            return;

        int mid = getMid(n);

        calculateSequences(mid, queue, stack);
        calculateSequences(n-mid, queue, stack);
        calculateSequences(mid, n-mid, queue, stack);
    }
    
    protected int calculateSequences(int first, int second, Queue<Character> globalQueue, Stack<Queue<Character>> stack) {
        Queue<Character> localQueue = new LinkedList<Character>();
        int count = 0;
        while ((first > 0) && (second > 0)) {
            Character c = globalQueue.remove();
            localQueue.add(c);
            if (c == '1') {
                first--;
            } else if (c == '2') {
                second--;
            } else {
                throw new IllegalStateException("illegal characted");
            }
            count++;
        }
        stack.push(localQueue);
        return count;
    }
}
