package com.sm9.prosperitymen;

import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;

@Mod(modid = ProsperityMen.MODID, name = ProsperityMen.NAME,
        version = ProsperityMen.VERSION,
        dependencies = ProsperityMen.DEPENDENCIES,
        acceptedMinecraftVersions = "1.12.2",
        acceptableRemoteVersions = "*")

public class ProsperityMen
{
    public static final String MODID = "prosperitymen";
    public static final String NAME = "Prosperity Men";
    public static final String VERSION = "0.1.1";
    public static final String MIN_FORGE_VER = "14.23.5.2815";
    public static final String DEPENDENCIES = "after:forge@[" + ProsperityMen.MIN_FORGE_VER + ",)" + ";required-after:mysticalagriculture@[1.7.0,)";

    public static Logger g_lLogger;
    public static boolean g_bDebugMode = false;
    public static boolean g_bMeleeOnly = true;
    public static boolean g_bNetherOnly = true;
    public static boolean g_bWorldRandom = true;

    public static int g_iChance = 50;
    public static int g_iAgroRange = 64;
    public static Configuration g_cConfig;
    public static Item maCrafting;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent evEvent)
    {
        MinecraftForge.EVENT_BUS.register(new ForgeEventHandlers());

        g_lLogger = LogManager.getLogger("ProsperityMen");

        File fConfigFolder = new File(evEvent.getModConfigurationDirectory(), "sm9/ProsperityMen");
        File fConfig = new File(fConfigFolder, "main.cfg");

        g_cConfig = new Configuration(fConfig);

        loadConfig();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent evEvent) {
        maCrafting = Item.getByNameOrId("mysticalagriculture:crafting");
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent evEvent) {
        evEvent.registerServerCommand(new CmdReload());
    }

    public static void loadConfig()
    {
        g_cConfig.load();

        g_bDebugMode = g_cConfig.getBoolean("DebugMode", Configuration.CATEGORY_GENERAL, false, "Prints debug information on zombie pigmen death");
        g_iChance = g_cConfig.getInt("DropChance", Configuration.CATEGORY_GENERAL, 50, 0, 100, "Drop chance for zombie pigmen");
        g_bMeleeOnly = g_cConfig.getBoolean("MeleeOnly", Configuration.CATEGORY_GENERAL, true, "Only allow prosperity drops when killed with a melee weapon");
        g_bNetherOnly = g_cConfig.getBoolean("NetherOnly", Configuration.CATEGORY_GENERAL, false, "Pigmen will only agro / drop prosperity in the Nether");
        g_bWorldRandom = g_cConfig.getBoolean("WorldRandom", Configuration.CATEGORY_GENERAL, true, "Use world random generator instead of built in java one");
        g_iAgroRange = g_cConfig.getInt("HoldingAgroRange", Configuration.CATEGORY_GENERAL, 48, 0, 64, "Range at which pigmen sense you are holding their precious shards, 0 turns this feature off");

        g_cConfig.save();
    }
}