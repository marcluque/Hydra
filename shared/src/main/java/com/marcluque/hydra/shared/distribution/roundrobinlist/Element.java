package com.marcluque.hydra.shared.distribution.roundrobinlist;

import com.marcluque.hydra.shared.handler.Session;

/**
 * Created by marcluque on 15.04.2018.
 */
public class Element {

    public int index;

    public Element next, prev;

    public Session node;

    public Element() {
        // For start element
    }

    public Element(Session node) {
        this.node = node;
    }
}