package com.tenjava.entries.turt2live.t3;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class GameListener implements Listener {

    // Not a real temperature scale
    private int shiverTemp = -5;
    private int hypothermiaTemp = -20;
    private int sweatingTemp = 15;
    private int burningTemp = 30;
    private String bodyTempKey = ChatColor.GREEN + "Body";
    private String worldTempKey = ChatColor.GRAY + "World";
    private static Map<Material, Double> armorWeights = new HashMap<Material, Double>();
    private static ConcurrentMap<UUID, Double> distances = new ConcurrentHashMap<UUID, Double>();

    static {
        armorWeights.put(Material.LEATHER_HELMET, 0.2);
        armorWeights.put(Material.LEATHER_CHESTPLATE, 0.2);
        armorWeights.put(Material.LEATHER_LEGGINGS, 0.2);
        armorWeights.put(Material.LEATHER_BOOTS, 0.2);

        armorWeights.put(Material.IRON_HELMET, 0.7);
        armorWeights.put(Material.IRON_CHESTPLATE, 0.7);
        armorWeights.put(Material.IRON_LEGGINGS, 0.7);
        armorWeights.put(Material.IRON_BOOTS, 0.7);

        armorWeights.put(Material.GOLD_HELMET, 0.7);
        armorWeights.put(Material.GOLD_CHESTPLATE, 0.7);
        armorWeights.put(Material.GOLD_LEGGINGS, 0.7);
        armorWeights.put(Material.GOLD_BOOTS, 0.7);

        armorWeights.put(Material.DIAMOND_HELMET, 1.2);
        armorWeights.put(Material.DIAMOND_CHESTPLATE, 1.2);
        armorWeights.put(Material.DIAMOND_LEGGINGS, 1.2);
        armorWeights.put(Material.DIAMOND_BOOTS, 1.2);

        armorWeights.put(Material.CHAINMAIL_HELMET, 2.1);
        armorWeights.put(Material.CHAINMAIL_CHESTPLATE, 2.1);
        armorWeights.put(Material.CHAINMAIL_LEGGINGS, 2.1);
        armorWeights.put(Material.CHAINMAIL_BOOTS, 2.1);
    }

    public GameListener(final TenJava plugin) {
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                // check all players for temperature
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    adjustTemperature(player);
                }
            }
        }, 0L, 20L * 1);
    }

    public void adjustTemperature(Player player) {
        int worldTemp;

        long time = player.getWorld().getTime();
        if (time > 2000 && time < 10000) worldTemp = 20;
        else if (time < 22000 && time > 14000) worldTemp = -20;
        else worldTemp = 0;

        player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore(worldTempKey).setScore(worldTemp);

        // Do actual temperature calculation
        int temperature;
        if (player.isDead()) temperature = -128;
        else {
            double warmth = 0;
            double cold = 0;

            // Find what is warm/cold around the player
            int r = 3;
            for (int x = -r; x < r; x++) {
                for (int y = -r; y < r; y++) {
                    for (int z = -r; z < r; z++) {
                        Block block = player.getLocation().clone().add(x, y, z).getBlock();
                        double distance = block.getLocation().distanceSquared(player.getLocation());

                        switch (block.getType()) {
                            // WARM
                            case STATIONARY_LAVA:
                            case LAVA:
                                warmth += distance * 2;
                                break;
                            case FIRE:
                                warmth += distance;
                                break;
                            case BURNING_FURNACE:
                            case TORCH:
                                warmth += distance / 2;
                                break;

                            // COLD
                            case PACKED_ICE:
                                cold += distance * 2;
                                break;
                            case ICE:
                                cold += distance;
                                break;
                            case STATIONARY_WATER:
                            case WATER:
                                cold += distance / 2;
                                break;
                        }
                    }
                }
            }

            // Check item weight
            // TODO: If time, add mapping so a pickaxe weighs more than diamonds
            double itemWeight = 0;

            for (ItemStack item : player.getInventory().getContents()) {
                if (item != null)
                    itemWeight += item.getAmount();
            }
            itemWeight /= 500;

            // Now check their armor weight
            double armorWeight = 0;
            armorWeight += getArmorWeight(player.getInventory().getHelmet());
            armorWeight += getArmorWeight(player.getInventory().getChestplate());
            armorWeight += getArmorWeight(player.getInventory().getLeggings());
            armorWeight += getArmorWeight(player.getInventory().getBoots());

            // Armor and item weight are directly related to how much the player has moved
            double distance = distances.containsKey(player.getUniqueId()) ? distances.get(player.getUniqueId()) : 0;
            distances.remove(player.getUniqueId());

            temperature = (int) Math.round(warmth - cold);
            temperature += (int) Math.round(distance * itemWeight);
            temperature += (int) Math.round(distance * armorWeight);

            temperature /= 10;

            // Incorporate world temperature
            temperature += worldTemp / 2;
        }

        if (temperature == 0) temperature = 1; // So it doesn't go away

        int currentTemperature = player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore(bodyTempKey).getScore();

        double diff = Math.abs(currentTemperature - temperature);
        if (diff > 8)
            temperature = (int) Math.round(temperature * 0.4); // Steady increase or decrease

        player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore(bodyTempKey).setScore(temperature);

        if (!player.isDead()) {
            if (temperature < hypothermiaTemp) player.damage(player.getMaxHealth() / 3);
            else if (temperature < shiverTemp) dropItems(player);
            else if (temperature > burningTemp) player.damage(player.getMaxHealth() / 3);
            else if (temperature > sweatingTemp) {
                if (!player.hasMetadata("slow")) {
                    player.setWalkSpeed(player.getWalkSpeed() / 2);
                    player.setMetadata("slow", new FixedMetadataValue(TenJava.getInstance(), "slow"));
                }
            }

            if (temperature < sweatingTemp && player.hasMetadata("slow")) {
                player.removeMetadata("slow", TenJava.getInstance());
                player.setWalkSpeed(player.getWalkSpeed() * 2);
            }
        }
    }

    private void dropItems(Player player) {
        Random random = new Random(); // Far enough apart that this won't matter

        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getAmount() > 0) {
                if (random.nextDouble() < 0.3) {
                    if (item.getAmount() == 1) player.getInventory().remove(item);
                    else item.setAmount(item.getAmount() - 1);

                    ItemStack drop = item.clone();
                    drop.setAmount(1);

                    player.getWorld().dropItemNaturally(player.getLocation(), drop);
                }
            }
        }

        player.updateInventory();
    }

    private double getArmorWeight(ItemStack item) {
        if (item == null || !armorWeights.containsKey(item.getType())) return 0;

        return armorWeights.get(item.getType());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.sendMessage("Welcome to tenjavria, where all you have to do is survive.");
        player.sendMessage("As night falls, you will need to stay near warm objects to avoid freezing.");
        player.sendMessage("As dawn breaks, you will need to stay hidden from the sun to avoid burning.");
        player.sendMessage("Keep safe in tenjavria!");

        // Create scoreboard
        Scoreboard scoreboard = TenJava.getInstance().getServer().getScoreboardManager().getNewScoreboard();

        Objective objective = scoreboard.registerNewObjective("dummy", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.AQUA + "Temperatures");

        Score score = objective.getScore(bodyTempKey);
        score.setScore(1);

        score = objective.getScore(worldTempKey);
        score.setScore(1);

        player.setScoreboard(scoreboard);
        adjustTemperature(player);

        if (!player.hasPlayedBefore()) {
            player.teleport(player.getWorld().getSpawnLocation());
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        double distance = Math.abs(event.getFrom().distance(event.getTo())); // TODO: THIS IS BAD
        double curr = distances.containsKey(player.getUniqueId()) ? distances.get(player.getUniqueId()) : 0;

        distances.put(player.getUniqueId(), curr + distance);
    }

}
