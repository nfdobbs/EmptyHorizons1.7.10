package com.nfdobbs.emptyhorizons.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class TravelButtonMessage implements IMessage {

    int tileEntityCoordsX;
    int tileEntityCoordsY;
    int tileEntityCoordsZ;

    public TravelButtonMessage() {}

    public TravelButtonMessage(int x, int y, int z) {
        tileEntityCoordsX = x;
        tileEntityCoordsY = y;
        tileEntityCoordsZ = z;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        tileEntityCoordsX = buf.readInt();
        tileEntityCoordsY = buf.readInt();
        tileEntityCoordsZ = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(tileEntityCoordsX);
        buf.writeInt(tileEntityCoordsY);
        buf.writeInt(tileEntityCoordsZ);
    }
}
