package com.nfdobbs.emptyhorizons.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.nfdobbs.emptyhorizons.CommonProxy;
import com.nfdobbs.emptyhorizons.EmptyHorizons;
import com.nfdobbs.emptyhorizons.network.TravelButtonMessage;
import com.nfdobbs.emptyhorizons.tileentities.TileEntityExcursionBlock;
import com.nfdobbs.emptyhorizons.util.ExcursionCoords;

public class ExcursionBlockGUI extends GuiScreen {

    private static ResourceLocation blankGui;
    private GuiButton travelButton;
    private ExcursionCoords travelCoords;

    private static final int BLANK_GUI_HEIGHT = 256;
    private static final int BLANK_GUI_WIDTH = 256;
    private static final int BLANK_GUI_VISIBLE_WIDTH = 176;
    private static final int BLANK_GUI_VISIBLE_HEIGHT = 133;
    private static final int BLANK_GUI_EMPTY_OFFSET_HEIGHT = 60;
    private static final int BLANK_GUI_EMPTY_OFFSET_WIDTH = 40;
    private static final int BLANK_GUI_TEXT_OFFSET_HEIGHT = 15;
    private static final int BLANK_GUI_TEXT_OFFSET_WIDTH = 15;

    public void initGui() {
        buttonList.clear();

        travelButton = makeTravelButton();
        buttonList.add(travelButton);

        blankGui = new ResourceLocation(EmptyHorizons.MODID + ":textures/gui/BlankUI.png");
    }

    public void updateScreen() {

    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    protected void actionPerformed(GuiButton button) {
        if (button == travelButton) {
            EmptyHorizons.LOG.info("Button Clicked");

            CommonProxy.networkWrapper.sendToServer(new TravelButtonMessage(travelCoords));
        }
    }

    public void drawScreen(int parWidth, int parHeight, float p_73863_3_) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        mc.getTextureManager()
            .bindTexture(blankGui);

        Minecraft minecraft = Minecraft.getMinecraft();

        ScaledResolution sr = new ScaledResolution(minecraft, minecraft.displayWidth, minecraft.displayHeight);
        int xOffset = (width - BLANK_GUI_WIDTH) / 2;
        int yOffset = (height - BLANK_GUI_HEIGHT) / 2;

        drawTexturedModalRect(xOffset, yOffset, 0, 0, BLANK_GUI_WIDTH, BLANK_GUI_HEIGHT);

        double days = minecraft.theWorld.getTotalWorldTime() / 24000.00;
        travelCoords = TileEntityExcursionBlock.getExcursionCoords(days);

        String textCoords = String.format("Location: X: %d, Z: %d", travelCoords.x, travelCoords.z);
        int coordsWidth = fontRendererObj.getStringWidth(textCoords);

        fontRendererObj.drawString(
            textCoords,
            xOffset + BLANK_GUI_EMPTY_OFFSET_WIDTH + BLANK_GUI_TEXT_OFFSET_WIDTH,
            yOffset + BLANK_GUI_EMPTY_OFFSET_HEIGHT + BLANK_GUI_TEXT_OFFSET_HEIGHT,
            0);

        super.drawScreen(parWidth, parHeight, p_73863_3_);
    }

    private GuiButton makeTravelButton() {
        Minecraft minecraft = Minecraft.getMinecraft();

        ScaledResolution sr = new ScaledResolution(minecraft, minecraft.displayWidth, minecraft.displayHeight);
        int xOffset = (width - BLANK_GUI_WIDTH) / 2;
        int yOffset = (height - BLANK_GUI_HEIGHT) / 2;

        int buttonYOffset = BLANK_GUI_VISIBLE_HEIGHT - BLANK_GUI_TEXT_OFFSET_HEIGHT;

        int btnWidth = BLANK_GUI_VISIBLE_WIDTH - BLANK_GUI_TEXT_OFFSET_WIDTH * 2;
        int btnHeight = 20;
        int xPosition = xOffset + BLANK_GUI_EMPTY_OFFSET_WIDTH + BLANK_GUI_TEXT_OFFSET_WIDTH;
        int yPosition = yOffset + buttonYOffset + BLANK_GUI_EMPTY_OFFSET_HEIGHT - btnHeight;

        String text = "Travel";

        return new GuiButton(0, xPosition, yPosition, btnWidth, btnHeight, text);
    }

}
