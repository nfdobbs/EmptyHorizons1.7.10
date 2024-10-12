package com.nfdobbs.emptyhorizons.network;

import net.minecraft.nbt.NBTTagCompound;

import com.nfdobbs.emptyhorizons.worlddata.FogData;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class FogDataMessage implements IMessage {

    public FogData fogData;

    public FogDataMessage() {
        fogData = new FogData("EmptyHorizonsFogData");
    }

    public FogDataMessage(FogData fogData) {
        this.fogData = fogData;
    }

    @Override
    public void fromBytes(ByteBuf buf) {

        NBTTagCompound tag = ByteBufUtils.readTag(buf);
        fogData.SetFogDataNBT(tag);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, fogData.getFogData());
    }
}
