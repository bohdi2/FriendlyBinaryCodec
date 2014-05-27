
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
     * can ever be getSignedInt1  from the input stream buffer.
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

    public int skip(int n) {
        alloc(n);
        return n;
    }

    public int available() {
        return limit - pos;
    }

    public byte[] copyBytes() {
        return Arrays.copyOf(buf, pos);
    }



    public int getBytes(byte b[]) throws IOException {

        return getBytes(b, 0, b.length);
    }

    public int getBytes(byte b[], int off, int len) {
        assert null != b;

        if (off < 0 || len < 0 || len > b.length - off) {
            throw new IndexOutOfBoundsException();
        }

        System.arraycopy(buf, alloc(len), b, off, len);
        return len;
    }


    public int getSignedInt1() {
        return buf[alloc(1)];
    }

    public int getSignedInt1(int offset) {
        return buf[offset];
    }

    public int getUnsignedInt1() {
        return buf[alloc(1)] & 0xff;
    }

    public int getUnsignedInt1(int offset) {
        return buf[offset] & 0xff;
    }


    public char getUtfChar() {
        char result = Endian.getUtfChar(buf, pos, true);
        pos += 2;
        return result;
    }


     public int getSignedInt2()  {
        return Endian.getBigInt2(buf, alloc(2));
    }

    public int getSignedInt2(int offset)  {
        return Endian.getBigInt2(buf, offset);
    }

    public int getUnsignedInt2()  {
        int n = getSignedInt2();
        return n & 0xffff;
    }

    public int getUnsignedInt2(int offset)  {
        int n = getSignedInt2(offset);
        return n & 0xffff;
    }


    public int getSignedInt4() {
        return Endian.getBigInt4(buf, alloc(4));
    }

    public long getSignedInt8() {
        return Endian.getBigInt8(buf, alloc(8));
    }




    public void putBytes(byte[] bytes) {
        putBytes(bytes, 0, bytes.length);
    }

    public void putBytes(byte[] bytes, int offset, int length) {
        System.arraycopy(bytes, offset, buf, pos, length);
        pos += length;
    }

    public void putUtfChar(char c) {
        Endian.putUtfChar(buf, alloc(2), c, true);
    }

    public void putInt1(int n) {
        Endian.putInt1(buf, alloc(1), n);
    }

    public void putInt2(int n) {
        Endian.putInt2(buf, alloc(2), n, true);
    }

    public void putInt4(int n) {
        Endian.putInt4(buf, alloc(4), n, true);
    }

    public void putInt4(int offset, int n) {
        Endian.putInt4(buf, offset, n, true);
    }

    public void putInt8(long n) {
        Endian.putInt8(buf, alloc(8), n, true);
    }

    public int hashCode() {
        return buf.hashCode() + pos + limit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Buffer that = (Buffer) o;

        if (limit != that.limit) return false;
        if (pos != that.pos) return false;

        byte[] thisCopy = Arrays.copyOf(buf, pos);
        byte[] thatCopy = Arrays.copyOf(that.buf, pos);


        if (!Arrays.equals(thisCopy, thatCopy)) return false;

        return true;
    }

    public String hex(int index) {
        //System.err.format("cjh index: %d, pos: %d, limit: %d%n", index, pos, limit);
        return (index < pos) ? String.format("0x%02x", getUnsignedInt1(index)) : "----";
    }

    private int alloc(int n) {
        assert pos + n <= limit;

        int result = pos;
        pos += n;
        return result;
    }


}

