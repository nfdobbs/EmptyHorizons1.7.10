package com.nfdobbs.emptyhorizons.playerdata;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.IExtendedEntityProperties;

import com.nfdobbs.emptyhorizons.CommonProxy;
import com.nfdobbs.emptyhorizons.Config;
import com.nfdobbs.emptyhorizons.EmptyDimension.EmptyDimRegister;
import com.nfdobbs.emptyhorizons.EmptyHorizons;
import com.nfdobbs.emptyhorizons.network.SyncMessage;

import betterquesting.api.properties.NativeProps;
import betterquesting.questing.QuestInstance;

public class ExtendedEmptyHorizonsPlayer implements IExtendedEntityProperties {

    public final static String EXT_PROP_NAME = "ExtendedEmptyHorizonsPlayer";
    public final static String MAX_EXPEDITION_TIME_KEY = "MaxExpeditionTime";
    public final static String CURRENT_EXPEDITION_TIME_KEY = "CurrentExpeditionTime";
    public final static String DOING_CHALLENGE_KEY = "DoingChallenge";
    public final static String HAS_CHOSEN_PLAYSTYLE_KEY = "HasChosenPlaystyle";
    public final static String HAS_USED_FREE_PARTY_TP_KEY = "HasUsedFreePartyTP";

    private final EntityPlayer player;

    public final static int CURRENT_EXPEDITION_TIME_WATCHER = 24;
    public final static int startingExposureTime = Config.startingExposureTime;
    private int maxExpeditionTime = startingExposureTime;
    private final static int mainQuestRewardTime = Config.baseMainQuestReward;
    public final static int achievementRewardTime = Config.achievementReward;
    private final static int optionalQuestRewardTime = Config.baseOptionalQuestReward;
    private boolean doingChallenge = false;
    private boolean hasChosenPlaystyle = false;
    private boolean hasUsedFreePartyTP = false;

    public ExtendedEmptyHorizonsPlayer(EntityPlayer player) {
        this.player = player;

        this.player.getDataWatcher()
            .addObject(CURRENT_EXPEDITION_TIME_WATCHER, maxExpeditionTime);
    }

    public static void register(EntityPlayer player) {
        player.registerExtendedProperties(EXT_PROP_NAME, new ExtendedEmptyHorizonsPlayer(player));
    }

    public static ExtendedEmptyHorizonsPlayer get(EntityPlayer player) {
        return (ExtendedEmptyHorizonsPlayer) player.getExtendedProperties(EXT_PROP_NAME);
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        NBTTagCompound properties = new NBTTagCompound();

        // Save Values
        properties.setInteger(MAX_EXPEDITION_TIME_KEY, maxExpeditionTime);

        properties.setInteger(
            CURRENT_EXPEDITION_TIME_KEY,
            this.player.getDataWatcher()
                .getWatchableObjectInt(CURRENT_EXPEDITION_TIME_WATCHER));

        properties.setBoolean(DOING_CHALLENGE_KEY, doingChallenge);
        properties.setBoolean(HAS_CHOSEN_PLAYSTYLE_KEY, hasChosenPlaystyle);
        properties.setBoolean(HAS_USED_FREE_PARTY_TP_KEY, hasUsedFreePartyTP);

        // Use unique tags to avoid conflicts
        compound.setTag(EXT_PROP_NAME, properties);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        NBTTagCompound properties = compound.getCompoundTag(EXT_PROP_NAME);

        setExpeditionTime(properties.getInteger(CURRENT_EXPEDITION_TIME_KEY));

        maxExpeditionTime = properties.getInteger(MAX_EXPEDITION_TIME_KEY);

        doingChallenge = properties.getBoolean(DOING_CHALLENGE_KEY);

        hasChosenPlaystyle = properties.getBoolean(HAS_CHOSEN_PLAYSTYLE_KEY);

        hasUsedFreePartyTP = properties.getBoolean(HAS_USED_FREE_PARTY_TP_KEY);
    }

    private static String getSaveKey(EntityPlayer player) {
        return player.getDisplayName() + ":" + EXT_PROP_NAME;
    }

    public static void saveProxyData(EntityPlayer player) {
        ExtendedEmptyHorizonsPlayer playerData = ExtendedEmptyHorizonsPlayer.get(player);

        NBTTagCompound savedData = new NBTTagCompound();
        playerData.saveNBTData(savedData);
        CommonProxy.storeEntityData(getSaveKey(player), savedData);
    }

    public static void loadProxyData(EntityPlayer player) {
        ExtendedEmptyHorizonsPlayer playerData = ExtendedEmptyHorizonsPlayer.get(player);

        NBTTagCompound savedData = CommonProxy.getEntityData(getSaveKey(player));

        if (savedData != null) {
            playerData.loadNBTData(savedData);
            playerData.sync();
        } else {
            playerData.sync();
        }
    }

    public int getExpeditionTime() {
        return this.player.getDataWatcher()
            .getWatchableObjectInt(CURRENT_EXPEDITION_TIME_WATCHER);
    }

    public void setExpeditionTime(int expeditionTime) {
        this.player.getDataWatcher()
            .updateObject(CURRENT_EXPEDITION_TIME_WATCHER, expeditionTime);
    }

    public int getMaxExpeditionTime() {
        return maxExpeditionTime;
    }

    public void setMaxExpeditionTime(int maxExpeditionTime) {
        this.maxExpeditionTime = maxExpeditionTime;
        sync();
    }

    public boolean isDoingChallenge() {
        return doingChallenge;
    }

    public boolean hasSetHasChosenPlaystyle() {
        return hasChosenPlaystyle;
    }

    public void setDoingChallenge(boolean isDoingChallenge) {
        EmptyHorizons.LOG.info("Setting Challenge");
        this.doingChallenge = isDoingChallenge;
        sync();
    }

    public void setHasChosenPlaystyle(boolean hasChosenPlaystyle) {
        this.hasChosenPlaystyle = hasChosenPlaystyle;
        sync();
    }

    public void setHasUsedFreePartyTP(boolean hasUsedFreePartyTP) {
        this.hasUsedFreePartyTP = hasUsedFreePartyTP;
        sync();
    }

    public boolean getHasUsedFreePartyTP() {
        return hasUsedFreePartyTP;
    }

    public void giveQuestReward(QuestInstance quest, List<String> questLineNames) {

        int rewardTime = getQuestRewardTime(quest, questLineNames);

        if (isDoingChallenge()) {
            EntityPlayerMP playerMP = (EntityPlayerMP) player;
            playerMP.addChatMessage(
                new ChatComponentText(EnumChatFormatting.GREEN + "Quest Complete! " + rewardTime + " seconds earned."));
        }

        setMaxExpeditionTime(maxExpeditionTime + rewardTime);
    }

    public static int getQuestRewardTime(QuestInstance quest, List<String> questLineNames) {
        int questBaseTime = 0;

        if (quest.getProperty(NativeProps.MAIN)) {
            questBaseTime = mainQuestRewardTime;
        } else {
            questBaseTime = optionalQuestRewardTime;
        }

        float questMultiplier = getQuestMultiplier(questLineNames);
        return Math.round(questBaseTime * questMultiplier);
    }

    public void giveAchievementReward() {
        setMaxExpeditionTime(maxExpeditionTime + achievementRewardTime);

        if (isDoingChallenge()) {
            EntityPlayerMP playerMP = (EntityPlayerMP) player;
            playerMP.addChatMessage(
                new ChatComponentText(
                    EnumChatFormatting.GREEN + "Achievement Complete! " + achievementRewardTime + " seconds earned."));
        }
    }

    private static float getQuestMultiplier(List<String> questLineNames) {
        float questMultiplier = 0.0f;

        for (String questLineName : questLineNames) {
            float multiplier = 0.0f;

            if (Config.questLineMultipliers.containsKey(questLineName)) {
                multiplier = Config.questLineMultipliers.get(questLineName);

                if (multiplier > questMultiplier) {
                    questMultiplier = multiplier;
                }
            }
        }

        if (questMultiplier == 0.0f) {
            questMultiplier = 1.0f;
        }

        return questMultiplier;
    }

    public void sync() {
        if (!player.worldObj.isRemote) {
            int expeditionTime = getExpeditionTime();

            CommonProxy.networkWrapper.sendTo(
                new SyncMessage(
                    maxExpeditionTime,
                    expeditionTime,
                    doingChallenge,
                    hasChosenPlaystyle,
                    hasUsedFreePartyTP),
                (EntityPlayerMP) player);
        }
    }

    @Override
    public void init(Entity entity, World world) {

    }

    public static boolean IsSafe(ExtendedEmptyHorizonsPlayer player, WorldProvider worldProvider) {
        boolean playerNotDoingChallenge = false;
        boolean dimensionIdMatch = false;

        playerNotDoingChallenge = !player.isDoingChallenge();

        if (playerNotDoingChallenge) {
            return true;
        }

        int dimensionId = worldProvider.dimensionId;
        String dimensionName = worldProvider.getDimensionName();

        dimensionIdMatch = dimensionId == EmptyDimRegister.EMPTY_DIMENSION_ID
            || Config.safeDimensions.containsValue(dimensionId);

        if (dimensionIdMatch) {
            return true;
        }

        for (String safeDimName : Config.safeFuzzyDimNames) {
            if (dimensionName.toUpperCase()
                .contains(safeDimName.toUpperCase())) {
                return true;
            }
        }

        return false;
    }
}
