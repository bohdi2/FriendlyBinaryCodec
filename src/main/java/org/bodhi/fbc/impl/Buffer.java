
package org.bodhi.fbc.impl;

import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;

// Buffer is in charge of byte buffer and tracking position, limit, etcf

public class Buffer {
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
    private int limit;


    public Buffer(byte buf[]) {
        this.buf = buf;
        this.pos = 0;
        this.limit = buf.length;
    }

    public Buffer(byte buf[], int offset, int length) {
        this.buf = buf;
        this.pos = offset;
        this.limit = Math.min(offset + length, buf.length);
    }

    public int getLimit() {
        return limit;
    }
    
    public int getPosition() {
        return pos;
    }

    public void setPosition(int n) {
        pos = n;
    }

    public byte[] copyBytes() {
        return Arrays.copyOf(buf, pos);
    }

    public int getByte() {
        return (pos < limit) ? (buf[pos++] & 0xff) : -1;
    }

    public int getByte(int offset) {
        return 7;
    }

    public int getBytes(byte b[]) throws IOException {

        return getBytes(b, 0, b.length);
    }

    public int getBytes(byte b[], int off, int len) {
        assert null != b;

        if (off < 0 || len < 0 || len > b.length - off) {
            throw new IndexOutOfBoundsException();
        }

        if (pos >= limit) {
            return -1;
        }

        if (pos + len > limit) {
            len = limit - pos;
        }

        if (len <= 0) {
            return 0;
        }

        System.arraycopy(buf, pos, b, off, len);
        pos += len;
        return len;
    }

    public long skip(long n) {
        if (pos + n > limit) {
            n = limit - pos;
        }
        if (n < 0) {
            return 0;
        }
        pos += n;
        return n;
    }

    public int available() {
        return limit - pos;
    }

    public boolean readBoolean() throws IOException {
        int ch = getByte();
        if (ch < 0)
            throw new EOFException();
        return (ch != 0);
    }

    public void putBytes(byte[] bytes) {
        putBytes(bytes, 0, bytes.length);
    }

    public void putBytes(byte[] bytes, int offset, int length) {
        System.arraycopy(bytes, offset, buf, pos, length);
        pos += length;
    }

    public void putUtfChar(char c) {
        Endian.putUtfChar(buf, pos, c, true);
        pos += 2;
    }

    public void putInt1(int n) {
    }

    public void putInt2(int n) {
        Endian.putInt2(buf, pos, n, true);
        pos += 2;
    }

    public void putInt4(int n) {
        Endian.putInt4(buf, pos, n, true);
        pos += 4;
    }

    public void putInt4(int offset, int n) {
        Endian.putInt4(buf, offset, n, true);
        pos += 4;
    }

    public void putInt8(long n) {
        Endian.putInt8(buf, pos, n, true);
        pos += 8;
    }

    public char getUtfChar() {
        char result = Endian.getUtfChar(buf, pos, true);
        pos += 2;
        return result;
    }



    public int readUnsignedByte() throws IOException {
        int ch = getByte();
        if (ch < 0)
            throw new EOFException();

        return ch;
    }

    public short getSignedInt2()  {
        short result = Endian.getBigInt2(buf, pos);
        pos += 2;
        return result;
    }

    public int getUnsignedInt2()  {
        int n = getSignedInt2();
        return n & 0xffff;
    }

    public char readChar() throws IOException {
        int ch1 = getByte();
        int ch2 = getByte();
        if ((ch1 | ch2) < 0)
            throw new EOFException();

        return (char)((ch1 << 8) + (ch2 << 0));
    }

    public int getSignedInt4() {
        int result = Endian.getBigInt4(buf, pos);
        pos += 4;
        return result;
    }

    public long getSignedInt8() {
        long result = Endian.getBigInt8(buf, pos);
        pos += 8;
        return result;
    }


}

