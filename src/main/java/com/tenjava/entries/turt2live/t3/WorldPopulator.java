package com.tenjava.entries.turt2live.t3;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

/**
 * The world populator used to enhance the spawn point of the world
 *
 * @author turt2live
 */
public class WorldPopulator extends BlockPopulator {

    private int spawnY;

    WorldPopulator(int spawnY) {
        this.spawnY = spawnY;
    }

    @Override
    public void populate(World world, Random random, Chunk chunk) {
        if (chunk.getX() == 0 && chunk.getZ() == 0) {
            chunk.getBlock(7, spawnY, 7).setType(Material.TORCH);
            chunk.getBlock(7, spawnY, 11).setType(Material.TORCH);
            chunk.getBlock(11, spawnY, 7).setType(Material.TORCH);
            chunk.getBlock(11, spawnY, 11).setType(Material.TORCH);
        }
    }
}
