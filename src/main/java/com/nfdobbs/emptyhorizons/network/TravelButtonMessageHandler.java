package com.nfdobbs.emptyhorizons.network;

import static com.nfdobbs.emptyhorizons.util.OverworldTeleporter.TeleportToOverworld;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;

import com.nfdobbs.emptyhorizons.EmptyDimension.EmptyDimRegister;
import com.nfdobbs.emptyhorizons.EmptyHorizons;
import com.nfdobbs.emptyhorizons.blocks.EmptyHorizonBlocks;
import com.nfdobbs.emptyhorizons.tileentities.TileEntityEvacuationBlock;
import com.nfdobbs.emptyhorizons.tileentities.TileEntityExcursionBlock;
import com.nfdobbs.emptyhorizons.util.ExcursionCoords;
import com.nfdobbs.emptyhorizons.util.OverworldTeleporter;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class TravelButtonMessageHandler implements IMessageHandler<TravelButtonMessage, IMessage> {

    @Override
    public IMessage onMessage(TravelButtonMessage message, MessageContext ctx) {

        if (ctx.side.isServer()) {

            EntityPlayerMP playerMP = ctx.getServerHandler().playerEntity;
            WorldServer worldServer = playerMP.mcServer.worldServerForDimension(0);
            WorldServer emptyDimWorldServer = playerMP.mcServer
                .worldServerForDimension(EmptyDimRegister.EMPTY_DIMENSION_ID);

            // Get Tile Entity
            TileEntityExcursionBlock tileEntityExcursionBlock = (TileEntityExcursionBlock) emptyDimWorldServer
                .getTileEntity(message.tileEntityCoordsX, message.tileEntityCoordsY, message.tileEntityCoordsZ);

            if (tileEntityExcursionBlock == null) {
                // Do nothing if we cant find the tile entity
                return null;
            }

            int x = 0;
            int y = 100;
            int z = 0;

            if (tileEntityExcursionBlock.inUse) {
                x = tileEntityExcursionBlock.targetBlockX;
                y = tileEntityExcursionBlock.targetBlockY + 2;
                z = tileEntityExcursionBlock.targetBlockZ;

                tileEntityExcursionBlock.decreaseAttemptsRemaining();
            } else {
                // Calculate location coords
                double days = playerMP.worldObj.getTotalWorldTime() / 24000.00;
                ExcursionCoords travelCoords = TileEntityExcursionBlock
                    .getExcursionCoords(days, message.tileEntityCoordsX, message.tileEntityCoordsZ);

                x = travelCoords.x;
                z = travelCoords.z;

                int topBlockY = OverworldTeleporter.getTopBlock(x, z, worldServer);

                y = topBlockY + 3;
                worldServer.setBlock(x, topBlockY + 1, z, EmptyHorizonBlocks.evacuationBlock);

                TileEntityEvacuationBlock tileEntityEvacuationBlock = (TileEntityEvacuationBlock) worldServer
                    .getTileEntity(x, topBlockY + 1, z);

                if (tileEntityEvacuationBlock != null) {
                    tileEntityEvacuationBlock.InitializeTileEntity(
                        (int) Math.floor(playerMP.posX),
                        (int) playerMP.posY,
                        (int) Math.floor(playerMP.posZ),
                        message.tileEntityCoordsX,
                        message.tileEntityCoordsY,
                        message.tileEntityCoordsZ,
                        playerMP.getDisplayName());
                } else {
                    EmptyHorizons.LOG.warn("TileEntityEvacuationBlock failed to be created.");
                }

                tileEntityExcursionBlock.setTargetBlock(x, topBlockY + 1, z, playerMP.getDisplayName());

                emptyDimWorldServer.markBlockForUpdate(
                    tileEntityExcursionBlock.xCoord,
                    tileEntityExcursionBlock.yCoord,
                    tileEntityExcursionBlock.zCoord);

                tileEntityExcursionBlock.decreaseAttemptsRemaining();
            }

            TeleportToOverworld(x, y, z, worldServer, ctx.getServerHandler().playerEntity);
        }

        return null;
    }
}
