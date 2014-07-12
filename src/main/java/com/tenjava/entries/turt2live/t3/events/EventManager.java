package com.tenjava.entries.turt2live.t3.events;

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

        // TODO: Load events
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
