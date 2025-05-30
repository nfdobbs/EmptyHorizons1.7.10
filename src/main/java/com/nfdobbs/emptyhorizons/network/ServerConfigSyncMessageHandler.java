package com.nfdobbs.emptyhorizons.network;

import com.nfdobbs.emptyhorizons.ClientProxy;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class ServerConfigSyncMessageHandler implements IMessageHandler<ServerConfigSyncMessage, IMessage> {

    @Override
    public IMessage onMessage(ServerConfigSyncMessage message, MessageContext ctx) {
        if (ctx.side.isClient()) {
            ClientProxy.serverDimensionMultipliers = message.dimensionMultipliers;
        }

        return null;
    }
}
