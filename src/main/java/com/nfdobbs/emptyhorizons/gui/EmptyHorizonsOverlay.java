package com.nfdobbs.emptyhorizons.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

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
        Gui.drawRect(10, 20, 30, 40, 0xFF0000FF);
        drawCenteredString(mc.fontRenderer, text, 100, 100, 0xFFFFFF);
    }

    /*
     * public EmptyHorizonsOverlay(Minecraft mc) {
     * ScaledResolution scaled = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
     * int width = scaled.getScaledWidth();
     * int height = scaled.getScaledHeight();
     * }
     */
}
