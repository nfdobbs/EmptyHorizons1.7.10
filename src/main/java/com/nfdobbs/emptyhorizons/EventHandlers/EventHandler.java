package com.nfdobbs.emptyhorizons.EventHandlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import com.nfdobbs.emptyhorizons.EmptyHorizons;
import com.nfdobbs.emptyhorizons.Helpers;
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
            ExtendedEmptyHorizonsPlayer.loadProxyData((EntityPlayer) event.entity);
        }
    }

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        if (Helpers.IsServerSide(event) && Helpers.IsPlayerEvent(event)) {
            NBTTagCompound modPlayerData = new NBTTagCompound();

            ExtendedEmptyHorizonsPlayer modPlayer = (ExtendedEmptyHorizonsPlayer) event.entity
                .getExtendedProperties(ExtendedEmptyHorizonsPlayer.EXT_PROP_NAME);
            modPlayer.saveNBTData(modPlayerData);

            // Call Save Proxy Data
            ExtendedEmptyHorizonsPlayer.saveProxyData((EntityPlayer) event.entity);
        }
    }
}
