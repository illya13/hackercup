package qualification2015;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

public class CookingTheBooksTest {
    private static final String INPUTDIR = "src/main/resources/qualification";

    private FileInputStream is;
    private OutputStream os;

    private CookingTheBooks problem;
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

        problem = new CookingTheBooks(is, os);
        problem.solve();
        problem.close();

        Assert.assertEquals("" +
                        "Case #1: 13524 51324\n" +
                        "Case #2: 798 987\n" +
                        "Case #3: 123 321\n" +
                        "Case #4: 10 10\n" +
                        "Case #5: 5 5\n" +
                        "Case #6: 798 987\n" +
                        "Case #7: 123 312\n"+
                        "Case #8: 123456789 923456781\n" +
                        "Case #9: 187654329 987654321\n" +
                        "Case #10: 103456789 903456781\n" +
                        "Case #11: 107654329 970654321\n" +
                        "Case #12: 103456789 904356781\n" +
                        "Case #13: 106754329 976054321\n" +
                        "Case #14: 100356784 804356710\n" +
                        "Case #15: 206754390 976054320\n",
                os.toString());
    }

    private static FileInputStream initInputStream(String fileName) throws FileNotFoundException {
        File inputDir = new File(INPUTDIR);
        File inputFile = new File(inputDir, fileName);
        FileInputStream is = new FileInputStream(inputFile);
        return is;
    }
}

