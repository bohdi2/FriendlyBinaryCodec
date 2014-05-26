package org.bodhi.fbc;

import java.nio.charset.Charset;
import org.junit.Test;

import static org.junit.Assert.*;

public class BinaryTest {

    @Test
    public void test_utf() throws Exception {
        BinaryWriter bw = new BinaryWriter(Charset.forName("ISO-8859-1"));

        bw.name("Msg Start");

        bw.putUtfChar('a', "field 1");
        bw.putUtfChar('b', "field 2");

        byte[] raw = bw.getBytes();
        assertEquals(4, raw.length);

        BinaryReader br = new BinaryReader(raw, Charset.forName("ISO-8859-1"));
        br.name("Msg Start");
        char field1 = br.getUtfChar("field 1");
        char field2 = br.getUtfChar("field 2");

        assertEquals('a', field1);
        assertEquals('b', field2);
    }


    @Test
    public void test_signed_int2() throws Exception {
        BinaryWriter bw = new BinaryWriter(Charset.forName("ISO-8859-1"));

        bw.name("Msg Start");

        bw.putInt2(0, "field 1");
        bw.putInt2(2, "field 2");

        byte[] raw = bw.getBytes();
        assertEquals(4, raw.length);

        BinaryReader br = new BinaryReader(raw, Charset.forName("ISO-8859-1"));
        br.name("Msg Start");
        short field1 = br.getSignedInt2("field 1");
        short field2 = br.getSignedInt2("field 2");

        assertEquals(0, field1);
        assertEquals(2, field2);
    }

    @Test
    public void test_unsigned_int2() throws Exception {
        BinaryWriter bw = new BinaryWriter(Charset.forName("ISO-8859-1"));

        bw.name("Msg Start");

        bw.putUnsignedInt2(0xffff, "field 1");
        bw.putUnsignedInt2(2, "field 2");

        byte[] raw = bw.getBytes();
        assertEquals(4, raw.length);

        BinaryReader br = new BinaryReader(raw, Charset.forName("ISO-8859-1"));
        br.name("Msg Start");
        int field1 = br.getUnsignedInt2("field 1");
        int field2 = br.getUnsignedInt2("field 2");

        assertEquals(0xffff, field1);
        assertEquals(2, field2);
    }

    @Test
    public void testInt4() throws Exception {
        BinaryWriter bw = new BinaryWriter(Charset.forName("ISO-8859-1"));

        bw.name("Msg Start");

        bw.putInt4(0, "field 1");
        bw.putInt4(2, "field 2");


        byte[] raw = bw.getBytes();
        assertEquals(8, raw.length);

        BinaryReader br = new BinaryReader(raw, Charset.forName("ISO-8859-1"));
        br.name("Msg Start");
        int field1 = br.getSignedInt4("field 1");
        int field2 = br.getSignedInt4("field 2");

        assertEquals(0, field1);
        assertEquals(2, field2);
    }

    @Test
    public void testInt8() throws Exception {
        BinaryWriter bw = new BinaryWriter(Charset.forName("ISO-8859-1"));

        bw.name("Msg Start");

        bw.putInt8(0, "field 1");
        bw.putInt8(2, "field 2");


        byte[] raw = bw.getBytes();
        assertEquals(16, raw.length);

        BinaryReader br = new BinaryReader(raw, Charset.forName("ISO-8859-1"));
        br.name("Msg Start");
        long field1 = br.getSignedInt8("field 1");
        long field2 = br.getSignedInt8("field 2");

        assertEquals(0, field1);
        assertEquals(2, field2);
    }


    @Test
    public void testBinary() throws Exception {

        BinaryWriter bw = new BinaryWriter(Charset.forName("ISO-8859-1"));

        bw.name("Msg Start");

        bw.putInt4(0, "header_length");
        bw.putString("One", "Field 1");
        bw.putString("Two", 10, "Field 2");
        bw.putInt8(3L, "Field 3");
        bw.name("Msg End");

        int writerDiffSize = bw.diff("Msg End", "Msg Start");
        bw.replaceInt4("header_length", writerDiffSize);

        byte[] raw = bw.getBytes();

        BinaryReader br = new BinaryReader(raw, Charset.forName("ISO-8859-1"));
        br.name("Msg Start");
        int length = br.getSignedInt4("header_length");
        String one = br.getString(3, "Field 1");
        String two = br.getString(10, "Field 2");
        long three = br.getSignedInt8("Field 3");
        br.name("Msg End");

        // Look at sizes

        int expectedSize = 25;
        int readerDiffSize = br.diff("Msg End", "Msg Start");

        assertEquals(expectedSize, length);
        assertEquals(expectedSize, writerDiffSize);
        assertEquals(expectedSize, readerDiffSize);

        assertEquals("One", one);
        assertEquals("Two", two);
        assertEquals(3, three);
    }


    @Test
    public void testBinaryReader() throws Exception {

        byte[] raw = new byte[] {10, 11, 12, 13, 14, 15, 16};

        BinaryReader br = new BinaryReader(raw, Charset.forName("ISO-8859-1"));

        assertEquals(10, br.getInt1());
        br.moveToPosition(5);
        assertEquals(15, br.getInt1());
    }
}
