package com.nfdobbs.emptyhorizons.blocks;

import net.minecraft.block.material.Material;

import com.nfdobbs.emptyhorizons.EmptyHorizons;
import com.nfdobbs.emptyhorizons.tileentities.TileEntityEvacuationBlock;
import com.nfdobbs.emptyhorizons.tileentities.TileEntityExcursionBlock;

import cpw.mods.fml.common.registry.GameRegistry;

public class EmptyHorizonBlocks {

    public static ExcursionBlock excursionBlock;
    public static EvacuationBlock evacuationBlock;

    public static void init() {
        excursionBlock = new ExcursionBlock(Material.iron);
        evacuationBlock = new EvacuationBlock(Material.iron);

        GameRegistry.registerBlock(excursionBlock, excursionBlock.getUnlocalizedName());
        GameRegistry.registerBlock(evacuationBlock, evacuationBlock.getUnlocalizedName());
        GameRegistry.registerTileEntity(TileEntityExcursionBlock.class, EmptyHorizons.MODID + ":te_excursion_block");
        GameRegistry.registerTileEntity(TileEntityEvacuationBlock.class, EmptyHorizons.MODID + ":te_evacuation_block");
    }
}
