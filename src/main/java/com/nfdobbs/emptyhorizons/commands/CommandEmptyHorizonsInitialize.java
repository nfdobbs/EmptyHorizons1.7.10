package com.nfdobbs.emptyhorizons.commands;

import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import com.nfdobbs.emptyhorizons.EmptyDimension.EmptyDimRegister;
import com.nfdobbs.emptyhorizons.util.ExcursionCoords;
import com.nfdobbs.emptyhorizons.util.SpawnCoordinates;
import com.nfdobbs.emptyhorizons.worlddata.SpawnData;

public class CommandEmptyHorizonsInitialize implements ICommand {

    public static final String SPAWN_COUNT_KEY = "SpawnCount";

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
            WorldServer worldServer = MinecraftServer.getServer()
                .worldServerForDimension(EmptyDimRegister.EMPTY_DIMENSION_ID);

            SpawnData data = SpawnData.forWorld(worldServer);
            NBTTagCompound tag = data.getData();

            int spawnCount = tag.getInteger(SPAWN_COUNT_KEY);

            if (spawnCount < 1) {
                // I don't want players spawning at 0,0
                spawnCount = 2;
            } else {
                spawnCount = spawnCount + 1;
            }

            tag.setInteger(SPAWN_COUNT_KEY, spawnCount);

            // I don't understand how this would work
            data.markDirty();

            ExcursionCoords spawnCoords = SpawnCoordinates.getSpawnCoords(spawnCount);

            sender.addChatMessage(
                (new ChatComponentText("Spawns: " + spawnCount + " X: " + spawnCoords.x + " Z: " + spawnCoords.z)));
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
