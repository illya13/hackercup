package liarliar;

import java.io.*;
import java.nio.BufferUnderflowException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;

public class LiarLiar {
    // general part
    private Scanner scanner;
    private PrintWriter writer;

    public LiarLiar(FileInputStream is, OutputStream os) {
        scanner = new Scanner(is);
        writer = new PrintWriter(os);
    }

    public void close() throws IOException {
        scanner.close();
        writer.flush();
    }

    private static void runTest(String fileName) throws Exception {
        FileInputStream is = initInputStream(fileName);
        OutputStream os = initOutputStream();

        LiarLiar problem = new LiarLiar(is, os);
        problem.solve();
        problem.close();

        doneStreams(is);
    }

    private static FileInputStream initInputStream(String fileName) throws FileNotFoundException {
        File inputFile = new File(fileName);
        FileInputStream is = new FileInputStream(inputFile);
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

    private final Set<String> liars           = new HashSet<String>(INITIAL_SET_SIZE);
    private final Set<String> nonLiars        = new HashSet<String>(INITIAL_SET_SIZE);
    private final Queue<QueueItem> queue      = new LinkedList<QueueItem>();

    /**
     * Solve the problem
     */
    public void solve() {
        final int n = scanner.nextInt();

        for (int i=0; i<n; i++) {
            final String accuser = scanner.nextString();
            final int m = scanner.nextInt();

            final Set<String> members = new HashSet<String>();
            for (int j=0; j<m; j++) {
                final String liar = scanner.nextString();
                members.add(liar);
            }
            tryToSolve(accuser, members);
        }

        while (!queue.isEmpty()) {
            final QueueItem item = queue.remove();
            tryToSolve(item.accuser, item.members);
        }

        printResult();
    }

    private void tryToSolve(String accuser, Set<String> members) {
        final Boolean isAccuserALier = isALier(accuser, members);
        if(isAccuserALier != null)
            updateGroups(accuser, members, isAccuserALier);
        else
            queue.add(new QueueItem(accuser, members));
    }

    private Boolean isALier(String accuser, Set<String> members) {
        Boolean isAccuserALier = null;

        if (nonLiars.isEmpty() && liars.isEmpty())
            isAccuserALier = false;
        if (nonLiars.contains(accuser))
            isAccuserALier = false;
        else if (liars.contains(accuser))
            isAccuserALier = true;
        else if (containsAny(liars, members))
            isAccuserALier = false;
        else if (containsAny(nonLiars, members))
            isAccuserALier = true;

        return isAccuserALier;
    }

    private void updateGroups(String accuser, Set<String> members, boolean accuserALier) {
        if (!accuserALier) {
            nonLiars.add(accuser);
            liars.addAll(members);
        } else {
            liars.add(accuser);
            nonLiars.addAll(members);
        }
    }

    private boolean containsAny(Set<String> set, Set<String> members) {
        for (String string: members) {
            if (set.contains(string))
                return true;
        }
        return false;
    }

    private void  printSet(String name, Set<String> set) {
        writer.printf("%1$s: ", name);
        for (String string: set)
            writer.printf("%1$s ", string);
        writer.printf("\n");
    }

    private void printResult() {
        //printSet("liars", liars);
        //printSet("nonLiars", nonLiars);

        if (liars.size() > nonLiars.size())
            writer.printf("%1$d %2$d\n", liars.size(), nonLiars.size());
        else
            writer.printf("%1$d %2$d\n", nonLiars.size(), liars.size());
    }

    private class QueueItem {
        private String accuser;
        private Set<String> members;

        private QueueItem(String accuser, Set<String> members) {
            this.accuser = accuser;
            this.members = members;
        }
    }

    class Scanner {
        private int MAX_STRING_SIZE = 10240;

        private FileInputStream fis;
        FileChannel fc;
        MappedByteBuffer byteBuffer;

        private boolean isStateSpace;
        private int number;
        private long hash;

        private char[] chars;
        private int charIndex;

        private Scanner(FileInputStream fis) {
            try {
                this.fis = fis;
                fc = fis.getChannel();
                byteBuffer = fc.map(FileChannel.MapMode.READ_ONLY,0, fc.size());
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
            chars = new char[MAX_STRING_SIZE];
            isStateSpace = true;
        }

        private void close() throws IOException {
            fc.close();
            fis.close();

        }

        private void next() {
            int bt;
            boolean isTokenSpace;

            do {
                try {
                    bt = byteBuffer.get();
                } catch (BufferUnderflowException e) {
                    bt = -1;
                }
                isTokenSpace = isSpace(bt);
            } while (isTokenSpace);

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
                try {
                    bt = byteBuffer.get();
                } catch (BufferUnderflowException e) {
                    bt = -1;
                }
                isTokenSpace = isSpace(bt);
            } while (!isTokenSpace);

            isStateSpace = isTokenSpace;
            chars[charIndex] = '\0';
        }

        private int nextInt() {
            next();
            return number;
        }

        private String nextString() {
            next();
            return new String(chars, 0, charIndex);
        }

        private boolean isSpace(int bt) {
            if (bt == -1 || bt == ' ' || bt == '\n' || bt == '\r' || bt == '\t')
                return true;
            return false;
        }
    }
}
