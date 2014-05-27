package org.bodhi.fbc.impl;

/**
 * Created by chris on 5/26/14.
 */
public class Bytes {

    public static byte[] toBytes(int... ints) { // helper function
        byte[] result = new byte[ints.length];
        for (int i = 0; i < ints.length; i++) {
            result[i] = (byte) ints[i];
        }
        return result;
    }


}
