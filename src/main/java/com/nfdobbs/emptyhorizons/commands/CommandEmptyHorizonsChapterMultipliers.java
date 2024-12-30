package com.nfdobbs.emptyhorizons.commands;

import com.nfdobbs.emptyhorizons.Config;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;
import java.util.Map;

public class CommandEmptyHorizonsChapterMultipliers implements ICommand {
    @Override
    public String getCommandName() {
        return "eh_chapterMultipliers";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "eh_chapterMultipliers";
    }

    @Override
    public List<String> getCommandAliases() {
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        for(Map.Entry<String, Float> entry : Config.questLineMultipliers.entrySet())
        {
            String chapter = entry.getKey();
            Float multiplier = entry.getValue();

            String multiplierText = "[x" + multiplier + "]";

            sender.addChatMessage(new ChatComponentText( EnumChatFormatting.YELLOW + String.format("%-" + 11 + "s", multiplierText) + " " + EnumChatFormatting.WHITE + chapter));
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
