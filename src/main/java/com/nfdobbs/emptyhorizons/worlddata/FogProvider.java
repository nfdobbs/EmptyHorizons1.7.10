package com.nfdobbs.emptyhorizons.worlddata;

import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class FogProvider {

    protected World world;
    private FogData fogData;

    private static final float MIN_FOG_DENSITY = 0.05F;
    private static final float MAX_FOG_DENSITY = 0.5F;

    public FogProvider() {}

    public FogRecord GetFogRecord(World world, int dimensionID) {
        LoadOrSetupFogProvider(world);

        FogRecord fogRecord = fogData.GetDimensionFogData(dimensionID);

        if (fogRecord == null) {
            CreateFogForDimension(dimensionID);
            return fogData.GetDimensionFogData(dimensionID);
        }

        return fogRecord;
    }

    private void CreateFogForDimension(int dimensionID) {

        long worldSeed = world.getSeed();
        Random rd = new Random(worldSeed + dimensionID);

        float fogDensity = MIN_FOG_DENSITY + rd.nextFloat() * (MAX_FOG_DENSITY - MIN_FOG_DENSITY);
        float fogColorR = rd.nextFloat();
        float fogColorG = rd.nextFloat();
        float fogColorB = rd.nextFloat();

        NBTTagCompound compound = new NBTTagCompound();

        fogData.AddDimensionFogData(compound, dimensionID, fogDensity, fogColorR, fogColorG, fogColorB);
        fogData.setDirty(true);
    }

    private void LoadOrSetupFogProvider(World world) {
        this.world = world;

        if (fogData == null) {
            fogData = (FogData) world.mapStorage.loadData(FogData.class, "EmptyHorizonsFogData");

            if (fogData == null) {
                fogData = new FogData("EmptyHorizonsFogData");
                world.mapStorage.setData("EmptyHorizonsFogData", fogData);
            }
        }
    }
}
