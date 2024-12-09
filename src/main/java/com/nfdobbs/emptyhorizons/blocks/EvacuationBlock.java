package com.nfdobbs.emptyhorizons.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.nfdobbs.emptyhorizons.EmptyDimension.EmptyDimTeleporter;
import com.nfdobbs.emptyhorizons.EmptyHorizons;
import com.nfdobbs.emptyhorizons.tileentities.TileEntityEvacuationBlock;
import com.nfdobbs.emptyhorizons.tileentities.TileEntityExcursionBlock;

public class EvacuationBlock extends Block implements ITileEntityProvider {

    public IIcon EvacuationBlockTop;
    public IIcon EvacuationBlockSide;

    private static final String EVACUATION_BLOCK_NAME = "EvacuationBlock";
    private static final Float EVACUATION_BLOCK_HARDNESS = -1.0F;
    private static final Float EVACUATION_BLOCK_LIGHT_LEVEL = 5.0F;
    private static final int EVACUATION_BLOCK_HARVEST_LEVEL = 3;
    private static final Float EVACUATION_BLOCK_RESISTANCE = 20000.0F;

    protected EvacuationBlock(Material materialIn) {
        super(materialIn);

        setBlockName(EVACUATION_BLOCK_NAME);
        setBlockTextureName(EmptyHorizons.MODID + ":" + getUnlocalizedName());
        setCreativeTab(CreativeTabs.tabTransport);

        setHardness(EVACUATION_BLOCK_HARDNESS);
        setLightLevel(EVACUATION_BLOCK_LIGHT_LEVEL);
        setHarvestLevel("pickaxe", EVACUATION_BLOCK_HARVEST_LEVEL);
        setResistance(EVACUATION_BLOCK_RESISTANCE);
        setStepSound(soundTypeMetal);
    }

    public void registerBlockIcons(IIconRegister icon) {
        EvacuationBlockTop = icon.registerIcon(EmptyHorizons.MODID + ":EvacuationBlockTop");
        EvacuationBlockSide = icon.registerIcon(EmptyHorizons.MODID + ":ExcursionBlockSide");
    }

    public IIcon getIcon(int side, int meta) {
        if (side == 1) {
            return EvacuationBlockTop;
        } else {
            return EvacuationBlockSide;
        }
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7,
        float par8, float par9) {

        if (!world.isRemote) {
            TileEntityEvacuationBlock tileEntityEvacuationBlock = (TileEntityEvacuationBlock) world
                .getTileEntity(x, y, z);

            if (tileEntityEvacuationBlock == null
                || !tileEntityEvacuationBlock.targetUser.equals(player.getDisplayName())) {
                return false;
            } else {
                // Teleport Player Home
                EmptyDimTeleporter.teleportToEmptyDim(
                    player,
                    tileEntityEvacuationBlock.returnPlayerX,
                    tileEntityEvacuationBlock.returnPlayerY,
                    tileEntityEvacuationBlock.returnPlayerZ,
                    0,
                    0);

                // Remove Evacuation Block
                world.removeTileEntity(x, y, z);
                world.setBlockToAir(x, y, z);

                TileEntityExcursionBlock tileEntityExcursionBlock = (TileEntityExcursionBlock) player.getEntityWorld()
                    .getTileEntity(
                        tileEntityEvacuationBlock.returnBlockX,
                        tileEntityEvacuationBlock.returnBlockY,
                        tileEntityEvacuationBlock.returnBlockZ);

                if (tileEntityExcursionBlock != null) {
                    tileEntityExcursionBlock.reset();
                }

                return true;
            }
        }
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityEvacuationBlock();
    }
}
