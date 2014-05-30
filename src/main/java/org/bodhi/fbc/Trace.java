package org.bodhi.fbc;

import java.util.HashMap;
import java.util.Map;

/**
 * Trace keeps track of labels and comments attached to positions in a byte array.
 * labels: are alias for a position. There can be multiple labels for the same position.
 * fields: are labels, but they are one to one with a position. You can think of them as the "main" label.
 * comments: are one to one with position. The contain free form text.
 */
public class Trace {
    private final Map<String, Integer> m_labels; // map names -> positions
    private final Map<Integer, String> m_fields;     // map positions -> names
    private final Map<Integer, String> m_comments;     // map positions -> names

    public Trace() {
        this(new HashMap<String, Integer>(),
             new HashMap<Integer, String>(),
             new HashMap<Integer, String>());
    }

    private Trace(Map<String, Integer> labels,
                  Map<Integer, String> fields,
                  Map<Integer, String> comments)
    {
        m_labels = labels;
        m_fields = fields;
        m_comments = comments;
    }

    public Trace copy() {
        return new Trace(new HashMap<String, Integer>(m_labels),
                         new HashMap<Integer, String>(m_fields),
                         new HashMap<Integer, String>(m_comments));
    }

    public void trace(int position, String field, String comment) {
        label(position, field);
        m_fields.put(position, field);
        m_comments.put(position, comment);
    }

    public void label(int position, String field) {
        m_labels.put(field, position);
    }


    // Returns byte offset to the named position

    public int getPosition(String label) {
        assert m_labels.containsKey(label) : "No position defined for " + label;
        return m_labels.get(label);
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

    public void appendComment(int position, String s) {
        String newComment = getComment(position, "") + s;
        m_comments.put(position, newComment);
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
