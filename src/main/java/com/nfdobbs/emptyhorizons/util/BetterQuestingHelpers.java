package com.nfdobbs.emptyhorizons.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import betterquesting.api.properties.NativeProps;
import betterquesting.api.questing.IQuestLine;
import betterquesting.questing.QuestLine;
import betterquesting.questing.QuestLineDatabase;
import betterquesting.questing.QuestLineEntry;

public class BetterQuestingHelpers {

    public static List<String> FindQuestLineNames(UUID questUUID, QuestLineDatabase questLineDB) {
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

}
