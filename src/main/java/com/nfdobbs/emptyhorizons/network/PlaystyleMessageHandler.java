package com.nfdobbs.emptyhorizons.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChunkCoordinates;

import com.nfdobbs.emptyhorizons.EmptyDimension.EmptyDimRegister;
import com.nfdobbs.emptyhorizons.EmptyDimension.EmptyDimTeleporter;
import com.nfdobbs.emptyhorizons.blocks.EmptyHorizonBlocks;
import com.nfdobbs.emptyhorizons.blocks.ExcursionBlock;
import com.nfdobbs.emptyhorizons.playerdata.ExtendedEmptyHorizonsPlayer;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PlaystyleMessageHandler implements IMessageHandler<PlaystyleMessage, IMessage> {

    @Override
    public IMessage onMessage(PlaystyleMessage message, MessageContext ctx) {

        if (ctx.side.isServer()) {
            EntityPlayerMP playerMP = ctx.getServerHandler().playerEntity;

            ExtendedEmptyHorizonsPlayer modPlayer = (ExtendedEmptyHorizonsPlayer) playerMP
                .getExtendedProperties(ExtendedEmptyHorizonsPlayer.EXT_PROP_NAME);

            modPlayer.setHasChosenPlaystyle(true);
            modPlayer.setDoingChallenge(message.willDoChallenge);

            if (message.willDoChallenge) {
                int x = 0;
                int y = 100;
                int z = 0;

                EmptyDimTeleporter.teleportToEmptyDim(playerMP, x, y, z, 0, 0);

                playerMP.mcServer.worldServerForDimension(EmptyDimRegister.EMPTY_DIMENSION_ID)
                    .setBlock(x, y - 2, z, EmptyHorizonBlocks.excursionBlock);

                playerMP.mcServer.worldServerForDimension(EmptyDimRegister.EMPTY_DIMENSION_ID)
                    .setBlockMetadataWithNotify(x, y - 2, z, ExcursionBlock.DOWN_OFFSET, 2);

                ChunkCoordinates spawnCoords = new ChunkCoordinates(x, y, z);

                playerMP.setSpawnChunk(spawnCoords, true, EmptyDimRegister.EMPTY_DIMENSION_ID);
            }
        }

        return null;
    }
}
