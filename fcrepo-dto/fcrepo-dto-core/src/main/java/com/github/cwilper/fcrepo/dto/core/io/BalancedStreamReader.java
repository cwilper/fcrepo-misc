package com.github.cwilper.fcrepo.dto.core.io;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;
import java.util.NoSuchElementException;

/**
 * A tag-balanced <code>XMLStreamReader</code> suitable for working with a
 * well-formed chunk of an XML document as if it's a whole document.
 * 
 * Wraps an XMLStreamReader positioned on an element, delegating all
 * calls until the corresponding END_ELEMENT event is seen, after which
 * it emulates an END_DOCUMENT state, leaving the underlying reader on the
 * END_ELEMENT.
 */
public class BalancedStreamReader extends StreamReaderDelegate {

    private final XMLStreamReader reader;

    private int level;

    public BalancedStreamReader(XMLStreamReader reader)
            throws XMLStreamException {
        super(reader);
        this.reader = reader;
    }

    @Override
    public int next() throws XMLStreamException {
        if (level == -2) {
            throw new NoSuchElementException();
        }
        if (level == -1) {
            level--;
            return END_DOCUMENT;
        }
        int code = reader.next();
        if (code == START_ELEMENT) {
            level++;
        } else if (code == END_ELEMENT) {
            level--;
        }
        return code;
    }

    @Override
    public int getEventType() {
        if (level == -2) return END_DOCUMENT;
        if (level == -1) return END_ELEMENT;
        return reader.getEventType();
    }

    @Override
    public boolean hasNext() throws XMLStreamException {
        if (level == -2) return false;
        if (level == -1) return true;
        return reader.hasNext();
    }

}
