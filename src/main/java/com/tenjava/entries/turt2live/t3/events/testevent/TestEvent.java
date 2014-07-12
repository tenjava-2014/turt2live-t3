package com.tenjava.entries.turt2live.t3.events.testevent;

import com.tenjava.entries.turt2live.t3.events.EventManager;
import com.tenjava.entries.turt2live.t3.events.RandomEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

/**
 * Represents an event used for testing
 *
 * @author turt2live
 */
public class TestEvent extends RandomEvent {

    private int ticks = 0;

    @Override
    public boolean canRun() {
        return true; // Can run always
    }

    @Override
    public float getChance() {
        return 1;
    }

    @Override
    public long getCooldownTime() {
        return 0;
    }

    @Override
    protected void onStart() {
        ticks = 0;
        Bukkit.broadcastMessage(ChatColor.GREEN + "STARTED");
    }

    @Override
    public void tick(EventManager eventManager) {
        ticks++;

        Bukkit.broadcastMessage(ChatColor.AQUA + "TICK #" + ticks);

        if (ticks > 30) eventManager.stopEvent(this);
    }

    @Override
    protected void onStop() {
        Bukkit.broadcastMessage(ChatColor.GREEN + "STOPPED");
        ticks = 0;
    }
}
