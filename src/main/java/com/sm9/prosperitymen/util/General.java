package com.sm9.prosperitymen.util;

import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.Formatter;
import java.util.List;

import static com.sm9.prosperitymen.common.Config.MainConfig.agroRange;

public class General {
    public static boolean playerMeetsPigmenAgroConditions(EntityPlayer localPlayer) {
        if (agroRange < 1) {
            return false;
        }

        if (localPlayer.isCreative() || localPlayer.isSpectator()) {
            return false;
        }

        InventoryPlayer localInventory = localPlayer.inventory;
        ItemStack currentItem;
        ResourceLocation resourceLocation;

        for (int i = 0; i < InventoryPlayer.getHotbarSize(); i++) {
            currentItem = localInventory.getStackInSlot(i);
            resourceLocation = currentItem.getItem().getRegistryName();

            if (resourceLocation == null) {
                continue;
            }

            if (!resourceLocation.toString().equals("mysticalagriculture:crafting") || currentItem.getMetadata() != 5) {
                continue;
            }

            return true;
        }

        return false;
    }

    public static void angerPigmen(EntityPlayer localPlayer) {
        World worldAny = localPlayer.getEntityWorld();

        if (!(worldAny instanceof WorldServer)) {
            return;
        }

        WorldServer worldServer = (WorldServer) worldAny;
        BlockPos playerPosition = localPlayer.getPosition();

        AxisAlignedBB axisAlignedBaby = new AxisAlignedBB(new BlockPos(playerPosition).add(-agroRange, -agroRange, -agroRange), new BlockPos(playerPosition).add(+agroRange, +agroRange, +agroRange));
        List<EntityPigZombie> pigMenList = worldServer.getEntitiesWithinAABB(EntityPigZombie.class, axisAlignedBaby);

        for (EntityPigZombie pigMan : pigMenList) {
            if ((pigMan.getRevengeTarget() instanceof EntityPlayer) || pigMan.getAttackTarget() instanceof EntityPlayer) {
                continue;
            }

            pigMan.setRevengeTarget(localPlayer);
            pigMan.setAttackTarget(localPlayer);
        }
    }

    public static void printToPlayer(EntityPlayer entityPlayer, String format, Object... args) {
        String message = new Formatter().format(format, args).toString();
        entityPlayer.sendMessage(new TextComponentString("[PB] " + message));
    }
}