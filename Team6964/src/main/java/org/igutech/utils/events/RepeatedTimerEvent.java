package org.igutech.utils.events;

public class RepeatedTimerEvent extends Event {

    private long initalTime;

    public RepeatedTimerEvent(long time, Callback m) {
        super(time, m);
        initalTime = time;
    }

    public long getInitalTime() {
        return initalTime;
    }


}
