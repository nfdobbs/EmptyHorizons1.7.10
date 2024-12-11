package com.nfdobbs.emptyhorizons.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class ShowWelcomeGuiMessage implements IMessage {

    public ShowWelcomeGuiMessage() {}

    @Override
    public void fromBytes(ByteBuf buf) {}

    @Override
    public void toBytes(ByteBuf buf) {}
}
