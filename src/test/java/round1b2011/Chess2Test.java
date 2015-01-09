package round1b2011;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

public class Chess2Test {
    private static final String INPUTDIR = "src/main/resources/round1a2011";

    private FileInputStream is;
    private OutputStream os;

    private Chess2 problem;
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

        problem = new Chess2(is, os);
        problem.solve();
        problem.close();

        Assert.assertEquals("" +
                "siht is a ortsh encesent\n" +
                "siht oniptrycen is lame\n" +
                "racecar\n" +
                "tset msleobpr are ttypre easy to solve\n" +
                "q we rty uiop\n",
                os.toString());
    }

    private static FileInputStream initInputStream(String fileName) throws FileNotFoundException {
        File inputDir = new File(INPUTDIR);
        File inputFile = new File(inputDir, fileName);
        FileInputStream is = new FileInputStream(inputFile);
        return is;
    }
}

