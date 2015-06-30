import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;

public abstract class _Packet<P extends BNetProtocolPacketId> extends BNetOutputStream {
    private final P packetId;
    public _Packet(P packetId) {
        super(new ByteArrayOutputStream());
        this.packetId = packetId;
    }

    public void sendPacket(OutputStream out) throws IOException, SocketException {
        byte data[] = ((ByteArrayOutputStream)this.out).toByteArray();

        data = buildPacket(packetId, data);

        try {
            out.write(data);
            out.flush();
        } catch(SocketException e) {
            e.printStackTrace();
        }
    }

    protected abstract byte[] buildPacket(P packetId, byte[] data);
}
