package com.nfdobbs.emptyhorizons.commands;

import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class CommandEmptyHorizonsWhereAmI implements ICommand {

    @Override
    public String getCommandName() {
        return "eh_whereami";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "eh_whereami";
    }

    @Override
    public List<String> getCommandAliases() {
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {

        int dimensionId = sender.getEntityWorld().provider.dimensionId;
        String dimensionName = sender.getEntityWorld().provider.getDimensionName();

        sender.addChatMessage(new ChatComponentText("Dimension: " + dimensionName + " (" + dimensionId + ")"));

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
