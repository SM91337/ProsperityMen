package com.sm9.prosperitymen;

import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

import static com.sm9.prosperitymen.ProsperityMen.*;

public class Util
{
    public static boolean playerMeetsPigmenAgroConditions(EntityPlayer localPlayer)
    {
        if(g_iAgroRange < 1) {
            return false;
        }
		
		if(localPlayer.isCreative() || localPlayer.isSpectator()) {
            return false;
        }

        InventoryPlayer localInventory = localPlayer.inventory;
        ItemStack currentItem;

        for(int i = 0; i < localInventory.getHotbarSize(); i++) {
            currentItem = localInventory.getStackInSlot(i);

            if(!currentItem.getItem().getRegistryName().toString().equals("mysticalagriculture:crafting") || currentItem.getMetadata() != 5)  {
                continue;
            }

            return true;
        }

        return false;
    }

    public static void angerPigmen(EntityPlayer localPlayer)
    {
        BlockPos bpPlayerPos = localPlayer.getPosition();
        BlockPos bpStart = new BlockPos(bpPlayerPos).add(-g_iAgroRange, -g_iAgroRange, -g_iAgroRange);
        BlockPos bpEnd = new BlockPos(bpPlayerPos).add(+g_iAgroRange, +g_iAgroRange, +g_iAgroRange);

        AxisAlignedBB axisAlignedBaby = new AxisAlignedBB(bpStart, bpEnd);

        List<EntityPigZombie> pigMenList = localPlayer.world.getEntitiesWithinAABB(EntityPigZombie.class, axisAlignedBaby);

        for (EntityPigZombie pigMan : pigMenList) {
            if (!(pigMan.getRevengeTarget() instanceof EntityPlayer)) {
                pigMan.setRevengeTarget(localPlayer);
            }

            if (!(pigMan.getAttackTarget() instanceof EntityPlayer)) {
                pigMan.setAttackTarget(localPlayer);
            }
        }
    }
}