package breathalyzer;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

public class BreathalyzerTest {
    private static final String INPUT = "src/test/config/breathalyzer";

    private InputStream is;
    private InputStream tw;
    private OutputStream os;

    private Breathalyzer problem;
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
        tw = initInputStream("twl06.txt");

        problem = new Breathalyzer(is, os, tw);
        problem.solve();
        problem.close();

        Assert.assertEquals("8\n", os.toString());
    }

    @Test
    public void testSame() throws Exception {
        is = initInputStream("same.in");
        tw = initInputStream("twl06.txt");

        problem = new Breathalyzer(is, os, tw);
        problem.solve();
        problem.close();

        Assert.assertEquals("0\n", os.toString());
    }

    @Test
    public void testDavid() throws Exception {
        is = initInputStream("david.in");
        tw = initInputStream("twl06.txt");

        problem = new Breathalyzer(is, os, tw);
        problem.solve();
        problem.close();

        Assert.assertEquals("22719\n", os.toString());
    }

    @Test
    public void testDavid_() throws Exception {
        is = initInputStream("david_.in");
        tw = initInputStream("twl06.txt");

        problem = new Breathalyzer(is, os, tw);
        problem.solve();
        problem.close();

        Assert.assertEquals("121\n", os.toString());
    }

    @Test
    public void testEmpty() throws Exception {
        is = initInputStream("empty.in");
        tw = initInputStream("twl06.txt");

        problem = new Breathalyzer(is, os, tw);
        problem.solve();
        problem.close();

        Assert.assertEquals("0\n", os.toString());
    }

    @Test
    public void testLetters() throws Exception {
        is = initInputStream("letters.in");
        tw = initInputStream("twl06.txt");

        problem = new Breathalyzer(is, os, tw);
        problem.solve();
        problem.close();

        Assert.assertEquals("28\n", os.toString());
    }

    @Test
    public void test187() throws Exception {
        is = initInputStream("187.in");
        tw = initInputStream("twl06.txt");

        problem = new Breathalyzer(is, os, tw);
        problem.solve();
        problem.close();

        Assert.assertEquals("187\n", os.toString());
    }

    @Test
    public void test63() throws Exception {
        is = initInputStream("63.in");
        tw = initInputStream("twl06.txt");

        problem = new Breathalyzer(is, os, tw);
        problem.solve();
        problem.close();

        Assert.assertEquals("63\n", os.toString());
    }

    @Test
    public void testLol() throws Exception {
        is = initInputStream("lol.in");
        tw = initInputStream("twl06.txt");

        problem = new Breathalyzer(is, os, tw);
        problem.solve();
        problem.close();

        Assert.assertEquals("86\n", os.toString());
    }

    @Test
    public void testLevenshtein() throws Exception {
        Assert.assertEquals(2, Breathalyzer.levenshtein("GUMBO", "GAMBOL", Integer.MAX_VALUE));
        Assert.assertEquals(3, Breathalyzer.levenshtein("kitten", "sitting", Integer.MAX_VALUE));
        Assert.assertEquals(1, Breathalyzer.levenshtein("abcd", "abce", Integer.MAX_VALUE));
        Assert.assertEquals(3, Breathalyzer.levenshtein("abcd", "xyzd", Integer.MAX_VALUE));
        Assert.assertEquals(4, Breathalyzer.levenshtein("abcdef", "ef", Integer.MAX_VALUE));
    }

    private static InputStream initInputStream(String fileName) throws FileNotFoundException {
        File inputDir = new File(INPUT);
        File inputFile = new File(inputDir, fileName);
        InputStream is = new FileInputStream(inputFile);
        return is;
    }
}
