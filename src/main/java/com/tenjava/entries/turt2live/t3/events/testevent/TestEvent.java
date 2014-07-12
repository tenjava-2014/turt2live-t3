package com.tenjava.entries.turt2live.t3.events.testevent;

import com.tenjava.entries.turt2live.t3.events.EventManager;
import com.tenjava.entries.turt2live.t3.events.RandomEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an event used for testing
 *
 * @author turt2live
 */
public class TestEvent extends RandomEvent {

    private int ticks = 0;

    private Creature mob1, mob2;
    private List<LivingEntity> crowd = new ArrayList<LivingEntity>();

    @Override
    public boolean canRun() {
        return Bukkit.getOnlinePlayers().length > 0;
    }

    @Override
    public float getChance() {
        return 1; // For testing
    }

    @Override
    public long getCooldownTime() {
        return 0; // For testing
    }

    @Override
    protected void onStart() {
        ticks = 0;
        Bukkit.broadcastMessage(ChatColor.GREEN + "STARTED");

        Location playerLocation = Bukkit.getOnlinePlayers()[0].getLocation();
        Location mobLocation1 = playerLocation.clone().add(10, 0, 0);
        Location mobLocation2 = playerLocation.clone().add(12, 0, 0);

        mob1 = (Creature) playerLocation.getWorld().spawnEntity(mobLocation1, EntityType.VILLAGER);
        mob2 = (Creature) playerLocation.getWorld().spawnEntity(mobLocation2, EntityType.VILLAGER);

        mob1.setTarget(mob2);
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

        mob1.remove();
        mob2.remove();

        for (Entity e : crowd) e.remove();
    }
}
