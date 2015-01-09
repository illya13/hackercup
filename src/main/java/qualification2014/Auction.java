package qualification2014;

import java.io.*;
import java.util.*;

public class Auction {
    // general part
    private static final String INPUTDIR = "src/main/resources";
    private static final String OUTPUTDIR = "target/output";
    private static final String ROUND = "qualification2014";

    private static final String SAMPLE = "B-sample.in";
    private static final String INPUT = "B-input.in";

    private Scanner scanner;
    private PrintWriter writer;

    public Auction(InputStream is, OutputStream os) {
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

        Auction problem = new Auction(is, os);
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

    private long n;
    private int p1, w1, m, k;
    private int a, b, c, d;

    private long bargainCount;
    private long terribleDealCount;    

    /**
     * Solve the problem
     */
    public void solve() {

        // 1 ≤ T ≤ 20
        int t = scanner.nextInt();

        for (int i = 1; i <= t; i++) {
            // N, P1, W1, M, K, A, B, C and D.
            // 1 ≤ N ≤ 10^18
            // 1 ≤ M, K ≤ 10^7
            // 1 ≤ P1 ≤ M
            // 1 ≤ W1 ≤ K
            // 0 ≤ A,B,C,D ≤ 10^9

            /**
                @see Long.MAX_VALUE (9223372036854775807 ~ 10^19)
                @see Integer.MAX_VALUE (2147483647 ~ 10^9 )
             */

            n = scanner.nextLong();
            p1 = scanner.nextInt();
            w1 = scanner.nextInt();
            m = scanner.nextInt();
            k = scanner.nextInt();
            a = scanner.nextInt();
            b = scanner.nextInt();
            c = scanner.nextInt();
            d = scanner.nextInt();

            writer.printf("Case #%1$d: ", i);
            solveInternal();
        }
    }

    private void solveInternal() {
        //init set
        Map<Product, Long> map = new HashMap<Product, Long>();

        Product last = new Product(p1, w1);

        // init indexes
        long lastIndex = n;
        long firstIndex = 0;
        long reminderCount = 0;
        long mult = 1;
        
        for (long i=0; i<n; i++) {
            if (map.containsKey(last)) {

                // update indexes
                lastIndex = i;
                firstIndex = map.get(last);
                long diff = lastIndex - firstIndex;
                mult = (n - firstIndex) / diff;
                reminderCount = n - firstIndex - mult*diff;
                break;
            }
            map.put(last, i);
            last = last.next();
        }
        
        // init counts
        this.bargainCount = 0;
        this.terribleDealCount = 0;
        
        calculateBargainAndTerribleDealOverSet(map.keySet(), new Product(p1, w1), firstIndex, lastIndex);
        bargainCount *= mult;
        terribleDealCount *= mult;
        
        if (firstIndex > 0)
            calculateBargainAndTerribleDealOverSet(map.keySet(), new Product(p1, w1), 0, firstIndex);

        if (reminderCount > 0)
            calculateBargainAndTerribleDealOverSet(map.keySet(), new Product(p1, w1), firstIndex, firstIndex+reminderCount);
        writer.printf("%1$d %2$d\n", terribleDealCount, bargainCount);
    }

    private void calculateBargainAndTerribleDealOverSet(Set<Product> set, Product curr, long start, long end) {
        for (long i=start; i<end; i++) {
            boolean isBargain = true;
            boolean isTerribleDeal = true;

            Iterator<Product> j = set.iterator();
            while (j.hasNext()) {
                Product loop = j.next();
                if (curr != loop) {
                    isBargain = (isBargain) && (!loop.isBetter(curr));
                    isTerribleDeal = (isTerribleDeal) && (!curr.isBetter(loop));

                    if ((!isBargain) && (!isTerribleDeal))
                        break;
                }
            }
            if (isBargain)
                bargainCount++;
            if (isTerribleDeal)
                terribleDealCount++;

            curr = curr.next();
        }
    }
    
    class Product {
        public int p;
        public int w;

        Product(int p, int w) {
            this.p = p;
            this.w = w;
        }

        public Product next() {
            // Pi = ((A*Pi-1 + B) mod M) + 1   (for all i = 2..N)
            // Wi = ((C*Wi-1 + D) mod K) + 1   (for all i = 2..N)

            long p_ = ((a*p + b) % m) + 1;
            long w_ = ((c*w + d) % k) + 1;
            return new Product((int)p_, (int)w_);
        }

        public boolean isBetter(Product product) {
            return ( (p < product.p) && (w <= product.w) ) ||
                    ( (w < product.w) && (p <= product.p) );
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;

            if ( (o == null) || (getClass() != o.getClass()) )
                return false;

            Product product = (Product) o;
            if (p != product.p)
                return false;
            if (w != product.w)
                return false;
            return true;
        }

        @Override
        public int hashCode() {
            int result = p;
            result = 31 * result + w;
            return result;
        }

        @Override
        public String toString() {
            return "{" + p + ", " + w + '}';
        }
    }
}
