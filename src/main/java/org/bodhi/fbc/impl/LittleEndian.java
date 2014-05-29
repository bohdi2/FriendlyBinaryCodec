package org.bodhi.fbc.impl;

public class LittleEndian extends Endian {

    // -- get/put char --

    char getUtfChar(byte[] bb, int offset) {
        return makeChar(bb[offset + 1],
                        bb[offset]);
    }

    void putUtfChar(byte[] bb, int offset, char x) {
        bb[offset    ] = char0(x);
        bb[offset + 1] = char1(x);
    }

    // -- get/put short --

    int getInt2(byte[] bb, int offset) {
        return makeShort(bb[offset + 1],
                         bb[offset    ]);
    }

    int getUInt2(byte[] bb, int offset) {
        return makeInt(bb[offset + 1],
                       bb[offset    ]);
    }

    void putInt2(byte[] bb, int offset, int x) {
        bb[offset    ] = short0(x);
        bb[offset + 1] = short1(x);
    }

    void putUInt2(byte[] bb, int offset, int x) {
        bb[offset    ] = short0(x);
        bb[offset + 1] = short1(x);
    }

    // -- get/put int --

    int getInt4(byte[] bb, int offset) {
        return makeInt(bb[offset + 3],
                       bb[offset + 2],
                       bb[offset + 1],
                       bb[offset    ]);
    }

    void putInt4(byte[] bb, int offset, int x) {
        bb[offset + 3] = int3(x);
        bb[offset + 2] = int2(x);
        bb[offset + 1] = int1(x);
        bb[offset    ] = int0(x);
    }


    // -- get/put long --

    long getInt8(byte[] bb, int offset) {
        return makeLong(bb[offset + 7],
                        bb[offset + 6],
                        bb[offset + 5],
                        bb[offset + 4],
                        bb[offset + 3],
                        bb[offset + 2],
                        bb[offset + 1],
                        bb[offset    ]);
    }

    void putInt8(byte[] bb, int offset, long x) {
        bb[offset + 7] = long7(x);
        bb[offset + 6] = long6(x);
        bb[offset + 5] = long5(x);
        bb[offset + 4] = long4(x);
        bb[offset + 3] = long3(x);
        bb[offset + 2] = long2(x);
        bb[offset + 1] = long1(x);
        bb[offset    ] = long0(x);
    }

}
