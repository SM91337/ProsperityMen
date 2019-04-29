package com.sm9.prosperitymen.common.Config;

import net.minecraftforge.common.config.Configuration;

import java.util.Arrays;
import java.util.List;

public class MainConfig {
    private static final List<String> configOrder = Arrays.asList("DebugMode", "DropChance", "MeleeOnly", "NetherOnly", "HotbarAgroRange", "WorldRandom");
    public static boolean debugMode = false;
    public static Configuration mainConfig;
    public static boolean meleeOnly = true;
    public static boolean netherOnly = false;
    public static boolean worldRandom = true;
    public static int dropChance = 50;
    public static int agroRange = 48;

    public static void loadConfig() {
        mainConfig.load();
        mainConfig.setCategoryPropertyOrder("General", configOrder);
        debugMode = mainConfig.getBoolean("DebugMode", Configuration.CATEGORY_GENERAL, debugMode, "Prints debug information on zombie pigmen death");
        dropChance = mainConfig.getInt("DropChance", Configuration.CATEGORY_GENERAL, dropChance, 0, 100, "Drop chance for zombie pigmen");
        meleeOnly = mainConfig.getBoolean("MeleeOnly", Configuration.CATEGORY_GENERAL, meleeOnly, "Only allow prosperity drops when killed with a melee weapon");
        netherOnly = mainConfig.getBoolean("NetherOnly", Configuration.CATEGORY_GENERAL, netherOnly, "Pigmen will only agro / drop prosperity in the Nether");
        agroRange = mainConfig.getInt("HotbarAgroRange", Configuration.CATEGORY_GENERAL, agroRange, 0, 128, "Range at which pigmen sense you are holding their precious shards, 0 turns this feature off");
        worldRandom = mainConfig.getBoolean("WorldRandom", Configuration.CATEGORY_GENERAL, worldRandom, "Use world random generator instead of built in java one");
        mainConfig.save();
    }
}
