package de.datasecs.hydra.shared.distribution.roundrobinlist;

import de.datasecs.hydra.shared.handler.Session;

/**
 * Created by DataSecs on 15.04.2018.
 */
public class LinkedRoundRobinList implements RoundRobinList {

    private Element start;

    private int size;

    private int index;

    private int robinIndex;

    public LinkedRoundRobinList() {
        start = new Element();
        size = robinIndex = index = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Session get() {
        Element e = start;
        while (e.next != null) {
            if (e.next.index == robinIndex) {
                robinIndex = (robinIndex + 1) % size;
                return e.next.node;
            }

            e = e.next;
        }

        return null;
    }

    @Override
    public boolean add(Session o) {
        if (o != null) {
            Element e = new Element(o);
            Element pointer = getLastElement();

            pointer.next = e;
            e.prev = pointer;
            e.index = index;

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
            if (e.next != null) {
                reduceIndex(e);

                e.prev.next = e.next;
                e.next.prev = e.prev;

                size--;
                index--;
            } else {
                e.prev.next = null;

                size--;
                index--;
            }

            robinIndex = (size != 0) ? (robinIndex + 1) % size : 0;
        }
    }

    private Element getLastElement() {
        Element e = start;
        while (e.next != null) {
            e = e.next;
        }

        return e;
    }

    private void reduceIndex(Element e) {
        while (e.next != null) {
            e.next.index--;
            e = e.next;
        }
    }

    private Element findObject(Session node) {
        Element e = start;
        while (e.next != null) {
            if (e.next.node.equals(node)) {
                return e.next;
            }

            e = e.next;
        }

        return null;
    }
}