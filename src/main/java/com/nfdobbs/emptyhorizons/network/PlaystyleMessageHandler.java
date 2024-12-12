package com.nfdobbs.emptyhorizons.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.WorldServer;

import com.nfdobbs.emptyhorizons.EmptyDimension.EmptyDimRegister;
import com.nfdobbs.emptyhorizons.EmptyDimension.EmptyDimTeleporter;
import com.nfdobbs.emptyhorizons.blocks.EmptyHorizonBlocks;
import com.nfdobbs.emptyhorizons.blocks.ExcursionBlock;
import com.nfdobbs.emptyhorizons.playerdata.ExtendedEmptyHorizonsPlayer;
import com.nfdobbs.emptyhorizons.util.ExcursionCoords;
import com.nfdobbs.emptyhorizons.util.SpawnCoordinates;
import com.nfdobbs.emptyhorizons.worlddata.SpawnData;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PlaystyleMessageHandler implements IMessageHandler<PlaystyleMessage, IMessage> {

    public static final String SPAWN_COUNT_KEY = "SpawnCount";

    @Override
    public IMessage onMessage(PlaystyleMessage message, MessageContext ctx) {

        if (ctx.side.isServer()) {
            EntityPlayerMP playerMP = ctx.getServerHandler().playerEntity;

            ExtendedEmptyHorizonsPlayer modPlayer = (ExtendedEmptyHorizonsPlayer) playerMP
                .getExtendedProperties(ExtendedEmptyHorizonsPlayer.EXT_PROP_NAME);

            modPlayer.setHasChosenPlaystyle(true);
            modPlayer.setDoingChallenge(message.willDoChallenge);

            if (message.willDoChallenge) {
                ExcursionCoords spawnCoords = getPlayerSpawnLocation();

                int x = spawnCoords.x;
                int y = 100;
                int z = spawnCoords.z;

                EmptyDimTeleporter.teleportToEmptyDim(playerMP, x, y, z, 0, 0);

                playerMP.mcServer.worldServerForDimension(EmptyDimRegister.EMPTY_DIMENSION_ID)
                    .setBlock(x, y - 2, z, EmptyHorizonBlocks.excursionBlock);

                playerMP.mcServer.worldServerForDimension(EmptyDimRegister.EMPTY_DIMENSION_ID)
                    .setBlockMetadataWithNotify(x, y - 2, z, ExcursionBlock.DOWN_OFFSET, 2);

                ChunkCoordinates spawnChunkCoords = new ChunkCoordinates(x, y, z);

                playerMP.setSpawnChunk(spawnChunkCoords, true, EmptyDimRegister.EMPTY_DIMENSION_ID);
            }
        }

        return null;
    }

    private ExcursionCoords getPlayerSpawnLocation() {
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

        return SpawnCoordinates.getSpawnCoords(spawnCount);
    }
}
