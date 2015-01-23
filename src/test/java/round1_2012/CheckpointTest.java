package round1_2012;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import round1_2012.Checkpoint;

import java.io.*;

public class CheckpointTest {
    private static final String INPUTDIR = "src/main/resources/round1_2014";

    private FileInputStream is;
    private OutputStream os;

    private Checkpoint problem;
    private long time;

    class CheckpointTestForTest extends Checkpoint {
        public CheckpointTestForTest(InputStream is, OutputStream os) {
            super(is, os);
        }
    }

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

        problem = new Checkpoint(is, os);
        problem.solve();
        problem.close();

        Assert.assertEquals("" +
                "Case #1: 4\n" +
                "Case #2: 6\n" +
                "Case #3: 6\n" +
                "Case #4: 9\n" +
                "Case #5: 2\n",
                os.toString());
    }

    private static FileInputStream initInputStream(String fileName) throws FileNotFoundException {
        File inputDir = new File(INPUTDIR);
        File inputFile = new File(inputDir, fileName);
        FileInputStream is = new FileInputStream(inputFile);
        return is;
    }

    @Test
    public void testC() throws Exception {
        is = initInputStream("A-sample.in");

        problem = new CheckpointTestForTest(is, os);

        problem.initDp();
        Assert.assertEquals(problem.MAX_S+1, problem.c(2, problem.MAGIC-1));

        Assert.assertEquals(495, problem.c(8, 4));
        Assert.assertEquals(495, problem.c(4, 8));
        Assert.assertEquals(3432, problem.c(7, 7));
        Assert.assertEquals(6435, problem.c(8, 7));
        Assert.assertEquals(184756, problem.c(10, 10));
    }


    @Test
    public void testS() throws Exception {
        is = initInputStream("A-sample.in");

        problem = new CheckpointTestForTest(is, os);

        problem.initDp();

        Assert.assertEquals(18, problem.s(3060));
        Assert.assertEquals(14, problem.s(3432));
        Assert.assertEquals(20, problem.s(184756));
    }
}

