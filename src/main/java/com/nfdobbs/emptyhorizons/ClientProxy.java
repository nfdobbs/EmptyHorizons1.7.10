package com.nfdobbs.emptyhorizons;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

import com.nfdobbs.emptyhorizons.EventHandlers.FogHandler;
import com.nfdobbs.emptyhorizons.gui.EmptyHorizonsOverlay;

import cpw.mods.fml.common.event.FMLInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new EmptyHorizonsOverlay(Minecraft.getMinecraft()));
        MinecraftForge.EVENT_BUS.register(new FogHandler());
        super.init(event);
    }

}
