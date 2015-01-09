package qualification2011;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

public class PegGameTest {
    private static final String INPUTDIR = "src/main/resources/qualification2011";

    private FileInputStream is;
    private OutputStream os;

    private PegGame problem;
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

        problem = new PegGame(is, os);
        problem.solve();
        problem.close();

        Assert.assertEquals("" +
                "0 0.375000\n" +
                "1 0.500000\n" +
                "1 1.000000\n" +
                "0 1.000000\n" +
                "0 0.500000\n" +
                "0 0.500000\n" +
                "0 0.500000\n" +
                "0 1.000000\n" +
                "2 0.375000\n" +
                "2 1.000000\n" +
                "2 0.500000\n",
                os.toString());
    }

    private static FileInputStream initInputStream(String fileName) throws FileNotFoundException {
        File inputDir = new File(INPUTDIR);
        File inputFile = new File(inputDir, fileName);
        FileInputStream is = new FileInputStream(inputFile);
        return is;
    }
}

