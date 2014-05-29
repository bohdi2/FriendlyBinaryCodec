package org.bodhi.fbc.impl;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.bodhi.fbc.impl.Utils.*;

public class LittleEndianTest {
    private static final byte xFF = (byte) 0xff;
    private static final boolean LITTLE = false;
    private Endian m_endian = new LittleEndian();


    @Test
    public void testUtfChar1() {
        assertGetUtfChar('a', 0, 97);
        //assertUtfChar((byte) -1, xFF);
    }

    @Test
    public void testInt1() {
        assertGetInt1((byte) 1, 1);
        assertGetInt1((byte) -1, xFF);

        assertPutInt1((byte) 1, 1);
        assertPutInt1((byte) -1, xFF);
    }

    @Test
    public void testInt2() {
        assertGetInt2((short) 0x0102, 1, 2);
        assertGetInt2((short) -1, xFF, xFF);

        assertPutInt2((short) 0x0102, 1, 2);
        assertPutInt2((short) -1, xFF, xFF);
    }

    @Test
    public void tesUInt2() {
        assertGetUInt2((int) 0x0102, 1, 2);
        assertGetUInt2((int) 0xffff, xFF, xFF);

        assertPutUInt2((short) 0x0102, 1, 2);
        assertPutUInt2((short) -1, xFF, xFF);
    }

    @Test
    public void testInt4() {
        assertGetInt4(0x01020304, 1, 2, 3, 4);
        assertGetInt4(-1, xFF, xFF, xFF, xFF);

        assertPutInt4(0x01020304, 1, 2, 3, 4);
        assertPutInt4(-1, xFF, xFF, xFF, xFF);
    }

    @Test
    public void testInt8() {
        assertGetInt8(0x0102030405060708L, 1, 2, 3, 4, 5, 6, 7, 8);
        assertGetInt8(-1, xFF, xFF, xFF, xFF, xFF, xFF, xFF, xFF);

        assertPutInt8(0x0102030405060708L, 1, 2, 3, 4, 5, 6, 7, 8);
        assertPutInt8(-1, xFF, xFF, xFF, xFF, xFF, xFF, xFF, xFF);
    }



    private void assertGetUtfChar(char expected, int b1, int b2)
    {
        assertEquals(expected, m_endian.getUtfChar(toBytes(b2, b1), 0));
    }

    private void assertGetInt1(byte expected, int b1)
    {
        assertEquals(expected, m_endian.getInt1(toBytes(b1), 0));
    }

    private void assertPutInt1(byte expected, int b1)
    {
        byte[] raw = toBytes(0);

        m_endian.putInt1(raw, 0, expected);
        assertArrayEquals(toBytes(b1), raw);
    }


    private void assertGetInt2(short expected, int b1, int b2)
    {
        assertEquals(expected, m_endian.getInt2(toBytes(b2, b1), 0));
    }

    private void assertGetUInt2(int expected, int b1, int b2)
    {
        assertEquals(expected, m_endian.getUInt2(toBytes(b2, b1), 0));
    }

    private void assertPutInt2(short expected, int b1, int b2)
    {
        byte[] raw = toBytes(0, 0);

        m_endian.putInt2(raw, 0, expected);
        assertArrayEquals(toBytes(b2, b1), raw);
    }

    private void assertPutUInt2(int expected, int b1, int b2)
    {
        byte[] raw = toBytes(0, 0);

        m_endian.putInt2(raw, 0, expected);
        assertArrayEquals(toBytes(b2, b1), raw);
    }

    private void assertGetInt4(int expected,
                                  int b1,
                                  int b2,
                                  int b3,
                                  int b4)
    {
        assertEquals(expected, m_endian.getInt4(toBytes(b4, b3, b2, b1), 0));
    }

    private void assertPutInt4(int expected,
                                     int b1,
                                     int b2,
                                     int b3,
                                     int b4)
    {
        byte[] raw = toBytes(0, 0, 0, 0);

        m_endian.putInt4(raw, 0, expected);
        assertArrayEquals(toBytes(b4, b3, b2, b1), raw);
    }

    private void assertGetInt8(long expected,
                                     int b1,
                                     int b2,
                                     int b3,
                                     int b4,
                                     int b5,
                                     int b6,
                                     int b7,
                                     int b8
                                    )
    {
        assertEquals(expected, m_endian.getInt8(toBytes(b8, b7, b6, b5, b4, b3, b2, b1), 0));

    }

    private void assertPutInt8(long expected,
                                     int b1,
                                     int b2,
                                     int b3,
                                     int b4,
                                     int b5,
                                     int b6,
                                     int b7,
                                     int b8)
    {
        byte[] raw = toBytes(0, 0, 0, 0, 0, 0, 0, 0);

        m_endian.putInt8(raw, 0, expected);
        assertArrayEquals(toBytes(b8, b7, b6, b5, b4, b3, b2, b1), raw);
    }

    private static void dump(byte[] bytes) {
        for (byte b : bytes)
            System.out.print(b + ", ");

        System.out.println();
    }
}
