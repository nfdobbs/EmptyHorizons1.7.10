package com.nfdobbs.emptyhorizons.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import com.nfdobbs.emptyhorizons.ClientProxy;
import com.nfdobbs.emptyhorizons.Config;
import com.nfdobbs.emptyhorizons.EmptyDimension.EmptyDimRegister;
import com.nfdobbs.emptyhorizons.playerdata.ExtendedEmptyHorizonsPlayer;
import com.nfdobbs.emptyhorizons.worlddata.FogRecord;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class DebugHandler {

    private static final String MOD_PREFIX = EnumChatFormatting.BLUE + "[EmptyHorizons]" + EnumChatFormatting.RESET;

    @SubscribeEvent
    public void onDrawDebugText(RenderGameOverlayEvent.Text event) {

        Minecraft minecraft = Minecraft.getMinecraft();

        if (minecraft.gameSettings.showDebugInfo) {
            event.left.add(null);

            ExtendedEmptyHorizonsPlayer player = ExtendedEmptyHorizonsPlayer.get(minecraft.thePlayer);
            int dimId = minecraft.thePlayer.dimension;

            if (player.isDoingChallenge() && dimId != EmptyDimRegister.EMPTY_DIMENSION_ID
                && !Config.safeDimensions.containsValue(dimId)) {

                event.left.add(MOD_PREFIX);

                FogRecord fogRecord = ClientProxy.fogProvider
                    .GetFogRecord(minecraft.theWorld, minecraft.thePlayer.dimension);

                if (GuiScreen.isCtrlKeyDown() && GuiScreen.isShiftKeyDown()) {
                    event.left.add("Fog Color");
                    event.left.add("R: " + fogRecord.getFogColorR());
                    event.left.add("G: " + fogRecord.getFogColorG());
                    event.left.add("B: " + fogRecord.getFogColorB());
                    event.left.add("D: " + fogRecord.getFogDensity());
                } else {
                    event.left.add(
                        "Fog Color: (" + FogRound(fogRecord.getFogColorR())
                            + ", "
                            + FogRound(fogRecord.getFogColorG())
                            + ", "
                            + FogRound(fogRecord.getFogColorB())
                            + ", "
                            + FogRound(fogRecord.getFogDensity())
                            + ")...");
                }
            } else {
                event.left.add(MOD_PREFIX + " Safe Dimension");
            }
        }
    }

    private float FogRound(float value) {
        return (float) Math.round(value * 100f) / 100f;
    }
}
