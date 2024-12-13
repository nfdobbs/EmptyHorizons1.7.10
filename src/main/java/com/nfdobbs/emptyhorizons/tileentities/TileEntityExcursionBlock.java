package com.nfdobbs.emptyhorizons.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import com.nfdobbs.emptyhorizons.Config;
import com.nfdobbs.emptyhorizons.util.ExcursionCoords;

public class TileEntityExcursionBlock extends TileEntity {

    public int attemptsRemaining;
    public boolean inUse;
    public String currentUser = "";

    public int targetBlockX;
    public int targetBlockY;
    public int targetBlockZ;

    private final static String TARGET_BLOCK_X_KEY = "TargetBlockX";
    private final static String TARGET_BLOCK_Y_KEY = "TargetBlockY";
    private final static String TARGET_BLOCK_Z_KEY = "TargetBlockZ";
    private final static String ATTEMPTS_REMAINING_KEY = "AttemptsRemaining";
    private final static String IN_USE_KEY = "InUse";
    private final static String EXCURSION_BLOCK_PROP_NAME = "ExcursionBlockData";
    private final static String CURRENT_USER_KEY = "CurrentUser";

    private int maxAttempts = Config.maxExcursionAttempts;

    public TileEntityExcursionBlock() {
        super();
    }

    public void markDirty() {
        super.markDirty();
    }

    public void readFromNBT(NBTTagCompound compound) {
        NBTTagCompound properties = compound.getCompoundTag(EXCURSION_BLOCK_PROP_NAME);

        targetBlockX = properties.getInteger(TARGET_BLOCK_X_KEY);
        targetBlockY = properties.getInteger(TARGET_BLOCK_Y_KEY);
        targetBlockZ = properties.getInteger(TARGET_BLOCK_Z_KEY);

        currentUser = properties.getString(CURRENT_USER_KEY);

        attemptsRemaining = properties.getInteger(ATTEMPTS_REMAINING_KEY);

        inUse = properties.getBoolean(IN_USE_KEY);

        super.readFromNBT(compound);
    }

    public void writeToNBT(NBTTagCompound compound) {
        NBTTagCompound properties = new NBTTagCompound();

        // Save Values
        properties.setInteger(TARGET_BLOCK_X_KEY, targetBlockX);
        properties.setInteger(TARGET_BLOCK_Y_KEY, targetBlockY);
        properties.setInteger(TARGET_BLOCK_Z_KEY, targetBlockZ);
        properties.setInteger(ATTEMPTS_REMAINING_KEY, attemptsRemaining);

        properties.setString(CURRENT_USER_KEY, currentUser);

        properties.setBoolean(IN_USE_KEY, inUse);

        // Use unique tags to avoid conflicts
        compound.setTag(EXCURSION_BLOCK_PROP_NAME, properties);

        super.writeToNBT(compound);
    }

    public void setTargetBlock(int x, int y, int z, String username) {
        targetBlockX = x;
        targetBlockY = y;
        targetBlockZ = z;

        currentUser = username;

        inUse = true;
        attemptsRemaining = maxAttempts;

        markDirty();
    }

    public void decreaseAttemptsRemaining() {
        attemptsRemaining = attemptsRemaining - 1;
        markDirty();
    }

    public void reset() {
        attemptsRemaining = maxAttempts;
        targetBlockX = 0;
        targetBlockY = 0;
        targetBlockZ = 0;
        inUse = false;
        currentUser = "";

        markDirty();
    }

    public static ExcursionCoords getExcursionCoords(double day, int clickedBlockX, int clickedBlockZ) {
        int decimal = (int) day;
        double fraction = day - decimal;

        int clamp = 60;

        double clampedValue = decimal % clamp;

        if (clampedValue % 2 == 0) {
            clampedValue = clamp - clampedValue;
        }

        double degrees = (clampedValue) * 6 + fraction;

        int centerCircleX = 0;
        int centerCircleZ = 0;
        int circleRadius = 750;

        int x = (int) (centerCircleX + circleRadius * Math.cos(Math.toRadians(degrees)));
        int z = (int) (centerCircleZ + circleRadius * Math.sin(Math.toRadians(degrees)));

        x = x + clickedBlockX;
        z = z + clickedBlockZ;

        return new ExcursionCoords(x, z);
    }

    public int getMaxAttempts() {
        return maxAttempts;
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
