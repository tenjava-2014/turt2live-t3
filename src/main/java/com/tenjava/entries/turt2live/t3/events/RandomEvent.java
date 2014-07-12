package com.tenjava.entries.turt2live.t3.events;

/**
 * Represents a random event that can occur within the minecraft world
 *
 * @author turt2live
 */
public abstract class RandomEvent {

    private long lastEnd = 0;
    protected boolean running = false;

    /**
     * Starts the event
     */
    public final void start() {
        running = true;

        onStart();
    }

    /**
     * Stops the event
     */
    public final void stop() {
        lastEnd = System.currentTimeMillis();
        running = false;

        onStop();
    }

    /**
     * Determines whether or not this event is running
     *
     * @return true if running, false otherwise
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Gets the last end time of this event
     *
     * @return the last end time of this event
     */
    public final long getLastEnd() {
        return lastEnd;
    }

    /**
     * Called from the event manager every interval
     *
     * @param eventManager the event manager calling this method
     */
    public void tick(EventManager eventManager) {
    }

    /**
     * Determines if this event is capable of running at the current time. This will only
     * be called if the event manager has an intent of starting the event
     *
     * @return true if the event can currently run, false otherwise
     */
    public abstract boolean canRun();

    /**
     * Gets the random chance (between 0 and 1) that this event will occur.
     *
     * @return the chance between 0 and 1. All other numbers are clamped to that range.
     */
    public abstract float getChance();

    /**
     * Gets the amount of cooldown time required for this event. This may be
     * negative or zero to be not applicable.
     *
     * @return the amount of cooldown time required
     */
    public abstract long getCooldownTime();

    /**
     * Called when the event is started
     */
    protected abstract void onStart();

    /**
     * Called when the event is stopped
     */
    protected abstract void onStop();

}
