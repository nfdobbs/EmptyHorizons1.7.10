package com.nfdobbs.emptyhorizons.gui;

import net.minecraft.block.material.Material;
import net.minecraftforge.client.event.EntityViewRenderEvent;

import com.nfdobbs.emptyhorizons.ClientProxy;
import com.nfdobbs.emptyhorizons.worlddata.FogProvider;
import com.nfdobbs.emptyhorizons.worlddata.FogRecord;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class FogHandler {

    private FogProvider fogProvider = ClientProxy.fogProvider;

    @SubscribeEvent
    public void onFog(EntityViewRenderEvent.FogDensity event) {

        // .05F best case scenario
        FogRecord fogRecord = fogProvider
            .GetFogRecord(event.entity.worldObj, event.entity.worldObj.provider.dimensionId);

        if (fogRecord != null) {
            event.density = fogRecord.getFogDensity();
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onFogColor(EntityViewRenderEvent.FogDensity.FogColors event) {
        if (IsTransparent(event)) {
            return;
        }

        FogRecord fogRecord = fogProvider
            .GetFogRecord(event.entity.worldObj, event.entity.worldObj.provider.dimensionId);

       /* event.red = .2156F;
        event.green = .7215F;
        event.blue = .0431F; */

        if (fogRecord != null) {
            event.red = fogRecord.getFogColorR();
            event.green = fogRecord.getFogColorG();
            event.blue = fogRecord.getFogColorB();
        }
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
