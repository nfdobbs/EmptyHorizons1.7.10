package com.nfdobbs.emptyhorizons.EmptyDimension;

import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;

public class EmptyDimProvider extends WorldProvider {

    @Override
    public String getDimensionName() {
        return "Empty Dimension";
    }

    @Override
    public IChunkProvider createChunkGenerator() {
        return new EmptyDimChunkGenerator(super.worldObj);
    }
}
