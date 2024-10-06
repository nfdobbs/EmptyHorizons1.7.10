package com.nfdobbs.emptyhorizons.playerdata;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

import com.nfdobbs.emptyhorizons.CommonProxy;
import com.nfdobbs.emptyhorizons.network.SyncMessage;

public class ExtendedEmptyHorizonsPlayer implements IExtendedEntityProperties {

    public final static String EXT_PROP_NAME = "ExtendedEmptyHorizonsPlayer";
    public final static String MAX_EXPEDITION_TIME_KEY = "MaxExpeditionTime";
    public final static String CURRENT_EXPEDITION_TIME_KEY = "CurrentExpeditionTime";
    public final static String DOING_CHALLENGE_KEY = "DoingChallenge";

    private final EntityPlayer player;

    public final static int CURRENT_EXPEDITION_TIME_WATCHER = 24;
    private int maxExpeditionTime = 60;
    private boolean doingChallenge = false;

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

        // Use unique tags to avoid conflicts
        compound.setTag(EXT_PROP_NAME, properties);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        NBTTagCompound properties = compound.getCompoundTag(EXT_PROP_NAME);

        setExpeditionTime(properties.getInteger(CURRENT_EXPEDITION_TIME_KEY));
        setMaxExpeditionTime(properties.getInteger(MAX_EXPEDITION_TIME_KEY));

        doingChallenge = properties.getBoolean(DOING_CHALLENGE_KEY);
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

    public void debugMessage() {
        int expeditionTime = getExpeditionTime();

        System.out.println("Expedition time: " + expeditionTime + " Max Expedition time: " + maxExpeditionTime);
        System.out.println("Challenge: " + doingChallenge);
    }

    public void sync() {
        if (!player.worldObj.isRemote) {
            int expeditionTime = getExpeditionTime();

            CommonProxy.networkWrapper
                .sendTo(new SyncMessage(maxExpeditionTime, expeditionTime), (EntityPlayerMP) player);
        }
    }

    @Override
    public void init(Entity entity, World world) {

    }
}
