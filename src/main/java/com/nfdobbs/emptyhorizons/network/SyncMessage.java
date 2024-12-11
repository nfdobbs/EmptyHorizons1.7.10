package com.nfdobbs.emptyhorizons.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class SyncMessage implements IMessage {

    public int MaxExpeditionTime;
    public int ExpeditionTime;
    public boolean DoingChallenge;
    public boolean HasChosenPlaystyle;

    public SyncMessage() {}

    public SyncMessage(int maxExpeditionTime, int expeditionTime, boolean doingChallenge, boolean hasChosenPlaystyle) {
        MaxExpeditionTime = maxExpeditionTime;
        ExpeditionTime = expeditionTime;
        DoingChallenge = doingChallenge;
        HasChosenPlaystyle = hasChosenPlaystyle;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.MaxExpeditionTime = buf.readInt();
        this.ExpeditionTime = buf.readInt();
        this.DoingChallenge = buf.readBoolean();
        this.HasChosenPlaystyle = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(MaxExpeditionTime);
        buf.writeInt(ExpeditionTime);
        buf.writeBoolean(DoingChallenge);
        buf.writeBoolean(HasChosenPlaystyle);
    }
}
