package de.sanandrew.mods.enderstuffplus.registry.event;

import java.util.ArrayList;

import de.sanandrew.core.manpack.mod.packet.IPacket;
import de.sanandrew.core.manpack.util.helpers.SAPUtils;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;

import net.minecraftforge.common.IShearable;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;

import net.minecraftforge.event.entity.player.EntityInteractEvent;

import de.sanandrew.mods.enderstuffplus.packet.PacketFXCstPortal;
import de.sanandrew.mods.enderstuffplus.registry.ESPModRegistry;
import de.sanandrew.mods.enderstuffplus.registry.ModItemRegistry;

public class EntityInteractEventInst
{
    @SubscribeEvent
    public void onEntityInteract(EntityInteractEvent event) {
        Entity entity = event.target;
        ItemStack itemstack = event.entityPlayer.getCurrentEquippedItem();

        if( !entity.worldObj.isRemote && itemstack != null && itemstack.getItem() == ModItemRegistry.niobShears
                && entity instanceof IShearable && entity instanceof EntityLivingBase )
        {
            IShearable target = (IShearable) entity;
            if( target.isShearable(itemstack, entity.worldObj, (int) entity.posX, (int) entity.posY, (int) entity.posZ) ) {
                ArrayList<ItemStack> drops =
                        target.onSheared(itemstack, entity.worldObj, (int) entity.posX, (int) entity.posY, (int) entity.posZ,
                                         EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, itemstack));
                boolean transportSucceed = false;
                for( ItemStack item : drops ) {
                    ItemStack newStack = EnchantmentHelper.getEnchantmentLevel(ESPModRegistry.enderChestTel.effectId, itemstack) > 0
                                                                                    ? SAPUtils.addItemStackToInventory(item.copy(), event.entityPlayer.getInventoryEnderChest())
                                                                                    : item.copy();
                    if( newStack != null ) {
                        newStack = SAPUtils.addItemStackToInventory(newStack.copy(), event.entityPlayer.inventory);

                        if( newStack == null ) {
                            transportSucceed = true;
                        } else {
                            EntityItem eItem = entity.entityDropItem(item, 1.0F);
                            eItem.motionY += SAPUtils.RANDOM.nextFloat() * 0.05F;
                            eItem.motionX += (SAPUtils.RANDOM.nextFloat() - SAPUtils.RANDOM.nextFloat()) * 0.1F;
                            eItem.motionZ += (SAPUtils.RANDOM.nextFloat() - SAPUtils.RANDOM.nextFloat()) * 0.1F;
                        }
                    } else {
                        transportSucceed = true;
                    }
                }

                if( transportSucceed ) {
                    IPacket packet = new PacketFXCstPortal(entity.posX, entity.posY, entity.posZ, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F, 10);
                    ESPModRegistry.channelHandler.sendToAllAround(packet, new TargetPoint(entity.dimension, entity.posX, entity.posY, entity.posZ, 64));

                    event.entityPlayer.inventoryContainer.detectAndSendChanges();

                    if( event.entityPlayer.openContainer != null ) {
                        event.entityPlayer.inventoryContainer.detectAndSendChanges();
                    }
                }

                if( !event.entityPlayer.capabilities.isCreativeMode ) {
                    itemstack.damageItem(1, (EntityLivingBase) entity);
                }
            }
            event.setCanceled(true);
        }
    }
}
