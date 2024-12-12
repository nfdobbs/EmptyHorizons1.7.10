package com.nfdobbs.emptyhorizons.worlddata;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;

import com.nfdobbs.emptyhorizons.EmptyHorizons;

public class SpawnData extends WorldSavedData {

    final static String key = EmptyHorizons.MODID + "_SpawnData";
    private NBTTagCompound data = new NBTTagCompound();

    public SpawnData(String tagName) {
        super(tagName);
    }

    public static SpawnData forWorld(World world) {
        MapStorage storage = world.perWorldStorage;
        SpawnData result = (SpawnData) storage.loadData(SpawnData.class, key);
        if (result == null) {
            result = new SpawnData(key);
            storage.setData(key, result);
        }

        return result;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        data = compound.getCompoundTag(key);
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        compound.setTag(key, data);
    }

    public NBTTagCompound getData() {
        return data;
    }
}
