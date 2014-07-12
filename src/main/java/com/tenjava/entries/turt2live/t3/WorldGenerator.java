package com.tenjava.entries.turt2live.t3;

import com.google.common.collect.ImmutableList;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import java.util.List;
import java.util.Random;

public class WorldGenerator extends ChunkGenerator {

    private int spawnY = 128;
    private int[] randomIds = new int[] {
            Material.DIRT.getId(),
            Material.STONE.getId(),
            Material.WOOD.getId(),
            Material.SPONGE.getId(),
            Material.GLASS.getId(),
            Material.LOG.getId(),
            Material.WOOL.getId(),
            Material.ICE.getId()
    };
    private int[] semiRareIds = new int[] {
            Material.IRON_BLOCK.getId(),
            Material.GOLD_BLOCK.getId(),
            Material.COAL_ORE.getId(),
            Material.IRON_ORE.getId(),
            Material.LAPIS_ORE.getId(),
            Material.REDSTONE_ORE.getId(),
            Material.GLOWSTONE.getId(),
            Material.NETHERRACK.getId(),
            Material.OBSIDIAN.getId()
    };
    private int[] rareIds = new int[] {
            Material.DIAMOND_BLOCK.getId(),
            Material.COAL_BLOCK.getId(),
            Material.LAPIS_BLOCK.getId(),
            Material.REDSTONE_BLOCK.getId(),
            Material.LAPIS_ORE.getId(),
            Material.BOOKSHELF.getId()
    };

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        return ImmutableList.<BlockPopulator>of(new WorldPopulator(spawnY));
    }

    @Override
    public byte[][] generateBlockSections(World world, Random random, int x, int z, BiomeGrid biomes) {
        // generates a chunk
        byte[][] blocks = new byte[world.getMaxHeight() / 16][];
        boolean spawnChunk = x == 0 && z == 0;

        if (spawnChunk) {
            // We'll generate an island for them
            setRange(8, spawnY - 4, 8, 11, spawnY - 3, 11, Material.STONE.getId(), blocks);
            setBlock(9, spawnY - 4, 9, Material.BEDROCK.getId(), blocks); // So they don't fall..

            /*
             7  8  9  10 11
            [x][ ][ ][ ][x] 7
            [ ]         [ ] 8
            [ ]         [ ] 9
            [ ]         [ ] 10
            [x][ ][ ][ ][x] 11
             */

            // Pillars
            setRange(7, spawnY - 4, 7, 8, spawnY, 8, Material.LOG.getId(), blocks);
            setRange(7, spawnY - 4, 11, 8, spawnY, 12, Material.LOG.getId(), blocks);
            setRange(11, spawnY - 4, 7, 12, spawnY, 8, Material.LOG.getId(), blocks);
            setRange(11, spawnY - 4, 11, 12, spawnY, 12, Material.LOG.getId(), blocks);

            // Set walls
            setRange(8, spawnY - 4, 7, 11, spawnY - 1, 8, Material.DIRT.getId(), blocks);
            setRange(7, spawnY - 4, 8, 8, spawnY - 1, 11, Material.DIRT.getId(), blocks);
            setRange(11, spawnY - 4, 8, 12, spawnY - 1, 11, Material.DIRT.getId(), blocks);
            setRange(8, spawnY - 4, 11, 11, spawnY - 1, 12, Material.DIRT.getId(), blocks);
        } else {
            // 0.2% chance that a sphere of diamond will spawn
            // 0.6% chance that a sphere of iron will spawn
            // 1.0% chance that a sphere of stone will spawn
            // 10% chance that a sphere of dirt will spawn

            // Random blocks spawn first though
            int minBlocks = world.getMaxHeight() / 4;
            int numBlocks = random.nextInt(minBlocks) + minBlocks;

            for (int i = 0; i < numBlocks; i++) {
                int rx = random.nextInt(16);
                int ry = random.nextInt(world.getMaxHeight());
                int rz = random.nextInt(16);

                while (getBlock(rx, ry, rz, blocks) != 0) {
                    rx = random.nextInt(16);
                    ry = random.nextInt(world.getMaxHeight());
                    rz = random.nextInt(16);
                }

                int choice = randomIds[random.nextInt(randomIds.length)];
                if (random.nextDouble() < 0.4) choice = semiRareIds[random.nextInt(semiRareIds.length)];
                if (random.nextDouble() < 0.15) choice = rareIds[random.nextInt(rareIds.length)];

                setBlock(rx, ry, rz, choice, blocks);
            }

            // Now see if spheres need to be generated
            int sphereId = 0;
            double r = random.nextDouble();

            if (r < 0.1) sphereId = Material.DIRT.getId();
            if (r < 0.01) sphereId = Material.PACKED_ICE.getId();
            if (r < 0.006) sphereId = Material.IRON_BLOCK.getId();
            if (r < 0.002) sphereId = Material.DIAMOND_BLOCK.getId();

            if (sphereId != 0) {
                int minSize = 3; // Radius
                int maxSize = 7;
                int size = random.nextInt(maxSize - minSize) + minSize;

                int ry = random.nextInt(world.getMaxHeight() / 2) + world.getMaxHeight() / 4;

                sphere(8, ry, 8, sphereId, size, blocks);
            }
        }

        return blocks;
    }

    private boolean inRadius(int sx, int sy, int sz, int dx, int dy, int dz, int r) {
        // s = center
        // d = desired
        int rsq = r * r;

        int a = Math.abs(sx - dx);
        int b = Math.abs(sy - dy);
        int c = Math.abs(sz - dz);

        int asq = a * a;
        int bsq = b * b;
        int csq = c * c;

        return asq + bsq + csq < rsq;
    }

    public void sphere(int x, int y, int z, int material, int radius, byte[][] chunk) {
        for (int cx = x - radius; cx < x + radius; cx++) {
            for (int cy = y - radius; cy < y + radius; cy++) {
                for (int cz = z - radius; cz < z + radius; cz++) {
                    if (inRadius(x, y, z, cx, cy, cz, radius)) {
                        int m = material;
                        if (cx == x && cy == y && cz == z) m = Material.DIAMOND_BLOCK.getId();

                        setBlock(cx, cy, cz, m, chunk);
                    }
                }
            }
        }
    }

    @Override
    public Location getFixedSpawnLocation(World world, Random random) {
        return new Location(world, 8.5, spawnY, 8.5);
    }

    public void setRange(int sx, int sy, int sz, int dx, int dy, int dz, int id, byte[][] chunk) {
        for (int x = sx; x < dx; x++) {
            for (int y = sy; y < dy; y++) {
                for (int z = sz; z < dz; z++) {
                    setBlock(x, y, z, id, chunk);
                }
            }
        }
    }

    // This is a slightly modified version of the method found in the javadocs for ChunkGenerator.
    // jkcclemens permitted use
    public void setBlock(int x, int y, int z, int id, byte[][] chunk) {
        if (chunk[y >> 4] == null) {
            chunk[y >> 4] = new byte[4096];
        }
        chunk[y >> 4][((y & 0xF) << 8) | (z << 4) | x] = (byte) id;
    }

    // This is a slightly modified version of the method found in the javadocs for ChunkGenerator.
    // jkcclemens permitted use
    public byte getBlock(int x, int y, int z, byte[][] chunk) {
        if (chunk[y >> 4] == null) {
            return (byte) 0;
        }
        return chunk[y >> 4][((y & 0xF) << 8) | (z << 4) | x];
    }
}
