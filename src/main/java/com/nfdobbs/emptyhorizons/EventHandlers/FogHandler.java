package com.nfdobbs.emptyhorizons.EventHandlers;

import net.minecraft.block.material.Material;
import net.minecraftforge.client.event.EntityViewRenderEvent;

import com.nfdobbs.emptyhorizons.EmptyHorizons;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class FogHandler {

    @SubscribeEvent
    public void onFog(EntityViewRenderEvent.FogDensity event) {

        if (IsTransparent(event)) {
            return;
        }

        // .05F best case scenario
        event.density = .05F;
        event.setCanceled(true);
    }

    @SubscribeEvent
    public void onFogColor(EntityViewRenderEvent.FogDensity.FogColors event) {

        if (IsTransparent(event)) {
            return;
        }

        event.red = .2156F;
        event.green = .7215F;
        event.blue = .0431F;
    }

    private boolean IsTransparent(EntityViewRenderEvent event) {
        int x = (int) event.entity.posX;
        int y = (int) event.entity.posY;
        int z = (int) event.entity.posZ;

        Material material = event.entity.worldObj.getBlock(x, y, z)
            .getMaterial();

        return material.isLiquid();
    }
}
