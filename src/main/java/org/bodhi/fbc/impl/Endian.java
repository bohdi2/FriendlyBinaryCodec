

package org.bodhi.fbc.impl;

public class Endian {

    private Endian() { }

    // -- get/put char --

    static private char makeChar(byte b1, byte b0) {
        return (char)((b1 << 8) | (b0 & 0xff));
    }

    static char getLittleUtfChar(byte[] bb, int offset) {
        return makeChar(bb[offset + 1],
                        bb[offset]);
    }

    static char getBigUtfChar(byte[] bb, int offset) {
        return makeChar(bb[offset    ],
                        bb[offset + 1]);
    }

    static char getUtfChar(byte[] bb, int offset, boolean bigEndian) {
        return bigEndian ? getBigUtfChar(bb, offset) : getLittleUtfChar(bb, offset);
    }

    private static byte char1(char x) { return (byte)(x >> 8); }
    private static byte char0(char x) { return (byte)(x     ); }

    static void putUtfCharL(byte[] bb, int offset, char x) {
        bb[offset    ] = char0(x);
        bb[offset + 1] = char1(x);
    }

    static void putUtfCharB(byte[] bb, int offset, char x) {
        bb[offset    ] = char1(x);
        bb[offset + 1] = char0(x);
    }

    static void putUtfChar(byte[] bb, int offset, char x, boolean bigEndian) {
        if (bigEndian)
            putUtfCharB(bb, offset, x);
        else
            putUtfCharL(bb, offset, x);
    }

    static int getInt1(byte[] bb, int offset) {
        return bb[offset];
    }

    static void putInt1(byte[] bb, int offset, int value) {
        bb[offset] = (byte) (value & 0xFF);
    }

    // -- get/put short --

    static private int makeShort(byte b1, byte b0) {
        return (int)((b1 << 8) | (b0 & 0xff));
    }

    static int getLittleInt2(byte[] bb, int offset) {
        return makeShort(bb[offset + 1],
                         bb[offset    ]);
    }

    static int getBigInt2(byte[] bb, int offset) {
        return makeShort(bb[offset    ],
                         bb[offset + 1]);
    }

    static int getInt2(byte[] bb, int offset, boolean bigEndian) {
        return bigEndian ? getBigInt2(bb, offset) : getLittleInt2(bb, offset);
    }


    static private int makeInt(byte b1, byte b0) {
        return (int)(((b1 & 0xff) << 8) | (b0 & 0xff));
    }

    static int getLittleUnsignedInt2(byte[] bb, int offset) {
        return makeInt(bb[offset + 1],
                         bb[offset    ]);
    }

    static int getBigUnsignedInt2(byte[] bb, int offset) {
        return makeInt(bb[offset    ],
                         bb[offset + 1]);
    }

    static int getUnsignedInt2(byte[] bb, int offset, boolean bigEndian) {
        return bigEndian ? getBigUnsignedInt2(bb, offset) : getLittleUnsignedInt2(bb, offset);
    }

    private static byte short1(int x) { return (byte)(x >> 8); }
    private static byte short0(int x) { return (byte)(x     ); }

    static void putLittleInt2(byte[] bb, int offset, int x) {
        bb[offset    ] = short0(x);
        bb[offset + 1] = short1(x);
    }

    static void putBigInt2(byte[] bb, int offset, int x) {
        bb[offset    ] = short1(x);
        bb[offset + 1] = short0(x);
    }

    static void putInt2(byte[] bb, int offset, int x, boolean bigEndian) {
        if (bigEndian)
            putBigInt2(bb, offset, x);
        else
            putLittleInt2(bb, offset, x);
    }


    static void putLittleSignedInt2(byte[] bb, int offset, int x) {
        bb[offset    ] = short0(x);
        bb[offset + 1] = short1(x);
    }

    static void putBigSignedInt2(byte[] bb, int offset, int x) {
        bb[offset    ] = short1(x);
        bb[offset + 1] = short0(x);
    }

    static void putSignedInt2(byte[] bb, int offset, int x, boolean bigEndian) {
        if (bigEndian)
            putBigSignedInt2(bb, offset, x);
        else
            putLittleSignedInt2(bb, offset, x);
    }

    // -- get/put int --

    static private int makeInt(byte b3, byte b2, byte b1, byte b0) {
        return (((b3       ) << 24) |
                ((b2 & 0xff) << 16) |
                ((b1 & 0xff) <<  8) |
                ((b0 & 0xff)      ));
    }

    static int getLittleInt4(byte[] bb, int offset) {
        return makeInt(bb[offset + 3],
                       bb[offset + 2],
                       bb[offset + 1],
                       bb[offset    ]);
    }

    static int getBigInt4(byte[] bb, int offset) {
        return makeInt(bb[offset    ],
                       bb[offset + 1],
                       bb[offset + 2],
                       bb[offset + 3]);
    }

    static int getInt4(byte[] bb, int offset, boolean bigEndian) {
        return bigEndian ? getBigInt4(bb, offset) : getLittleInt4(bb, offset) ;
    }

    private static byte int3(int x) { return (byte)(x >> 24); }
    private static byte int2(int x) { return (byte)(x >> 16); }
    private static byte int1(int x) { return (byte)(x >>  8); }
    private static byte int0(int x) { return (byte)(x      ); }

    static void putLittleInt4(byte[] bb, int offset, int x) {
        bb[offset + 3] = int3(x);
        bb[offset + 2] = int2(x);
        bb[offset + 1] = int1(x);
        bb[offset    ] = int0(x);
    }

    static void putBigInt4(byte[] bb, int offset, int x) {
        bb[offset    ] = int3(x);
        bb[offset + 1] = int2(x);
        bb[offset + 2] = int1(x);
        bb[offset + 3] = int0(x);
    }

    static void putInt4(byte[] bb, int offset, int x, boolean bigEndian) {
        if (bigEndian)
            putBigInt4(bb, offset, x);
        else
            putLittleInt4(bb, offset, x);
    }


    // -- get/put long --

    static private long makeLong(byte b7, byte b6, byte b5, byte b4,
                                 byte b3, byte b2, byte b1, byte b0)
    {
        return ((((long)b7       ) << 56) |
                (((long)b6 & 0xff) << 48) |
                (((long)b5 & 0xff) << 40) |
                (((long)b4 & 0xff) << 32) |
                (((long)b3 & 0xff) << 24) |
                (((long)b2 & 0xff) << 16) |
                (((long)b1 & 0xff) <<  8) |
                (((long)b0 & 0xff)      ));
    }

    static long getLittleInt8(byte[] bb, int offset) {
        return makeLong(bb[offset + 7],
                        bb[offset + 6],
                        bb[offset + 5],
                        bb[offset + 4],
                        bb[offset + 3],
                        bb[offset + 2],
                        bb[offset + 1],
                        bb[offset    ]);
    }

    static long getBigInt8(byte[] bb, int offset) {
        return makeLong(bb[offset    ],
                        bb[offset + 1],
                        bb[offset + 2],
                        bb[offset + 3],
                        bb[offset + 4],
                        bb[offset + 5],
                        bb[offset + 6],
                        bb[offset + 7]);
    }

    static long getInt8(byte[] bb, int offset, boolean bigEndian) {
        return bigEndian ? getBigInt8(bb, offset) : getLittleInt8(bb, offset);
    }

    private static byte long7(long x) { return (byte)(x >> 56); }
    private static byte long6(long x) { return (byte)(x >> 48); }
    private static byte long5(long x) { return (byte)(x >> 40); }
    private static byte long4(long x) { return (byte)(x >> 32); }
    private static byte long3(long x) { return (byte)(x >> 24); }
    private static byte long2(long x) { return (byte)(x >> 16); }
    private static byte long1(long x) { return (byte)(x >>  8); }
    private static byte long0(long x) { return (byte)(x      ); }

    static void putLittleInt8(byte[] bb, int offset, long x) {
        bb[offset + 7] = long7(x);
        bb[offset + 6] = long6(x);
        bb[offset + 5] = long5(x);
        bb[offset + 4] = long4(x);
        bb[offset + 3] = long3(x);
        bb[offset + 2] = long2(x);
        bb[offset + 1] = long1(x);
        bb[offset    ] = long0(x);
    }

    static void putBigInt8(byte[] bb, int offset, long x) {
        bb[offset    ] = long7(x);
        bb[offset + 1] = long6(x);
        bb[offset + 2] = long5(x);
        bb[offset + 3] = long4(x);
        bb[offset + 4] = long3(x);
        bb[offset + 5] = long2(x);
        bb[offset + 6] = long1(x);
        bb[offset + 7] = long0(x);
    }

    static void putInt8(byte[] bb, int offset, long x, boolean bigEndian) {
        if (bigEndian)
            putBigInt8(bb, offset, x);
        else
            putLittleInt8(bb, offset, x);
    }




}
