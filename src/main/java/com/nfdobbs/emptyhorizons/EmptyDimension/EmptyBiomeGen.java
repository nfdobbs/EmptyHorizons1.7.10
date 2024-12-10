package com.nfdobbs.emptyhorizons.EmptyDimension;

import net.minecraft.world.biome.BiomeGenBase;

public class EmptyBiomeGen extends BiomeGenBase {

    public EmptyBiomeGen(int id) {
        super(id);
    }

    @Override
    public BiomeGenBase.TempCategory getTempCategory() {
        return TempCategory.COLD;
    }
}
