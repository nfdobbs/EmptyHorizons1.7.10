package com.nfdobbs.emptyhorizons.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.nfdobbs.emptyhorizons.EmptyHorizons;

public class WelcomeGUI extends GuiScreen {

    private static ResourceLocation welcomeGui;
    private GuiButton vanillaButton;
    private GuiButton challengeButton;

    private static final int WELCOME_GUI_HEIGHT = 256;
    private static final int WELCOME_GUI_WIDTH = 256;
    private static final int WELCOME_GUI_VISIBLE_WIDTH = 219;
    private static final int WELCOME_GUI_VISIBLE_HEIGHT = 230;
    private static final int WELCOME_GUI_EMPTY_OFFSET_HEIGHT = 18;
    private static final int WELCOME_GUI_EMPTY_OFFSET_WIDTH = 13;
    private static final int WELCOME_GUI_TEXT_OFFSET_HEIGHT = 12;
    private static final int WELCOME_GUI_TEXT_OFFSET_WIDTH = 25;

    private static final int WELCOME_GUI_BUTTON_WIDTH = 104;
    private static final int WELCOME_GUI_BUTTON_OFFSET_WIDTH = 8;
    private static final int WELCOME_GUI_BUTTON_MIDDLE_WIDTH = 6;
    private static final int WELCOME_GUI_BUTTON_OFFSET_BOTTOM = 22;

    public WelcomeGUI() {}

    public void initGui() {
        buttonList.clear();

        vanillaButton = makeVanillaButton();
        buttonList.add(vanillaButton);

        challengeButton = makeChallengeButton();
        buttonList.add(challengeButton);

        welcomeGui = new ResourceLocation(EmptyHorizons.MODID + ":textures/gui/WelcomeUI.png");
    }

    public void updateScreen() {

    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    protected void actionPerformed(GuiButton button) {}

    public void drawScreen(int parWidth, int parHeight, float p_73863_3_) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        mc.getTextureManager()
            .bindTexture(welcomeGui);

        Minecraft minecraft = Minecraft.getMinecraft();

        ScaledResolution sr = new ScaledResolution(minecraft, minecraft.displayWidth, minecraft.displayHeight);
        int xOffset = (width - WELCOME_GUI_WIDTH) / 2;
        int yOffset = (height - WELCOME_GUI_HEIGHT) / 2;

        drawTexturedModalRect(xOffset, yOffset, 0, 0, WELCOME_GUI_WIDTH, WELCOME_GUI_HEIGHT);

        fontRendererObj.drawString(
            "Welcome! Please Choose a Playstyle:",
            xOffset + WELCOME_GUI_EMPTY_OFFSET_WIDTH + WELCOME_GUI_TEXT_OFFSET_WIDTH,
            yOffset + WELCOME_GUI_EMPTY_OFFSET_HEIGHT + WELCOME_GUI_TEXT_OFFSET_HEIGHT,
            0);

        super.drawScreen(parWidth, parHeight, p_73863_3_);

    }

    private GuiButton makeVanillaButton() {
        Minecraft minecraft = Minecraft.getMinecraft();

        ScaledResolution sr = new ScaledResolution(minecraft, minecraft.displayWidth, minecraft.displayHeight);
        int xOffset = (width - WELCOME_GUI_WIDTH) / 2;
        int yOffset = (height - WELCOME_GUI_HEIGHT) / 2;

        int buttonYOffset = WELCOME_GUI_VISIBLE_HEIGHT - WELCOME_GUI_TEXT_OFFSET_HEIGHT
            - WELCOME_GUI_BUTTON_OFFSET_BOTTOM;

        int btnWidth = WELCOME_GUI_BUTTON_WIDTH;
        int btnHeight = 20;
        int xPosition = xOffset + WELCOME_GUI_EMPTY_OFFSET_WIDTH + WELCOME_GUI_BUTTON_OFFSET_WIDTH;
        int yPosition = yOffset + buttonYOffset + WELCOME_GUI_EMPTY_OFFSET_HEIGHT - btnHeight;

        String text = "Vanilla";

        return new GuiButton(0, xPosition, yPosition, btnWidth, btnHeight, text);
    }

    private GuiButton makeChallengeButton() {
        Minecraft minecraft = Minecraft.getMinecraft();

        ScaledResolution sr = new ScaledResolution(minecraft, minecraft.displayWidth, minecraft.displayHeight);
        int xOffset = (width - WELCOME_GUI_WIDTH) / 2;
        int yOffset = (height - WELCOME_GUI_HEIGHT) / 2;

        int buttonYOffset = WELCOME_GUI_VISIBLE_HEIGHT - WELCOME_GUI_TEXT_OFFSET_HEIGHT;

        int btnWidth = WELCOME_GUI_BUTTON_WIDTH;
        int btnHeight = 20;
        int xPosition = xOffset + WELCOME_GUI_EMPTY_OFFSET_WIDTH
            + WELCOME_GUI_BUTTON_OFFSET_WIDTH
            + WELCOME_GUI_BUTTON_WIDTH
            + WELCOME_GUI_BUTTON_MIDDLE_WIDTH;
        int yPosition = yOffset + buttonYOffset
            + WELCOME_GUI_EMPTY_OFFSET_HEIGHT
            - btnHeight
            - WELCOME_GUI_BUTTON_OFFSET_BOTTOM;

        String text = "Challenge";

        return new GuiButton(0, xPosition, yPosition, btnWidth, btnHeight, text);
    }

}
