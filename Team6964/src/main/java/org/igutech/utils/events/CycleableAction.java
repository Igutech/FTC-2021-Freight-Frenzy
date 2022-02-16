package org.igutech.utils.events;

public class CycleableAction {
    private Callback[] callbacks;
    private int state = 0;

    public CycleableAction(Callback... callbacks) {
        this.callbacks = callbacks;
    }

    public void call() {
        try {
            callbacks[state].call();
        } catch (Exception e) {
            e.printStackTrace();
        }
        state = (state + 1) % callbacks.length;
    }
}
