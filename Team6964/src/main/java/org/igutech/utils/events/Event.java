package org.igutech.utils.events;

public class Event {
    private Callback callback;
    private long time;
    private boolean hasFired = false;

    public Event(long time, Callback callback) {
        this.callback = callback;
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setStatus(boolean hasFired) {
        this.hasFired = hasFired;
    }

    public Callback getCallback() {
        return callback;
    }

    public boolean hasFired() {
        return hasFired;
    }

    public void fire() {
        setStatus(true);
        getCallback().call();
    }

}
