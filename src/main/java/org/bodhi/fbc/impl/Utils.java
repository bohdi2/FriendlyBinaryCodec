package org.bodhi.fbc.impl;

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

    public static String toString(Trace trace, Buffer buffer) {
        StringBuilder b = new StringBuilder();

        for (int ii=0; ii<buffer.getLimit(); ii++) {
            String field = trace.getField(ii, "");
            String value = buffer.hex(ii);
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

    public static String toString(Trace trace, Buffer left, Buffer right) {
        StringBuilder b = new StringBuilder();

        b.append(String.format("%9s %20s %s %s\n", "Off", "Field", "Actual", "Expected"));

        int limit = Math.max(left.getLimit(), right.getLimit());

        for (int ii=0; ii<limit; ii++) {
            String name = trace.getField(ii, ""); //m_tracex.containsKey(ii) ? m_tracex.get(ii) : "";
            String leftByte = left.hex(ii);
            String rightByte = right.hex(ii);
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
        //Assert.assertEquals(actual.getHexDump(), m_buffer.getHexDump());
    }
}
