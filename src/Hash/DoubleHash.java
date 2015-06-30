package Hash;

public class DoubleHash {
    static public int[] doubleHash(String str, int clientToken, int serverToken) {
        Buffer initialHash = new Buffer();
        initialHash.addNTString(str);
        int[] hash1 = BrokenSHA1.calcHashBuffer(initialHash.getBuffer());

        Buffer secondHash = new Buffer();
        secondHash.add(clientToken);
        secondHash.add(serverToken);
        for (int i = 0; i < 5; i++)
            secondHash.add(hash1[i]);

        return BrokenSHA1.calcHashBuffer(secondHash.getBuffer());
    }
}
