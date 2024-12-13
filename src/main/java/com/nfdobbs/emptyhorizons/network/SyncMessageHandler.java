package com.nfdobbs.emptyhorizons.network;

import net.minecraft.entity.player.EntityPlayer;

import com.nfdobbs.emptyhorizons.ClientProxy;
import com.nfdobbs.emptyhorizons.playerdata.ExtendedEmptyHorizonsPlayer;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class SyncMessageHandler implements IMessageHandler<SyncMessage, IMessage> {

    @Override
    public IMessage onMessage(SyncMessage message, MessageContext ctx) {
        // Safety check, I believe this is only fired on the client
        if (ctx.side.isClient()) {

            EntityPlayer vanillaPlayer = ClientProxy.getPlayer();

            ExtendedEmptyHorizonsPlayer player = ExtendedEmptyHorizonsPlayer.get(vanillaPlayer);

            player.setMaxExpeditionTime(message.MaxExpeditionTime);
            player.setExpeditionTime(message.ExpeditionTime);
            player.setDoingChallenge(message.DoingChallenge);

        }

        return null;
    }
}
