package com.sm9.prosperitymen;

import com.sm9.prosperitymen.handler.ForgeEvents;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod
        (
                modid = Constants.MOD_ID,
                name = Constants.MOD_NAME,
                version = Constants.MOD_VERSION,
                dependencies = Constants.DEPENDENCIES,
                acceptedMinecraftVersions = Constants.MINECRAFT_VERSION,
                acceptableRemoteVersions = "*"
        )
public
class ProsperityMen {
    public static Item maCrafting;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent evEvent) {
        ForgeEvents.preInit(evEvent);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent evEvent) {
        ForgeEvents.postInit();
    }

    @Mod.EventHandler
    public void onWorldLoad(FMLServerStartingEvent evEvent) {
        ForgeEvents.onWorldLoad(evEvent);
    }
}