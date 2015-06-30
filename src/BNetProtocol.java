import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.TimeZone;

public class BNetProtocol {

    protected Socket socket = null;
//    private BNetInputStream BNetInputStream = null;
    private DataOutputStream BNetOutputStream = null;

    private int verByte;

    protected Socket makeSocket (String address, int port) throws UnknownHostException, IOException {
        Socket s;
        InetAddress addr = InetAddress.getByName(address);
        s = new Socket(addr, port);
        s.setKeepAlive(true);
        return s;
    }

    public BNetProtocol() {}

    public void connect() throws Exception {
        socket = makeSocket("119.194.195.251", 5004);
//        bncsInputStream = new BNetInputStream(socket.getInputStream());
        BNetOutputStream = new DataOutputStream(socket.getOutputStream());
        // Socket timeout will cause SocketTimeoutException to be thrown from read()
        socket.setSoTimeout(1000);

        // Game
        BNetOutputStream.writeByte(0x01);

        System.out.println("Connect to Server");

        Authorization();
    }

    public void Authorization() throws IOException {
        BNetProtocolPacket p;
        int tzBias = TimeZone.getDefault().getOffset(System.currentTimeMillis()) / -60000;

        p = new BNetProtocolPacket(BNetProtocolPacketId.SID_AUTH_INFO);
        p.writeDWord(0);
        p.writeDWord(0x49583836); // Platform IX86
        p.writeDWord(0x57335850); // Warcraft III
        p.writeDWord(0x00000000); // Version byte
        p.writeDWord("koKR");
        p.writeDWord(0); // Local IP
        p.writeDWord(tzBias); // TZ bias
        p.writeDWord(0x412); // Locale ID
        p.writeDWord(0x412); // Language ID
        p.writeNTString("KOR"); // Country abreviation
        p.writeNTString("Korea"); // Country
        p.sendPacket(BNetOutputStream);
    }

}
