package org.datarepo.utils;

public class Bits {


    public static boolean booleanAt(byte[] b, int off) {
        return b[off] != 0;
    }


    public static boolean booleanInBytePos1(int val) {
        val = val & 0x01;
        return val != 0;
    }

    public static boolean booleanInBytePos2(int val) {
        val = val & 0x02;
        return val != 0;
    }


    public static boolean booleanInBytePos3(int val) {
        val = val & 0x04;
        return val != 0;
    }

    public static boolean booleanInBytePos4(int val) {
        val = val & 0x08;
        return val != 0;
    }

    public static boolean booleanInBytePos1(byte[] b, int off) {
        int val = b[off] & 0x01;
        return val != 0;
    }

    public static boolean booleanInBytePos2(byte[] b, int off) {
        int val = b[off] & 0x02;
        return val != 0;
    }


    public static boolean booleanInBytePos3(byte[] b, int off) {
        int val = b[off] & 0x04;
        return val != 0;
    }

    public static boolean booleanInBytePos4(byte[] b, int off) {
        int val = b[off] & 0x08;
        return val != 0;
    }

    public static boolean booleanInBytePos5(byte[] b, int off) {
        int val = b[off] & 0x10;
        return val != 0;
    }

    public static boolean booleanInBytePos6(byte[] b, int off) {
        int val = b[off] & 0x20;
        return val != 0;
    }

    public static boolean booleanInBytePos7(byte[] b, int off) {
        int val = b[off] & 0x40;
        return val != 0;
    }

    public static boolean booleanInBytePos8(byte[] b, int off) {
        int val = b[off] & 0x80;
        return val != 0;
    }


    public static byte byteAt(byte[] b, int off) {
        return b[off];
    }


    public static byte topNibbleAt(byte[] b, int off) {
        return (byte) (b[off] & 0xF0);
    }

    public static byte bottomNibbleAt(byte[] b, int off) {
        return (byte) (b[off] & 0x0F);
    }

    public static byte topNibbleAt(int val) {
        return (byte) (val & 0xF0);
    }

    public static byte bottomNibbleAt(int val) {
        return (byte) (val & 0x0F);
    }


    public static char charAt(byte[] b, int off) {
        return (char) ((b[off + 1] & 0xFF) +
                (b[off] << 8));
    }

    public static short shortAt(byte[] b, int off) {
        return (short) ((b[off + 1] & 0xFF) +
                (b[off] << 8));
    }

    public static int unsignedShortAt(byte[] b, int off) {
        return ((b[off + 1] & 0xFF) +
                (b[off] << 8));
    }

    public static int intAt(byte[] b, int off) {
        return ((b[off + 3] & 0xFF)) +
                ((b[off + 2] & 0xFF) << 8) +
                ((b[off + 1] & 0xFF) << 16) +
                ((b[off]) << 24);
    }

    public static float floatAt(byte[] b, int off) {
        return Float.intBitsToFloat(intAt(b, off));
    }

    public static long longAt(byte[] b, int off) {
        return ((b[off + 7] & 0xFFL)) +
                ((b[off + 6] & 0xFFL) << 8) +
                ((b[off + 5] & 0xFFL) << 16) +
                ((b[off + 4] & 0xFFL) << 24) +
                ((b[off + 3] & 0xFFL) << 32) +
                ((b[off + 2] & 0xFFL) << 40) +
                ((b[off + 1] & 0xFFL) << 48) +
                (((long) b[off]) << 56);
    }

    public static double doubleAt(byte[] b, int off) {
        return Double.longBitsToDouble(longAt(b, off));
    }

    public static void booleanTo(byte[] b, int off, boolean val) {
        b[off] = (byte) (val ? 1 : 0);
    }

    public static void charTo(byte[] b, int off, char val) {
        b[off + 1] = (byte) (val);
        b[off] = (byte) (val >>> 8);
    }

    public static void shortTo(byte[] b, int off, short val) {
        b[off + 1] = (byte) (val);
        b[off] = (byte) (val >>> 8);
    }

    public static void intTo(byte[] b, int off, int val) {
        b[off + 3] = (byte) (val);
        b[off + 2] = (byte) (val >>> 8);
        b[off + 1] = (byte) (val >>> 16);
        b[off] = (byte) (val >>> 24);
    }

    public static void floatTo(byte[] b, int off, float val) {
        intTo(b, off, Float.floatToIntBits(val));
    }

    public static void longTo(byte[] b, int off, long val) {
        b[off + 7] = (byte) (val);
        b[off + 6] = (byte) (val >>> 8);
        b[off + 5] = (byte) (val >>> 16);
        b[off + 4] = (byte) (val >>> 24);
        b[off + 3] = (byte) (val >>> 32);
        b[off + 2] = (byte) (val >>> 40);
        b[off + 1] = (byte) (val >>> 48);
        b[off] = (byte) (val >>> 56);
    }

    public static void doubleTo(byte[] b, int off, double val) {
        longTo(b, off, Double.doubleToLongBits(val));
    }
}

