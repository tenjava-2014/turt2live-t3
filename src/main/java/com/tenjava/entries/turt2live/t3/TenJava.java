package com.tenjava.entries.turt2live.t3;

import org.bukkit.event.Listener;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main plugin class. Does nothing but delegate.
 *
 * @author turt2live
 */
public class TenJava extends JavaPlugin implements Listener {

    private static TenJava instance;

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new GameListener(this), this);
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);

        instance = null;
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new WorldGenerator();
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
