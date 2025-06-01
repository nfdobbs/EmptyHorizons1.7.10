package com.nfdobbs.emptyhorizons.EventHandlers;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;

import com.nfdobbs.emptyhorizons.CommonProxy;
import com.nfdobbs.emptyhorizons.Config;
import com.nfdobbs.emptyhorizons.EmptyDimension.EmptyDimRegister;
import com.nfdobbs.emptyhorizons.EmptyDimension.EmptyDimTeleporter;
import com.nfdobbs.emptyhorizons.EmptyHorizons;
import com.nfdobbs.emptyhorizons.network.ServerConfigSyncMessage;
import com.nfdobbs.emptyhorizons.playerdata.ExtendedEmptyHorizonsPlayer;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class FMLEventHandler {

    DamageSource Exposure = new DamageSource(EmptyHorizons.MODID + "_exposure");

    private int tickCounter = 0;

    @SubscribeEvent
    public void onPlayerRespawnEvent(PlayerEvent.PlayerRespawnEvent event) {
        if (!event.player.getEntityWorld().isRemote) {
            EntityPlayer player = (EntityPlayer) event.player;

            ExtendedEmptyHorizonsPlayer modPlayer = (ExtendedEmptyHorizonsPlayer) player
                .getExtendedProperties(ExtendedEmptyHorizonsPlayer.EXT_PROP_NAME);

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

    @SubscribeEvent
    public void onChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        int fromDim = event.fromDim;
        int toDim = event.toDim;

        ExtendedEmptyHorizonsPlayer modPlayer = ExtendedEmptyHorizonsPlayer.get(event.player);

        if (!modPlayer.isDoingChallenge()) {
            return;
        }

        int newExpeditionTime = modPlayer.getExpeditionTime();

        if (Config.dimensionMultipliers.containsKey(fromDim)) {
            float fromDimMultiplier = Config.dimensionMultipliers.get(fromDim);

            newExpeditionTime = (int) (newExpeditionTime * fromDimMultiplier);
            modPlayer.setExpeditionTime(Math.min(newExpeditionTime, modPlayer.getMaxExpeditionTime()));
        }

        if (Config.dimensionMultipliers.containsKey(toDim)) {
            float toDimMultiplier = Config.dimensionMultipliers.get(toDim);
            newExpeditionTime = (int) (newExpeditionTime / toDimMultiplier);
        }

        // Safety Checks
        if (newExpeditionTime < 1) {
            newExpeditionTime = 1;
        }

        modPlayer.setExpeditionTime(Math.min(newExpeditionTime, modPlayer.getMaxExpeditionTime() - 1));
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        List<EntityPlayerMP> onlinePlayers = MinecraftServer.getServer()
            .getConfigurationManager().playerEntityList;

        if (event.phase == TickEvent.Phase.END) {
            tickCounter++;
            if (tickCounter % 20 == 0) {
                tickCounter = 0;

                for (EntityPlayerMP player : onlinePlayers) {
                    ExtendedEmptyHorizonsPlayer ehPlayer = ExtendedEmptyHorizonsPlayer.get(player);

                    if (ehPlayer.isDoingChallenge() && !player.worldObj.isRemote && player.isEntityAlive()) {

                        int maxExpTime = ehPlayer.getMaxExpeditionTime();
                        int currentExpTime = ehPlayer.getExpeditionTime();

                        if (!ExtendedEmptyHorizonsPlayer.IsSafe(ehPlayer, player.worldObj.provider)) {
                            ehPlayer.setExpeditionTime(currentExpTime - 1);
                        } else if (maxExpTime > currentExpTime) {
                            ehPlayer.setExpeditionTime(currentExpTime + 1);
                        }

                        // Tick seems to re-fire before player respawns resulting in repeat deaths this prevents that
                        if (currentExpTime < 0) {
                            ehPlayer.setExpeditionTime(1);
                            currentExpTime = 1;
                        }

                        if (currentExpTime < 1) {
                            player.attackEntityFrom(Exposure, Float.MAX_VALUE);
                            return;
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void OnPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        EmptyHorizons.LOG.info("Sending Dimension Multipliers Packet");

        if (!event.player.worldObj.isRemote) {
            CommonProxy.networkWrapper
                .sendTo(new ServerConfigSyncMessage(Config.dimensionMultipliers), (EntityPlayerMP) event.player);
        }
    }

}
