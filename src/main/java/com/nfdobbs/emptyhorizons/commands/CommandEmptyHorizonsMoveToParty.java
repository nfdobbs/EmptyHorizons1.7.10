package com.nfdobbs.emptyhorizons.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;

import com.nfdobbs.emptyhorizons.EmptyDimension.EmptyDimRegister;
import com.nfdobbs.emptyhorizons.EmptyDimension.EmptyDimTeleporter;
import com.nfdobbs.emptyhorizons.blocks.EmptyHorizonBlocks;
import com.nfdobbs.emptyhorizons.playerdata.ExtendedEmptyHorizonsPlayer;

import betterquesting.api.questing.party.IParty;
import betterquesting.api2.storage.DBEntry;
import betterquesting.questing.party.PartyManager;

public class CommandEmptyHorizonsMoveToParty implements ICommand {

    @Override
    public String getCommandName() {
        return "eh_moveToParty";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "eh_moveToParty <text>";
    }

    @Override
    public List<String> getCommandAliases() {
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {

        if (!sender.getEntityWorld().isRemote) {
            if (args.length != 1) {
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Incorrect number of arguments."));
            } else {
                EntityPlayerMP player = (EntityPlayerMP) sender.getEntityWorld()
                    .getPlayerEntityByName(sender.getCommandSenderName());

                PartyManager partyManager = PartyManager.INSTANCE;
                DBEntry<IParty> partyList = partyManager.getParty(player.getUniqueID());

                if (partyList == null) {
                    sender.addChatMessage(
                        new ChatComponentText(EnumChatFormatting.RED + "You must be in a party to use this command."));
                    return;
                }

                ExtendedEmptyHorizonsPlayer modPlayer = (ExtendedEmptyHorizonsPlayer) player
                    .getExtendedProperties(ExtendedEmptyHorizonsPlayer.EXT_PROP_NAME);

                if (!modPlayer.isDoingChallenge()) {
                    sender.addChatMessage(
                        new ChatComponentText(EnumChatFormatting.RED + "You must be playing challenge mode for this."));
                    return;
                }

                if (player.dimension != EmptyDimRegister.EMPTY_DIMENSION_ID) {
                    sender.addChatMessage(
                        new ChatComponentText(
                            EnumChatFormatting.RED + "You must be in the Empty Dimension to use this command."));
                    return;
                }

                if (modPlayer.getHasUsedFreePartyTP()) {
                    sender.addChatMessage(
                        new ChatComponentText(
                            EnumChatFormatting.RED + "You have already used your free party teleport."));
                    return;
                }

                String partyMemberNameArg = args[0];

                String senderName = sender.getCommandSenderName();

                if (partyMemberNameArg.equals(senderName)) {
                    sender.addChatMessage(
                        new ChatComponentText(EnumChatFormatting.RED + "You may not teleport to yourself."));
                    return;
                }

                EntityPlayer playerFromArg = getPlayer(partyMemberNameArg);

                if (playerFromArg == null) {
                    sender.addChatMessage(
                        new ChatComponentText(
                            EnumChatFormatting.RED + "Player " + partyMemberNameArg + " could not be found."));
                    return;
                }

                if (playerFromArg.dimension != EmptyDimRegister.EMPTY_DIMENSION_ID) {
                    sender.addChatMessage(
                        new ChatComponentText(
                            EnumChatFormatting.RED + "Player "
                                + partyMemberNameArg
                                + " must be in the Empty Dimension."));
                    return;
                }

                if (!isPlayerInParty(playerFromArg, player)) {
                    sender.addChatMessage(
                        new ChatComponentText(
                            EnumChatFormatting.RED + "Player " + partyMemberNameArg + " is not in your party."));
                    return;
                }

                int playerX = (int) player.posX;
                int playerY = (int) player.posY;
                int playerZ = (int) player.posZ;

                Block block = sender.getEntityWorld()
                    .getBlock(playerX, playerY - 1, playerZ);

                if (block != EmptyHorizonBlocks.excursionBlock) {
                    sender.addChatMessage(
                        new ChatComponentText(
                            EnumChatFormatting.RED + "You must be standing on your Excursion Block to do this."));
                    return;
                }

                // Command should now be able to run
                int argPlayerX = (int) playerFromArg.posX;
                int argPlayerY = (int) playerFromArg.posY;
                int argPlayerZ = (int) playerFromArg.posZ;

                EmptyDimTeleporter.teleportToEmptyDim(player, argPlayerX + 5.5, argPlayerY, argPlayerZ + .5, 0, 0);

                player.mcServer.worldServerForDimension(EmptyDimRegister.EMPTY_DIMENSION_ID)
                    .setBlock(argPlayerX + 5, argPlayerY - 1, argPlayerZ, Block.getBlockById(20));

                ChunkCoordinates spawnChunkCoords = new ChunkCoordinates(argPlayerX + 5, argPlayerY, argPlayerZ);

                player.setSpawnChunk(spawnChunkCoords, true, EmptyDimRegister.EMPTY_DIMENSION_ID);

                ItemStack blockStack = new ItemStack(EmptyHorizonBlocks.excursionBlock);

                player.inventory.addItemStackToInventory(blockStack);

                // Remove Old Block
                player.mcServer.worldServerForDimension(EmptyDimRegister.EMPTY_DIMENSION_ID)
                    .removeTileEntity(playerX, playerY - 1, playerZ);
                player.mcServer.worldServerForDimension(EmptyDimRegister.EMPTY_DIMENSION_ID)
                    .setBlockToAir(playerX, playerY - 1, playerZ);

                modPlayer.setHasUsedFreePartyTP(true);

                return;
            }
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {

        if (!sender.getEntityWorld().isRemote) {
            EntityPlayer player = sender.getEntityWorld()
                .getPlayerEntityByName(sender.getCommandSenderName());

            PartyManager partyManager = PartyManager.INSTANCE;

            DBEntry<IParty> partyList = partyManager.getParty(player.getUniqueID());

            if (partyList == null) {
                return null;
            }

            List<UUID> memberUUIDs = partyList.getValue()
                .getMembers();
            List<String> onlinePartyMembers = new ArrayList<String>();

            String senderName = sender.getCommandSenderName();

            for (UUID memberUUID : memberUUIDs) {
                String playerName = getPlayerName(memberUUID);

                if (playerName != null && !playerName.equals(senderName)) {
                    onlinePartyMembers.add(playerName);
                }
            }

            if (!onlinePartyMembers.isEmpty()) {
                return onlinePartyMembers;
            }
        }

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

    private String getPlayerName(UUID playerUUID) {
        ServerConfigurationManager serverConfigManager = MinecraftServer.getServer()
            .getConfigurationManager();
        List<EntityPlayerMP> playerList = serverConfigManager.playerEntityList;

        for (EntityPlayerMP player : playerList) {

            UUID onlinePlayerUUID = player.getUniqueID();

            if (playerUUID.equals(onlinePlayerUUID)) {
                return player.getDisplayName();
            }
        }

        return null;
    }

    public static EntityPlayer getPlayer(String playerName) {
        ServerConfigurationManager serverConfigManager = MinecraftServer.getServer()
            .getConfigurationManager();
        List<EntityPlayerMP> playerList = serverConfigManager.playerEntityList;

        for (EntityPlayerMP player : playerList) {

            String onlinePlayerName = player.getDisplayName();

            if (playerName.equals(onlinePlayerName)) {
                return player;
            }
        }

        return null;
    }

    private boolean isPlayerInParty(EntityPlayer argPlayer, EntityPlayer senderPlayer) {
        PartyManager partyManager = PartyManager.INSTANCE;

        DBEntry<IParty> partyList = partyManager.getParty(senderPlayer.getUniqueID());

        if (partyList == null) {
            return false;
        }

        List<UUID> memberUUIDs = partyList.getValue()
            .getMembers();

        String senderName = senderPlayer.getDisplayName();

        for (UUID memberUUID : memberUUIDs) {
            String playerName = getPlayerName(memberUUID);

            if (playerName != null && !playerName.equals(senderName)) {
                return true;
            }
        }

        return false;
    }
}
