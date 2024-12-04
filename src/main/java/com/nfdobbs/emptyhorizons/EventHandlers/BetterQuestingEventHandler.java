package com.nfdobbs.emptyhorizons.EventHandlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

import com.nfdobbs.emptyhorizons.EmptyHorizons;
import com.nfdobbs.emptyhorizons.playerdata.ExtendedEmptyHorizonsPlayer;

import betterquesting.api.events.QuestEvent;
import betterquesting.api.properties.NativeProps;
import betterquesting.api.questing.IQuestLine;
import betterquesting.questing.QuestDatabase;
import betterquesting.questing.QuestInstance;
import betterquesting.questing.QuestLine;
import betterquesting.questing.QuestLineDatabase;
import betterquesting.questing.QuestLineEntry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class BetterQuestingEventHandler {

    private final QuestDatabase questDB = QuestDatabase.INSTANCE;
    private final QuestLineDatabase questLineDB = QuestLineDatabase.INSTANCE;
    private MinecraftServer server = null;

    @SubscribeEvent
    public void OnQuestCompletion(QuestEvent event) {
        server = MinecraftServer.getServer();

        // Do nothing on client side
        if (server.getEntityWorld().isRemote) {
            return;
        }

        if (!event.getQuestIDs()
            .isEmpty() && event.getType() == QuestEvent.Type.COMPLETED) {

            Set<UUID> quests = event.getQuestIDs();

            EntityPlayer player = GetPlayer(event.getPlayerID());

            if (player == null) {
                EmptyHorizons.LOG.warn(
                    "No player found with UUID: {}",
                    event.getPlayerID()
                        .toString());
                return;
            }

            ExtendedEmptyHorizonsPlayer moddedPlayer = (ExtendedEmptyHorizonsPlayer) player
                .getExtendedProperties(ExtendedEmptyHorizonsPlayer.EXT_PROP_NAME);

            for (UUID currentQuestUUID : quests) {
                // Find the Quest and QuestLine for the completed quests
                QuestInstance currentQuest = (QuestInstance) questDB.get(currentQuestUUID);
                List<String> currentQuestLineNames = FindQuestLineNames(currentQuestUUID);

                if (currentQuest != null) {
                    moddedPlayer.giveQuestReward(currentQuest, currentQuestLineNames);
                } else {
                    EmptyHorizons.LOG.warn("No quest found with UUID: {}", questDB.get(currentQuestUUID));
                }
            }
        }
    }

    private List<String> FindQuestLineNames(UUID questUUID) {
        List<Map.Entry<UUID, IQuestLine>> questLines = questLineDB.getOrderedEntries();

        List<String> questLineNames = new ArrayList<>();

        for (Map.Entry<UUID, IQuestLine> entry : questLines) {
            QuestLine currentQuestline = (QuestLine) entry.getValue();
            QuestLineEntry foundQuest = (QuestLineEntry) currentQuestline.get(questUUID);

            if (foundQuest != null) {
                questLineNames.add(currentQuestline.getProperty(NativeProps.NAME));
            }
        }

        return questLineNames;
    }

    private EntityPlayer GetPlayer(UUID playerUUID) {
        for (WorldServer world : server.worldServers) {
            EntityPlayer player = world.func_152378_a(playerUUID);

            if (player != null) {
                return player;
            }
        }

        return null;
    }
}
