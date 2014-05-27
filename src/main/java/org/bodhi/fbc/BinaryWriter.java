
package org.bodhi.fbc;

import static java.lang.String.format;

import org.bodhi.fbc.impl.Buffer;
import org.bodhi.fbc.impl.Trace;

import java.nio.charset.Charset;

public class BinaryWriter {
    private Buffer m_buffer;
    private final Trace m_trace;

    private Charset m_charset;

    public BinaryWriter(Charset charset) {
        m_charset = charset;

        m_buffer = new Buffer(new byte[30]);
        m_trace = new Trace();
    }

    public void addTrace(String name, String comment) {
        m_trace.addTrace(m_buffer.getPosition(), name, comment);
    }

    public void addField(String field) {
        m_trace.addField(m_buffer.getPosition(), field);
    }

    // Returns byte offset to the named position

    public int getPosition(String name) {
        return m_trace.getPosition(name);
    }

    public void putBoolean(boolean b) {
        putInt1(b ? 1 : 0);
    }

    public void putBoolean(boolean b, String name) {
        addTrace(name, format("// bool: %b", b));
        putBoolean(b);
    }

    public void putUtfChar(char c) {
        m_buffer.putUtfChar(c);
    }

    public void putUtfChar(char c, String name) {
        addTrace(name, format("// UTF Char: %c", c));
        putUtfChar(c);
    }


    public void putInt1(int n) {
        m_buffer.putInt1(n);
    }

    public void putInt1(int n, String name) {
        addTrace(name, format("// SInt1: %d", n));
        putInt1(n);
    }

    public void putUnsignedInt2(int n) {
        m_buffer.putInt2((short)n);
    }

    public void putUnsignedInt2(int n, String name) {
        addTrace(name, format("// UInt2: %d", n));
        putUnsignedInt2(n);
    }

    public void putInt2(int n) {
        m_buffer.putInt2((short)n);
    }

    public void putInt2(int n, String name) {
        addTrace(name, format("// SInt2: %d", n));
        putInt2(n);
    }

    public void putInt4(int n) {
        m_buffer.putInt4(n);
    }

    public void putInt4(int n, String name) {
        addTrace(name, format("// SInt4: %d", n));
        putInt4(n);
    }

    public void putInt8(long n) {
        m_buffer.putInt8(n);
    }

    public void putInt8(long n, String name) {
        addTrace(name, format("// SInt8: %d", n));
        putInt8(n);
    }

    public void putBytes(byte[] bytes, String name) {
        addTrace(name, format("// bytes[]"));
        m_buffer.putBytes(bytes);
    }


    public void putString(String s, String name) {
        addTrace(name, String.format("// String: '%s'", s));
        m_buffer.putBytes(s.getBytes(m_charset));
    }

    public void putString(String s, int length, String name) {
        addTrace(name, String.format("// String[%d]: '%s'", length, s));
        m_buffer.putBytes(padRight(s, length).getBytes(m_charset), 0, length);
    }


    public void replaceInt4(String name, int n) {
        //System.out.println("Replace " + addField + " with " + n);
        m_buffer.putInt4(getPosition(name), n);
    }

    public int diff(String name1, String name2) {
        return getPosition(name1) - getPosition(name2);
    }


    public byte[] getBytes() {
        return m_buffer.copyBytes();
    }

    public String toString() {
        StringBuilder b = new StringBuilder();

        for (int ii=0; ii<m_buffer.getLimit(); ii++) {
            String name = m_trace.getField(ii, "");
            String value = m_buffer.hex(ii);
            b.append(String.format("0x%04x %20s %s\n", ii, name, value));
        }
        return b.toString();
    }


    public void assertEquals(Buffer actual) {
        if (!actual.equals(m_buffer)) {
            System.out.format("%9s %20s %s %s\n", "Off", "Field", "Actual", "Expected");

            int limit = Math.max(m_buffer.getLimit(), actual.getLimit());

            for (int ii=0; ii<limit; ii++) {
                String name = m_trace.getField(ii, ""); //m_tracex.containsKey(ii) ? m_tracex.get(ii) : "";
                String actualByte = actual.hex(ii);
                String expectedByte = m_buffer.hex(ii);
                char mark = actualByte.equals(expectedByte) ? ' ' : '*';
                String comment = m_trace.getComment(ii, "");
                System.out.format("%4d 0x%04x %20s %4s %4s %c %s\n",
                                  ii,
                                  ii,
                                  name,
                                  actualByte,
                                  expectedByte,
                                  mark,
                                  comment);
            }

        }

        //Assert.assertEquals(actual.getHexDump(), m_buffer.getHexDump());
    }



    private static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);  
    }

}
