package com.nfdobbs.emptyhorizons.EventHandlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;

import com.nfdobbs.emptyhorizons.EmptyDimension.EmptyDimRegister;
import com.nfdobbs.emptyhorizons.EmptyDimension.EmptyDimTeleporter;
import com.nfdobbs.emptyhorizons.EmptyHorizons;
import com.nfdobbs.emptyhorizons.playerdata.ExtendedEmptyHorizonsPlayer;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FMLEventHandler {

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
    @SideOnly(Side.SERVER)
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;

        ExtendedEmptyHorizonsPlayer ehPlayer = ExtendedEmptyHorizonsPlayer.get(player);

        if (ehPlayer.isDoingChallenge() && event.phase == TickEvent.Phase.END
            && !player.worldObj.isRemote
            && player.isEntityAlive()) {
            tickCounter++;

            if (tickCounter % 20 == 0) {
                tickCounter = 0;

                // ExtendedEmptyHorizonsPlayer ehPlayer = ExtendedEmptyHorizonsPlayer.get(player);
                int maxExpTime = ehPlayer.getMaxExpeditionTime();
                int currentExpTime = ehPlayer.getExpeditionTime();

                if (player.dimension != EmptyHorizons.SAFE_DIMENSION) {
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
                    player.attackEntityFrom(DamageSource.outOfWorld, Float.MAX_VALUE);
                    return;
                }

            }
        }
    }

}
