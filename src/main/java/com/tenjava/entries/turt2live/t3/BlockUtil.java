package com.tenjava.entries.turt2live.t3;

import org.bukkit.Location;
import org.bukkit.Material;

/**
 * Block utility methods
 *
 * @author turt2live
 */
public class BlockUtil {

    /**
     * Represents a simple block replacement
     */
    public final static class BlockReplacement {

        protected char c;
        protected byte data = 0;
        protected Material material;

        /**
         * Creates a new block replacement
         *
         * @param c        the character to replace
         * @param material the material to replace with, cannot be null
         */
        public BlockReplacement(char c, Material material) {
            this(c, material, (byte) 0);
        }

        /**
         * Cerates a new block replacement
         *
         * @param c        the character to replace
         * @param material the material to replace with, cannot be null
         * @param data     the block data to use
         */
        public BlockReplacement(char c, Material material, byte data) {
            if (material == null) throw new IllegalArgumentException();

            this.c = c;
            this.material = material;
            this.data = data;
        }
    }

    /**
     * Generates a structure. The passed array is a relative mapping to blocks where index [0][0][0] is
     * the block at the location specified, [0][0][1] being 1 x-coordinate over, [0][1][0] being 1
     * z-coordinate over, and [1][0][0] being 1 y-coordinate over.
     *
     * @param start        the starting point, cannot be null
     * @param map          the map to use, cannot be null. Unknown characters are assumed to be air
     * @param replacements the replacements to use, can be null
     */
    public static void generateStructure(Location start, String[][] map, BlockReplacement... replacements) {
        if (start == null || map == null) throw new IllegalArgumentException();

        for (int y = 0; y < map.length; y++) {
            String[] zd = map[y];
            for (int z = 0; z < zd.length; z++) {
                int x=0;
                for (char c : zd[z].toCharArray()) {
                    Location l = start.clone().add(x, y, z);

                    byte data = 0;
                    Material material = Material.AIR;

                    if (replacements != null) {
                        for (BlockReplacement br : replacements) {
                            if (br.c == c) {
                                data = br.data;
                                material = br.material;
                                break;
                            }
                        }
                    }

                    l.getBlock().setType(material);
                    l.getBlock().setData(data);

                    x++;
                }
            }
        }
    }

}
