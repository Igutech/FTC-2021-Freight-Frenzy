package org.igutech.utils.events;

public class SingleTimerEvent extends Event {

    private String name;

    public SingleTimerEvent(int time, Callback m) {
        super(time + System.currentTimeMillis(), m);
    }

    public SingleTimerEvent(long time, String name, Callback m) {
        super(time + System.currentTimeMillis(), m);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SingleTimerEvent)) {
            return false;
        }
        return name.equals(((SingleTimerEvent) obj).name);
    }
}
