package gattaca;

import java.io.*;
import java.util.*;

public class Gattaca {
    // general part
    private static final String INPUT = "./";

    private Scanner scanner;
    private PrintWriter writer;

    public Gattaca(InputStream is, OutputStream os) {
        scanner = new Scanner(is);
        writer = new PrintWriter(os);
    }

    public void close() throws IOException {
        scanner.close();
        writer.flush();
    }

    private static void runTest(String fileName) throws Exception {
        InputStream is = initInputStream(fileName);
        OutputStream os = initOutputStream();

        Gattaca problem = new Gattaca(is, os);
        problem.solve();
        problem.close();

        doneStreams(is);
    }

    private static InputStream initInputStream(String fileName) throws FileNotFoundException {
        File inputFile = new File(fileName);
        InputStream is = new FileInputStream(inputFile);
        return is;
    }

    private static OutputStream initOutputStream() throws FileNotFoundException {
        OutputStream os = System.out;
        return os;
    }

    private static void doneStreams(InputStream is) throws IOException {
        is.close();
    }

    public static void main(String[] args) {
        try {
            runTest(args[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // problem part
    private final SortedSet<Range> rangeSet = new TreeSet<Range>();
    private Range rangeArray[];
    private int dp[];

    /**
     * Solve the problem
     */
    public void solve() {
        final int n = scanner.nextInt();
        initSet(n);

        initDp();

        writer.printf("%1$d\n", dp[rangeSet.size()-1]);
    }

    private void initSet(int n) {
        int l = 0;
        StringBuilder sb = new StringBuilder();
        while (l < n) {
            String dna = scanner.next();
            l += dna.length();
            sb.append(dna);
        }

        final int g = scanner.nextInt();
        for (int i=0; i<g; i++) {
            final int start = scanner.nextInt();
            final int stop = scanner.nextInt();
            final int score = scanner.nextInt();

            final Range r = new Range(start, stop, score);
            rangeSet.add(r);
        }
        final Range r = new Range(n, n, 0);
        rangeSet.add(r);

        rangeArray = new Range[rangeSet.size()];
        rangeSet.toArray(rangeArray);
    }

    private void initDp() {
        dp = new int[rangeSet.size()];

        final SortedMap<Integer, Integer> stop2indexMap = new TreeMap<Integer, Integer>(new DpComparator());
        final SortedMap<Integer, Integer> dp2stopMap = new TreeMap<Integer, Integer>(new DpComparator());

        for (int i=0; i<rangeArray.length; i++) {
            int notOverlapDp = getMaxDpForNotOverlap(i, dp2stopMap);
            int smallerStopDp = getMaxDpForSmallerStop(i, stop2indexMap);
            dp[i] = Math.max(smallerStopDp, notOverlapDp + rangeArray[i].score);

            stop2indexMap.put(rangeArray[i].stop, i);

            if (!dp2stopMap.containsKey(dp[i]))
                dp2stopMap.put(dp[i], rangeArray[i].stop);
            else
                dp2stopMap.put(dp[i], Math.min(rangeArray[i].stop, dp2stopMap.get(dp[i])));
        }
    }

    private int getMaxDpForSmallerStop(int i, SortedMap<Integer, Integer> map) {
        Iterator<Integer> iterator = map.keySet().iterator();
        while(iterator.hasNext()) {
            int key = iterator.next();
            int value = map.get(key);

            if (rangeArray[i].stop >= rangeArray[value].stop)
                return dp[value];
        }
        return 0;
/*
        int maxDp = 0;
        for (int j=i-1; j>=0; j--) {
            if ((rangeArray[i].stop >= rangeArray[j].stop) && (dp[j] > maxDp))
                maxDp = dp[j];
        }
        return maxDp;
*/
    }

    private int getMaxDpForNotOverlap(int i, SortedMap<Integer, Integer> map) {
/*
        int maxDp2 = 0;
        int maxJ = 0;
        for (int j=i-1; j>=0; j--) {
            if ((rangeArray[i].isNotOverlap(rangeArray[j])) && (dp[j] > maxDp2)) {
                maxDp2 = dp[j];
                maxJ = j;
                //break;
            }
        }
        return maxDp2;
*/

        Iterator<Integer> iterator = map.keySet().iterator();
        while(iterator.hasNext()) {
            int key = iterator.next();
            int stop = map.get(key);

            if (rangeArray[i].isNotOverlap(stop)) {
                return key;
            }
        }
        return 0;
    }

    private class Range implements Comparable {
        private int start;
        private int stop;
        private int score;

        private Range(int start, int stop, int score) {
            this.start = start;
            this.stop = stop;
            this.score = score;
        }

        public int compareTo(Object o) {
            Range r = (Range) o;
            if (start < r.start)
                return -1;
            if (start > r.start)
                return 1;

            if (stop < r.stop)
                return -1;
            if (stop > r.stop)
                return 1;

            if (score < r.score)
                return -1;
            if (score > r.score)
                return 1;
            return 0;
        }

        private boolean isNotOverlap(Range r) {
            return start > r.stop;
        }

        private boolean isNotOverlap(int stop) {
            return start > stop;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Range range = (Range) o;

            if (score != range.score) return false;
            if (start != range.start) return false;
            if (stop != range.stop) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = start;
            result = 31 * result + stop;
            result = 31 * result + score;
            return result;
        }

        @Override
        public String toString() {
            return String.format("{ %1$2s, %2$2s| %3$2s }", start, stop, score);
        }
    }

    private class DpComparator implements Comparator<Integer> {
        public int compare(Integer o1, Integer o2) {
            return o2.compareTo(o1);
        }
    }

    private class Scanner {
        private int MAX_STRING_SIZE = 10240;

        private InputStream is;

        private boolean isStateSpace;
        private int number;
        private long hash;

        private char[] chars;
        private int charIndex;

        private Scanner(InputStream is) {
            this.is = new BufferedInputStream(is);
            chars = new char[MAX_STRING_SIZE];
            isStateSpace = true;
        }

        private void close() throws IOException {
            is.close();
        }

        private void internalNext() {
            try {
                int bt;
                boolean isTokenSpace;

                do {
                    bt = is.read();
                    isTokenSpace = isSpace(bt);
                } while (isTokenSpace && bt != -1);

                if (bt == -1) {
                    charIndex = 0;
                    number = 0;
                    throw new NoSuchElementException();
                }

                do {
                    if (isStateSpace) {
                        isStateSpace = isTokenSpace;
                        number = bt - 48;

                        charIndex = 0;
                        chars[charIndex++] = (char) bt;

                        hash = bt;
                    } else {
                        number = number * 10 + (bt - 48);
                        chars[charIndex++] = (char) bt;
                        hash = hash * 31 + bt;
                    }
                    bt = is.read();
                    isTokenSpace = isSpace(bt);
                } while (!isTokenSpace);

                isStateSpace = isTokenSpace;
                chars[charIndex] = '\0';

            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        private int nextInt() {
            internalNext();
            return number;
        }

        private String next() {
            internalNext();
            return new String(chars, 0, charIndex);
        }

        private long nextHash() {
            internalNext();
            return hash;
        }

        private boolean isSpace(int bt) {
            if (bt == -1 || bt == ' ' || bt == '\n' || bt == '\r' || bt == '\t')
                return true;
            return false;
        }
    }
}


