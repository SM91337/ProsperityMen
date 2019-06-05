package com.sm9.prosperitymen.handler;

import com.sm9.prosperitymen.command.Reload;
import com.sm9.prosperitymen.util.General;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.sm9.prosperitymen.ProsperityMen.maCrafting;
import static com.sm9.prosperitymen.common.Config.MainConfig.*;

public class ForgeEvents {
    public static void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new ForgeEvents());

        File configDirectory = new File(event.getModConfigurationDirectory(), "sm9/ProsperityMen");
        File configFile = new File(configDirectory, "main.cfg");

        mainConfig = new Configuration(configFile);
    }

    public static void postInit() {
        loadConfig();
        maCrafting = Item.getByNameOrId("mysticalagriculture:crafting");
    }

    public static void onWorldLoad(FMLServerStartingEvent event) {
        event.registerServerCommand(new Reload());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onLivingDrops(LivingDropsEvent event) {
        if (maCrafting == null) {
            return;
        }

        List<EntityItem> entityDrops = event.getDrops();

        if (entityDrops == null) {
            return;
        }

        ItemStack itemStack;
        ArrayList<EntityItem> dropsToRemove = new ArrayList<>();

        for (EntityItem item : entityDrops) {
            itemStack = item.getItem();

            if (!itemStack.getItem().equals(maCrafting) || itemStack.getMetadata() != 5) {
                continue;
            }

            dropsToRemove.add(item);
        }

        entityDrops.removeAll(dropsToRemove);

        DamageSource damageSource = event.getSource();
        Entity damageEntity = damageSource.getTrueSource();

        if (!(damageEntity instanceof EntityPlayer)) {
            return;
        }

        EntityPlayer localPlayer = (EntityPlayer) damageEntity;
        EntityLivingBase damageVictim = event.getEntityLiving();

        if (!(damageVictim instanceof EntityPigZombie)) {
            return;
        }

        World worldAny = damageVictim.getEntityWorld();

        if (!(worldAny instanceof WorldServer)) {
            return;
        }

        WorldServer worldServer = (WorldServer) worldAny;

        int dimensionId = worldServer.provider.getDimension();

        if (netherOnly && dimensionId != -1) {
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

        Random random = worldRandom ? worldServer.rand : new Random();
        int randomInt = Math.round(random.nextFloat() * 100.0f);

        if (debugMode) {
            General.printToPlayer(localPlayer, "Drop Chance: %d, RNG: %d, Should Drop: %s", dropChance, randomInt, randomInt <= dropChance ? "true" : "false");
        }

        if (randomInt <= dropChance) {
            entityDrops.add(new EntityItem(worldServer, damageVictim.posX, damageVictim.posY, damageVictim.posZ, new ItemStack(maCrafting, 1, 5)));
        }
    }

    @SubscribeEvent
    public void tickPlayer(TickEvent.PlayerTickEvent event) {
        if (maCrafting == null || event.side != Side.SERVER || event.phase != TickEvent.Phase.END) {
            return;
        }

        EntityPlayer tickingPlayer = event.player;

        if (tickingPlayer == null) {
            return;
        }

        if (!General.playerMeetsPigmenAgroConditions(tickingPlayer)) {
            return;
        }

        General.angerPigmen(tickingPlayer);
    }
}