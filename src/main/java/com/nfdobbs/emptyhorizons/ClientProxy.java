package com.nfdobbs.emptyhorizons;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

import com.nfdobbs.emptyhorizons.gui.DebugHandler;
import com.nfdobbs.emptyhorizons.gui.EmptyHorizonsOverlay;
import com.nfdobbs.emptyhorizons.gui.ExcursionBlockGUI;
import com.nfdobbs.emptyhorizons.gui.FogHandler;
import com.nfdobbs.emptyhorizons.worlddata.FogProvider;

import cpw.mods.fml.common.event.FMLInitializationEvent;

public class ClientProxy extends CommonProxy {

    // Provides fog density and color
    public static FogProvider fogProvider = new FogProvider();

    @Override
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new EmptyHorizonsOverlay(Minecraft.getMinecraft()));
        MinecraftForge.EVENT_BUS.register(new FogHandler(Minecraft.getMinecraft()));
        MinecraftForge.EVENT_BUS.register(new DebugHandler());
        super.init(event);
    }

    public static EntityPlayer getPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }

    public static void showExcursionScreen(int x, int y, int z) {
        Minecraft.getMinecraft()
            .displayGuiScreen(new ExcursionBlockGUI(x, y, z));
    }

}
