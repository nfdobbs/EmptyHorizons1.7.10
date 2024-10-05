package com.nfdobbs.emptyhorizons.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class SyncMessage implements IMessage {

    public int MaxExpeditionTime;
    public int ExpeditionTime;

    public SyncMessage() {}

    public SyncMessage(int maxExpeditionTime, int expeditionTime) {
        MaxExpeditionTime = maxExpeditionTime;
        ExpeditionTime = expeditionTime;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.MaxExpeditionTime = buf.readInt();
        this.ExpeditionTime = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(MaxExpeditionTime);
        buf.writeInt(ExpeditionTime);
    }
}
