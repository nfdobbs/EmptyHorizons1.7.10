package com.nfdobbs.emptyhorizons;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;

import com.nfdobbs.emptyhorizons.blocks.EmptyHorizonBlocks;
import com.nfdobbs.emptyhorizons.network.*;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class CommonProxy {

    // This is used to store modded player data between respawns
    public static final Map<String, NBTTagCompound> extendedEntityData = new HashMap<String, NBTTagCompound>();
    public static SimpleNetworkWrapper networkWrapper;

    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        Config.synchronizeConfiguration(event.getSuggestedConfigurationFile());

        networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(EmptyHorizons.MODID);

        networkWrapper.registerMessage(SyncMessageHandler.class, SyncMessage.class, 0, Side.CLIENT);
        networkWrapper.registerMessage(ShowWelcomeGuiMessageHandler.class, ShowWelcomeGuiMessage.class, 2, Side.CLIENT);
        networkWrapper.registerMessage(FogDataMessageHandler.class, FogDataMessage.class, 5, Side.CLIENT);
        networkWrapper
            .registerMessage(ServerConfigSyncMessageHandler.class, ServerConfigSyncMessage.class, 7, Side.CLIENT);
        networkWrapper.registerMessage(TravelButtonMessageHandler.class, TravelButtonMessage.class, 10, Side.SERVER);
        networkWrapper.registerMessage(PlaystyleMessageHandler.class, PlaystyleMessage.class, 11, Side.SERVER);

        EmptyHorizonBlocks.init();

        EmptyHorizons.LOG.info(Config.greeting);
        EmptyHorizons.LOG.info("I am Empty Horizons at version " + Tags.VERSION);
    }

    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {}

    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {}

    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {}

    public static void storeEntityData(String name, NBTTagCompound compound) {
        extendedEntityData.put(name, compound);
    }

    public static NBTTagCompound getEntityData(String name) {
        return extendedEntityData.remove(name);
    }
}
