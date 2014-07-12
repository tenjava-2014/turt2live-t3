package com.tenjava.entries.turt2live.t3;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class GameListener implements Listener {

    // These numbers are in C
    private int shiverTemp = -5;
    private int hypothermiaTemp = -20;
    private int sweatingTemp = 15;
    private int burningTemp = 30;

    public GameListener(final TenJava plugin) {
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                // check all players for temperature
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    adjustTemperature(player);
                }
            }
        }, 0L, 20L * 10);
    }

    public void adjustTemperature(Player player) {
        if (player.isDead()) return;

        player.sendMessage("-- adjust temperature --");
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPlayedBefore()) {
            player.sendMessage("TP");
            player.teleport(player.getWorld().getSpawnLocation());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.sendMessage("Welcome to tenjavria, where all you have to do is survive.");
        player.sendMessage("As night falls, you will need to stay near warm objects to avoid freezing.");
        player.sendMessage("As dawn breaks, you will need to stay hidden from the sun to avoid burning.");
        player.sendMessage("Don't fall though, or you'll drop some items.");
        player.sendMessage("But stay moving, or foes will find you.");
        player.sendMessage("Keep safe in tenjavria!");

        player.setGameMode(GameMode.CREATIVE);
        player.setFlying(true);
    }

}
