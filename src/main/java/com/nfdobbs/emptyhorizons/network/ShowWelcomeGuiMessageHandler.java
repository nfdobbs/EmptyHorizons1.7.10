package com.nfdobbs.emptyhorizons.network;

import com.nfdobbs.emptyhorizons.ClientProxy;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class ShowWelcomeGuiMessageHandler implements IMessageHandler<ShowWelcomeGuiMessage, IMessage> {

    @Override
    public IMessage onMessage(ShowWelcomeGuiMessage message, MessageContext ctx) {
        if (ctx.side.isClient()) {
            ClientProxy.fogProvider.showWelcomeMessage = true;
        }

        return null;
    }
}
