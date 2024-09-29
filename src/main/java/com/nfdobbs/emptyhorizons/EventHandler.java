package com.nfdobbs.emptyhorizons;

import static com.nfdobbs.emptyhorizons.EmptyHorizons.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import com.nfdobbs.emptyhorizons.playerdata.ExtendedEmptyHorizonsPlayer;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EventHandler {

    @SubscribeEvent
    public void onEntityConstructing(EntityEvent.EntityConstructing event) {
        if (event.entity instanceof EntityPlayer
            && ExtendedEmptyHorizonsPlayer.get((EntityPlayer) event.entity) == null) {

            EmptyHorizons.LOG.info("Empty Horizons Registering Player");

            ExtendedEmptyHorizonsPlayer.register((EntityPlayer) event.entity);
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (!event.entity.worldObj.isRemote && event.entity instanceof EntityPlayer) {
            NBTTagCompound playerData = proxy.getEntityData(((EntityPlayer) event.entity).getCommandSenderName());

            if (playerData != null) {
                event.entity.getExtendedProperties(ExtendedEmptyHorizonsPlayer.EXT_PROP_NAME)
                    .loadNBTData(playerData);
                // event.entity.getExtendedProperties(ExtendedEmptyHorizonsPlayer.EXT_PROP_NAME).syncExtendedProperties(playerData);
            }
        }

        // Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Welcome to the Server"));
    }

}
