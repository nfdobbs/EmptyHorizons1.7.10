package com.nfdobbs.emptyhorizons.worlddata;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

import com.nfdobbs.emptyhorizons.EmptyHorizons;

public class FogData extends WorldSavedData {

    private static final String key = EmptyHorizons.MODID + "Fog";
    private NBTTagCompound data = new NBTTagCompound();

    public FogData(String tagName) {
        super(tagName);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        data = compound.getCompoundTag(key);
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        compound.setTag(key, data);
    }

    public NBTTagCompound getFogData() {
        return data;
    }

    public void AddDimensionFogData(NBTTagCompound compound, int dimension, float fogDensity, float fogColorR,
        float fogColorG, float fogColorB) {

        // NBTTagCompound compound = new NBTTagCompound();
        compound.setFloat("FogDensity", fogDensity);
        compound.setFloat("FogColorR", fogColorR);
        compound.setFloat("FogColorG", fogColorG);
        compound.setFloat("FogColorB", fogColorB);
        this.data.setTag(GetFogKey(dimension), compound);
        // this.setDirty(true);
    }

    public FogRecord GetDimensionFogData(int dimension) {
        NBTTagCompound compound = (NBTTagCompound) this.data.getTag(GetFogKey(dimension));

        if (compound == null) {
            return null;
        }

        return new FogRecord(
            compound.getFloat("FogDensity"),
            compound.getFloat("FogColorR"),
            compound.getFloat("FogColorG"),
            compound.getFloat("FogColorB"));
    }

    private String GetFogKey(int dimension) {
        return "" + dimension;
    }
}
