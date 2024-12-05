package com.nfdobbs.emptyhorizons.commands;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import com.nfdobbs.emptyhorizons.EmptyDimension.EmptyDimRegister;
import com.nfdobbs.emptyhorizons.EmptyDimension.EmptyDimTeleporter;
import com.nfdobbs.emptyhorizons.playerdata.ExtendedEmptyHorizonsPlayer;

public class CommandEmptyHorizonsInitialize implements ICommand {

    @Override
    public String getCommandName() {
        return "eh_initialize";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "eh_initialize <text>";
    }

    @Override
    public List<String> getCommandAliases() {
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        World world = sender.getEntityWorld();

        /*
         * ExtendedEmptyHorizonsPlayer player = (ExtendedEmptyHorizonsPlayer) ((EntityPlayer) sender)
         * .getExtendedProperties(ExtendedEmptyHorizonsPlayer.EXT_PROP_NAME);
         * int expeditionTime = player.getExpeditionTime();
         * expeditionTime = expeditionTime + 1;
         * player.setExpeditionTime(expeditionTime);
         * player.debugMessage();
         */

        if (!world.isRemote) {
            sender.addChatMessage(new ChatComponentText("Teleporting"));

            int x = 0;
            int y = 100;
            int z = 0;

            ExtendedEmptyHorizonsPlayer player = (ExtendedEmptyHorizonsPlayer) ((EntityPlayer) sender)
                .getExtendedProperties(ExtendedEmptyHorizonsPlayer.EXT_PROP_NAME);

            player.setDoingChallenge(!player.isDoingChallenge());

            EmptyDimTeleporter.teleportToEmptyDim((EntityPlayer) sender, x, y, z, 0, 0);

            ((EntityPlayerMP) sender).mcServer.worldServerForDimension(EmptyDimRegister.EMPTY_DIMENSION_ID)
                .setBlock(x, y - 2, z, Block.getBlockById(20));
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
