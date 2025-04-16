package com.nfdobbs.emptyhorizons.EmptyDimension;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.DimensionManager;

public class EmptyDimRegister {

    public static final String EMPTY_DIMENSION_NAME = "EmptyDimension";
    public static final int EMPTY_DIMENSION_ID = 404;

    public static BiomeGenBase emptyBiome = new EmptyBiomeGen(191).setColor(40)
        .setBiomeName("Empty Biome");

    public static void registerDimensions() {
        DimensionManager.registerProviderType(EMPTY_DIMENSION_ID, EmptyDimProvider.class, false);
        DimensionManager.registerDimension(EMPTY_DIMENSION_ID, EMPTY_DIMENSION_ID);
    }

    public static void registerBiomes() {
        BiomeManager.addBiome(BiomeManager.BiomeType.ICY, new BiomeManager.BiomeEntry(emptyBiome, 1000));

        BiomeDictionary.registerBiomeType(emptyBiome, BiomeDictionary.Type.COLD);
    }

}
