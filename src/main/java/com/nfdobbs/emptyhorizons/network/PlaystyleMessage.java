package com.nfdobbs.emptyhorizons.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class PlaystyleMessage implements IMessage {

    boolean willDoChallenge;

    public PlaystyleMessage() {}

    public PlaystyleMessage(boolean willDoChallenge) {
        this.willDoChallenge = willDoChallenge;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        willDoChallenge = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(willDoChallenge);
    }
}
