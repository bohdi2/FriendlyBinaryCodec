package org.bodhi.fbc;

import java.nio.charset.Charset;
import org.junit.Test;

import static org.bodhi.fbc.Utils.toBytes;
import static org.junit.Assert.*;

public class UtilsTest {

    @Test
    public void test_toBytes() {
        byte[] expected = new byte[] {(byte) 0xff, (byte) 0x10};
        byte[] actual = Utils.toBytes(255, 16);

        assertArrayEquals(expected, actual);
    }


    @Test
    public void test_toString_with_one_byte_array() {
        byte[] bytes = Utils.toBytes(1, 2, 3);

        Trace trace = new Trace();
        trace.trace(1, "F2", "Comment");

        String actual = Utils.toString(trace, bytes);

        String expected =
                "   0 0x0000                      0x01 \n" +
                "   1 0x0001                   F2 0x02 Comment\n" +
                "   2 0x0002                      0x03 \n";

        assertEquals(expected, actual);
    }

    @Test
    public void test_toString_with_two_byte_arrays() {
        byte[] bytes1 = Utils.toBytes(1, 2, 3);
        byte[] bytes2 = Utils.toBytes(1, 2, 4, 5);

        Trace trace = new Trace();
        trace.trace(1, "F2", "Comment");

        String actual = Utils.toString(trace, bytes1, bytes2);

        String expected =
                "      Off                Field Actual Expected\n" +
                "   0 0x0000                      0x01 0x01   \n" +
                "   1 0x0001                   F2 0x02 0x02   Comment\n" +
                "   2 0x0002                      0x03 0x04 * \n" +
                "   3 0x0003                      ---- 0x05 * \n";

        assertEquals(expected, actual);
    }

    @Test
    public void testAssert() throws Exception {

        BinaryWriter bw = new BinaryWriter(Charset.forName("ISO-8859-1"));

        bw.label("Msg Start");

        bw.putInt4(0, "header_length");
        bw.putString("One", "Field 1");
        bw.putString("Two", 10, "Field 2");
        bw.putInt8(3L, "Field 3");
        bw.label("Msg End");

        int writerDiffSize = bw.diff("Msg End", "Msg Start");
        bw.replaceInt4("header_length", writerDiffSize);

        byte[] raw = toBytes(  0,   0,   0,  25,  79, 110, 101,  84,
                             119, 111,  32,  32,  32,  32,  32,  32,
                              32,   0,   0,   0,   0,   0,   0,   0,
                               3);

        assertTrue(Utils.isEqual(bw, raw));

        raw[7] = 12;
        assertFalse(Utils.isEqual(bw, raw));

    }
}
