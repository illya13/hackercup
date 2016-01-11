package qualification;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

public class BoomerangConstellationsTest {
    private static final String INPUTDIR = "src/main/resources/qualification";

    private FileInputStream is;
    private OutputStream os;

    private BoomerangConstellations problem;
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

        problem = new BoomerangConstellations(is, os);
        problem.solve();
        problem.close();

        Assert.assertEquals("" +
                "Case #1: 0\n" +
                "Case #2: 4\n" +
                "Case #3: 4\n" +
                "Case #4: 3\n" +
                "Case #5: 12\n",
                os.toString());
    }

    private static FileInputStream initInputStream(String fileName) throws FileNotFoundException {
        File inputDir = new File(INPUTDIR);
        File inputFile = new File(inputDir, fileName);
        FileInputStream is = new FileInputStream(inputFile);
        return is;
    }
}

