package com.nfdobbs.emptyhorizons.commands;

import static com.nfdobbs.emptyhorizons.commands.CommandEmptyHorizonsMoveToParty.getPlayer;
import static com.nfdobbs.emptyhorizons.playerdata.ExtendedEmptyHorizonsPlayer.getQuestRewardTime;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import com.nfdobbs.emptyhorizons.playerdata.ExtendedEmptyHorizonsPlayer;
import com.nfdobbs.emptyhorizons.util.TimeString;

import betterquesting.api.api.QuestingAPI;
import betterquesting.api.properties.NativeProps;
import betterquesting.api.questing.IQuestLine;
import betterquesting.questing.QuestDatabase;
import betterquesting.questing.QuestInstance;
import betterquesting.questing.QuestLine;
import betterquesting.questing.QuestLineDatabase;
import betterquesting.questing.QuestLineEntry;
import cpw.mods.fml.common.FMLCommonHandler;

public class CommandEmptyHorizonsRecalcTime implements ICommand {

    private final QuestDatabase questDB = QuestDatabase.INSTANCE;
    private final QuestLineDatabase questLineDB = QuestLineDatabase.INSTANCE;

    @Override
    public String getCommandName() {
        return "eh_recalcTime";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "eh_recalcTime <text>";
    }

    @Override
    public List<String> getCommandAliases() {
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Incorrect number of arguments."));
        } else {
            String partyMemberNameArg = args[0];
            EntityPlayerMP playerFromArg = (EntityPlayerMP) getPlayer(partyMemberNameArg);

            if (playerFromArg == null) {
                sender.addChatMessage(
                    new ChatComponentText(
                        EnumChatFormatting.RED + "Player " + partyMemberNameArg + " could not be found."));
                return;
            }

            int newMaxTime = RecalculateTime(playerFromArg, questDB, questLineDB);

            ExtendedEmptyHorizonsPlayer moddedPlayer = (ExtendedEmptyHorizonsPlayer) playerFromArg
                .getExtendedProperties(ExtendedEmptyHorizonsPlayer.EXT_PROP_NAME);

            if (moddedPlayer.getExpeditionTime() > newMaxTime) {
                moddedPlayer.setExpeditionTime(newMaxTime - 5);
            }

            moddedPlayer.setMaxExpeditionTime(newMaxTime);

            sender.addChatMessage(
                new ChatComponentText(
                    EnumChatFormatting.YELLOW + "Max Time for "
                        + playerFromArg.getDisplayName()
                        + " updated to: "
                        + TimeString.CreateTimeString(newMaxTime)));
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {

        EntityPlayerMP player = (EntityPlayerMP) sender.getEntityWorld()
            .getPlayerEntityByName(sender.getCommandSenderName());

        // Check if player is OP
        return FMLCommonHandler.instance()
            .getMinecraftServerInstance()
            .getConfigurationManager()
            .func_152596_g(player.getGameProfile());
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        List<EntityPlayerMP> playerList = MinecraftServer.getServer()
            .getConfigurationManager().playerEntityList;

        List<String> playerNames = new ArrayList<String>();

        for (EntityPlayerMP player : playerList) {
            ExtendedEmptyHorizonsPlayer modPlayer = (ExtendedEmptyHorizonsPlayer) player
                .getExtendedProperties(ExtendedEmptyHorizonsPlayer.EXT_PROP_NAME);

            if (modPlayer.isDoingChallenge()) {
                playerNames.add(player.getDisplayName());
            }
        }

        return playerNames;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }

    public static int RecalculateTime(EntityPlayerMP player, QuestDatabase questDbInstance,
        QuestLineDatabase questLineDbInstance) {
        List<Map.Entry<UUID, QuestInstance>> completedQuests = CompletedQuests(player, questDbInstance);
        int achievementCount = CompletedAchievements(player);

        int rewardTime = ExtendedEmptyHorizonsPlayer.startingExposureTime;

        for (Map.Entry<UUID, QuestInstance> questMapping : completedQuests) {

            UUID questUUID = questMapping.getKey();
            QuestInstance questInstance = questMapping.getValue();

            List<String> questLineNames = FindQuestLineNames(questUUID, questLineDbInstance);

            rewardTime += getQuestRewardTime(questInstance, questLineNames);
        }

        rewardTime += achievementCount * ExtendedEmptyHorizonsPlayer.achievementRewardTime;

        return rewardTime;
    }

    // Duplicating Function Mainly Because I don't know how to get it in both places rn
    public static List<String> FindQuestLineNames(UUID questUUID, QuestLineDatabase questLineDbInstance) {
        List<Map.Entry<UUID, IQuestLine>> questLines = questLineDbInstance.getOrderedEntries();

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

    public static List<Map.Entry<UUID, QuestInstance>> CompletedQuests(EntityPlayerMP player,
        QuestDatabase questDbInstance) {
        List<Map.Entry<UUID, QuestInstance>> completedQuests = new ArrayList<Map.Entry<UUID, QuestInstance>>();

        for (UUID questID : questDbInstance.keySet()) {
            QuestInstance currentQuest = (QuestInstance) questDbInstance.get(questID);

            if (currentQuest != null && currentQuest.getProperty(NativeProps.REPEAT_TIME) < 0) {
                UUID uuid = QuestingAPI.getQuestingUUID(player);

                if (currentQuest.isComplete(uuid)) {
                    completedQuests.add(new AbstractMap.SimpleEntry<UUID, QuestInstance>(questID, currentQuest));
                }
            }
        }

        return completedQuests;
    }

    public static int CompletedAchievements(EntityPlayerMP player) {
        List<Achievement> fullAchievementList = AchievementList.achievementList;

        List<Achievement> completedAchievement = new ArrayList<Achievement>();

        for (Achievement currentAchievement : fullAchievementList) {
            if (player.func_147099_x()
                .hasAchievementUnlocked(currentAchievement)) {

                completedAchievement.add(currentAchievement);
            }
        }

        return completedAchievement.size();
    }

}
