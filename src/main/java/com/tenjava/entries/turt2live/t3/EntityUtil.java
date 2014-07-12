package com.tenjava.entries.turt2live.t3;

import net.minecraft.server.v1_7_R3.EntityInsentient;
import net.minecraft.server.v1_7_R3.EntityLiving;
import net.minecraft.server.v1_7_R3.PathfinderGoalSelector;
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
    public static void removeGoals(LivingEntity entity) throws NoSuchFieldException, IllegalAccessException {
        if (entity == null) throw new IllegalArgumentException();

        EntityLiving nmsEntity = ((CraftLivingEntity) entity).getHandle();

        //Ewww reflection
        if (nmsEntity instanceof EntityInsentient) {
            EntityInsentient insentient = (EntityInsentient) nmsEntity;

            Field goals = EntityInsentient.class.getDeclaredField("goalSelector");
            goals.setAccessible(true);
            clearPathfinderGoals((PathfinderGoalSelector) goals.get(insentient));

            Field target = EntityInsentient.class.getDeclaredField("targetSelector");
            target.setAccessible(true);
            clearPathfinderGoals((PathfinderGoalSelector) target.get(insentient));
        }
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
