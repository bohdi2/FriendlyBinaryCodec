
package org.bodhi.fbc.impl;

import java.io.EOFException;
import java.io.IOException;

public class Foo {
    private byte buf[];

    private int pos;

    /**
     * The index one greater than the last valid character in the input
     * stream buffer.
     * This value should always be nonnegative
     * and not larger than the length of <code>buf</code>.
     * It  is one greater than the position of
     * the last byte within <code>buf</code> that
     * can ever be getByte  from the input stream buffer.
     */
    private int count;


    public Foo(byte buf[]) {
        this.buf = buf;
        this.pos = 0;
        this.count = buf.length;
    }

    public Foo(byte buf[], int offset, int length) {
        this.buf = buf;
        this.pos = offset;
        this.count = Math.min(offset + length, buf.length);
    }

    public int getLimit() {
        return count;
    }
    
    public int getPosition() {
        return pos;
    }

    public void setPosition(int n) {
        pos = n;
    }

    public int getByte() {
        return (pos < count) ? (buf[pos++] & 0xff) : -1;
    }

    public int getBytes(byte b[]) throws IOException {

        return getBytes(b, 0, b.length);
    }

    public int getBytes(byte b[], int off, int len) {
        assert null != b;

        if (off < 0 || len < 0 || len > b.length - off) {
            throw new IndexOutOfBoundsException();
        }

        if (pos >= count) {
            return -1;
        }

        if (pos + len > count) {
            len = count - pos;
        }

        if (len <= 0) {
            return 0;
        }

        System.arraycopy(buf, pos, b, off, len);
        pos += len;
        return len;
    }

    public long skip(long n) {
        if (pos + n > count) {
            n = count - pos;
        }
        if (n < 0) {
            return 0;
        }
        pos += n;
        return n;
    }

    public int available() {
        return count - pos;
    }

    public boolean readBoolean() throws IOException {
        int ch = getByte();
        if (ch < 0)
            throw new EOFException();
        return (ch != 0);
    }

    public byte readByte() throws IOException {
        int ch = getByte();
        if (ch < 0)
            throw new EOFException();

        return (byte)(ch);
    }

    public int readUnsignedByte() throws IOException {
        int ch = getByte();
        if (ch < 0)
            throw new EOFException();

        return ch;
    }

    public short getSignedInt2() throws IOException {
        int ch1 = getByte();
        int ch2 = getByte();
        if ((ch1 | ch2) < 0)
            throw new EOFException();

        return (short)((ch1 << 8) + (ch2 << 0));
    }

    public int getUnsignedInt2() throws IOException {
        int ch1 = getByte();
        int ch2 = getByte();
        if ((ch1 | ch2) < 0)
            throw new EOFException();

        return (ch1 << 8) + (ch2 << 0);
    }

    public char readChar() throws IOException {
        int ch1 = getByte();
        int ch2 = getByte();
        if ((ch1 | ch2) < 0)
            throw new EOFException();

        return (char)((ch1 << 8) + (ch2 << 0));
    }

    public int getSignedInt4() throws IOException {
        int ch1 = getByte();
        int ch2 = getByte();
        int ch3 = getByte();
        int ch4 = getByte();
        if ((ch1 | ch2 | ch3 | ch4) < 0)
            throw new EOFException();

        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
    }

    public long getSignedInt8() throws IOException {
        byte readBuffer[] = new byte[8];

        getBytes(readBuffer, 0, 8);

        return (((long)readBuffer[0] << 56) +
                ((long)(readBuffer[1] & 255) << 48) +
                ((long)(readBuffer[2] & 255) << 40) +
                ((long)(readBuffer[3] & 255) << 32) +
                ((long)(readBuffer[4] & 255) << 24) +
                ((readBuffer[5] & 255) << 16) +
                ((readBuffer[6] & 255) <<  8) +
                ((readBuffer[7] & 255) <<  0));
    }


}

