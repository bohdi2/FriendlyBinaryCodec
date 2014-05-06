/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package org.chelck.fbc.impl;

import java.io.FilterOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.EnumSet;
import java.util.Set;

/**
 * A {@link IoBuffer} that wraps a buffer and proxies any operations to it.
 * <p>
 * You can think this class like a {@link FilterOutputStream}.  All operations
 * are proxied by default so that you can extend this class and override existing
 * operations selectively.  You can introduce new operations, too.
 *
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public class IoBufferWrapper extends IoBuffer {

    /**
     * The buffer proxied by this proxy.
     */
    private final IoBuffer buf;

    /**
     * Create a new instance.
     * @param buf the buffer to be proxied
     */
    protected IoBufferWrapper(IoBuffer buf) {
        if (buf == null) {
            throw new IllegalArgumentException("buf");
        }
        this.buf = buf;
    }
    
    /**
     * Returns the parent buffer that this buffer wrapped.
     */
    public IoBuffer getParentBuffer() {
        return buf;
    }

    @Override
    public ByteBuffer buf() {
        return buf.buf();
    }

    @Override
    public int capacity() {
        return buf.capacity();
    }

    @Override
    public int position() {
        return buf.position();
    }

    @Override
    public IoBuffer position(int newPosition) {
        buf.position(newPosition);
        return this;
    }

    @Override
    public int limit() {
        return buf.limit();
    }

    @Override
    public IoBuffer limit(int newLimit) {
        buf.limit(newLimit);
        return this;
    }

    @Override
    public IoBuffer mark() {
        buf.mark();
        return this;
    }

    @Override
    public IoBuffer reset() {
        buf.reset();
        return this;
    }

    @Override
    public IoBuffer clear() {
        buf.clear();
        return this;
    }

    @Override
    public IoBuffer sweep() {
        buf.sweep();
        return this;
    }

    @Override
    public IoBuffer sweep(byte value) {
        buf.sweep(value);
        return this;
    }

    @Override
    public IoBuffer flip() {
        buf.flip();
        return this;
    }

    @Override
    public IoBuffer rewind() {
        buf.rewind();
        return this;
    }

    @Override
    public int remaining() {
        return buf.remaining();
    }

    @Override
    public boolean hasRemaining() {
        return buf.hasRemaining();
    }

    @Override
    public byte get() {
        return buf.get();
    }

    @Override
    public short getUnsigned() {
        return buf.getUnsigned();
    }

    @Override
    public IoBuffer put(byte b) {
        buf.put(b);
        return this;
    }

    @Override
    public byte get(int index) {
        return buf.get(index);
    }

    @Override
    public short getUnsigned(int index) {
        return buf.getUnsigned(index);
    }

    @Override
    public IoBuffer put(int index, byte b) {
        buf.put(index, b);
        return this;
    }

    @Override
    public IoBuffer get(byte[] dst, int offset, int length) {
        buf.get(dst, offset, length);
        return this;
    }

    @Override
    public IoBuffer getSlice(int index, int length) {
        return buf.getSlice(index, length);
    }

    @Override
    public IoBuffer getSlice(int length) {
        return buf.getSlice(length);
    }

    @Override
    public IoBuffer get(byte[] dst) {
        buf.get(dst);
        return this;
    }

    @Override
    public IoBuffer put(IoBuffer src) {
        buf.put(src);
        return this;
    }

    @Override
    public IoBuffer put(ByteBuffer src) {
        buf.put(src);
        return this;
    }

    @Override
    public IoBuffer put(byte[] src, int offset, int length) {
        buf.put(src, offset, length);
        return this;
    }

    @Override
    public IoBuffer put(byte[] src) {
        buf.put(src);
        return this;
    }

    @Override
    public IoBuffer compact() {
        buf.compact();
        return this;
    }

    @Override
    public String toString() {
        return buf.toString();
    }

    @Override
    public int hashCode() {
        return buf.hashCode();
    }

    @Override
    public boolean equals(Object ob) {
        return buf.equals(ob);
    }

    public int compareTo(IoBuffer that) {
        return buf.compareTo(that);
    }

    @Override
    public ByteOrder order() {
        return buf.order();
    }

    @Override
    public IoBuffer order(ByteOrder bo) {
        buf.order(bo);
        return this;
    }

    @Override
    public char getChar() {
        return buf.getChar();
    }

    @Override
    public IoBuffer putChar(char value) {
        buf.putChar(value);
        return this;
    }

    @Override
    public char getChar(int index) {
        return buf.getChar(index);
    }

    @Override
    public IoBuffer putChar(int index, char value) {
        buf.putChar(index, value);
        return this;
    }

    @Override
    public CharBuffer asCharBuffer() {
        return buf.asCharBuffer();
    }

    @Override
    public short getShort() {
        return buf.getShort();
    }

    @Override
    public int getUnsignedShort() {
        return buf.getUnsignedShort();
    }

    @Override
    public IoBuffer putShort(short value) {
        buf.putShort(value);
        return this;
    }

    @Override
    public short getShort(int index) {
        return buf.getShort(index);
    }

    @Override
    public int getUnsignedShort(int index) {
        return buf.getUnsignedShort(index);
    }

    @Override
    public IoBuffer putShort(int index, short value) {
        buf.putShort(index, value);
        return this;
    }

    @Override
    public int getInt() {
        return buf.getInt();
    }

    @Override
    public long getUnsignedInt() {
        return buf.getUnsignedInt();
    }

    @Override
    public IoBuffer putInt(int value) {
        buf.putInt(value);
        return this;
    }

    @Override
    public int getInt(int index) {
        return buf.getInt(index);
    }

    @Override
    public long getUnsignedInt(int index) {
        return buf.getUnsignedInt(index);
    }

    @Override
    public IoBuffer putInt(int index, int value) {
        buf.putInt(index, value);
        return this;
    }

    @Override
    public long getLong() {
        return buf.getLong();
    }

    @Override
    public IoBuffer putLong(long value) {
        buf.putLong(value);
        return this;
    }

    @Override
    public long getLong(int index) {
        return buf.getLong(index);
    }

    @Override
    public IoBuffer putLong(int index, long value) {
        buf.putLong(index, value);
        return this;
    }

    @Override
    public String getHexDump() {
        return buf.getHexDump();
    }

    @Override
    public String getString(int fieldSize, CharsetDecoder decoder)
            throws CharacterCodingException {
        return buf.getString(fieldSize, decoder);
    }

    @Override
    public String getString(CharsetDecoder decoder)
            throws CharacterCodingException {
        return buf.getString(decoder);
    }

    @Override
    public String getPrefixedString(CharsetDecoder decoder)
            throws CharacterCodingException {
        return buf.getPrefixedString(decoder);
    }

    @Override
    public String getPrefixedString(int prefixLength, CharsetDecoder decoder)
            throws CharacterCodingException {
        return buf.getPrefixedString(prefixLength, decoder);
    }

    @Override
    public IoBuffer putString(CharSequence in, int fieldSize,
            CharsetEncoder encoder) throws CharacterCodingException {
        buf.putString(in, fieldSize, encoder);
        return this;
    }

    @Override
    public IoBuffer putString(CharSequence in, CharsetEncoder encoder)
            throws CharacterCodingException {
        buf.putString(in, encoder);
        return this;
    }

    @Override
    public IoBuffer putPrefixedString(CharSequence in, CharsetEncoder encoder)
            throws CharacterCodingException {
        buf.putPrefixedString(in, encoder);
        return this;
    }

    @Override
    public IoBuffer putPrefixedString(CharSequence in, int prefixLength,
            CharsetEncoder encoder) throws CharacterCodingException {
        buf.putPrefixedString(in, prefixLength, encoder);
        return this;
    }

    @Override
    public IoBuffer putPrefixedString(CharSequence in, int prefixLength,
            int padding, CharsetEncoder encoder)
            throws CharacterCodingException {
        buf.putPrefixedString(in, prefixLength, padding, encoder);
        return this;
    }

    @Override
    public IoBuffer putPrefixedString(CharSequence in, int prefixLength,
            int padding, byte padValue, CharsetEncoder encoder)
            throws CharacterCodingException {
        buf.putPrefixedString(in, prefixLength, padding, padValue, encoder);
        return this;
    }

    @Override
    public IoBuffer skip(int size) {
        buf.skip(size);
        return this;
    }

    @Override
    public IoBuffer fill(byte value, int size) {
        buf.fill(value, size);
        return this;
    }

    @Override
    public IoBuffer fillAndReset(byte value, int size) {
        buf.fillAndReset(value, size);
        return this;
    }

    @Override
    public IoBuffer fill(int size) {
        buf.fill(size);
        return this;
    }

    @Override
    public IoBuffer fillAndReset(int size) {
        buf.fillAndReset(size);
        return this;
    }

    @Override
    public boolean isAutoExpand() {
        return buf.isAutoExpand();
    }

    @Override
    public IoBuffer setAutoExpand(boolean autoExpand) {
        buf.setAutoExpand(autoExpand);
        return this;
    }

    @Override
    public IoBuffer expand(int pos, int expectedRemaining) {
        buf.expand(pos, expectedRemaining);
        return this;
    }

    @Override
    public IoBuffer expand(int expectedRemaining) {
        buf.expand(expectedRemaining);
        return this;
    }

    @Override
    public IoBuffer duplicate() {
        return buf.duplicate();
    }

    @Override
    public IoBuffer slice() {
        return buf.slice();
    }

    @Override
    public IoBuffer asReadOnlyBuffer() {
        return buf.asReadOnlyBuffer();
    }

    @Override
    public byte[] array() {
        return buf.array();
    }

    @Override
    public int arrayOffset() {
        return buf.arrayOffset();
    }

    @Override
    public int minimumCapacity() {
        return buf.minimumCapacity();
    }

    @Override
    public IoBuffer minimumCapacity(int minimumCapacity) {
        buf.minimumCapacity(minimumCapacity);
        return this;
    }

    @Override
    public IoBuffer capacity(int newCapacity) {
        buf.capacity(newCapacity);
        return this;
    }

    @Override
    public boolean isReadOnly() {
        return buf.isReadOnly();
    }

    @Override
    public int markValue() {
        return buf.markValue();
    }

    @Override
    public boolean hasArray() {
        return buf.hasArray();
    }

    @Override
    public void free() {
        buf.free();
    }

    @Override
    public boolean isDerived() {
        return buf.isDerived();
    }

    @Override
    public boolean isAutoShrink() {
        return buf.isAutoShrink();
    }

    @Override
    public IoBuffer setAutoShrink(boolean autoShrink) {
        buf.setAutoShrink(autoShrink);
        return this;
    }

    @Override
    public IoBuffer shrink() {
        buf.shrink();
        return this;
    }

    @Override
    public String getHexDump(int lengthLimit) {
        return buf.getHexDump(lengthLimit);
    }

    @Override
    public boolean prefixedDataAvailable(int prefixLength) {
        return buf.prefixedDataAvailable(prefixLength);
    }

    @Override
    public boolean prefixedDataAvailable(int prefixLength, int maxDataLength) {
        return buf.prefixedDataAvailable(prefixLength, maxDataLength);
    }

    @Override
    public int indexOf(byte b) {
        return buf.indexOf(b);
    }

}
