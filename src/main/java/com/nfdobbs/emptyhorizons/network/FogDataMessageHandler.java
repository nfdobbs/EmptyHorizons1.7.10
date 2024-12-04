package com.nfdobbs.emptyhorizons.network;

import com.nfdobbs.emptyhorizons.ClientProxy;
import com.nfdobbs.emptyhorizons.worlddata.FogProvider;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class FogDataMessageHandler implements IMessageHandler<FogDataMessage, IMessage> {

    @Override
    public IMessage onMessage(FogDataMessage message, MessageContext ctx) {
        // Safety check, I believe this is only fired on the client
        if (ctx.side.isClient()) {
            ClientProxy.fogProvider = new FogProvider();
            ClientProxy.fogProvider.SetFogData(message.fogData);
        }

        return null;
    }
}
