package com.tenjava.entries.turt2live.t3.events.fightclub;

import com.tenjava.entries.turt2live.t3.BlockUtil;
import com.tenjava.entries.turt2live.t3.EntityUtil;
import com.tenjava.entries.turt2live.t3.events.RandomEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A random fight club event which is performed near a player.
 *
 * @author turt2live
 */
public class FightClubEvent extends RandomEvent {

    private Random random = new Random();
    private List<Entity> entities = new ArrayList<Entity>();
    private Location center;

    @Override
    public boolean canRun() {
        return getSuitableCenter() != null;
    }

    @Override
    public float getChance() {
        return 0.7f; // TODO: Config
    }

    @Override
    public long getCooldownTime() {
        return 0; // TODO: Config
    }

    @Override
    protected void onStart() {
        // Generate the board
        center = getSuitableCenter();
        Location boardStart = center.clone().add(-3, 0, -4);

        String[][] blockMap = new String[][] {
                new String[] {
                        "WWWWWWW",
                        "WWWWWWW",
                        "WWWWWWW",
                        "WWWOWWW",
                        "WWWWWWW",
                        "WWWOWWW",
                        "WWWWWWW",
                        "WWWWWWW",
                        "WWWWWWW"
                },
                new String[] {
                        "       ",
                        " FFFFF ",
                        " F   F ",
                        " F   F ",
                        " F   F ",
                        " F   F ",
                        " F   F ",
                        " FFFFF ",
                        "       "
                }
        };

        BlockUtil.generateStructure(boardStart, blockMap,
                new BlockUtil.BlockReplacement('W', Material.WOOD),
                new BlockUtil.BlockReplacement('O', Material.WOOD, (byte) 5),
                new BlockUtil.BlockReplacement('F', Material.FENCE));

        // Spawn the mobs
        LivingEntity one = (LivingEntity) center.getWorld().spawnEntity(center.clone().add(0.5, 1, -1.5), EntityType.VILLAGER);
        LivingEntity two = (LivingEntity) center.getWorld().spawnEntity(center.clone().add(-0.5, 1, 1.5), EntityType.VILLAGER);

        EntityUtil.removeGoals(one);
        EntityUtil.removeGoals(two);

        EntityUtil.look(one, center);
        EntityUtil.look(two, center);

        entities.add(one);
        entities.add(two);
    }

    @Override
    protected void onStop() {
        entities.clear();
    }

    private Location getSuitableCenter() {
        Location l = Bukkit.getOnlinePlayers()[0].getLocation().add(0, 10, 0);

        l.setX(l.getBlockX() + 0.5);
        l.setY(l.getBlockY() + 0.5);
        l.setZ(l.getBlockZ() + 0.5);

        return l;
    }
}
