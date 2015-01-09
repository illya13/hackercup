package qualification2011;

import java.io.*;
import java.util.*;

public class StudiousStudent {
    // general part
    private static final String INPUTDIR = "src/main/resources";
    private static final String OUTPUTDIR = "target/output";
    private static final String ROUND = "qualification2011";

    private static final String SAMPLE = "C-sample.in";
    private static final String INPUT = "C-input.in";

    private Scanner scanner;
    private PrintWriter writer;

    public StudiousStudent(InputStream is, OutputStream os) {
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

        StudiousStudent problem = new StudiousStudent(is, os);
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
        // 1 ? N ? 100
        int n = scanner.nextInt();

        for (int i = 1; i <= n; i++) {
            // 1 <= M <= 9
            int m = scanner.nextInt();

            List<String> list = new LinkedList<String>();
            for (int j=0; j<m; j++) {
                String string = scanner.next();
                list.add(string);
            }
            writer.printf("%1$s\n", solve(list));
        }
    }

    private String solve(List<String> list) {
        Collections.sort(list);
        String[] array = new String[list.size()];
        list.toArray(array);

        String min = null;

        for(int i=0; i<array.length; i++) {
            for(int j=i; j<array.length; j++) {
                // swap i, j, if i==j no need to swap

                StringBuilder sb = new StringBuilder();
                for(int k=0; k<array.length; k++)
                    if (k==i)
                        sb.append(array[j]);
                    else if (k==j)
                        sb.append(array[i]);
                    else
                        sb.append(array[k]);

                String string = sb.toString();
                if (min == null)
                    min = string;
                else if (string.compareTo(min) < 0)
                    min = string;
            }
        }
        return min;
    }

}

