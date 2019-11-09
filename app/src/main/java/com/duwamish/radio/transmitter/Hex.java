package com.duwamish.radio.transmitter;

public class Hex {
    /**
     * bytesToHex method
     */
    private static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String toHexString(byte[] dataBytes, int a, int b) {
        int mn = Math.min(dataBytes.length - a, b);
        char[] array = new char[b * 2];

        for (int index = 0; index < mn; ++index) {
            array[2 * index + 0] = hexArray[dataBytes[index + a] >>> 4 & 15];
            array[2 * index + 1] = hexArray[dataBytes[index + a] >>> 0 & 15];
        }

        return new String(array);
    }

    public static int major(byte[] scanRecord, int startByte) {
        return (scanRecord[startByte + 20] & 0xff) * 0x100 + (scanRecord[startByte + 21] & 0xff);
    }

    public static int minor(byte[] scanRecord, int startByte) {
        return (scanRecord[startByte + 22] & 0xff) * 0x100 + (scanRecord[startByte + 23] & 0xff);
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
}
