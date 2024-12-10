package com.nfdobbs.emptyhorizons.EmptyDimension;

import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.IChunkProvider;

import com.nfdobbs.emptyhorizons.EmptyHorizons;

public class EmptyDimProvider extends WorldProvider {

    @Override
    public String getDimensionName() {
        return "Empty Dimension";
    }

    @Override
    public IChunkProvider createChunkGenerator() {
        return new EmptyDimChunkGenerator(super.worldObj);
    }

    @Override
    public BiomeGenBase getBiomeGenForCoords(int x, int z) {
        return EmptyHorizons.emptyBiome;
    }

    @Override
    public void registerWorldChunkManager() {
        this.worldChunkMgr = new WorldChunkManagerHell(EmptyHorizons.emptyBiome, 0.5F);
    }
}
