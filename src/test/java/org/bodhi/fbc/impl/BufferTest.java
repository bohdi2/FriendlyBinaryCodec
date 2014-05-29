package org.bodhi.fbc.impl;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.bodhi.fbc.impl.Utils.*;

public class BufferTest {
    private static final byte xFF = (byte) 0xff;

    @Test
    public void test_initial() {
        Buffer buffer = new Buffer(toBytes(1, 2, 3, 4));
        assertEquals(0, buffer.getPosition());
        assertEquals(4, buffer.getLimit());
    }

    @Test
    public void test_skip() {
        Buffer buffer = new Buffer(toBytes(1, 2, 3, 4));
        buffer.skip(3);
        assertEquals(3, buffer.getPosition());
        assertEquals(4, buffer.getLimit());
    }

    @Test
    public void test_copyBytes() {
        byte[] original = toBytes(1, 2, 3, 4, 5);
        Buffer buffer = new Buffer(original);
        buffer.skip(5);
        byte[] copy = buffer.copyBytes();

        assertArrayEquals(copy, original);
    }


    @Test
    public void test_getUtfChar() {
        Buffer buffer = new Buffer(toBytes(0, 65, 0, 97));

        assertEquals('A', buffer.getUtfChar());
        assertEquals('a', buffer.getUtfChar());
        assertEquals(4, buffer.getPosition());
    }


    @Test
    public void test_read_int1() {
        Buffer buffer = new Buffer(toBytes(1, xFF));

        assertEquals(0x01, buffer.getSignedInt1());
        assertEquals(-1, buffer.getSignedInt1());
        assertEquals(2, buffer.getPosition());
    }

    @Test
    public void test_read_unsigned_int1() {
        Buffer buffer = new Buffer(toBytes(1, xFF));

        assertEquals(0x01, buffer.getUnsignedInt1());
        assertEquals(255, buffer.getUnsignedInt1());
        assertEquals(2, buffer.getPosition());
    }


    @Test
    public void test_read_int2() {
        Buffer buffer = new Buffer(toBytes(1, 2, 3, 4));

        assertEquals(0x0102, buffer.getUnsignedInt2());
        assertEquals(0x0304, buffer.getUnsignedInt2());
        assertEquals(4, buffer.getPosition());
    }

    @Test
    public void test_read_unsigned_int2() {
        Buffer buffer = new Buffer(toBytes(1, 2, xFF, xFF));

        assertEquals(0x0102, buffer.getSignedInt2());
        assertEquals(-1, buffer.getSignedInt2());
        assertEquals(4, buffer.getPosition());
    }

    @Test
    public void test_read_int4() {
        Buffer buffer = new Buffer(toBytes(1, 2, 3, 4, 5, 6, 7, 8));

        assertEquals(0x01020304, buffer.getSignedInt4());
        assertEquals(0x05060708, buffer.getSignedInt4());
        assertEquals(8, buffer.getPosition());
    }

    //@Test
    public void test_read_unsigned_int4() {
        //Buffer foo = new Buffer(toBytes(1,2,3,4,xFF,xFF));

        //assertEquals(0x0102, foo.getUnsignedInt4());
        //assertEquals(-1, foo.getUnsignedInt4());
        //assertEquals(4, foo.getPosition());
    }

    @Test
    public void test_putUtfChar() {
        Buffer buffer = new Buffer(toBytes(0, 0, 0, 0, 0, 0));
        buffer.putUtfChar('A');
        buffer.putUtfChar('a');

        assertArrayEquals(toBytes(0, 65, 0, 97), buffer.copyBytes());
    }

    @Test
    public void test_hex() {
        Buffer buffer = new Buffer(toBytes(0, 1, 0xFF));
        buffer.skip(3);
        assertEquals("0x00", buffer.hex(0));
        assertEquals("0x01", buffer.hex(1));
        assertEquals("0xff", buffer.hex(2));
        assertEquals("----", buffer.hex(3));
    }

    @Test
    public void test_equals_with_cruft_at_the_end() {
        Buffer buffer1 = new Buffer(toBytes(0, 1, 0xFF));
        Buffer buffer2 = new Buffer(toBytes(0, 1, 0xF2));

        buffer1.skip(1);
        buffer2.skip(1);

        assertEquals(buffer1, buffer2);
    }

    @Test
    public void test_simple_equals() {
        Buffer buffer1 = new Buffer(toBytes(0, 1, 0xFF));
        Buffer buffer2 = new Buffer(toBytes(0, 1, 0xFF));
        Buffer buffer3 = new Buffer(toBytes(0, 1, 0xFF));


        buffer1.skip(2);
        buffer2.skip(2);

        assertEquals(buffer1, buffer2);
        assertNotEquals(buffer1, buffer3);
    }

    private static void dump(byte[] bytes) {
        for (byte b : bytes)
            System.out.print(b + ", ");

        System.out.println();
    }
}
