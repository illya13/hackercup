package meepmeep;

import java.io.*;
import java.util.Scanner;

public class MeepMeep {
    // general part
    private static final String INPUT = "./";

    private Scanner scanner;
    private PrintWriter writer;

    public MeepMeep(InputStream is, OutputStream os) {
        scanner = new Scanner(is);
        writer = new PrintWriter(os);
    }

    public void close() {
        scanner.close();
        writer.flush();
    }

    private static void runTest(String fileName) throws Exception {
        InputStream is = initInputStream(fileName);
        OutputStream os = initOutputStream();

        MeepMeep problem = new MeepMeep(is, os);
        problem.solve();
        problem.close();

        doneStreams(is);
    }

    private static InputStream initInputStream(String fileName) throws FileNotFoundException {
        File inputDir = new File(INPUT);
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
        } catch (Throwable e) {
            System.out.println("Meep meep!");
        }
    }

    // problem part

    /**
     * Solve the problem
     */
    public void solve() {
        writer.println("Meep meep!");
    }
}
