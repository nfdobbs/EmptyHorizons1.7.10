package com.nfdobbs.emptyhorizons.EmptyDimension;

import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.IChunkProvider;

public class EmptyDimProvider extends WorldProvider {

    public static BiomeGenBase emptyBiome;

    public EmptyDimProvider() {
        emptyBiome = new EmptyBiomeGen(191).setColor(40)
            .setBiomeName("Empty Biome");
    }

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
        return emptyBiome;
    }

    @Override
    public void registerWorldChunkManager() {
        this.worldChunkMgr = new WorldChunkManagerHell(emptyBiome, 0.5F);
    }
}
