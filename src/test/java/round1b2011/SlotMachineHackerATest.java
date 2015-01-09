package round1b2011;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

public class SlotMachineHackerATest {
    private static final String INPUTDIR = "src/main/resources/round1b2011";

    private FileInputStream is;
    private OutputStream os;

    private SlotMachineHacker problem;
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

        problem = new SlotMachineHacker(is, os);
        problem.solve();
        problem.close();

        Assert.assertEquals("" +
                "Not enough observations\n" +
                "577 428 402 291 252 544 735 545 771 34\n" +
                "762 18 98 703 456 676 621 291 488 332\n" +
                "38 802 434 531 725 594 86 921 607 35\n" +
                "Wrong machine\n",
                os.toString());
    }

    private static FileInputStream initInputStream(String fileName) throws FileNotFoundException {
        File inputDir = new File(INPUTDIR);
        File inputFile = new File(inputDir, fileName);
        FileInputStream is = new FileInputStream(inputFile);
        return is;
    }
}

