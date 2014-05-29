
package org.bodhi.fbc;

import static java.lang.String.format;

import org.bodhi.fbc.impl.Buffer;
import org.bodhi.fbc.impl.Trace;
import org.bodhi.fbc.impl.Utils;

import java.nio.charset.Charset;

public class BinaryWriter {
    private Buffer m_buffer;
    private final Trace m_trace;

    private Charset m_charset;

    public BinaryWriter(Charset charset) {
        m_charset = charset;

        m_buffer = new Buffer(new byte[128]);
        m_trace = new Trace();
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

    public void putBoolean(boolean b) {
        putInt1(b ? 1 : 0);
    }

    public void putBoolean(boolean b, String name) {
        trace(name, format("// bool: %b", b));
        putBoolean(b);
    }

    public void putUtfChar(char c) {
        m_buffer.putUtfChar(c);
    }

    public void putUtfChar(char c, String name) {
        trace(name, format("// UTF Char: %c", c));
        putUtfChar(c);
    }


    public void putInt1(int n) {
        m_buffer.putInt1(n);
    }

    public void putInt1(int n, String name) {
        trace(name, format("// SInt1: %d", n));
        putInt1(n);
    }

    public void putUInt2(int n) {
        m_buffer.putInt2((short)n); // this looks wrong to me.
    }

    public void putUInt2(int n, String name) {
        trace(name, format("// UInt2: %d", n));
        putUInt2(n);
    }

    public void putInt2(int n) {
        m_buffer.putInt2((short)n);
    }

    public void putInt2(int n, String name) {
        trace(name, format("// SInt2: %d", n));
        putInt2(n);
    }

    public void putInt4(int n) {
        m_buffer.putInt4(n);
    }

    public void putInt4(int n, String name) {
        trace(name, format("// SInt4: %d", n));
        putInt4(n);
    }

    public void putInt8(long n) {
        m_buffer.putInt8(n);
    }

    public void putInt8(long n, String name) {
        trace(name, format("// SInt8: %d", n));
        putInt8(n);
    }

    public void putBytes(byte[] bytes, String name) {
        trace(name, format("// bytes[]"));
        m_buffer.putBytes(bytes);
    }


    public void putString(String s, String name) {
        trace(name, String.format("// String: '%s'", s));
        m_buffer.putBytes(s.getBytes(m_charset));
    }

    public void putString(String s, int length, String name) {
        trace(name, String.format("// String[%d]: '%s'", length, s));
        m_buffer.putBytes(padRight(s, length).getBytes(m_charset), 0, length);
    }


    public void replaceInt4(String name, int n) {
        int position = getPosition(name);
        m_trace.appendComment(position, format(" (Replaced with SInt4 %d)", n));
        m_buffer.putInt4(position, n);
    }

    public int diff(String name1, String name2) {
        return getPosition(name1) - getPosition(name2);
    }


    public byte[] getBytes() {
        return m_buffer.copyBytes();
    }

    public String toString() {
        return Utils.toString(m_trace, m_buffer);
    }

    private static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);  
    }

}
