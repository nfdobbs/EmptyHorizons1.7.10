package com.nfdobbs.emptyhorizons.network;

import static com.nfdobbs.emptyhorizons.util.OverworldTeleporter.TeleportToOverworld;

import com.nfdobbs.emptyhorizons.EmptyHorizons;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class TravelButtonMessageHandler implements IMessageHandler<TravelButtonMessage, IMessage> {

    @Override
    public IMessage onMessage(TravelButtonMessage message, MessageContext ctx) {

        if (ctx.side.isServer()) {

            EmptyHorizons.LOG.info("Message Received");

            ctx.getServerHandler().playerEntity.getServerForPlayer();

            TeleportToOverworld(
                message.locationCoords.x,
                message.locationCoords.z,
                ctx.getServerHandler().playerEntity);
        }

        return null;
    }
}
