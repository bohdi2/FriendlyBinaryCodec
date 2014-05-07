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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;



/**
 * A simplistic {@link IoBufferAllocator} which simply allocates a new
 * buffer every time.
 *
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public class SimpleBufferAllocator implements IoBufferAllocator {

    public IoBuffer allocate(int capacity) {
        return wrap(allocateNioBuffer(capacity));
    }
    
    public ByteBuffer allocateNioBuffer(int capacity) {
        return ByteBuffer.allocate(capacity);
    }

    public IoBuffer wrap(ByteBuffer nioBuffer) {
        return new SimpleBuffer(nioBuffer);
    }

    public void dispose() {
        // Do nothing
    }

    private class SimpleBuffer extends AbstractIoBuffer {
        private ByteBuffer buf;

        protected SimpleBuffer(ByteBuffer buf) {
            super(/*SimpleBufferAllocator.this,*/ buf.capacity());
            this.buf = buf;
            buf.order(ByteOrder.BIG_ENDIAN);
        }

        @Override
        public ByteBuffer buf() {
            return buf;
        }
        
        @Override
        protected void buf(ByteBuffer buf) {
            this.buf = buf;
        }

        @Override
        public byte[] array() {
            return buf.array();
        }
    }
}
