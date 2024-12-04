package com.nfdobbs.emptyhorizons.blocks;

import net.minecraft.block.material.Material;

import cpw.mods.fml.common.registry.GameRegistry;

public class EmptyHorizonBlocks {

    public static ExcursionBlock excursionBlock;

    public static void init() {
        excursionBlock = new ExcursionBlock(Material.iron);
        GameRegistry.registerBlock(excursionBlock, excursionBlock.getUnlocalizedName());
    }
}
