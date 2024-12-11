package com.nfdobbs.emptyhorizons.commands;

import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

import com.nfdobbs.emptyhorizons.EmptyDimension.EmptyDimRegister;
import com.nfdobbs.emptyhorizons.EmptyDimension.EmptyDimTeleporter;
import com.nfdobbs.emptyhorizons.blocks.EmptyHorizonBlocks;
import com.nfdobbs.emptyhorizons.blocks.ExcursionBlock;
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

        if (!world.isRemote) {
            int x = 0;
            int y = 100;
            int z = 0;

            ExtendedEmptyHorizonsPlayer player = (ExtendedEmptyHorizonsPlayer) ((EntityPlayer) sender)
                .getExtendedProperties(ExtendedEmptyHorizonsPlayer.EXT_PROP_NAME);

            EmptyDimTeleporter.teleportToEmptyDim((EntityPlayer) sender, x, y, z, 0, 0);

            ((EntityPlayerMP) sender).mcServer.worldServerForDimension(EmptyDimRegister.EMPTY_DIMENSION_ID)
                .setBlock(x, y - 2, z, EmptyHorizonBlocks.excursionBlock);

            ((EntityPlayerMP) sender).mcServer.worldServerForDimension(EmptyDimRegister.EMPTY_DIMENSION_ID)
                .setBlockMetadataWithNotify(x, y - 2, z, ExcursionBlock.DOWN_OFFSET, 2);

            ChunkCoordinates spawnCoords = new ChunkCoordinates(x, y, z);

            ((EntityPlayer) sender).setSpawnChunk(spawnCoords, true, EmptyDimRegister.EMPTY_DIMENSION_ID);
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
