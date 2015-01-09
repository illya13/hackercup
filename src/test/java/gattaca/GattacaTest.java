package gattaca;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

public class GattacaTest {
    private static final String INPUT = "src/test/config/gattaca";

    private FileInputStream is;
    private OutputStream os;

    private Gattaca problem;
    private long time;

    @Before
    public void before() {
        is = null;
        os = new ByteArrayOutputStream();
        time = System.nanoTime();
    }

    @After
    public void after() throws Exception {
        time = System.nanoTime() - time;

        if (is != null)
            is.close();
        os.close();

        System.out.printf("%1$f ms\n", time/1E6);
    }

    @Test
    public void testOriginal() throws Exception {
        is = initInputStream("original.in");

        problem = new Gattaca(is, os);
        problem.solve();
        problem.close();

        Assert.assertEquals("100\n", os.toString());
    }

    @Test
    public void testSmall() throws Exception {
        is = initInputStream("small.in");

        problem = new Gattaca(is, os);
        problem.solve();
        problem.close();

        Assert.assertEquals("50\n", os.toString());
    }

    @Test
    public void testSmall2() throws Exception {
        is = initInputStream("small2.in");

        problem = new Gattaca(is, os);
        problem.solve();
        problem.close();

        Assert.assertEquals("14\n", os.toString());
    }

    @Test
    public void testSmall3() throws Exception {
        is = initInputStream("small3.in");

        problem = new Gattaca(is, os);
        problem.solve();
        problem.close();

        Assert.assertEquals("266\n", os.toString());
    }

    @Test
    public void test10k() throws Exception {
        is = initInputStream("10k.in");

        problem = new Gattaca(is, os);
        problem.solve();
        problem.close();

        Assert.assertEquals("79453\n", os.toString());
    }

    @Test
    public void test100k() throws Exception {
        is = initInputStream("100k.in");

        problem = new Gattaca(is, os);
        problem.solve();
        problem.close();

        Assert.assertEquals("94406\n", os.toString());
    }

    @Test
    public void test4() throws Exception {
        is = initInputStream("4.in");

        problem = new Gattaca(is, os);
        problem.solve();
        problem.close();

        Assert.assertEquals("968886\n", os.toString());
    }

    private static FileInputStream initInputStream(String fileName) throws FileNotFoundException {
        File inputDir = new File(INPUT);
        File inputFile = new File(inputDir, fileName);
        FileInputStream is = new FileInputStream(inputFile);
        return is;
    }
}
