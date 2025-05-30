package com.nfdobbs.emptyhorizons.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import com.nfdobbs.emptyhorizons.ClientProxy;
import com.nfdobbs.emptyhorizons.playerdata.ExtendedEmptyHorizonsPlayer;
import com.nfdobbs.emptyhorizons.util.TimeString;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EmptyHorizonsOverlay extends Gui {

    private final Minecraft mc;

    @SideOnly(Side.CLIENT)
    public EmptyHorizonsOverlay(Minecraft mc) {
        super();
        this.mc = mc;
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onRenderText(RenderGameOverlayEvent.Post event) {

        if (event.isCanceled() || mc.gameSettings.showDebugInfo
            || event.type != RenderGameOverlayEvent.ElementType.TEXT) {
            return;
        }

        ExtendedEmptyHorizonsPlayer player = ExtendedEmptyHorizonsPlayer.get(this.mc.thePlayer);

        if (!player.isDoingChallenge()) {
            return;
        }

        int expeditionTime = player.getExpeditionTime();
        int maxExpeditionTime = player.getMaxExpeditionTime();

        if (expeditionTime < 0) {
            expeditionTime = 1;
        }

        String expeditionTimeString = TimeString.CreateTimeString(expeditionTime);

        EnumChatFormatting color = EnumChatFormatting.WHITE;

        if (expeditionTime < 300 && expeditionTime > 60) {
            color = EnumChatFormatting.YELLOW;
        } else if (expeditionTime < 60) {
            color = EnumChatFormatting.RED;
        }

        expeditionTimeString = color + expeditionTimeString;

        // Check for dimension multiplier
        int dimId = this.mc.thePlayer.dimension;
        float dimMultiplier = 1.0f;

        if (ClientProxy.serverDimensionMultipliers.containsKey(dimId)) {
            dimMultiplier = ClientProxy.serverDimensionMultipliers.get(dimId);
        }

        String displayString = "Lethal Exposure: " + expeditionTimeString;

        if (dimMultiplier != 1.0f) {
            displayString += " (" + dimMultiplier + "x)";
        }

        drawString(mc.fontRenderer, displayString, 2, 2, 0xFFFFFF);
    }
}
