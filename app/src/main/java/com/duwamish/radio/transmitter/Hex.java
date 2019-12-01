package com.duwamish.radio.transmitter;

public class Hex {
    /**
     * bytesToHex method
     */
    private static final char[] HEX_ARRAY_UPPERCASE = "0123456789ABCDEF".toCharArray();
    private static final char[] HEX_ARRAY_LOWERCASE = "0123456789abcdef".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY_UPPERCASE[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY_UPPERCASE[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String toHexString(byte[] dataBytes, int from, int to) {
        int mn = Math.min(dataBytes.length - from, to);
        char[] array = new char[to * 2];

        for (int index = 0; index < mn; ++index) {
            array[2 * index + 0] = HEX_ARRAY_UPPERCASE[dataBytes[index + from] >>> 4 & 15];
            array[2 * index + 1] = HEX_ARRAY_UPPERCASE[dataBytes[index + from] >>> 0 & 15];
        }

        return new String(array);
    }

    public static String toHexStringv3(byte[] data, boolean upper) {
        if (data == null) {
            return null;
        }

        char[] table = (upper ? HEX_ARRAY_UPPERCASE : HEX_ARRAY_LOWERCASE);
        char[] chars = new char[data.length * 2];

        for (int i = 0; i < data.length; ++i) {
            chars[i * 2    ] = table[ (data[i] & 0xF0) >> 4 ];
            chars[i * 2 + 1] = table[ (data[i] & 0x0F)      ];
        }

        return new String(chars);
    }

    public static int extract(byte[] byteString, int from, int to) {
        return (byteString[from] & 0xff) * 0x100 + (byteString[to] & 0xff);
    }

    public static boolean isEddy(byte[] advertisementPacket) {
        return (advertisementPacket[0] & 0xFF) == 0xAA && (advertisementPacket[1] & 0xFF) == 0xFE;
    }

    public static boolean isNotEddy(byte[] advertisementPacket) {
        return (advertisementPacket[0] & 0xFF) != 0xAA || (advertisementPacket[1] & 0xFF) != 0xFE;
    }

    public static boolean isEddyV2(byte[] scanRecord, int startByte) {
        return (scanRecord[startByte + 0] == 0xaa &&
                scanRecord[startByte + 1] == 0xfe &&
                scanRecord[startByte + 2] == 0x00);
    }

    public static byte[] copyOfRange(byte[] source, int from, int to) {
        if (source == null || from < 0 || to < 0) {
            return null;
        }

        int length = to - from;

        if (length < 0) {
            return null;
        }

        if (source.length < from + length) {
            return null;
        }

        byte[] destination = new byte[length];

        System.arraycopy(source, from, destination, 0, length);

        return destination;
    }
}
