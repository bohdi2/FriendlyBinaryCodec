package org.bodhi.fbc;

import java.nio.charset.Charset;
import org.junit.Test;

import static org.junit.Assert.*;

public class BinaryWriterTest {

    @Test
    public void test_growth() throws Exception {
        BinaryWriter bw = new BinaryWriter(4, Charset.forName("ISO-8859-1"));

        for (int ii=0; ii<20; ii++) {
            bw.putInt1(ii);
        }

        byte[] raw = bw.getBytes();
        assertEquals(20, raw.length);
        for (int ii=0; ii<20; ii++) {
            assertEquals(ii, raw[ii]);
        }
    }

}