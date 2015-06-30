import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by DingGGu on 2015-07-01.
 */
public abstract class _PacketReader<P extends _PacketId> {
    public P packetId;
    public int packetLength;
    public byte data[];

    public _PacketReader(BNetInputStream bnis) throws IOException {
        parse(bnis);
    }

    protected abstract void parse(BNetInputStream is) throws IOException;

    public BNetInputStream getData() {
        return new BNetInputStream(new ByteArrayInputStream(data));
    }
}