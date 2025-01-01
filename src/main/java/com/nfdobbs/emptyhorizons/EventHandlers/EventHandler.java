package com.nfdobbs.emptyhorizons.EventHandlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AchievementEvent;

import com.nfdobbs.emptyhorizons.CommonProxy;
import com.nfdobbs.emptyhorizons.EmptyHorizons;
import com.nfdobbs.emptyhorizons.Helpers;
import com.nfdobbs.emptyhorizons.network.ShowWelcomeGuiMessage;
import com.nfdobbs.emptyhorizons.playerdata.ExtendedEmptyHorizonsPlayer;
import com.nfdobbs.emptyhorizons.worlddata.FogProvider;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EventHandler {

    @SubscribeEvent
    public void onEntityConstructing(EntityEvent.EntityConstructing event) {
        if (event.entity instanceof EntityPlayer
            && ExtendedEmptyHorizonsPlayer.get((EntityPlayer) event.entity) == null) {

            EmptyHorizons.LOG.info("Empty Horizons Registering Player");

            ExtendedEmptyHorizonsPlayer.register((EntityPlayer) event.entity);
        }
    }

    @SubscribeEvent
    public void onAchievement(AchievementEvent event) {
        if (!event.entity.worldObj.isRemote) {
            ExtendedEmptyHorizonsPlayer modPlayer = (ExtendedEmptyHorizonsPlayer) ((EntityPlayer) event.entity)
                .getExtendedProperties(ExtendedEmptyHorizonsPlayer.EXT_PROP_NAME);

            EntityPlayerMP player = (EntityPlayerMP) event.entityPlayer;

            if (player.func_147099_x()
                .canUnlockAchievement(event.achievement)) {
                if (player.func_147099_x()
                    .hasAchievementUnlocked(event.achievement)) {
                    return;
                }

                if (modPlayer.isDoingChallenge() && event.achievement.isAchievement()) {
                    modPlayer.giveAchievementReward();

                    EmptyHorizons.LOG.info(
                        "Giving achievement reward to " + player.getDisplayName()
                            + " for achievement: '"
                            + event.achievement.statId
                            + "'");
                }
            }
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {

        if (event.entity instanceof EntityPlayer) {
            ExtendedEmptyHorizonsPlayer modPlayer = (ExtendedEmptyHorizonsPlayer) ((EntityPlayer) event.entity)
                .getExtendedProperties(ExtendedEmptyHorizonsPlayer.EXT_PROP_NAME);

            if (!event.entity.worldObj.isRemote) {
                // Setup persistent player data
                ExtendedEmptyHorizonsPlayer.loadProxyData((EntityPlayer) event.entity);

                if (!modPlayer.hasSetHasChosenPlaystyle()) {
                    CommonProxy.networkWrapper.sendTo(new ShowWelcomeGuiMessage(), (EntityPlayerMP) event.entity);
                }

                FogProvider serverFogProvider = new FogProvider();
                serverFogProvider.GetFogRecord(event.entity.worldObj, event.entity.dimension);

                serverFogProvider.SyncFogData((EntityPlayerMP) event.entity);
            }
        }
    }

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        if (Helpers.IsServerSide(event) && Helpers.IsPlayerEvent(event)) {
            NBTTagCompound modPlayerData = new NBTTagCompound();
            EntityPlayer player = (EntityPlayer) event.entity;

            ExtendedEmptyHorizonsPlayer modPlayer = (ExtendedEmptyHorizonsPlayer) player
                .getExtendedProperties(ExtendedEmptyHorizonsPlayer.EXT_PROP_NAME);
            modPlayer.saveNBTData(modPlayerData);

            // Call Save Proxy Data
            ExtendedEmptyHorizonsPlayer.saveProxyData(player);

            if (modPlayer.getExpeditionTime() < 1) {
                modPlayer.setExpeditionTime(1);
            }
        }
    }
}
