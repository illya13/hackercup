package qualification2013;

import java.io.*;
import java.util.*;

public class BalancedSmileys {
    // general part
    private static final String INPUTDIR = "src/main/resources";
    private static final String OUTPUTDIR = "target/output";
    private static final String ROUND = "qualification2013";

    private static final String SAMPLE = "B-sample.in";
    private static final String INPUT = "B-input.in";

    private Scanner scanner;
    private PrintWriter writer;

    public BalancedSmileys(InputStream is, OutputStream os) {
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

        BalancedSmileys problem = new BalancedSmileys(is, os);
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
        // 1 <= T <= 50
        int t = scanner.nextInt();
        scanner.nextLine();

        for (int i = 1; i <= t; i++) {
            // 1 <= length of s <= 100
            String s = scanner.nextLine();
            writer.printf("Case #%1$d: %2$s\n",i, solveInternal(s.toLowerCase()));
        }
    }

    private String solveInternal(String s) {
        Machine machine = new Machine();
        try {
            for(int i=0; i<s.length(); i++)
                machine.next(s.charAt(i));
            machine.finalParenthesisCheck();
        } catch (UnbalancedException ue) {
            return "NO";
        }
        return "YES";
    }

    private enum States {
        BALANCED, COLON, OPEN
    }

    private class UnbalancedException extends Exception {
    }

    private class Machine {
        private States state;
        private int openParenthesis;
        private int closeAfterColon;
        private int openAfterColon;

        private Machine() {
            state = States.BALANCED;
            openParenthesis = 0;
            closeAfterColon = 0;
            openAfterColon = 0;
        }

        private void next(char c) throws UnbalancedException {
            switch (state) {
                case BALANCED:
                    balancedNext(c);
                    break;

                case COLON:
                    colonNext(c);
                    break;

                case OPEN:
                    openNext(c);
                    break;
            }
        }

        private void balancedNext(char c) throws UnbalancedException {
            if ( ((c >= 'a') && (c <= 'z')) || (c == ' ')) {
                return;
            }
            if (c == ':') {
                state = States.COLON;
                return;
            }
            if (c == '(') {
                state = States.OPEN;
                incParenthesis();
                return;
            }
            if (c == ')') {
                decParenthesis();
                return;
            }
            throw new UnbalancedException();
        }

        private void colonNext(char c) throws UnbalancedException {
            if (c == '(') {
                openAfterColon++;
                state = States.BALANCED;
                return;
            }
            if (c == ')') {
                if (openParenthesis > 0)
                    closeAfterColon++;
                state = States.BALANCED;
                return;
            }
            if (c == ':') {
                return;
            }
            if ( ((c >= 'a') && (c <= 'z')) || (c == ' ')) {
                state = States.BALANCED;
                return;
            }
            throw new UnbalancedException();
        }

        private void openNext(char c) throws UnbalancedException {
            if (c == '(') {
                incParenthesis();
                return;
            }
            if (c == ')') {
                state = States.BALANCED;
                decParenthesis();
                return;
            }
            if (c == ':') {
                state = States.COLON;
                return;
            }
            if ( ((c >= 'a') && (c <= 'z')) || (c == ' ')) {
                state = States.BALANCED;
                return;
            }
            throw new UnbalancedException();
        }

        private void incParenthesis() throws UnbalancedException {
            openParenthesis++;
        }

        private void decParenthesis() throws UnbalancedException {
            openParenthesis--;
            postDecParenthesisCheck();
        }

        private void postDecParenthesisCheck() throws UnbalancedException {
            if (openParenthesis < 0) {
                if (closeAfterColon > 0) {
                    closeAfterColon--;
                    openParenthesis++;
                } else if (openAfterColon > 0) {
                    openAfterColon--;
                    openParenthesis++;
                }
            }
            if (openParenthesis < 0)
                throw new UnbalancedException();
        }

        private void finalParenthesisCheck() throws UnbalancedException {
            while ((openParenthesis > 0) && (closeAfterColon > 0)) {
                closeAfterColon--;
                openParenthesis--;
            }
            if (openParenthesis > 0)
                throw new UnbalancedException();
        }
    }
}


