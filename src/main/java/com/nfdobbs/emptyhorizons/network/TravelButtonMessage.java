package com.nfdobbs.emptyhorizons.network;

import com.nfdobbs.emptyhorizons.util.ExcursionCoords;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class TravelButtonMessage implements IMessage {

    ExcursionCoords locationCoords;

    public TravelButtonMessage() {}

    public TravelButtonMessage(ExcursionCoords coords) {
        locationCoords = coords;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int x = buf.readInt();
        int z = buf.readInt();

        locationCoords = new ExcursionCoords(x, z);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(locationCoords.x);
        buf.writeInt(locationCoords.z);
    }
}
