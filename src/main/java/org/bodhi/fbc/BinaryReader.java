
package org.bodhi.fbc;

import java.io.IOException;
import org.bodhi.fbc.impl.Buffer;
import org.bodhi.fbc.impl.Trace;

import java.nio.charset.Charset;

import static java.lang.String.format;

public class BinaryReader {
    private Buffer m_buffer;
    private final Trace m_trace;

    private Charset m_charset;

    public BinaryReader(byte[] bytes, Charset charset) {
        m_charset = charset;

        m_buffer = new Buffer(bytes);//IoBuffer.wrap(bytes);
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
        return m_buffer.getSignedInt1();
    }

    public int getInt1(String name) {
        addTrace(name, format("// SInt1"));
        return getInt1();
    }

    public int getSignedInt2() throws IOException {
        return m_buffer.getSignedInt2();
    }

    public int getSignedInt2(String name) throws IOException {
        addTrace(name, format("// SInt2"));
        return getSignedInt2();
    }

    public int getUnsignedInt2() throws IOException {
        return m_buffer.getUnsignedInt2();
    }

    public int getUnsignedInt2(String name) throws IOException {
        addTrace(name, format("// UInt2"));
        return getUnsignedInt2();
    }


    public int getSignedInt4() throws IOException {
        return m_buffer.getSignedInt4();
    }

    public int getSignedInt4(String name) throws IOException{
        addTrace(name, format("// SInt4"));
        return getSignedInt4();
    }

    public long getSignedInt8() throws IOException {
        return m_buffer.getSignedInt8();
    }

    public long getSignedInt8(String name) throws IOException {
        addTrace(name, format("// SInt8"));
        return getSignedInt8();
    }

    public byte[] getBytes(int length, String name) {
        addTrace(name, format("// bytes[%d]", length));

        byte[] dst = new byte[length];
        m_buffer.getBytes(dst, 0, length);
        return dst;
    }

    public char getUtfChar() {
        return m_buffer.getUtfChar();
    }

    public char getUtfChar(String name) {
        addTrace(name, format("// UTF Char"));
        return getUtfChar();
    }

    public String getString(int length, String name) {
        addTrace(name, format("// String[%d", length));

        byte[] dst = new byte[length];
        m_buffer.getBytes(dst, 0, length);
        return new String(dst, m_charset).trim();
    }

    public int diff(String name1, String name2) {
        return getPosition(name1) - getPosition(name2);
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

    /*
        public void assertEquals(IoBuffer actual) {
            m_buffer.flip();

            if (!actual.equals(m_buffer)) {
                System.out.format("%9s %20s %s %s\n", "Off", "Field", "Actual", "Expected");

                int limit = m_buffer.limit() > actual.limit() ? m_buffer.limit() : actual.limit();
                for (int ii=0; ii<limit; ii++) {
                    String addField = m_trace.containsKey(ii) ? m_trace.get(ii) : "";
                    String actualByte = x(actual, ii);
                    String expectedByte = x(m_buffer, ii);
                    char mark = actualByte.equals(expectedByte) ? ' ' : '*';
                    System.out.format("%4d 0x%04x %20s %4s %4s %c\n", ii, ii, addField, actualByte, expectedByte, mark);
                }

            }

            Assert.assertEquals(actual.getHexDump(), m_buffer.getHexDump());
        }
    */

    public Buffer getBuffer() {
        return m_buffer;
    }

    private static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }

}
