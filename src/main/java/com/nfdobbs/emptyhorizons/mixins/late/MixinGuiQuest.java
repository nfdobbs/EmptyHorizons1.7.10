package com.nfdobbs.emptyhorizons.mixins.late;

import static com.nfdobbs.emptyhorizons.playerdata.ExtendedEmptyHorizonsPlayer.getQuestRewardTime;

import java.util.List;
import java.util.UUID;

import net.minecraft.client.Minecraft;

import org.lwjgl.util.vector.Vector4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;
import com.nfdobbs.emptyhorizons.playerdata.ExtendedEmptyHorizonsPlayer;
import com.nfdobbs.emptyhorizons.util.BetterQuestingHelpers;
import com.nfdobbs.emptyhorizons.util.TimeString;

import betterquesting.api2.client.gui.misc.GuiPadding;
import betterquesting.api2.client.gui.misc.GuiTransform;
import betterquesting.api2.client.gui.panels.CanvasTextured;
import betterquesting.api2.client.gui.panels.content.PanelTextBox;
import betterquesting.api2.client.gui.themes.presets.PresetColor;
import betterquesting.client.gui2.GuiQuest;
import betterquesting.questing.QuestDatabase;
import betterquesting.questing.QuestInstance;
import betterquesting.questing.QuestLineDatabase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin(GuiQuest.class)
public class MixinGuiQuest {

    @Unique
    private PanelTextBox timeReward;

    @Unique
    private final QuestLineDatabase questLineDB = QuestLineDatabase.INSTANCE;

    @Unique
    private final QuestDatabase questDB = QuestDatabase.INSTANCE;

    @Final
    @Shadow(remap = false)
    private UUID questID;

    @Inject(method = "initPanel", at = @At(value = "TAIL"), remap = false)
    private void addTimeBonus(CallbackInfo ci, @Local(ordinal = 0) CanvasTextured cvBackground) {

        ExtendedEmptyHorizonsPlayer player = ExtendedEmptyHorizonsPlayer.get(Minecraft.getMinecraft().thePlayer);

        if (!player.isDoingChallenge()) {
            return;
        }

        List<String> questLineNames = BetterQuestingHelpers.FindQuestLineNames(questID, questLineDB);

        int rewardTime = getQuestRewardTime((QuestInstance) questDB.get(questID), questLineNames);

        String timeRewardText = "+" + TimeString.CreatePrettyTimeString(rewardTime);

        final int BORDER_PADDING = 9;
        int timeProgressLength = (Minecraft.getMinecraft().fontRenderer.getStringWidth(timeRewardText) + BORDER_PADDING)
            * -1;

        timeReward = new PanelTextBox(
            new GuiTransform(new Vector4f(1F, 0F, 1F, 0F), new GuiPadding(timeProgressLength, 16, 0, -32), 0),
            timeRewardText);
        timeReward.setColor(PresetColor.TEXT_HEADER.getColor());

        cvBackground.addPanel(timeReward);
    }
}
