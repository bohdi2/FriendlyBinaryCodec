package org.bodhi.fbc.impl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chris on 5/26/14.
 */
public class Trace {
    private final Map<String, Integer> m_positions; // map names -> positions
    private final Map<Integer, String> m_fields;     // map positions -> names
    private final Map<Integer, String> m_comments;     // map positions -> names

    public Trace() {
        m_positions = new HashMap<String, Integer>();
        m_comments = new HashMap<Integer, String>();
        m_fields = new HashMap<Integer, String>();
    }

    public void addTrace(int position, String field, String comment) {
        addField(position, field);
        m_fields.put(position, field);
        m_comments.put(position, comment);
    }

    public void addField(int position, String field) {
        m_positions.put(field, position);
    }


    // Returns byte offset to the named position

    public int getPosition(String field) {
        assert m_positions.containsKey(field) : "No position defined for " + field;
        return m_positions.get(field);
    }



    public boolean hasComment(int position) {
        return m_comments.containsKey(position);
    }

    public String getComment(int position) {
        return m_comments.get(position);
    }

    public String getComment(int position, String defaultValue) {
        if (hasComment(position))
            return getComment(position);
        else
            return defaultValue;
    }



    public boolean hasField(int position) {
        return m_fields.containsKey(position);
    }

    public String getField(int position) {
        return m_fields.get(position);
    }

    public String getField(int position, String defaultValue) {
        if (hasField(position))
            return getField(position);
        else
            return defaultValue;
    }
}
