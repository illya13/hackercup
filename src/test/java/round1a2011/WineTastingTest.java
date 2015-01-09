package round1a2011;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

public class WineTastingTest {
    private static final String INPUTDIR = "src/main/resources/round1a2011";

    private FileInputStream is;
    private OutputStream os;

    private WineTasting problem;
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
        is = initInputStream("A-sample.in");

        problem = new WineTasting(is, os);
        problem.solve();
        problem.close();

        Assert.assertEquals("" +
                "1\n" +
                "7\n" +
                "1\n" +
                "651\n" +
                "405146859\n",
                os.toString());
    }

    private static FileInputStream initInputStream(String fileName) throws FileNotFoundException {
        File inputDir = new File(INPUTDIR);
        File inputFile = new File(inputDir, fileName);
        FileInputStream is = new FileInputStream(inputFile);
        return is;
    }
}

