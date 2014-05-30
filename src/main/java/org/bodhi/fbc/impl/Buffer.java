
package org.bodhi.fbc.impl;

import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;

// Buffer is in charge of byte buffer and tracking position, limit, etcf

public class Buffer {
    private static final Endian BIG = new BigEndian();
    private static final Endian LITTLE = new LittleEndian();

    private byte buf[];
    private int pos;
    private int limit;
    private final Endian m_endian;
    private final int m_growthFactor;

    public Buffer(int size) {
        m_growthFactor = Math.max(size, 16);
        this.buf = new byte[m_growthFactor];
        this.pos = 0;
        m_endian = BIG;
        this.limit = m_growthFactor;
    }

    public Buffer(byte buf[]) {
        this(buf, 0, buf.length, BIG, 0);
    }

    public Buffer(byte buf[], int offset, int length) {
        this(buf, offset, Math.min(offset + length, buf.length), BIG, 0);
    }

    private Buffer(byte buf[],
                   int pos,
                   int limit,
                   Endian endian,
                   int growthFactor)
    {
        this.buf = buf;
        this.pos = pos;
        this.limit = limit;
        m_endian = endian;
        m_growthFactor = growthFactor;
    }

    public Buffer copy() {
        return new Buffer(Arrays.copyOf(buf, limit),
                          pos,
                          limit,
                          m_endian,
                          m_growthFactor);
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
        grow(n);
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


    public char getUtfChar() {
        char result = m_endian.getUtfChar(buf, pos);
        pos += 2;
        return result;
    }


    public int getInt1() {
        return buf[alloc(1)];
    }

    public int getInt1(int offset) {
        return buf[offset];
    }

    public int getUInt1() {
        return buf[alloc(1)] & 0xff;
    }

    public int getUInt1(int offset) {
        return buf[offset] & 0xff;
    }

     public int getInt2()  {
         return m_endian.getInt2(buf, alloc(2));
    }

    public int getInt2(int offset)  {
        return m_endian.getInt2(buf, offset);
    }

    public int getUInt2()  {
        int n = getInt2();
        return n & 0xffff;
    }

    public int getUInt2(int offset)  {
        int n = getInt2(offset);
        return n & 0xffff;
    }


    public int getInt4() {
        return m_endian.getInt4(buf, alloc(4));
    }

    public long getInt8() {
        return m_endian.getInt8(buf, alloc(8));
    }




    public void putBytes(byte[] bytes) {
        putBytes(bytes, 0, bytes.length);
    }

    public void putBytes(byte[] bytes, int offset, int length) {
        grow(length);
        System.arraycopy(bytes, offset, buf, alloc(length), length);
        //pos += length;
    }

    public void putUtfChar(char c) {
        grow(2);
        m_endian.putUtfChar(buf, alloc(2), c);
    }


    public void putInt1(int n) {
        grow(1);
        m_endian.putInt1(buf, alloc(1), n);
    }

    public void putInt2(int n) {
        grow(2);
        m_endian.putInt2(buf, alloc(2), n);
    }

    public void putInt4(int n) {
        grow(4);
        m_endian.putInt4(buf, alloc(4), n);
    }

    public void putInt4(int offset, int n) {
        grow(4);
        m_endian.putInt4(buf, offset, n);
    }

    public void putInt8(long n) {
        grow(8);
        m_endian.putInt8(buf, alloc(8), n);
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
        return (index < pos) ? String.format("0x%02x", getUInt1(index)) : "----";
    }

    private int alloc(int n) {
        int overflow = pos + n - limit;

        assert overflow <= 0;
            
        int result = pos;
        pos += n;
        return result;
    }

    private void grow(int n) {
        int overflow = pos + n - limit;

        if (overflow > 0 && m_growthFactor > 0) {
            int newLimit = limit + overflow + m_growthFactor;
            byte[] newBuf = new byte[newLimit];
            System.arraycopy(buf, 0, newBuf, 0, limit);
            buf = newBuf;
            limit += newLimit;
        }
    }


}

