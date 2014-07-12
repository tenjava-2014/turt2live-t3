package com.tenjava.entries.turt2live.t3.events;

import com.tenjava.entries.turt2live.t3.TenJava;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents the event manager to handle various random events
 *
 * @author turt2live
 */
public class EventManager {

    private List<RandomEvent> events = new ArrayList<RandomEvent>();
    private List<RandomEvent> currentEvents = new ArrayList<RandomEvent>();
    private Random random = new Random(); // This should be good enough
    private int maxEvents; // negative or zero = infinite

    /**
     * Creates a new event manager
     *
     * @param maxEvents the maximum number of events which can occur
     *
     * @see #setMaxEvents(int)
     */
    public EventManager(int maxEvents) {
        setMaxEvents(maxEvents);
    }

    /**
     * Starts the event manager, provided it hasn't started already
     */
    public void start() {
        TenJava.getInstance().getServer().getScheduler().scheduleSyncRepeatingTask(TenJava.getInstance(), new Runnable() {
            @Override
            public void run() {
                if (!canStartNewEvent()) return; // Don't run anything if we can't

                float chosen = random.nextFloat();

                for (RandomEvent event : events) {
                    if (!currentEvents.contains(event) && event.getChance() <= chosen && event.canRun()) {
                        currentEvents.add(event);
                        event.start();
                    }

                    if (!canStartNewEvent()) break; // We're done!
                }
            }
        }, 0L, 20L); // 1 second timer
    }

    /**
     * Registers a new event to be potentially executed by the event manager. This
     * makes no attempt to verify that the event is not already registered.
     *
     * @param event the new event to add, cannot be null
     */
    public void registerEvent(RandomEvent event) {
        if (event == null) throw new IllegalArgumentException();

        events.add(event);
    }

    /**
     * Stops an event. This will do nothing if the event is not running.
     *
     * @param event the event to stop, cannot be null
     */
    public void stopEvent(RandomEvent event) {
        if (event == null) throw new IllegalArgumentException();

        if (currentEvents.contains(event)) {
            event.stop();
            currentEvents.remove(event);
        }
    }

    /**
     * Stops the event manager by cleaning up all the potentially running events
     */
    public void stop() {
        for (RandomEvent event : currentEvents) {
            event.stop();
        }
    }

    /**
     * Sets the new maximum amount of events that can occur. Negative numbers
     * or zero represent "infinite" while all other positive numbers are a hard
     * limit.
     *
     * @param newMaximum the new maximum
     */
    public void setMaxEvents(int newMaximum) {
        this.maxEvents = newMaximum;
    }

    /**
     * Gets the total maximum number of events that can occur at any one time.
     *
     * @return the total maximum number of events
     *
     * @see #setMaxEvents(int)
     */
    public int getMaxEvents() {
        return maxEvents;
    }

    /**
     * Determines if this manager can start a new event
     *
     * @return true if a new event can be started, false otherwise
     */
    public boolean canStartNewEvent() {
        return maxEvents <= 0 || currentEvents.size() < maxEvents;
    }

}
