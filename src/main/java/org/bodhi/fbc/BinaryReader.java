
package org.bodhi.fbc;

import java.io.IOException;
import org.bodhi.fbc.impl.Buffer;
import java.nio.charset.Charset;

import static java.lang.String.format;

public class BinaryReader implements Binary {
    private Buffer m_buffer;
    private final Trace m_trace;

    private Charset m_charset;

    public BinaryReader(byte[] bytes, Charset charset) {
        m_charset = charset;

        m_buffer = new Buffer(bytes);
        m_trace = new Trace();
    }

    public byte[] getBytes() {
        return m_buffer.copyBytes();
    }

    public Trace getTrace() {
        return m_trace.copy();
    }


    public void trace(String name, String comment) {
        m_trace.trace(m_buffer.getPosition(), name, comment);
    }

    public void label(String field) {
        m_trace.label(m_buffer.getPosition(), field);
    }


    // Returns byte offset to the named position

    public int getPosition(String name) {
        return m_trace.getPosition(name);
    }

    public void moveToPosition(int offset) {
        m_buffer.setPosition(offset);
    }

    public boolean getBoolean() {
        return 0 != getInt1();
    }

    public boolean getBoolean(String name) {
        return 0 != getInt1(name);
    }

    public int skip(int n) {
        return m_buffer.skip(n);
    }

    public int getInt1() {
        return m_buffer.getInt1();
    }

    public int getInt1(String name) {
        trace(name, format("// SInt1"));
        return getInt1();
    }

    public int getInt2() throws IOException {
        return m_buffer.getInt2();
    }

    public int getInt2(String name) throws IOException {
        trace(name, format("// SInt2"));
        return getInt2();
    }

    public int getUInt2() throws IOException {
        return m_buffer.getUInt2();
    }

    public int getUInt2(String name) throws IOException {
        trace(name, format("// UInt2"));
        return getUInt2();
    }


    public int getInt4() throws IOException {
        return m_buffer.getInt4();
    }

    public int getInt4(String name) throws IOException{
        trace(name, format("// SInt4"));
        return getInt4();
    }

    public long getInt8() throws IOException {
        return m_buffer.getInt8();
    }

    public long getInt8(String name) throws IOException {
        trace(name, format("// SInt8"));
        return getInt8();
    }

    public byte[] getBytes(int length, String name) {
        trace(name, format("// bytes[%d]", length));

        byte[] dst = new byte[length];
        m_buffer.getBytes(dst, 0, length);
        return dst;
    }

    public char getUtfChar() {
        return m_buffer.getUtfChar();
    }

    public char getUtfChar(String name) {
        trace(name, format("// UTF Char"));
        return getUtfChar();
    }

    public String getString(int length, String name) {
        trace(name, format("// String[%d", length));

        byte[] dst = new byte[length];
        m_buffer.getBytes(dst, 0, length);
        return new String(dst, m_charset).trim();
    }

    public int diff(String name1, String name2) {
        return getPosition(name1) - getPosition(name2);
    }

    public String toString() {
        return Utils.toString(m_trace, m_buffer.copyBytes());

    }

}
