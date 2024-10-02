package com.nfdobbs.emptyhorizons;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

public class CommonProxy {

    // This is used to store modded player data between respawns
    public static final Map<String, NBTTagCompound> extendedEntityData = new HashMap<String, NBTTagCompound>();

    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        Config.synchronizeConfiguration(event.getSuggestedConfigurationFile());

        EmptyHorizons.LOG.info(Config.greeting);
        EmptyHorizons.LOG.info("I am Empty Horizons at version " + Tags.VERSION);
    }

    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {
        /*
         * MinecraftForge.EVENT_BUS.register(new EmptyHorizonsOverlay(Minecraft.getMinecraft()));
         * EmptyHorizons.LOG.info("Registering GUI Handler");
         */
    }

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
