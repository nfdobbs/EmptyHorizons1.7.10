package com.nfdobbs.emptyhorizons.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import com.nfdobbs.emptyhorizons.playerdata.ExtendedEmptyHorizonsPlayer;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EmptyHorizonsOverlay extends Gui {

    private final Minecraft mc;

    public EmptyHorizonsOverlay(Minecraft mc) {
        super();
        this.mc = mc;
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onRenderExperience(RenderGameOverlayEvent.Post event) {
        if (event.isCanceled() || event.type != RenderGameOverlayEvent.ElementType.EXPERIENCE) {
            return;
        }

        ExtendedEmptyHorizonsPlayer player = ExtendedEmptyHorizonsPlayer.get(this.mc.thePlayer);

        int expeditionTime = player.getExpeditionTime();
        int maxExpeditionTime = player.getMaxExpeditionTime();

        if (expeditionTime < 0) {
            expeditionTime = 1;
        }

        String expeditionTimeString = CreateTimeString(expeditionTime);
        String maxExpeditionTimeString = "Max Expedition Time: " + CreateTimeString(maxExpeditionTime);

        EnumChatFormatting color = EnumChatFormatting.WHITE;

        if (expeditionTime < 300 && expeditionTime > 60) {
            color = EnumChatFormatting.YELLOW;
        } else if (expeditionTime < 60) {
            color = EnumChatFormatting.RED;
        }

        expeditionTimeString = color + expeditionTimeString;

        drawString(mc.fontRenderer, "Lethal Exposure: " + expeditionTimeString, 2, 2, 0xFFFFFF);

        drawString(mc.fontRenderer, maxExpeditionTimeString, 2, 15, 0xFF5555);
    }

    private String CreateTimeString(int timeInSeconds) {
        int minutes = timeInSeconds / 60;
        int seconds = timeInSeconds % 60;

        int hours = minutes / 60;
        minutes = minutes % 60;

        String outputTime = "";
        if (hours > 0) {
            outputTime = String.format("%d:%02d:%02d", hours, minutes, seconds);
        } else if (minutes > 0) {
            outputTime = String.format("%d:%02d", minutes, seconds);
        } else {
            outputTime = String.format("%d", seconds);
        }

        return outputTime;
    }
}
