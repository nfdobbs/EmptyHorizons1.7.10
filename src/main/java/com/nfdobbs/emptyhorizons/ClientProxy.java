package com.nfdobbs.emptyhorizons;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

import com.nfdobbs.emptyhorizons.gui.EmptyHorizonsOverlay;
import com.nfdobbs.emptyhorizons.gui.FogHandler;
import com.nfdobbs.emptyhorizons.worlddata.FogProvider;

import cpw.mods.fml.common.event.FMLInitializationEvent;

public class ClientProxy extends CommonProxy {

    // Provides fog density and color
    public static FogProvider fogProvider = new FogProvider();

    @Override
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new EmptyHorizonsOverlay(Minecraft.getMinecraft()));
        MinecraftForge.EVENT_BUS.register(new FogHandler());
        super.init(event);
    }
}
