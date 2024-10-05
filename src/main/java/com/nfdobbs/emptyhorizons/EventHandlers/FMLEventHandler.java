package com.nfdobbs.emptyhorizons.EventHandlers;

import com.nfdobbs.emptyhorizons.playerdata.ExtendedEmptyHorizonsPlayer;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

public class FMLEventHandler {

    @SubscribeEvent
    public void OnPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        ExtendedEmptyHorizonsPlayer player = ExtendedEmptyHorizonsPlayer.get(event.player);
        int maxExpeditionTime = player.getMaxExpeditionTime();

        player.setMaxExpeditionTime(maxExpeditionTime + 100);

        player.debugMessage();
    }
}
