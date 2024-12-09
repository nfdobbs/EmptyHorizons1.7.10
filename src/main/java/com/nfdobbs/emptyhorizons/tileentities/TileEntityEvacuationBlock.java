package com.nfdobbs.emptyhorizons.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityEvacuationBlock extends TileEntity {

    public int returnPlayerX;
    public int returnPlayerY;
    public int returnPlayerZ;
    public int returnBlockX;
    public int returnBlockY;
    public int returnBlockZ;
    public String targetUser;

    private final static String EVACUATION_BLOCK_PROP_NAME = "EvacuationBlockData";
    private final static String RETURN_PLAYER_X_KEY = "ReturnPlayerX";
    private final static String RETURN_PLAYER_Y_KEY = "ReturnPlayerY";
    private final static String RETURN_PLAYER_Z_KEY = "ReturnPlayerZ";
    private final static String RETURN_BLOCK_X_KEY = "ReturnBlockX";
    private final static String RETURN_BLOCK_Y_KEY = "ReturnBlockY";
    private final static String RETURN_BLOCK_Z_KEY = "ReturnBlockZ";
    private final static String TARGET_USER_KEY = "";

    public TileEntityEvacuationBlock() {
        super();
    }

    public void InitializeTileEntity(int playerX, int playerY, int playerZ, int blockX, int blockY, int blockZ,
        String user) {
        returnPlayerX = playerX;
        returnPlayerY = playerY;
        returnPlayerZ = playerZ;
        returnBlockX = blockX;
        returnBlockY = blockY;
        returnBlockZ = blockZ;
        targetUser = user;
        markDirty();
    }

    public void markDirty() {
        super.markDirty();
    }

    public void readFromNBT(NBTTagCompound compound) {
        NBTTagCompound properties = compound.getCompoundTag(EVACUATION_BLOCK_PROP_NAME);

        returnPlayerX = properties.getInteger(RETURN_PLAYER_X_KEY);
        returnPlayerY = properties.getInteger(RETURN_PLAYER_Y_KEY);
        returnPlayerZ = properties.getInteger(RETURN_PLAYER_Z_KEY);

        returnBlockX = properties.getInteger(RETURN_BLOCK_X_KEY);
        returnBlockY = properties.getInteger(RETURN_BLOCK_Y_KEY);
        returnBlockZ = properties.getInteger(RETURN_BLOCK_Z_KEY);

        targetUser = properties.getString(TARGET_USER_KEY);

        super.readFromNBT(compound);
    }

    public void writeToNBT(NBTTagCompound compound) {
        NBTTagCompound properties = new NBTTagCompound();

        // Save Values
        properties.setInteger(RETURN_PLAYER_X_KEY, returnPlayerX);
        properties.setInteger(RETURN_PLAYER_Y_KEY, returnPlayerY);
        properties.setInteger(RETURN_PLAYER_Z_KEY, returnPlayerZ);

        properties.setInteger(RETURN_BLOCK_X_KEY, returnBlockX);
        properties.setInteger(RETURN_BLOCK_Y_KEY, returnBlockY);
        properties.setInteger(RETURN_BLOCK_Z_KEY, returnBlockZ);

        properties.setString(TARGET_USER_KEY, targetUser);

        // Use unique tags to avoid conflicts
        compound.setTag(EVACUATION_BLOCK_PROP_NAME, properties);

        super.writeToNBT(compound);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        this.writeToNBT(tagCompound);

        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, tagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        readFromNBT(pkt.func_148857_g());

        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

}
