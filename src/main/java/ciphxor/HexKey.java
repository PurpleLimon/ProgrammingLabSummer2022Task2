package ciphxor;

public class HexKey {

    private final short[] keyBytes;

    public HexKey(String key) {
        int pointer = 0;
        final int strLength = key.length();
        keyBytes = new short[strLength / 2 + strLength % 2];
        while (pointer < strLength) {
            short b = Short.parseShort(
                    pointer + 2 <= strLength?
                            key.substring(pointer, pointer + 2) :
                            Character.toString(key.charAt(strLength - 1)) + key.charAt(0),
                    16
            );
            keyBytes[pointer / 2] = b;
            pointer += 2;
        }
    }

    public short get(int index) throws IndexOutOfBoundsException {
        return keyBytes[index];
    }

    public int getLength() {
        return keyBytes.length;
    }

    public byte[] applyXorTo(byte[] input, int amount) {
        byte[] res = new byte[amount];
        for(int i = 0; i < amount; i++) {
            res[i] = (byte) (input[i] ^ get(i));
        }
        return res;
    }
}
