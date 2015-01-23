package round1_2012;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import round1_2012.RecoverTheSequence;

import java.io.*;
import java.util.Arrays;
import java.util.Queue;
import java.util.Stack;

public class RecoverTheSequenceTest {
    private static final String INPUTDIR = "src/main/resources/round1_2014";

    private FileInputStream is;
    private OutputStream os;

    private RecoverTheSequence problem;
    private long time;

    class RecoverTheSequenceForTest extends RecoverTheSequence {
        public RecoverTheSequenceForTest(InputStream is, OutputStream os) {
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

    private static FileInputStream initInputStream(String fileName) throws FileNotFoundException {
        File inputDir = new File(INPUTDIR);
        File inputFile = new File(inputDir, fileName);
        FileInputStream is = new FileInputStream(inputFile);
        return is;
    }

    @Test
    public void testSample() throws Exception {
        is = initInputStream("B-sample.in");

        problem = new RecoverTheSequence(is, os);
        problem.solve();
        problem.close();

        Assert.assertEquals("" +
                "Case #1: 994\n" +
                "Case #2: 1024\n" +
                "Case #3: 987041\n" +
                "Case #4: 570316\n" +
                "Case #5: 940812\n",
                os.toString());
    }

    @Test
    public void testRestoreMerge() throws Exception {
        is = initInputStream("B-sample.in");
        RecoverTheSequenceForTest test = new RecoverTheSequenceForTest(is, os);

        int[] a = new int[1];
        int[] b = new int[2];
        Queue<Character> queue = test.initQueue("22");
        test.restoreMerge(new int[]{1, 3, 4}, queue, a, b);
        Assert.assertArrayEquals(new int[]{4}, a);
        Assert.assertArrayEquals(new int[]{1, 3}, b);
        Assert.assertEquals(0, queue.size());

        a = new int[1];
        b = new int[2];
        queue = test.initQueue("212");
        test.restoreMerge(new int[]{1, 3, 4}, queue, a, b);
        Assert.assertArrayEquals(new int[]{3}, a);
        Assert.assertArrayEquals(new int[]{1, 4}, b);
        Assert.assertEquals(1, queue.size());
        Assert.assertTrue(Arrays.equals(new Object[]{new Character('2')}, queue.toArray()));

        a = new int[1];
        b = new int[2];
        queue = test.initQueue("121");
        test.restoreMerge(new int[]{1, 3, 4}, queue, a, b);
        Assert.assertArrayEquals(new int[]{1}, a);
        Assert.assertArrayEquals(new int[]{3, 4}, b);
        Assert.assertEquals(2, queue.size());
        Assert.assertTrue(Arrays.equals(new Object[]{new Character('2'), new Character('1')}, queue.toArray()));

        a = new int[1];
        b = new int[2];
        queue = test.initQueue("111");
        test.restoreMerge(new int[]{1, 3, 4}, queue, a, b);
        Assert.assertArrayEquals(new int[]{1}, a);
        Assert.assertArrayEquals(new int[]{3, 4}, b);
        Assert.assertEquals(2, queue.size());
        Assert.assertTrue(Arrays.equals(new Object[]{new Character('1'), new Character('1')}, queue.toArray()));

        a = new int[1];
        b = new int[1];
        queue = test.initQueue("111");
        test.restoreMerge(new int[]{1, 2}, queue, a, b);
        Assert.assertArrayEquals(new int[]{1}, a);
        Assert.assertArrayEquals(new int[]{2}, b);
        Assert.assertEquals(2, queue.size());
        Assert.assertTrue(Arrays.equals(new Object[]{new Character('1'), new Character('1')}, queue.toArray()));

        a = new int[1];
        b = new int[1];
        queue = test.initQueue("211");
        test.restoreMerge(new int[]{1, 2}, queue, a, b);
        Assert.assertArrayEquals(new int[]{2}, a);
        Assert.assertArrayEquals(new int[]{1}, b);
        Assert.assertEquals(2, queue.size());
        Assert.assertTrue(Arrays.equals(new Object[]{new Character('1'), new Character('1')}, queue.toArray()));
    }

    @Test
    public void testCalculateSequences() throws Exception {
        is = initInputStream("B-sample.in");
        RecoverTheSequenceForTest test = new RecoverTheSequenceForTest(is, os);

        Stack<Queue<Character>> stack = new Stack<Queue<Character>>();
        test.calculateSequences(4, test.initQueue("12212"), stack);
        Assert.assertEquals(3, stack.size());

        stack = new Stack<Queue<Character>>();
        test.calculateSequences(6, test.initQueue("2112111221"), stack);
        Assert.assertEquals(5, stack.size());
    }


    @Test
    public void testChecksum() throws Exception {
        is = initInputStream("B-sample.in");
        RecoverTheSequenceForTest test = new RecoverTheSequenceForTest(is, os);

        Assert.assertEquals(994, test.checksum(new int[]{1, 2}));
        Assert.assertEquals(1024, test.checksum(new int[]{2, 1}));
        Assert.assertEquals(987041, test.checksum(new int[]{2, 4, 3, 1}));
        Assert.assertEquals(955331, test.checksum(new int[]{1, 2, 3, 4}));
        Assert.assertEquals(748602, test.checksum(new int[]{1, 2, 3, 4, 5, 6, 7, 8}));
    }

    @Test
    public void testGetMid() throws Exception {
        is = initInputStream("B-sample.in");
        RecoverTheSequenceForTest test = new RecoverTheSequenceForTest(is, os);

        Assert.assertEquals(2, test.getMid(5));
        Assert.assertEquals(56, test.getMid(113));
    }
}

