package org.bodhi.fbc;

import org.junit.Test;

import static org.junit.Assert.*;

public class TraceTest {

    @Test
    public void testLabel() {
        Trace trace = new Trace();

        trace.label(4, "Foo");
        assertEquals(4, trace.getPosition("Foo"));

        assertFalse(trace.hasField(4));
        assertEquals("x", trace.getField(4, "x"));

        assertFalse(trace.hasComment(4));
        assertEquals("x", trace.getComment(4, "x"));
    }

    @Test
    public void testTrace() {
        Trace trace = new Trace();

        trace.trace(4, "Foo", "Comment");
        assertEquals(4, trace.getPosition("Foo"));

        assertTrue(trace.hasField(4));
        assertEquals("Foo", trace.getField(4, "x"));

        assertTrue(trace.hasComment(4));
        assertEquals("Comment", trace.getComment(4, "x"));
    }

    @Test
    public void test_append_to_non_existent_comment() {
        Trace trace = new Trace();

        trace.appendComment(3, "Hello");
        assertEquals("Hello", trace.getComment(3));
    }

    @Test
    public void test_append_to_existing_comment() {
        Trace trace = new Trace();

        trace.trace(4, "Foo", "Comment");
        trace.appendComment(4, ", more");

        assertTrue(trace.hasComment(4));
        assertEquals("Comment, more", trace.getComment(4, "x"));
    }
}
