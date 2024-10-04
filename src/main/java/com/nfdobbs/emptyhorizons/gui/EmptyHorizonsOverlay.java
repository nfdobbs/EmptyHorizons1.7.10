package com.nfdobbs.emptyhorizons.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import com.nfdobbs.emptyhorizons.playerdata.ExtendedEmptyHorizonsPlayer;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EmptyHorizonsOverlay extends Gui {

    String text = "Does this work?";
    private Minecraft mc;

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

        drawString(mc.fontRenderer, String.valueOf(expeditionTime), 0, 0, 0xFFFFFF);
    }

    /*
     * public EmptyHorizonsOverlay(Minecraft mc) {
     * ScaledResolution scaled = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
     * int width = scaled.getScaledWidth();
     * int height = scaled.getScaledHeight();
     * }
     */
}
