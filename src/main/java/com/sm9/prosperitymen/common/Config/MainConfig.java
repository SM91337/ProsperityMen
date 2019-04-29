package com.sm9.prosperitymen.common.Config;

import net.minecraftforge.common.config.Configuration;

import java.util.Arrays;
import java.util.List;

public class MainConfig {
    private static final List<String> configOrder = Arrays.asList("DebugMode", "DropChance", "MeleeOnly", "NetherOnly", "HotbarAgroRange", "WorldRandom");
    public static boolean debugMode = false;
    public static Configuration mainConfig;
    public static boolean meleeOnly = true;
    public static boolean netherOnly = true;
    public static boolean worldRandom = true;
    public static int dropChance = 50;
    public static int agroRange = 48;

    public static void loadConfig() {
        mainConfig.load();
        mainConfig.setCategoryPropertyOrder("General", configOrder);
        debugMode = mainConfig.getBoolean("DebugMode", Configuration.CATEGORY_GENERAL, false, "Prints debug information on hostile entity death");
        dropChance = mainConfig.getInt("DropChance", Configuration.CATEGORY_GENERAL, 50, 0, 100, "Drop chance for hostiles");
        meleeOnly = mainConfig.getBoolean("MeleeOnly", Configuration.CATEGORY_GENERAL, meleeOnly, "Only allow essence drops when killed with a melee weapon");
        netherOnly = mainConfig.getBoolean("NetherOnly", Configuration.CATEGORY_GENERAL, false, "Pigmen will only agro / drop prosperity in the Nether");
        agroRange = mainConfig.getInt("HotbarAgroRange", Configuration.CATEGORY_GENERAL, 48, 0, 128, "Range at which pigmen sense you are holding their precious shards, 0 turns this feature off");
        worldRandom = mainConfig.getBoolean("WorldRandom", Configuration.CATEGORY_GENERAL, worldRandom, "Use world random generator instead of built in java one");
        mainConfig.save();
    }
}
