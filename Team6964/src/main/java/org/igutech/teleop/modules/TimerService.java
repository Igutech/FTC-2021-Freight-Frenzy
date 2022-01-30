package org.igutech.teleop.modules;

import org.igutech.teleop.Service;
import org.igutech.utils.events.Callback;
import org.igutech.utils.events.Event;
import org.igutech.utils.events.RepeatedTimerEvent;
import org.igutech.utils.events.SingleTimerEvent;

import java.util.*;

public class TimerService extends Service {
    private long startTime;

    private ArrayList<RepeatedTimerEvent> repeatedTimerEvents = new ArrayList<>();
    private Set<SingleTimerEvent> uniqueEventsToBeAdded = new HashSet<>();
    private ArrayList<SingleTimerEvent> events = new ArrayList<>();

    public TimerService() {
        super("TimerService");
    }

    @Override
    public void start() {
        startTime = System.currentTimeMillis();
    }

    @Override
    public void loop() {
        long current = System.currentTimeMillis() - startTime;

        events.addAll(uniqueEventsToBeAdded);
        uniqueEventsToBeAdded.clear();

        Iterator<SingleTimerEvent> iterator = events.iterator();

        while (iterator.hasNext()) {
            Event e = iterator.next();
            if (System.currentTimeMillis() >= e.getTime()) {
                try {
                    e.fire();
                } catch (Exception error) {
                    error.printStackTrace();
                }
                iterator.remove();
            }
        }

        for (RepeatedTimerEvent e : repeatedTimerEvents) {
            if (current >= e.getTime()) {
                e.fire();
                e.setTime(current + e.getInitalTime());
            }
        }
    }

    public TimerService registerUniqueTimerEvent(long time, String name, Callback m) {
        for (SingleTimerEvent event : events) {
            if (event.getName().equals(name)) {
                return this;
            }
        }
        uniqueEventsToBeAdded.add(new SingleTimerEvent(time, name, m));
        return this;
    }

    public TimerService registerSingleTimerEvent(int time, Callback m) {
        events.add(new SingleTimerEvent(time, m));
        return this;
    }

    public TimerService registerRepeatedTimerEvents(int time, Callback m) {
        repeatedTimerEvents.add(new RepeatedTimerEvent(time, m));
        return this;
    }


}
