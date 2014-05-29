package org.bodhi.fbc.impl;

public class BigEndian extends Endian {

    // -- get/put char --

    char getUtfChar(byte[] bb, int offset) {
        return makeChar(bb[offset    ],
                        bb[offset + 1]);
    }

    void putUtfChar(byte[] bb, int offset, char x) {
        bb[offset    ] = char1(x);
        bb[offset + 1] = char0(x);
    }

    // -- get/put short --

    int getInt2(byte[] bb, int offset) {
        return makeShort(bb[offset    ],
                         bb[offset + 1]);
    }


    int getUInt2(byte[] bb, int offset) {
        return makeInt(bb[offset    ],
                       bb[offset + 1]);
    }

    void putInt2(byte[] bb, int offset, int x) {
        bb[offset    ] = short1(x);
        bb[offset + 1] = short0(x);
    }

    void putUInt2(byte[] bb, int offset, int x) {
        bb[offset    ] = short1(x);
        bb[offset + 1] = short0(x);
    }


    // -- get/put int --

    int getInt4(byte[] bb, int offset) {
        return makeInt(bb[offset    ],
                       bb[offset + 1],
                       bb[offset + 2],
                       bb[offset + 3]);
    }

    void putInt4(byte[] bb, int offset, int x) {
        bb[offset    ] = int3(x);
        bb[offset + 1] = int2(x);
        bb[offset + 2] = int1(x);
        bb[offset + 3] = int0(x);
    }


    // -- get/put long --

    long getInt8(byte[] bb, int offset) {
        return makeLong(bb[offset    ],
                        bb[offset + 1],
                        bb[offset + 2],
                        bb[offset + 3],
                        bb[offset + 4],
                        bb[offset + 5],
                        bb[offset + 6],
                        bb[offset + 7]);
    }

    void putInt8(byte[] bb, int offset, long x) {
        bb[offset    ] = long7(x);
        bb[offset + 1] = long6(x);
        bb[offset + 2] = long5(x);
        bb[offset + 3] = long4(x);
        bb[offset + 4] = long3(x);
        bb[offset + 5] = long2(x);
        bb[offset + 6] = long1(x);
        bb[offset + 7] = long0(x);
    }
}
