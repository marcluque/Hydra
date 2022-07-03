package com.marcluque.hydra.shared.distribution.roundrobinlist;

import com.marcluque.hydra.shared.handler.Session;

/**
 * Created by marcluque on 15.04.2018.
 */
public class LinkedRoundRobinList implements RoundRobinList {

    private final Element start;

    private int size;

    private int index;

    private int robinIndex;

    public LinkedRoundRobinList() {
        start = new Element(null);
        size = 0;
        robinIndex = 0;
        index = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Session get() {
        Element e = start;
        while (e.getNext() != null) {
            if (e.getNext().getIndex() == robinIndex) {
                robinIndex = (robinIndex + 1) % size;
                return e.getNext().getNode();
            }

            e = e.getNext();
        }

        return null;
    }

    @Override
    public boolean add(Session o) {
        if (o != null) {
            Element e = new Element(o);
            Element pointer = getLastElement();

            pointer.setNext(e);
            e.setPrev(pointer);
            e.setIndex(index);

            index++;
            size++;

            return true;
        }

        return false;
    }

    @Override
    public void remove(Session o) {
        Element e = findObject(o);
        if (e != null) {
            if (e.getNext() != null) {
                reduceIndex(e);

                e.getPrev().setNext(e.getNext());
                e.getNext().setPrev(e.getPrev());
            } else {
                e.getPrev().setNext(null);
            }

            size--;
            index--;
            robinIndex = (size != 0) ? (robinIndex + 1) % size : 0;
        }
    }

    private Element getLastElement() {
        Element e = start;
        while (e.getNext() != null) {
            e = e.getNext();
        }

        return e;
    }

    private void reduceIndex(Element e) {
        while (e.getNext() != null) {
            e.setIndex(e.getNext().getIndex() - 1);
            e = e.getNext();
        }
    }

    private Element findObject(Session node) {
        Element e = start;
        while (e.getNext() != null) {
            if (e.getNext().getNode().equals(node)) {
                return e.getNext();
            }

            e = e.getNext();
        }

        return null;
    }
}