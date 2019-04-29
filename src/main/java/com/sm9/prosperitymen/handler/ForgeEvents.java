package com.sm9.prosperitymen.handler;

import com.sm9.prosperitymen.command.Reload;
import com.sm9.prosperitymen.util.General;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static com.sm9.prosperitymen.ProsperityMen.maCrafting;
import static com.sm9.prosperitymen.ProsperityMen.pmLogger;
import static com.sm9.prosperitymen.common.Config.MainConfig.*;

public class ForgeEvents {
    public static void preInit(FMLPreInitializationEvent evEvent) {
        MinecraftForge.EVENT_BUS.register(new ForgeEvents());

        File configDirectory = new File(evEvent.getModConfigurationDirectory(), "sm9/ProsperityMen");
        File configFile = new File(configDirectory, "main.cfg");

        pmLogger = LogManager.getLogger("ProsperityMen");
        mainConfig = new Configuration(configFile);
    }

    public static void postInit() {
        loadConfig();
        maCrafting = Item.getByNameOrId("mysticalagriculture:crafting");
    }

    public static void onWorldLoad(FMLServerStartingEvent evEvent) {
        evEvent.registerServerCommand(new Reload());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onLivingDrops(LivingDropsEvent evEvent) {
        if (maCrafting == null) {
            return;
        }

        List<EntityItem> entityDrops = evEvent.getDrops();

        if (entityDrops == null) {
            return;
        }
        
        ItemStack itemStack;
        ArrayList<EntityItem> dropsToRemove = new ArrayList<>();

        for (EntityItem item : entityDrops) {
            itemStack = item.getItem();

            if (!Objects.requireNonNull(itemStack.getItem().getRegistryName()).toString().equals("mysticalagriculture:crafting") || itemStack.getMetadata() != 5) {
                continue;
            }

            dropsToRemove.add(item);
        }

        entityDrops.removeAll(dropsToRemove);

        DamageSource damageSource = evEvent.getSource();
        Entity damageEntity = damageSource.getTrueSource();

        if (!(damageEntity instanceof EntityPlayer)) {
            return;
        }

        Entity damageVictim = evEvent.getEntity();

        if (!(damageVictim instanceof EntityPigZombie)) {
            return;
        }

        World localWorld = damageVictim.world;
        EntityPlayer localPlayer = (EntityPlayer) damageEntity;

        if (localWorld == null) {
            return;
        }

        int iLocalDimension = localPlayer.dimension;

        if (netherOnly && iLocalDimension != -1) {
            if (debugMode) {
                General.printToPlayer(localPlayer, "Ignoring none nether kill.");
            }
            return;
        }

        if (meleeOnly && damageSource.isProjectile()) {
            if (debugMode) {
                General.printToPlayer(localPlayer, "Ignoring none melee kill.");
            }
            return;
        }

        Random rRandom = worldRandom ? localWorld.rand : new Random();
        int iRandom = Math.round(rRandom.nextFloat() * 100.0f);

        if (iRandom <= dropChance) {
            entityDrops.add(new EntityItem(localWorld, damageVictim.posX, damageVictim.posY, damageVictim.posZ, new ItemStack(maCrafting, 1, 5)));
        }

        if (debugMode) {
            General.printToPlayer(localPlayer, "Drop Chance: %d, RNG: %d, Should Drop: %s", dropChance, iRandom, iRandom <= dropChance ? "true" : "false");
        }
    }

    @SubscribeEvent
    public void tickPlayer(TickEvent.PlayerTickEvent evEvent) {
        if (maCrafting == null || evEvent.side != Side.SERVER || evEvent.phase != TickEvent.Phase.END) {
            return;
        }

        EntityPlayer tickingPlayer = evEvent.player;

        if (tickingPlayer == null) {
            return;
        }

        if (!General.playerMeetsPigmenAgroConditions(tickingPlayer)) {
            return;
        }

        General.angerPigmen(tickingPlayer);
    }
}