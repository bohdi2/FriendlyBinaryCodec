package org.bodhi.fbc;

import java.util.Arrays;

/**
 * Created by chris on 5/26/14.
 */
public class Utils {

    public static byte[] toBytes(int... ints) { // helper function
        byte[] result = new byte[ints.length];
        for (int i = 0; i < ints.length; i++) {
            result[i] = (byte) ints[i];
        }
        return result;
    }

    public static String toString(Trace trace, byte[] bytes) {
        StringBuilder b = new StringBuilder();

        for (int ii=0; ii<bytes.length; ii++) {
            String field = trace.getField(ii, "");
            String value = hex(bytes, ii);
            String comment = trace.getComment(ii, "");

            b.append(String.format("%4d 0x%04x %20s %s %s\n",
                                   ii,
                                   ii,
                                   field,
                                   value,
                                   comment));
        }
        return b.toString();
    }

    public static String toString(Trace trace, byte[] left, byte[] right) {
        StringBuilder b = new StringBuilder();

        b.append(String.format("%9s %20s %s %s\n", "Off", "Field", "Actual", "Expected"));

        int lastPosition = Math.max(left.length, right.length);

        for (int ii=0; ii<lastPosition; ii++) {
            String name = trace.getField(ii, ""); //m_tracex.containsKey(ii) ? m_tracex.get(ii) : "";
            String leftByte = hex(left, ii);
            String rightByte = hex(right, ii);
            char mark = leftByte.equals(rightByte) ? ' ' : '*';
            String comment = trace.getComment(ii, "");
            b.append(String.format("%4d 0x%04x %20s %4s %4s %c %s\n",
                                   ii,
                                   ii,
                                   name,
                                   leftByte,
                                   rightByte,
                                   mark,
                                   comment));
        }

        return b.toString();
    }

    public static String hex(byte[] bytes, int index) {
        return (index < bytes.length) ? String.format("0x%02x", bytes[index]) : "----";
    }

    public static boolean isEqual(BinaryWriter bw, byte[] raw) {
        assert null != bw;
        assert null != raw;
        return Arrays.equals(bw.getBytes(), raw);
    }

    public static String toString(Binary binary, byte[] raw) {
        return toString(binary.getTrace(), binary.getBytes(), raw);
    }

    public static String toString(Binary b1, Binary b2) {
        return toString(b1.getTrace(), b1.getBytes(), b2.getBytes());
    }
}
