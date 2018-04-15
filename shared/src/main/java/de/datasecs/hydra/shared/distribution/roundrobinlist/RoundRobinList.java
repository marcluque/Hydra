package de.datasecs.hydra.shared.distribution.roundrobinlist;

import de.datasecs.hydra.shared.handler.Session;

/**
 * Created by DataSec on 15.04.2018.
 */
public interface RoundRobinList {

    int size();

    Session get();

    boolean add(Session e);

    void remove(Session e);
}