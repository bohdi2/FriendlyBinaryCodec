
package org.bodhi.fbc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.bodhi.fbc.impl.Buffer;
import java.nio.charset.Charset;

public class BinaryReader {
    private Buffer m_buffer;
    private Map<String, Integer> m_positions;
    private Map<Integer, String> m_trace;
    private Charset m_charset;

    public BinaryReader(byte[] bytes, Charset charset) {
        m_charset = charset;

        m_buffer = new Buffer(bytes);//IoBuffer.wrap(bytes);
        //m_buffer.order(ByteOrder.BIG_ENDIAN);

        m_positions = new HashMap<String, Integer>();
        m_trace = new HashMap<Integer, String>();
    }

    public void name(String name) {
        m_positions.put(name, m_buffer.getPosition());
    }

    // Returns byte offset to the named position

    public int position(String name) {
        assert m_positions.containsKey(name) : name;
        return m_positions.get(name);
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

    public int getInt1() {
        return m_buffer.getByte();
    }

    public int getInt1(String name) {
        m_trace.put(m_buffer.getPosition(), name);
        name(name);
        return getInt1();
    }

    public short getSignedInt2() throws IOException {
        return m_buffer.getSignedInt2();
    }

    public short getSignedInt2(String name) throws IOException {
        m_trace.put(m_buffer.getPosition(), name);
        name(name);
        return getSignedInt2();
    }

    public int getUnsignedInt2() throws IOException {
        return m_buffer.getUnsignedInt2();
    }

    public int getUnsignedInt2(String name) throws IOException {
        m_trace.put(m_buffer.getPosition(), name);
        name(name);
        return getUnsignedInt2();
    }


    public int getSignedInt4() throws IOException {
        return m_buffer.getSignedInt4();
    }

    public int getSignedInt4(String name) throws IOException{
        m_trace.put(m_buffer.getPosition(), name);
        name(name);
        return getSignedInt4();
    }

    public long getSignedInt8() throws IOException {
        return m_buffer.getSignedInt8();
    }

    public long getSignedInt8(String name) throws IOException {
        m_trace.put(m_buffer.getPosition(), name);
        name(name);
        return getSignedInt8();
    }

    public byte[] getBytes(int length, String name) {
        m_trace.put(m_buffer.getPosition(), name);
        name(name);

        byte[] dst = new byte[length];
        m_buffer.getBytes(dst, 0, length);
        return dst;
    }

    public char getUtfChar() {
        return m_buffer.getUtfChar();
    }

    public char getUtfChar(String name) {
        m_trace.put(m_buffer.getPosition(), name);
        name(name);
        return getUtfChar();
    }

    public String getString(int length, String name) {
        m_trace.put(m_buffer.getPosition(), name);
        name(name);

        byte[] dst = new byte[length];
        m_buffer.getBytes(dst, 0, length);
        return new String(dst, m_charset).trim();
    }

    public int diff(String name1, String name2) {
        return position(name1) - position(name2);
    }

    public String toString() {
        StringBuilder b = new StringBuilder();

        for (int ii=0; ii<m_buffer.getLimit(); ii++) {
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
    private String hex(Buffer buffer, int index) {
        return "FF"; //(index < buffer.getLimit()) ? String.format("0x%02x", buffer.getUnsignedInt2(index)) : "----";
    }

    private static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }

}
