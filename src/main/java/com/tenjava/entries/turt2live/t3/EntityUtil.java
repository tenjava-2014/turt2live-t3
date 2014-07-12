package com.tenjava.entries.turt2live.t3;

import net.minecraft.server.v1_7_R3.EntityInsentient;
import net.minecraft.server.v1_7_R3.EntityLiving;
import net.minecraft.server.v1_7_R3.PathfinderGoalSelector;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Entity utility methods
 *
 * @author turt2live
 */
// TODO: If it mattered, this should be versioned. But this is ten.java and only one server version will be used.
public class EntityUtil {

    /**
     * Removes goals from an entity. This will clear all goals, such as movement,
     * from the entity so that you have a clean slate to work with.
     *
     * @param entity the entity to clear goals from, cannot be null
     */
    public static void removeGoals(LivingEntity entity) {
        if (entity == null) throw new IllegalArgumentException();

        EntityLiving nmsEntity = ((CraftLivingEntity) entity).getHandle();

        //Ewww reflection
        try {
            if (nmsEntity instanceof EntityInsentient) {
                EntityInsentient insentient = (EntityInsentient) nmsEntity;

                Field goals = EntityInsentient.class.getDeclaredField("goalSelector");
                goals.setAccessible(true);
                clearPathfinderGoals((PathfinderGoalSelector) goals.get(insentient));

                Field target = EntityInsentient.class.getDeclaredField("targetSelector");
                target.setAccessible(true);
                clearPathfinderGoals((PathfinderGoalSelector) target.get(insentient));
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * Forces an entity to look at a specified location
     *
     * @param entity the entity to turn, cannot be null
     * @param lookAt the location to look at, cannot be null
     */
    public static void look(LivingEntity entity, Location lookAt) {
        if (entity == null || lookAt == null) throw new IllegalArgumentException();

        Location start = entity.getLocation().clone();

        double dx = lookAt.getX() - start.getX();
        double dy = lookAt.getY() - start.getY();
        double dz = lookAt.getZ() - start.getZ();

        // Set yaw
        if (dx != 0) {
            if (dx < 0) start.setYaw((float) (1.5 * Math.PI));
            else start.setYaw((float) (0.5 * Math.PI));
        } else if (dz < 0) start.setYaw((float) Math.PI);

        double dxz = Math.sqrt(Math.pow(dx, 2) + Math.pow(dz, 2));
        start.setPitch((float) -Math.atan(dy / dxz));

        start.setYaw(-start.getYaw() * 180f / (float) Math.PI);
        start.setPitch(start.getPitch() * 180f / (float) Math.PI);

        entity.teleport(start);
    }

    private static void clearPathfinderGoals(PathfinderGoalSelector selector) throws NoSuchFieldException, IllegalAccessException {
        Class<?> clazz = PathfinderGoalSelector.class;

        Field bList = clazz.getDeclaredField("b");
        bList.setAccessible(true);
        bList.set(selector, new ArrayList());

        Field cList = clazz.getDeclaredField("c");
        cList.setAccessible(true);
        cList.set(selector, new ArrayList());
    }
}
