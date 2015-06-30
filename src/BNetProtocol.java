import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.TimeZone;

public class BNetProtocol {

    protected Socket socket = null;
    private BNetInputStream BNInputStream = null;
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

    public void BNetConnect() throws Exception {
        socket = makeSocket("119.194.195.251", 5004);
        BNInputStream = new BNetInputStream(socket.getInputStream());
        BNetOutputStream = new DataOutputStream(socket.getOutputStream());

        socket.setSoTimeout(1000);

        BNetOutputStream.writeByte(0x01);

        System.out.println("Connect to Server");
    }

    public void BNetAuthInfo() throws IOException {
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

    private BNetPacketReader obtainPacket() throws IOException {
        byte magic;
        do {
            magic = BNInputStream.readByte();
        } while(magic != (byte)0xFF);
        try {
            return new BNetPacketReader(BNInputStream);
        } catch(SocketTimeoutException e) {
            throw new IOException("Unexpected socket timeout while reading packet", e);
        }
    }

    public void BNetLogin() throws Exception {
        while (!socket.isClosed()) {
            BNetPacketReader pr;
            try {
                pr = obtainPacket();
            } catch (SocketTimeoutException e) {
                continue;
            } catch (SocketException e) {
                if (socket == null) break;
                if (socket.isClosed()) break;
                throw e;
            }

            BNetInputStream is = pr.getData();
            switch (pr.packetId) {
                case SID_OPTIONALWORK:
                case SID_EXTRAWORK:
                case SID_REQUIREDWORK:
                    break;
                case SID_NULL: {
                    BNetProtocolPacket p = new BNetProtocolPacket(BNetProtocolPacketId.SID_NULL);
                    p.sendPacket(BNetOutputStream);
                    break;
                }

                case SID_PING: {
                    BNetProtocolPacket p = new BNetProtocolPacket(BNetProtocolPacketId.SID_PING);
                    p.writeDWord(is.readDWord());
                    p.sendPacket(BNetOutputStream);
                    break;
                }

            }
        }
    }

}
