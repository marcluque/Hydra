package com.marcluque.hydra.shared.distribution.roundrobinlist;

import com.marcluque.hydra.shared.handler.Session;

/**
 * Created by marcluque on 15.04.2018.
 */
public class Element {

    private int index;

    private Element next;

    private Element prev;

    private final Session node;

    public Element(Session node) {
        this.node = node;
    }

    public Element getPrev() {
        return prev;
    }

    public void setPrev(Element prev) {
        this.prev = prev;
    }

    public Element getNext() {
        return next;
    }

    public void setNext(Element next) {
        this.next = next;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Session getNode() {
        return node;
    }
}