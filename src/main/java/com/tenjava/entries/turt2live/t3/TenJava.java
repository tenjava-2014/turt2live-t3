package com.tenjava.entries.turt2live.t3;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

public class TenJava extends JavaPlugin implements Listener {

    private static TenJava instance;

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.getPlayer().setGameMode(GameMode.CREATIVE);
        event.getPlayer().teleport(new Location(event.getPlayer().getWorld(), 8, 130, 8));
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
