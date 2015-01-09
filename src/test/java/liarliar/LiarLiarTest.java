package liarliar;

import java.io.*;
import org.junit.*;

public class LiarLiarTest {
    private static final String INPUT = "src/test/config/liarliar";

    private FileInputStream is;
    private OutputStream os;

    private LiarLiar problem;
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
        is = initInputStream("sample.in");

        problem = new LiarLiar(is, os);
        problem.solve();
        problem.close();

        Assert.assertEquals("3 2\n", os.toString());
    }

    @Test
    public void testOriginal() throws Exception {
        is = initInputStream("original.in");

        problem = new LiarLiar(is, os);
        problem.solve();
        problem.close();

        Assert.assertEquals("3 2\n", os.toString());
    }

    @Test
    public void test1() throws Exception {
        is = initInputStream("1.in");

        problem = new LiarLiar(is, os);
        problem.solve();
        problem.close();

        Assert.assertEquals("2 2\n", os.toString());
    }

    @Test
    public void test1k100() throws Exception {
        is = initInputStream("1k_100.in");

        problem = new LiarLiar(is, os);
        problem.solve();
        problem.close();

        Assert.assertEquals("500 500\n", os.toString());
    }

    @Test
    public void test1k() throws Exception {
        is = initInputStream("1k.in");

        problem = new LiarLiar(is, os);
        problem.solve();
        problem.close();

        Assert.assertEquals("500 500\n", os.toString());
    }

    @Test
    public void test2() throws Exception {
        is = initInputStream("2.in");

        problem = new LiarLiar(is, os);
        problem.solve();
        problem.close();

        Assert.assertEquals("50091 49909\n", os.toString());
    }

    @Test
    public void testAbhishek() throws Exception {
        is = initInputStream("abhishek.in");

        problem = new LiarLiar(is, os);
        problem.solve();
        problem.close();

        Assert.assertEquals("2 2\n", os.toString());
    }

    @Test
    public void testDavid() throws Exception {
        is = initInputStream("david.in");

        problem = new LiarLiar(is, os);
        problem.solve();
        problem.close();

        Assert.assertEquals("12 8\n", os.toString());
    }

    private static FileInputStream initInputStream(String fileName) throws FileNotFoundException {
        File inputDir = new File(INPUT);
        File inputFile = new File(inputDir, fileName);
        FileInputStream is = new FileInputStream(inputFile);
        return is;
    }
}
