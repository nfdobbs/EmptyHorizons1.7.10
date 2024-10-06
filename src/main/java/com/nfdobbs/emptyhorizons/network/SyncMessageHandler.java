package com.nfdobbs.emptyhorizons.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;

import com.nfdobbs.emptyhorizons.playerdata.ExtendedEmptyHorizonsPlayer;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class SyncMessageHandler implements IMessageHandler<SyncMessage, IMessage> {

    @Override
    public IMessage onMessage(SyncMessage message, MessageContext ctx) {
        // Safety check, I believe this is only fired on the client
        if (ctx.side.isClient()) {
            EntityClientPlayerMP vanillaPlayer = Minecraft.getMinecraft().thePlayer;
            ExtendedEmptyHorizonsPlayer player = ExtendedEmptyHorizonsPlayer.get(vanillaPlayer);

            player.setMaxExpeditionTime(message.MaxExpeditionTime);
            player.setExpeditionTime(message.ExpeditionTime);
        }

        return null;
    }
}
