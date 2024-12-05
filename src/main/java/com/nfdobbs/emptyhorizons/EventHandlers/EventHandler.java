package com.nfdobbs.emptyhorizons.EventHandlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import com.nfdobbs.emptyhorizons.EmptyDimension.EmptyDimRegister;
import com.nfdobbs.emptyhorizons.EmptyDimension.EmptyDimTeleporter;
import com.nfdobbs.emptyhorizons.EmptyHorizons;
import com.nfdobbs.emptyhorizons.Helpers;
import com.nfdobbs.emptyhorizons.playerdata.ExtendedEmptyHorizonsPlayer;
import com.nfdobbs.emptyhorizons.worlddata.FogProvider;

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
            // Setup persistent player data
            ExtendedEmptyHorizonsPlayer.loadProxyData((EntityPlayer) event.entity);

            FogProvider serverFogProvider = new FogProvider();
            serverFogProvider.GetFogRecord(event.entity.worldObj, event.entity.dimension);

            serverFogProvider.SyncFogData((EntityPlayerMP) event.entity);
            EmptyHorizons.LOG.info("Syncing Fog Data");
        }
    }

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        if (Helpers.IsServerSide(event) && Helpers.IsPlayerEvent(event)) {
            NBTTagCompound modPlayerData = new NBTTagCompound();
            EntityPlayer player = (EntityPlayer) event.entity;

            ExtendedEmptyHorizonsPlayer modPlayer = (ExtendedEmptyHorizonsPlayer) player
                .getExtendedProperties(ExtendedEmptyHorizonsPlayer.EXT_PROP_NAME);
            modPlayer.saveNBTData(modPlayerData);

            // Call Save Proxy Data
            ExtendedEmptyHorizonsPlayer.saveProxyData(player);

            if (modPlayer.getExpeditionTime() < 1) {
                modPlayer.setExpeditionTime(1);
            }

            if (modPlayer.isDoingChallenge()) {
                ChunkCoordinates chunkCoordinates = player.getBedLocation(EmptyDimRegister.EMPTY_DIMENSION_ID);

                if (chunkCoordinates != null) {
                    EmptyDimTeleporter.teleportToEmptyDim(
                        player,
                        chunkCoordinates.posX,
                        chunkCoordinates.posY,
                        chunkCoordinates.posZ,
                        0,
                        0);
                }
            }
        }
    }
}
