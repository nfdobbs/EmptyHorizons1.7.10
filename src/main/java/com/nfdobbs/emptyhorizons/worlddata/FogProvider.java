package com.nfdobbs.emptyhorizons.worlddata;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

import com.nfdobbs.emptyhorizons.CommonProxy;
import com.nfdobbs.emptyhorizons.network.FogDataMessage;

public class FogProvider {

    protected World world;
    private FogData fogData;
    public Boolean showWelcomeMessage = false;

    private static final float MIN_FOG_DENSITY = 0.05F;
    private static final float MAX_FOG_DENSITY = 0.5F;

    public FogProvider() {}

    public FogRecord GetFogRecord(World world, int dimensionID) {
        LoadOrSetupFogProvider(world);

        FogRecord fogRecord = fogData.GetDimensionFogData(dimensionID);

        if (fogRecord != null) {
            return fogRecord;
        }

        if (!world.isRemote) {
            CreateFogForDimension(dimensionID);
            return fogData.GetDimensionFogData(dimensionID);
        } else {
            return null;
        }
    }

    private void CreateFogForDimension(int dimensionID) {

        float fogDensity;
        float fogColorR;
        float fogColorG;
        float fogColorB;

        if (dimensionID == 0) {
            fogDensity = .3044337F;
            fogColorR = .21159399F;
            fogColorG = .9669117F;
            fogColorB = .7094888F;
        } else {
            long worldSeed = world.getSeed();
            Random rd = new Random(worldSeed + dimensionID);

            fogDensity = MIN_FOG_DENSITY + rd.nextFloat() * (MAX_FOG_DENSITY - MIN_FOG_DENSITY);
            fogColorR = rd.nextFloat();
            fogColorG = rd.nextFloat();
            fogColorB = rd.nextFloat();
        }

        fogData.AddDimensionFogData(dimensionID, fogDensity, fogColorR, fogColorG, fogColorB);
        fogData.setDirty(true);
    }

    private void LoadOrSetupFogProvider(World currentWorld) {
        world = currentWorld;

        if (fogData == null) {
            fogData = (FogData) currentWorld.mapStorage.loadData(FogData.class, "EmptyHorizonsFogData");

            if (fogData == null) {
                fogData = new FogData("EmptyHorizonsFogData");
                world.mapStorage.setData("EmptyHorizonsFogData", fogData);
            }
        }
    }

    public void SyncFogData(EntityPlayerMP player) {
        if (!player.worldObj.isRemote) {
            LoadOrSetupFogProvider(player.worldObj);
            CommonProxy.networkWrapper.sendTo(new FogDataMessage(fogData), player);
        }
    }

    public void SetFogData(FogData newFogData) {
        fogData = newFogData;
    }

    public Boolean DimHasFogData(World world, int dimensionID) {
        LoadOrSetupFogProvider(world);

        return fogData.DimHasFogKey(dimensionID);
    }
}
