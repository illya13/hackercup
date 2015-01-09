package qualification2014;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import qualification.Auction;

import java.io.*;

public class AuctionTest {
    private static final String INPUTDIR = "src/main/resources/qualification2014";

    private FileInputStream is;
    private OutputStream os;

    private Auction problem;
    private long time;

    @Before
    public void before() {
        os = new ByteArrayOutputStream();
        time = System.nanoTime();
    }

    @After
    public void after() throws Exception {
        time = System.nanoTime() - time;

        is.close();
        os.close();

        System.out.printf("%1$f ms\n", time/1E6);
    }

    @Test
    public void testSample() throws Exception {
        is = initInputStream("B-sample.in");

        problem = new Auction(is, os);
        problem.solve();
        problem.close();

        Assert.assertEquals("" +
                "Case #1: 3 3\n" +
                "Case #2: 3 3\n" +
                "Case #3: 2 3\n" +
                "Case #4: 2 2\n" +
                "Case #5: 3 1\n",
                os.toString());
    }

    private static FileInputStream initInputStream(String fileName) throws FileNotFoundException {
        File inputDir = new File(INPUTDIR);
        File inputFile = new File(inputDir, fileName);
        FileInputStream is = new FileInputStream(inputFile);
        return is;
    }
}

