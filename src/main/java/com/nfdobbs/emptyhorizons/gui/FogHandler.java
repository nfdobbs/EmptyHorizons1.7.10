package com.nfdobbs.emptyhorizons.gui;

import com.nfdobbs.emptyhorizons.EmptyDimension.EmptyDimRegister;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.EntityViewRenderEvent;

import org.lwjgl.opengl.GL11;

import com.nfdobbs.emptyhorizons.ClientProxy;
import com.nfdobbs.emptyhorizons.EmptyHorizons;
import com.nfdobbs.emptyhorizons.playerdata.ExtendedEmptyHorizonsPlayer;
import com.nfdobbs.emptyhorizons.worlddata.FogRecord;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FogHandler {

    // private FogProvider fogProvider = ClientProxy.fogProvider;
    final private Minecraft mc;

    public FogHandler(Minecraft minecraft) {
        mc = minecraft;
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onFog(EntityViewRenderEvent.FogDensity event) {

        ExtendedEmptyHorizonsPlayer player = ExtendedEmptyHorizonsPlayer.get(this.mc.thePlayer);

        if (!player.isDoingChallenge() || event.entity.worldObj.provider.dimensionId == EmptyDimRegister.EMPTY_DIMENSION_ID) {
            if (ClientProxy.fogProvider.showWelcomeMessage && Minecraft.getMinecraft().currentScreen == null) {
                Minecraft.getMinecraft()
                    .displayGuiScreen(new WelcomeGUI());
                ClientProxy.fogProvider.showWelcomeMessage = false;
            }

            return;
        }

        FogRecord fogRecord = ClientProxy.fogProvider
            .GetFogRecord(event.entity.worldObj, event.entity.worldObj.provider.dimensionId);

        // For some reason when we cancel the event we never get back into GL11.GL_EXP mode
        // This seems to be the default mode until the event is not canceled a single time
        // then the mode gets stuck in GL11.GL_LINEAR
        GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);

        if (fogRecord != null) {
            event.density = fogRecord.getFogDensity();
            event.setCanceled(true);
        } else {
            event.density = 1F;
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onFogColor(EntityViewRenderEvent.FogDensity.FogColors event) {

        ExtendedEmptyHorizonsPlayer player = ExtendedEmptyHorizonsPlayer.get(this.mc.thePlayer);

        if (!player.isDoingChallenge() || event.entity.worldObj.provider.dimensionId == EmptyDimRegister.EMPTY_DIMENSION_ID
            || IsTransparent(event)) {
            return;
        }

        FogRecord fogRecord = ClientProxy.fogProvider
            .GetFogRecord(event.entity.worldObj, event.entity.worldObj.provider.dimensionId);

        /*
         * event.red = .21159399F;
         * event.green = .9669117F;
         * event.blue =
         */

        if (fogRecord != null) {
            event.red = fogRecord.getFogColorR();
            event.green = fogRecord.getFogColorG();
            event.blue = fogRecord.getFogColorB();
        } else {
            event.red = .99F;
            event.green = .99F;
            event.blue = .99F;
        }
    }

    private boolean IsTransparent(EntityViewRenderEvent event) {
        int x = (int) event.entity.posX;
        int y = (int) event.entity.posY;
        int z = (int) event.entity.posZ;

        Material material = event.entity.worldObj.getBlock(x, y, z)
            .getMaterial();

        return material.isLiquid();
    }
}
