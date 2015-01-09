package breathalyzer;

import java.io.*;
import java.util.*;

public class Breathalyzer {
    // general part
    private static final String INPUT = "./";

    private Scanner scanner;
    private PrintWriter writer;
    private Scanner dictScanner;

    public Breathalyzer(InputStream is, OutputStream os, InputStream tw) {
        scanner = new Scanner(is);
        writer = new PrintWriter(os);
        dictScanner = new Scanner(tw);
    }

    public void close() throws IOException {
        scanner.close();
        writer.flush();
        dictScanner.close();
    }

    private static void runTest(String fileName) throws Exception {
        InputStream is = initInputStream(fileName);
        OutputStream os = initOutputStream();
        InputStream tw = initInputStream("/var/tmp/twl06.txt");

        Breathalyzer problem = new Breathalyzer(is, os, tw);
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
    private static final int INITIAL_SET_SIZE = 10000;
    private final Set<String> dictSet = new HashSet<String>(INITIAL_SET_SIZE);
    private final List<String> wordList = new LinkedList<String>();

    /**
     * Solve the problem
     */
    public void solve() {
        initSets();

        Map<String, String> wordMap = new HashMap<String, String>(INITIAL_SET_SIZE);
        Map<String, Integer> distMap = new HashMap<String, Integer>(INITIAL_SET_SIZE);
        getMinDist(wordMap, distMap);

        writer.printf("%1$d\n", getTotaMinlDist(wordMap, distMap));
    }

    private int getTotaMinlDist(Map<String, String> wordMap, Map<String, Integer> distMap) {
        int sum = 0;
        for(String word: wordList) {
            // writer.printf("%1$s\t %2$s\t %3$d\n", word, wordMap.get(word), distMap.get(word));
            sum += distMap.get(word);
        }
        return sum;
    }

    private void initSets() {
        String word = null;
        do {
            word = dictScanner.nextString();
            // writer.printf("%1$s ", word);
            if (!word.equals(""))
                dictSet.add(word.toLowerCase());
        } while (!word.equals(""));

        do {
            word = scanner.nextString();
            // writer.printf("%1$s ", word);
            if (!word.equals(""))
                wordList.add(word);
        } while (!word.equals(""));
    }

    private void getMinDist(Map<String, String> wordMap, Map<String, Integer> distMap) {
        String[] dictArray = new String[dictSet.size()];
        dictSet.toArray(dictArray);

        String[] wordArray = new String[wordList.size()];
        wordList.toArray(wordArray);

        for(String word: wordList) {
            if (wordMap.containsKey(word))
                continue;

            if (dictSet.contains(word)) {
                wordMap.put(word, word);
                distMap.put(word, 0);
                continue;
            }

            int minDist = Integer.MAX_VALUE;
            String dictWord = null;
            for(int i=0; i<dictArray.length; i++) {
                int dist = levenshtein(dictArray[i], word, minDist);
                if (minDist > dist) {
                    minDist = dist;
                    dictWord = dictArray[i];
                }
                if (minDist == 1)
                    break;
            }
            wordMap.put(word, dictWord);
            distMap.put(word, minDist);
        }
    }

    private static int min(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }

	public static int levenshtein(String str1, String str2, int maxDist) {
        if (Math.abs(str1.length() - str2.length()) >= maxDist)
            return maxDist;

		int[][] d = new int[str1.length() + 1][str2.length() + 1];

		for (int i = 0; i <= str1.length(); i++)
			d[i][0] = i;
		for (int j = 0; j <= str2.length(); j++)
			d[0][j] = j;

		for (int i = 1; i <= str1.length(); i++) {
			for (int j = 1; j <= str2.length(); j++) {
				d[i][j] = min(
                        d[i - 1][j] + 1,
                        d[i][j - 1] + 1,
                        d[i - 1][j - 1] + ((str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0 : 1));
            }
        }
		return d[str1.length()][str2.length()];
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

        private void next() {
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
                    return;
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
            next();
            return number;
        }

        private String nextString() {
            next();
            return new String(chars, 0, charIndex);
        }

        private long nextHash() {
            next();
            return hash;
        }

        private boolean isSpace(int bt) {
            if (bt == -1 || bt == ' ' || bt == '\n' || bt == '\r' || bt == '\t')
                return true;
            return false;
        }
    }
}

