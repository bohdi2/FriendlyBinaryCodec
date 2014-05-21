
package org.bodhi.fbc;

import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;
import org.bodhi.fbc.impl.IoBuffer;
import java.nio.charset.Charset;

public class BinaryReader {
    private IoBuffer m_buffer;
    private Map<String, Integer> m_positions;
    private Map<Integer, String> m_trace;
    private Charset m_charset;

    public BinaryReader(byte[] bytes, Charset charset) {
        m_charset = charset;

        m_buffer = IoBuffer.wrap(bytes);
        m_buffer.order(ByteOrder.BIG_ENDIAN);

        m_positions = new HashMap<String, Integer>();
        m_trace = new HashMap<Integer, String>();
    }

    public void name(String name) {
        m_positions.put(name, m_buffer.position());
    }

    // Returns byte offset to the named position

    public int position(String name) {
        assert m_positions.containsKey(name) : name;
        return m_positions.get(name);
    }

    public void moveToPosition(int offset) {
        m_buffer.position(offset);
    }

    public boolean getBoolean() {
        return 0 != getInt1();
    }

    public boolean getBoolean(String name) {
        return 0 != getInt1(name);
    }

    public int getInt1() {
        return m_buffer.get();
    }

    public int getInt1(String name) {
        m_trace.put(m_buffer.position(), name);
        name(name);
        return getInt1();
    }

    public int getInt2() {
        return m_buffer.getShort();
    }

    public int getInt2(String name) {
        m_trace.put(m_buffer.position(), name);
        name(name);
        return getInt2();
    }

    public int getInt4() {
        return m_buffer.getInt();
    }

    public int getInt4(String name) {
        m_trace.put(m_buffer.position(), name);
        name(name);
        return getInt4();
    }

    public long getInt8() {
        return m_buffer.getLong();
    }

    public long getInt8(String name) {
        m_trace.put(m_buffer.position(), name);
        name(name);
        return getInt8();
    }

    public byte[] getBytes(int length, String name) {
        m_trace.put(m_buffer.position(), name);
        name(name);

        byte[] dst = new byte[length];
        m_buffer.get(dst, 0, length);
        return dst;
    }

    public String getString(int length, String name) {
        m_trace.put(m_buffer.position(), name);
        name(name);

        byte[] dst = new byte[length];
        m_buffer.get(dst, 0, length);
        return new String(dst, m_charset).trim();
    }

    public int diff(String name1, String name2) {
        return position(name1) - position(name2);
    }

    public String toString() {
        StringBuilder b = new StringBuilder();

        for (int ii=0; ii<m_buffer.limit(); ii++) {
            String name = m_trace.containsKey(ii) ? m_trace.get(ii) : "";
            String value = hex(m_buffer, ii);
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
                    String name = m_trace.containsKey(ii) ? m_trace.get(ii) : "";
                    String actualByte = x(actual, ii);
                    String expectedByte = x(m_buffer, ii);
                    char mark = actualByte.equals(expectedByte) ? ' ' : '*';
                    System.out.format("%4d 0x%04x %20s %4s %4s %c\n", ii, ii, name, actualByte, expectedByte, mark);
                }

            }

            Assert.assertEquals(actual.getHexDump(), m_buffer.getHexDump());
        }
    */
    private String hex(IoBuffer buffer, int index) {
        return (index < buffer.limit()) ? String.format("0x%02x", buffer.getUnsigned(index)) : "----";
    }

    private static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }

}
