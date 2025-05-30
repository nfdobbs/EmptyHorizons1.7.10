package com.nfdobbs.emptyhorizons;

import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nfdobbs.emptyhorizons.EmptyDimension.EmptyDimRegister;
import com.nfdobbs.emptyhorizons.EventHandlers.BetterQuestingEventHandler;
import com.nfdobbs.emptyhorizons.EventHandlers.EventHandler;
import com.nfdobbs.emptyhorizons.EventHandlers.FMLEventHandler;
import com.nfdobbs.emptyhorizons.commands.CommandEmptyHorizonsChapterMultipliers;
import com.nfdobbs.emptyhorizons.commands.CommandEmptyHorizonsMoveToParty;
import com.nfdobbs.emptyhorizons.commands.CommandEmptyHorizonsRecalcTime;
import com.nfdobbs.emptyhorizons.commands.CommandEmptyHorizonsWhereAmI;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(
    modid = EmptyHorizons.MODID,
    version = Tags.VERSION,
    name = "Empty Horizons",
    acceptedMinecraftVersions = "[1.7.10]")
public class EmptyHorizons {

    public static final String MODID = "emptyhorizons";
    public static final Logger LOG = LogManager.getLogger(MODID);

    @SidedProxy(
        clientSide = "com.nfdobbs.emptyhorizons.ClientProxy",
        serverSide = "com.nfdobbs.emptyhorizons.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        EmptyDimRegister.registerDimensions();
        EmptyDimRegister.registerBiomes();
        proxy.preInit(event);
    }

    @Mod.EventHandler
    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
        MinecraftForge.EVENT_BUS.register(new EventHandler());

        MinecraftForge.EVENT_BUS.register(new BetterQuestingEventHandler());
        MinecraftForge.TERRAIN_GEN_BUS.register(new BetterQuestingEventHandler());

        FMLCommonHandler.instance()
            .bus()
            .register(new FMLEventHandler());
    }

    @Mod.EventHandler
    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {
        proxy.serverStarting(event);
        event.registerServerCommand(new CommandEmptyHorizonsMoveToParty());
        event.registerServerCommand(new CommandEmptyHorizonsChapterMultipliers());
        event.registerServerCommand(new CommandEmptyHorizonsRecalcTime());
        event.registerServerCommand(new CommandEmptyHorizonsWhereAmI());
    }
}
