package com.nfdobbs.emptyhorizons.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

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
        String maxExpeditionTimeString = "Max Expedition Time: " + TimeString.CreateTimeString(maxExpeditionTime);

        EnumChatFormatting color = EnumChatFormatting.WHITE;

        if (expeditionTime < 300 && expeditionTime > 60) {
            color = EnumChatFormatting.YELLOW;
        } else if (expeditionTime < 60) {
            color = EnumChatFormatting.RED;
        }

        expeditionTimeString = color + expeditionTimeString;

        drawString(mc.fontRenderer, "Lethal Exposure: " + expeditionTimeString, 2, 2, 0xFFFFFF);

        // drawString(mc.fontRenderer, maxExpeditionTimeString, 2, 15, 0xFF5555);
    }
}
