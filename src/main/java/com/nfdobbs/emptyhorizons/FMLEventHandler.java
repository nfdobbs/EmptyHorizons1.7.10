package com.nfdobbs.emptyhorizons;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

public class FMLEventHandler {

    @SubscribeEvent
    public void OnPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        EmptyHorizons.LOG.info("Player Changed Event: ");
    }
}
