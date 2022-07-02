package com.marcluque.hydra.shared.distribution.roundrobinlist;

import com.marcluque.hydra.shared.handler.Session;

/**
 * Created by marcluque on 15.04.2018.
 */
public interface RoundRobinList {

    int size();

    Session get();

    boolean add(Session e);

    void remove(Session e);
}