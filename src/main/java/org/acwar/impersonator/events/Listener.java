package org.acwar.impersonator.events;

interface Listener<E extends BaseEvent> {
    void handle(E event);
}