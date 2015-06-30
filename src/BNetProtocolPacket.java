/**
 * Created by DingGGu on 2015-07-01.
 */

public class BNetProtocolPacket extends _Packet<BNetProtocolPacketId> {

    public BNetProtocolPacket(BNetProtocolPacketId packetId) {
        super(packetId);
    }

    protected byte[] buildPacket(BNetProtocolPacketId packetId, byte[] data) {
        byte[] out = new byte[data.length+4];
        out[0] = (byte) 0xFF;
        out[1] = (byte) packetId.ordinal();
        out[2] = (byte) ((data.length + 4) & 0x00FF);
        out[3] = (byte) (((data.length + 4) & 0xFF00) >> 8);
        System.arraycopy(data, 0, out, 4, data.length);
        return out;
    }
}
