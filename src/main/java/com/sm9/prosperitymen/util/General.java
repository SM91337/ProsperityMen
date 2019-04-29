package com.sm9.prosperitymen.util;

import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import java.util.Formatter;
import java.util.List;
import java.util.Objects;

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

        for (int i = 0; i < InventoryPlayer.getHotbarSize(); i++) {
            currentItem = localInventory.getStackInSlot(i);

            if (!Objects.requireNonNull(currentItem.getItem().getRegistryName()).toString().equals("mysticalagriculture:crafting") || currentItem.getMetadata() != 5) {
                continue;
            }

            return true;
        }

        return false;
    }

    public static void angerPigmen(EntityPlayer localPlayer) {
        BlockPos bpPlayerPos = localPlayer.getPosition();
        BlockPos bpStart = new BlockPos(bpPlayerPos).add(-agroRange, -agroRange, -agroRange);
        BlockPos bpEnd = new BlockPos(bpPlayerPos).add(+agroRange, +agroRange, +agroRange);

        AxisAlignedBB axisAlignedBaby = new AxisAlignedBB(bpStart, bpEnd);
        List<EntityPigZombie> pigMenList = localPlayer.world.getEntitiesWithinAABB(EntityPigZombie.class, axisAlignedBaby);

        for (EntityPigZombie pigMan : pigMenList) {
            if ((pigMan.getRevengeTarget() instanceof EntityPlayer) || pigMan.getAttackTarget() instanceof EntityPlayer) {
                continue;
            }

            pigMan.setRevengeTarget(localPlayer);
            pigMan.setAttackTarget(localPlayer);
        }
    }

    public static void printToPlayer(EntityPlayer entityPlayer, String sFormat, Object... oArgs) {
        String sMessage = new Formatter().format(sFormat, oArgs).toString();
        entityPlayer.sendMessage(new TextComponentString("[PB] " + sMessage));
    }

}