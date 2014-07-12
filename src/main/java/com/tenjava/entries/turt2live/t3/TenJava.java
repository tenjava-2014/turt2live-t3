package com.tenjava.entries.turt2live.t3;

import com.tenjava.entries.turt2live.t3.events.EventManager;
import com.tenjava.entries.turt2live.t3.events.fightclub.FightClubEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class TenJava extends JavaPlugin {

    private static TenJava instance;

    private EventManager eventManager;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        // Init everything
        eventManager = new EventManager(getConfig().getInt("max-events", 10));

        // Load some events
        eventManager.registerEvent(new FightClubEvent());

        // Start everything
        eventManager.start();
    }

    @Override
    public void onDisable() {
        // HALT
        eventManager.stop();

        instance = null;
    }

    /**
     * Gets the current event manager
     *
     * @return the current event manager
     */
    public EventManager getEventManager() {
        return eventManager;
    }

    /**
     * Gets the current instance of the plugin. May return null
     * if not yet initialized.
     *
     * @return the current instance of the plugin.
     */
    public static TenJava getInstance() {
        return instance;
    }

}
