package com.nfdobbs.emptyhorizons.EventHandlers;

import net.minecraft.entity.player.EntityPlayer;

import com.nfdobbs.emptyhorizons.playerdata.ExtendedEmptyHorizonsPlayer;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class FMLEventHandler {

    private int tickCounter = 0;
    private static final int SAFE_DIMENSION = 0;

    @SubscribeEvent
    public void OnPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        ExtendedEmptyHorizonsPlayer player = ExtendedEmptyHorizonsPlayer.get(event.player);
        int maxExpeditionTime = player.getMaxExpeditionTime();

        player.setMaxExpeditionTime(maxExpeditionTime + 100);

        player.debugMessage();
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;

        if (event.phase == TickEvent.Phase.END && !player.worldObj.isRemote) {
            tickCounter++;

            if (tickCounter % 20 == 0) {
                tickCounter = 0;

                ExtendedEmptyHorizonsPlayer ehPlayer = ExtendedEmptyHorizonsPlayer.get(player);
                int maxExpTime = ehPlayer.getMaxExpeditionTime();
                int currentExpTime = ehPlayer.getExpeditionTime();

                if (player.dimension != SAFE_DIMENSION) {
                    ehPlayer.setExpeditionTime(currentExpTime - 1);
                } else if (maxExpTime > currentExpTime) {
                    ehPlayer.setExpeditionTime(currentExpTime + 1);
                }
            }
        }
    }
}
