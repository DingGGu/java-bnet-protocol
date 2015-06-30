/**
 * Created by DingGGu on 2015-06-30.
 */
public class example {
    public static void main(String[] args) {
        BNetProtocol bNetProtocol = new BNetProtocol();
        try {
            bNetProtocol.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(BNetProtocolPacketId.SID_NULL);
    }

}
