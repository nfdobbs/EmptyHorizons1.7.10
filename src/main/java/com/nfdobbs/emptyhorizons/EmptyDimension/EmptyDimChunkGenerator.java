package com.nfdobbs.emptyhorizons.EmptyDimension;

import java.util.Collections;
import java.util.List;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

public class EmptyDimChunkGenerator implements IChunkProvider {

    protected World world;

    public EmptyDimChunkGenerator(World worldIn) {
        world = worldIn;
    }

    @Override
    public boolean chunkExists(int x, int z) {
        return true;
    }

    @Override
    public Chunk provideChunk(int chunkX, int chunkZ) {
        return new Chunk(world, chunkX, chunkZ);
    }

    @Override
    public Chunk loadChunk(int x, int z) {
        return this.provideChunk(x, z);
    }

    @Override
    public void populate(IChunkProvider provider, int chunkX, int chunkZ) {

    }

    @Override
    public boolean saveChunks(boolean saveAllChunks, IProgressUpdate progress) {
        return true;
    }

    @Override
    public boolean unloadQueuedChunks() {
        return false;
    }

    @Override
    public boolean canSave() {
        return true;
    }

    @Override
    public String makeString() {
        return "EmptyDimensionSource";
    }

    @Override
    public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType type, int x, int y, int z) {
        return Collections.emptyList();
    }

    @Override
    public ChunkPosition func_147416_a(World world, String structureType, int x, int y, int z) {
        return null;
    }

    @Override
    public int getLoadedChunkCount() {
        return 0;
    }

    @Override
    public void recreateStructures(int x, int z) {

    }

    @Override
    public void saveExtraData() {

    }
}
