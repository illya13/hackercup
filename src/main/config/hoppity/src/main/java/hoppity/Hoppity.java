package hoppity;

import java.io.*;
import java.util.Scanner;

public class Hoppity {
    // general part
    private static final String INPUT = "./";

    private Scanner scanner;
    private PrintWriter writer;

    public Hoppity(InputStream is, OutputStream os) {
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

        Hoppity problem = new Hoppity(is, os);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // problem part

    /**
     * Solve the problem
     */
    public void solve() {
        int n= scanner.nextInt();

        for (int i=1; i<=n; i++)
            solve(i);
    }

    private void solve(int i) {
        int mod3 = i % 3;
        int mod5 = i % 5;
        if ((mod3 == 0) && (mod5 == 0))
            writer.println("Hop");
        else if (mod3 == 0)
            writer.println("Hoppity");
        else if (mod5 == 0)
            writer.println("Hophop");
    }
}
