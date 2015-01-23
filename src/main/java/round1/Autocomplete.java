package round1;

import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Autocomplete {
    public static final String INPUTDIR = "src/main/resources";
    public static final String OUTPUTDIR = "target/output";
    public static final String ROUND = "round1";

    /*
     * Input definition
     */

    private enum Input {
        SAMPLE("B-sample.in", true),
        INPUT("B-input.in", false);
        
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
            runTest(Input.INPUT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void runTest(Input input) throws Exception {
        Autocomplete problem = new Autocomplete();
        IOUtils ioUtils = new IOUtilsImpl();

        ioUtils.init(input);
        problem.solve(ioUtils);
        ioUtils.close();
    }

    /*
     * Problem part
     */

    public void solve(IOUtils ioUtils) {
        // 1 <= T <= 100
        int t = ioUtils.scanner().nextInt();

        for (int i = 1; i <= t; i++) {
            // 1<= N <= 100,000
            int n = ioUtils.scanner().nextInt();

            String[] words = new String[n];
            Node root = new Tree();

            int count = 0;
            for (int j=0; j<n; j++) {
                words[j] = ioUtils.scanner().next();
                root.insert(words[j], 0);
                count += root.find(words[j], 0);
            }

            ioUtils.writer().printf("Case #%1$d: %2$d\n", i, count);
        }
    }

    private interface Node {
        public Node insert(String word, int index);
        public int find(String word, int index);
    }

    public static class Leaf implements Node {
        private String word;
        private int index;

        public Leaf(String word, int index) {
            this.word = word;
            this.index = index;
        }

        public Node insert(String word, int index) {
            Tree tree = new Tree(this.word.charAt(this.index));

            tree.insert(this.word, this.index+1);
            tree.insert(word, index);
            return tree;
        }

        public int find(String word, int index) {
            return 0;
        }

        @Override
        public String toString() {
            return "Leaf{" + word.charAt(index) + "} " + word;
        }
    }

    private static class Tree implements Node {
        private char ch;
        private Node[] nodes;

        public Tree() {
            this('/');
        }

        public Tree(char ch) {
            nodes = new Node['z'-'a'+1];
            for (char c='a'; c<='z'; c++)
                nodes[reindex(c)] = null;

            this.ch = ch;
        }

        public Node insert(String word, int index) {
            if (index == word.length())
                return this;

            int i = reindex(word.charAt(index));
            if (nodes[i] == null) {
                nodes[i] = new Leaf(word, index);
            } else {
                nodes[i] = nodes[i].insert(word, index+1);
            }
            return this;
        }

        public int find(String word, int index) {
            if (index == word.length())
                return 0;

            int i = reindex(word.charAt(index));
            if (nodes[i] == null)
                return 0;

            return nodes[i].find(word, index+1) + 1;
        }

        private static int reindex(char c) {
            return c - 'a';
        }

        @Override
        public String toString() {
            return "Tree{" + ch + '}';
        }
    }
}

