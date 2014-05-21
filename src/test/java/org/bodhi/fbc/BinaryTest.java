package org.bodhi.fbc;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutpyutStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

import static org.junit.Assert.*;

public class BinaryTest {

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
        int length = br.getInt4("header_length");
        String one = br.getString(3, "Field 1");
        String two = br.getString(10, "Field 2");
        long three = br.getInt8("Field 3");
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
