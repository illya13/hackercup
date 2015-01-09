package gattaca;

public class GattaceTestCasesGenerator {

    public static void main(String[] args) throws java.io.IOException {
        int n = 0;
        int g = 0;
        long seed = 0;
        try {
            seed = Long.parseLong(args[2]);
            n = Integer.parseInt(args[0]);
            g = Integer.parseInt(args[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
        } catch (NumberFormatException e) {
        }
        if (n <= 0 || g <= 0) {
            System.err.println("usage: java GatTest n g seed");
            System.exit(1);
        }
        java.util.Random r = new java.util.Random(seed);
        java.io.BufferedWriter out = new java.io.BufferedWriter(new java.io.OutputStreamWriter(System.out));
        out.write(String.valueOf(n));
        for (int i = 0; i < n; ++i) {
            if (i % 80 == 0) {
                out.newLine();
            }
            out.write("ACGT".charAt(r.nextInt(4)));
        }
        out.newLine();
        out.write(String.valueOf(g));
        out.newLine();
        int maxdist = (int) Math.round(Math.ceil(Math.sqrt(n)));
        for (int j = 0; j < g; ++j) {
            int dist = r.nextInt(maxdist);
            int start = r.nextInt(n - dist);
            out.write(start + " " + (start + dist) + "\t" + (1 + r.nextInt(1 + dist)));
            out.newLine();
        }
        out.close();
    }
}