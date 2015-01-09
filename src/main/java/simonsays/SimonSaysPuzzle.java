package simonsays;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import simonsays.gen.SimonSays;

import java.awt.datatransfer.Transferable;
import java.util.List;

public class SimonSaysPuzzle {
    // general part
    private static final int SOCKET_TIMEOUT = 60000;
    private static final String PUBLIC_THRIFT_PUZZLE_SERVER = "thriftpuzzle.facebook.com";
    private static final int SIMON_SAYS_PORT = 9030;
    private static final String MY_EMAIL = "illya.havsiyevych@gmail.com";

    private TTransport transport;
    private TProtocol protocol;
    private SimonSays.Client client;

    public SimonSaysPuzzle() {
        this(PUBLIC_THRIFT_PUZZLE_SERVER, SIMON_SAYS_PORT);
    }

    public SimonSaysPuzzle(String host, int port) {
        //setup the transport and protocol
        transport = new TSocket(host, port);
        protocol = new TBinaryProtocol(transport);

        client = new SimonSays.Client(protocol);
    }

    private void open() {
        // the transport must be opened before you can begin using
        try {
            transport.open();
        } catch (TTransportException e) {
            throw new IllegalStateException(e);
        }
    }

    private void close() {
        transport.close();
    }

    private static void runTest() throws Exception {
        SimonSaysPuzzle problem = new SimonSaysPuzzle();
        problem.open();
        try {
            problem.solve();
        } catch (TException e) {
            throw new IllegalStateException(e);
        }
        problem.close();
    }

    public static void main(String[] args) {
        try {
            runTest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // problem part

    /**
     * Solve the problem
     */
    public void solve() throws TException{
        boolean good = client.registerClient(MY_EMAIL);
        if (!good) {
            System.out.printf("failed to registerClient\n");
            return;
        }

        boolean win = false;
        do {
            List<Integer> colors = client.startTurn();
            System.out.printf("%1$d\n", colors.size());
            for(int color: colors) {
                good = client.chooseColor(color);
                if (!good)
                    break;
            }
            win = client.endTurn();
        }
        while (!win);
        System.out.printf("we win\n");

        String string = client.winGame();
        System.out.printf("%1$s\n", string);
    }
}