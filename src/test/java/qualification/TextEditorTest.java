package qualification;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

public class TextEditorTest {
    private static final String INPUTDIR = "src/main/resources/qualification";

    private FileInputStream is;
    private OutputStream os;

    private TextEditor problem;
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
        is = initInputStream("D-sample.in");

        problem = new TextEditor(is, os);
        problem.solve();
        problem.close();

        Assert.assertEquals("" +
                        "Case #1: 9\n" +
                        "Case #2: 9\n" +
                        "Case #3: 11\n" +
                        "Case #4: 15\n" +
                        "Case #5: 26\n",
                os.toString());
    }

    private static FileInputStream initInputStream(String fileName) throws FileNotFoundException {
        File inputDir = new File(INPUTDIR);
        File inputFile = new File(inputDir, fileName);
        FileInputStream is = new FileInputStream(inputFile);
        return is;
    }
}

