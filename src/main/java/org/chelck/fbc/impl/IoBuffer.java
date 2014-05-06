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

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;


/**
 * A byte buffer used by MINA applications.
 * <p>
 * This is a replacement for {@link ByteBuffer}. Please refer to
 * {@link ByteBuffer} documentation for preliminary usage. MINA does not use NIO
 * {@link ByteBuffer} directly for two reasons:
 * <ul>
 * <li>It doesn't provide useful getters and putters such as <code>fill</code>,
 * <code>get/putString</code>, and <code>get/putAsciiInt()</code> enough.</li>
 * <li>It is difficult to write variable-length data due to its fixed capacity</li>
 * </ul>
 * </p>
 * 
 * <h2>Allocation</h2>
 * <p>
 * You can allocate a new heap buffer.
 * 
 * <pre>
 * IoBuffer buf = IoBuffer.allocate(1024);
 * </pre>
 * 
 *
 * </p>
 * 
 * <h2>Wrapping existing NIO buffers and arrays</h2>
 * <p>
 * This class provides a few <tt>wrap(...)</tt> methods that wraps any NIO
 * buffers and byte arrays.
 * 
 * <h2>AutoExpand</h2>
 * <p>
 * Writing variable-length data using NIO <tt>ByteBuffers</tt> is not really
 * easy, and it is because its size is fixed. {@link IoBuffer} introduces
 * <tt>autoExpand</tt> property. If <tt>autoExpand</tt> property is true, you
 * never get {@link BufferOverflowException} or
 * {@link IndexOutOfBoundsException} (except when index is negative). It
 * automatically expands its capacity and limit value. For example:
 * 
 * <pre>
 * String greeting = messageBundle.getMessage(&quot;hello&quot;);
 * IoBuffer buf = IoBuffer.allocate(16);
 * // Turn on autoExpand (it is off by default)
 * buf.setAutoExpand(true);
 * buf.putString(greeting, utf8encoder);
 * </pre>
 * 
 * The underlying {@link ByteBuffer} is reallocated by {@link IoBuffer} behind
 * the scene if the encoded data is larger than 16 bytes in the example above.
 * Its capacity will double, and its limit will increase to the last position
 * the string is written.
 * </p>
 * 
 * <h2>AutoShrink</h2>
 * <p>
 * You might also want to decrease the capacity of the buffer when most of the
 * allocated memory area is not being used. {@link IoBuffer} provides
 * <tt>autoShrink</tt> property to take care of this issue. If
 * <tt>autoShrink</tt> is turned on, {@link IoBuffer} halves the capacity of the
 * buffer when {@link #compact()} is invoked and only 1/4 or less of the current
 * capacity is being used.
 * <p>
 * You can also {@link #shrink()} method manually to shrink the capacity of the
 * buffer.
 * <p>
 * The underlying {@link ByteBuffer} is reallocated by {@link IoBuffer} behind
 * the scene, and therefore {@link #buf()} will return a different
 * {@link ByteBuffer} instance once capacity changes. Please also note
 * {@link #compact()} or {@link #shrink()} will not decrease the capacity if the
 * new capacity is less than the {@link #minimumCapacity()} of the buffer.
 * 
 * <h2>Derived Buffers</h2>
 * <p>
 * Derived buffers are the buffers which were created by {@link #duplicate()},
 * {@link #slice()}, or {@link #asReadOnlyBuffer()}. They are useful especially
 * when you broadcast the same messages to multiple {@link IoSession}s. Please
 * note that the buffer derived from and its derived buffers are not both
 * auto-expandable neither auto-shrinkable. Trying to call
 * {@link #setAutoExpand(boolean)} or {@link #setAutoShrink(boolean)} with
 * <tt>true</tt> parameter will raise an {@link IllegalStateException}.
 * </p>
 * 
 * <h2>Changing Buffer Allocation Policy</h2>
 * <p>
 * {@link IoBufferAllocator} interface lets you override the default buffer
 * management behavior. There are two allocators provided out-of-the-box:
 * <ul>
 * <li>{@link SimpleBufferAllocator} (default)</li>
 * <li>{@link CachedBufferAllocator}</li>
 * </ul>
 * You can implement your own allocator and use it by calling
 * {@link #setAllocator(IoBufferAllocator)}.
 * </p>
 * 
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public abstract class IoBuffer implements Comparable<IoBuffer> {
    /** The allocator used to create new buffers */
    private static IoBufferAllocator allocator = new SimpleBufferAllocator();

    /**
     * Returns the allocator used by existing and new buffers
     */
    public static IoBufferAllocator getAllocator() {
        return allocator;
    }

    /**
     * Sets the allocator used by existing and new buffers
     */
    public static void setAllocator(IoBufferAllocator newAllocator) {
        if (newAllocator == null) {
            throw new IllegalArgumentException("allocator");
        }

        IoBufferAllocator oldAllocator = allocator;

        allocator = newAllocator;

        if (null != oldAllocator) {
            oldAllocator.dispose();
        }
    }



    /**
     * Returns the buffer which is capable of the specified size.
     * 
     * @param capacity
     *            the capacity of the buffer
     */
    public static IoBuffer allocate(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("capacity: " + capacity);
        }

        return allocator.allocate(capacity);
    }

    /**
     * Wraps the specified NIO {@link ByteBuffer} into MINA buffer.
     */
    public static IoBuffer wrap(ByteBuffer nioBuffer) {
        return allocator.wrap(nioBuffer);
    }

    /**
     * Wraps the specified byte array into MINA heap buffer.
     */
    public static IoBuffer wrap(byte[] byteArray) {
        return wrap(ByteBuffer.wrap(byteArray));
    }

    /**
     * Wraps the specified byte array into MINA heap buffer.
     */
    public static IoBuffer wrap(byte[] byteArray, int offset, int length) {
        return wrap(ByteBuffer.wrap(byteArray, offset, length));
    }

    /**
     * Normalizes the specified capacity of the buffer to power of 2, which is
     * often helpful for optimal memory usage and performance. If it is greater
     * than or equal to {@link Integer#MAX_VALUE}, it returns
     * {@link Integer#MAX_VALUE}. If it is zero, it returns zero.
     */
    protected static int normalizeCapacity(int requestedCapacity) {
        if (requestedCapacity < 0) {
            return Integer.MAX_VALUE;
        }

        int newCapacity = Integer.highestOneBit(requestedCapacity);
        newCapacity <<= (newCapacity < requestedCapacity ? 1 : 0);
        return newCapacity < 0 ? Integer.MAX_VALUE : newCapacity;
    }

    /**
     * Creates a new instance. This is an empty constructor.
     */
    protected IoBuffer() {
        // Do nothing
    }

    /**
     * Declares this buffer and all its derived buffers are not used anymore so
     * that it can be reused by some {@link IoBufferAllocator} implementations.
     * It is not mandatory to call this method, but you might want to invoke
     * this method for maximum performance.
     */
    public abstract void free();

    /**
     * Returns the underlying NIO buffer instance.
     */
    public abstract ByteBuffer buf();

    /**
     * Returns the minimum capacity of this buffer which is used to determine
     * the new capacity of the buffer shrunk by {@link #compact()} and
     * {@link #shrink()} operation. The default value is the initial capacity of
     * the buffer.
     */
    public abstract int minimumCapacity();

    /**
     * Sets the minimum capacity of this buffer which is used to determine the
     * new capacity of the buffer shrunk by {@link #compact()} and
     * {@link #shrink()} operation. The default value is the initial capacity of
     * the buffer.
     */
    public abstract IoBuffer minimumCapacity(int minimumCapacity);

    /**
     * @see ByteBuffer#capacity()
     */
    public abstract int capacity();

    /**
     * Increases the capacity of this buffer. If the new capacity is less than
     * or equal to the current capacity, this method returns silently. If the
     * new capacity is greater than the current capacity, the buffer is
     * reallocated while retaining the position, limit, mark and the content of
     * the buffer.
     */
    public abstract IoBuffer capacity(int newCapacity);

    /**
     * Returns <tt>true</tt> if and only if <tt>autoExpand</tt> is turned on.
     */
    public abstract boolean isAutoExpand();

    /**
     * Turns on or off <tt>autoExpand</tt>.
     */
    public abstract IoBuffer setAutoExpand(boolean autoExpand);

    /**
     * Returns <tt>true</tt> if and only if <tt>autoShrink</tt> is turned on.
     */
    public abstract boolean isAutoShrink();

    /**
     * Turns on or off <tt>autoShrink</tt>.
     */
    public abstract IoBuffer setAutoShrink(boolean autoShrink);

    /**
     * Changes the capacity and limit of this buffer so this buffer get the
     * specified <tt>expectedRemaining</tt> room from the current position. This
     * method works even if you didn't set <tt>autoExpand</tt> to <tt>true</tt>.
     */
    public abstract IoBuffer expand(int expectedRemaining);

    /**
     * Changes the capacity and limit of this buffer so this buffer get the
     * specified <tt>expectedRemaining</tt> room from the specified
     * <tt>position</tt>. This method works even if you didn't set
     * <tt>autoExpand</tt> to <tt>true</tt>.
     */
    public abstract IoBuffer expand(int position, int expectedRemaining);

    /**
     * Changes the capacity of this buffer so this buffer occupies as less
     * memory as possible while retaining the position, limit and the buffer
     * content between the position and limit. The capacity of the buffer never
     * becomes less than {@link #minimumCapacity()}. The mark is discarded once
     * the capacity changes.
     */
    public abstract IoBuffer shrink();

    /**
     * @see java.nio.Buffer#position()
     */
    public abstract int position();

    /**
     * @see java.nio.Buffer#position(int)
     */
    public abstract IoBuffer position(int newPosition);

    /**
     * @see java.nio.Buffer#limit()
     */
    public abstract int limit();

    /**
     * @see java.nio.Buffer#limit(int)
     */
    public abstract IoBuffer limit(int newLimit);

    /**
     * @see java.nio.Buffer#mark()
     */
    public abstract IoBuffer mark();

    /**
     * Returns the position of the current mark. This method returns <tt>-1</tt>
     * if no mark is set.
     */
    public abstract int markValue();

    /**
     * @see java.nio.Buffer#reset()
     */
    public abstract IoBuffer reset();

    /**
     * @see java.nio.Buffer#clear()
     */
    public abstract IoBuffer clear();

    /**
     * Clears this buffer and fills its content with <tt>NUL</tt>. The position
     * is set to zero, the limit is set to the capacity, and the mark is
     * discarded.
     */
    public abstract IoBuffer sweep();

    /**
     * double Clears this buffer and fills its content with <tt>value</tt>. The
     * position is set to zero, the limit is set to the capacity, and the mark
     * is discarded.
     */
    public abstract IoBuffer sweep(byte value);

    /**
     * @see java.nio.Buffer#flip()
     */
    public abstract IoBuffer flip();

    /**
     * @see java.nio.Buffer#rewind()
     */
    public abstract IoBuffer rewind();

    /**
     * @see java.nio.Buffer#remaining()
     */
    public abstract int remaining();

    /**
     * @see java.nio.Buffer#hasRemaining()
     */
    public abstract boolean hasRemaining();

    /**
     * @see ByteBuffer#slice()
     */
    //public abstract IoBuffer slice();

    /**
     * @see ByteBuffer#hasArray()
     */
    public abstract boolean hasArray();

    /**
     * @see ByteBuffer#array()
     */
    public abstract byte[] array();

    /**
     * @see ByteBuffer#arrayOffset()
     */
    public abstract int arrayOffset();

    /**
     * @see ByteBuffer#get()
     */
    public abstract byte get();

    /**
     * Reads one unsigned byte as a short integer.
     */
    public abstract short getUnsigned();

    /**
     * @see ByteBuffer#put(byte)
     */
    public abstract IoBuffer put(byte b);

    /**
     * @see ByteBuffer#get(int)
     */
    public abstract byte get(int index);

    /**
     * Reads one byte as an unsigned short integer.
     */
    public abstract short getUnsigned(int index);

    /**
     * @see ByteBuffer#put(int, byte)
     */
    public abstract IoBuffer put(int index, byte b);

    /**
     * @see ByteBuffer#get(byte[], int, int)
     */
    public abstract IoBuffer get(byte[] dst, int offset, int length);

    /**
     * @see ByteBuffer#get(byte[])
     */
    public abstract IoBuffer get(byte[] dst);

    /**
     * Writes the content of the specified <tt>src</tt> into this buffer.
     */
    public abstract IoBuffer put(ByteBuffer src);

    /**
     * Writes the content of the specified <tt>src</tt> into this buffer.
     */
    public abstract IoBuffer put(IoBuffer src);

    /**
     * @see ByteBuffer#put(byte[], int, int)
     */
    public abstract IoBuffer put(byte[] src, int offset, int length);

    /**
     * @see ByteBuffer#put(byte[])
     */
    public abstract IoBuffer put(byte[] src);

    /**
     * @see ByteBuffer#compact()
     */
    public abstract IoBuffer compact();

    /**
     * @see ByteBuffer#order()
     */
    public abstract ByteOrder order();

    /**
     * @see ByteBuffer#order(ByteOrder)
     */
    public abstract IoBuffer order(ByteOrder bo);

    /**
     * @see ByteBuffer#getChar()
     */
    public abstract char getChar();

    /**
     * @see ByteBuffer#putChar(char)
     */
    public abstract IoBuffer putChar(char value);

    /**
     * @see ByteBuffer#getChar(int)
     */
    public abstract char getChar(int index);

    /**
     * @see ByteBuffer#putChar(int, char)
     */
    public abstract IoBuffer putChar(int index, char value);

    /**
     * @see ByteBuffer#asCharBuffer()
     */
    public abstract CharBuffer asCharBuffer();

    /**
     * @see ByteBuffer#getShort()
     */
    public abstract short getShort();

    /**
     * Reads two bytes unsigned integer.
     */
    public abstract int getUnsignedShort();

    /**
     * @see ByteBuffer#putShort(short)
     */
    public abstract IoBuffer putShort(short value);

    /**
     * @see ByteBuffer#getShort()
     */
    public abstract short getShort(int index);

    /**
     * Reads two bytes unsigned integer.
     */
    public abstract int getUnsignedShort(int index);

    /**
     * @see ByteBuffer#putShort(int, short)
     */
    public abstract IoBuffer putShort(int index, short value);

    /**
     * @see ByteBuffer#getInt()
     */
    public abstract int getInt();

    /**
     * Reads four bytes unsigned integer.
     */
    public abstract long getUnsignedInt();


    /**
     * @see ByteBuffer#putInt(int)
     */
    public abstract IoBuffer putInt(int value);

    /**
     * @see ByteBuffer#getInt(int)
     */
    public abstract int getInt(int index);

    /**
     * Reads four bytes unsigned integer.
     */
    public abstract long getUnsignedInt(int index);

    /**
     * @see ByteBuffer#putInt(int, int)
     */
    public abstract IoBuffer putInt(int index, int value);

    /**
     * @see ByteBuffer#asIntBuffer()
     */
    //public abstract IntBuffer asIntBuffer();

    /**
     * @see ByteBuffer#getLong()
     */
    public abstract long getLong();

    /**
     * @see ByteBuffer#putLong(int, long)
     */
    public abstract IoBuffer putLong(long value);

    /**
     * @see ByteBuffer#getLong(int)
     */
    public abstract long getLong(int index);

    /**
     * @see ByteBuffer#putLong(int, long)
     */
    public abstract IoBuffer putLong(int index, long value);

    /**
     * Returns hexdump of this buffer. The data and pointer are not changed as a
     * result of this method call.
     * 
     * @return hexidecimal representation of this buffer
     */
    public abstract String getHexDump();

    /**
     * Return hexdump of this buffer with limited length.
     * 
     * @param lengthLimit
     *            The maximum number of bytes to dump from the current buffer
     *            position.
     * @return hexidecimal representation of this buffer
     */
    public abstract String getHexDump(int lengthLimit);

    // //////////////////////////////
    // String getters and putters //
    // //////////////////////////////

    /**
     * Reads a <code>NUL</code>-terminated string from this buffer using the
     * specified <code>decoder</code> and returns it. This method reads until
     * the limit of this buffer if no <tt>NUL</tt> is found.
     */
    //public abstract String getString(CharsetDecoder decoder) throws CharacterCodingException;

    /**
     * Reads a <code>NUL</code>-terminated string from this buffer using the
     * specified <code>decoder</code> and returns it.
     * 
     * @param fieldSize
     *            the maximum number of bytes to read
     */
    //public abstract String getString(int fieldSize, CharsetDecoder decoder) throws CharacterCodingException;

    /**
     * Writes the content of <code>in</code> into this buffer using the
     * specified <code>encoder</code>. This method doesn't terminate string with
     * <tt>NUL</tt>. You have to do it by yourself.
     * 
     * @throws BufferOverflowException
     *             if the specified string doesn't fit
     */
    //public abstract IoBuffer putString(CharSequence val, CharsetEncoder encoder) throws CharacterCodingException;

    /**
     * Writes the content of <code>in</code> into this buffer as a
     * <code>NUL</code>-terminated string using the specified
     * <code>encoder</code>.
     * <p>
     * If the charset name of the encoder is UTF-16, you cannot specify odd
     * <code>fieldSize</code>, and this method will append two <code>NUL</code>s
     * as a terminator.
     * <p>
     * Please note that this method doesn't terminate with <code>NUL</code> if
     * the input string is longer than <tt>fieldSize</tt>.
     * 
     * @param fieldSize
     *            the maximum number of bytes to write
     */
    //public abstract IoBuffer putString(CharSequence val, int fieldSize, CharsetEncoder encoder) throws CharacterCodingException;

    // ///////////////////
    // IndexOf methods //
    // ///////////////////

    /**
     * Returns the first occurence position of the specified byte from the
     * current position to the current limit.
     * 
     * @return <tt>-1</tt> if the specified byte is not found
     */
    public abstract int indexOf(byte b);

    // ////////////////////////
    // Skip or fill methods //
    // ////////////////////////

    /**
     * Forwards the position of this buffer as the specified <code>size</code>
     * bytes.
     */
    public abstract IoBuffer skip(int size);

    /**
     * Fills this buffer with the specified value. This method moves buffer
     * position forward.
     */
    public abstract IoBuffer fill(byte value, int size);

    /**
     * Fills this buffer with the specified value. This method does not change
     * buffer position.
     */
    public abstract IoBuffer fillAndReset(byte value, int size);

    /**
     * Fills this buffer with <code>NUL (0x00)</code>. This method moves buffer
     * position forward.
     */
    public abstract IoBuffer fill(int size);

    /**
     * Fills this buffer with <code>NUL (0x00)</code>. This method does not
     * change buffer position.
     */
    public abstract IoBuffer fillAndReset(int size);


}
