package com.nfdobbs.emptyhorizons.mixins.early;

import static com.nfdobbs.emptyhorizons.playerdata.ExtendedEmptyHorizonsPlayer.getQuestRewardTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.client.Minecraft;

import org.lwjgl.util.vector.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;
import com.nfdobbs.emptyhorizons.util.TimeString;

import betterquesting.api.api.QuestingAPI;
import betterquesting.api.properties.NativeProps;
import betterquesting.api.questing.IQuestLine;
import betterquesting.api.questing.IQuestLineEntry;
import betterquesting.api2.client.gui.misc.GuiPadding;
import betterquesting.api2.client.gui.misc.GuiTransform;
import betterquesting.api2.client.gui.panels.CanvasTextured;
import betterquesting.api2.client.gui.panels.content.PanelTextBox;
import betterquesting.api2.client.gui.themes.presets.PresetColor;
import betterquesting.client.gui2.GuiQuestLines;
import betterquesting.questing.*;

@Mixin(GuiQuestLines.class)
class MixinGuiQuestLines {

    private final QuestDatabase questDB = QuestDatabase.INSTANCE;
    private final QuestLineDatabase questLineDB = QuestLineDatabase.INSTANCE;

    private PanelTextBox timeProgressBox;

    @Shadow(remap = false)
    private IQuestLine selectedLine;

    @Shadow(remap = false)
    private PanelTextBox completionText;

    @Inject(method = "initPanel", at = @At(value = "TAIL"), remap = false)
    private void addPanel(CallbackInfo ci, @Local(ordinal = 0) CanvasTextured cvBackground) {

        // Need this for first quest open padding
        String timeProgress = GetQuestlineTimeProgress();

        final int BORDER_PADDING = 9;
        int timeProgressLength = (Minecraft.getMinecraft().fontRenderer.getStringWidth(timeProgress) + BORDER_PADDING)
            * -1;

        if (timeProgress.isEmpty()) {
            timeProgressLength = (Minecraft.getMinecraft().fontRenderer.getStringWidth("[0m 0s of 0m 0s]")
                + BORDER_PADDING) * -1;
        }

        timeProgressBox = new PanelTextBox(
            new GuiTransform(new Vector4f(1F, 0F, 1F, 0F), new GuiPadding(timeProgressLength, 12, 0, -24), 0),
            timeProgress);
        timeProgressBox.setColor(PresetColor.TEXT_HEADER.getColor());

        cvBackground.addPanel(timeProgressBox);

        return;
    }

    @Inject(method = "openQuestLine", at = @At(value = "TAIL"), remap = false)
    private void openQuestline(CallbackInfo ci) {
        if (selectedLine == null) {
            return;
        }

        String timeProgress = GetQuestlineTimeProgress();

        timeProgressBox.setText(timeProgress);

    }

    private String GetQuestlineTimeProgress() {

        if (selectedLine == null) {
            return "";
        }

        int totalTime = 0;
        int completedTime = 0;

        for (Map.Entry<UUID, IQuestLineEntry> questMapping : selectedLine.entrySet()) {
            QuestInstance currentQuest = (QuestInstance) questDB.get(questMapping.getKey());

            if (currentQuest != null && currentQuest.getProperty(NativeProps.REPEAT_TIME) < 0) {
                List<String> questLineNames = FindQuestLineNames(questMapping.getKey());

                UUID uuid = QuestingAPI.getQuestingUUID(Minecraft.getMinecraft().thePlayer);

                int rewardTime = getQuestRewardTime(currentQuest, questLineNames);
                totalTime += rewardTime;

                if (currentQuest.isComplete(uuid)) {
                    completedTime += rewardTime;
                }
            }
            int i = 1 + 2;
        }

        String timeProgress = "[" + TimeString.CreatePrettyTimeString(completedTime)
            + " of "
            + TimeString.CreatePrettyTimeString(totalTime)
            + "]";

        int minecraftWidth = Minecraft.getMinecraft().displayWidth;

        return timeProgress;
    }

    public List<String> FindQuestLineNames(UUID questUUID) {
        List<Map.Entry<UUID, IQuestLine>> questLines = questLineDB.getOrderedEntries();

        List<String> questLineNames = new ArrayList<>();

        for (Map.Entry<UUID, IQuestLine> entry : questLines) {
            QuestLine currentQuestline = (QuestLine) entry.getValue();
            QuestLineEntry foundQuest = (QuestLineEntry) currentQuestline.get(questUUID);

            if (foundQuest != null) {
                questLineNames.add(currentQuestline.getProperty(NativeProps.NAME));
            }
        }

        return questLineNames;
    }
}
