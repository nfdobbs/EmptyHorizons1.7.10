package com.nfdobbs.emptyhorizons.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import com.nfdobbs.emptyhorizons.EmptyHorizons;
import com.nfdobbs.emptyhorizons.gui.ExcursionBlockGUI;
import com.nfdobbs.emptyhorizons.tileentities.TileEntityExcursionBlock;

public class ExcursionBlock extends Block implements ITileEntityProvider {

    public IIcon ExcursionBlockFront;
    public IIcon ExcursionBlockFrontEast;
    public IIcon ExcursionBlockFrontSouth;
    public IIcon ExcursionBlockFrontWest;
    public IIcon ExcursionBlockSide;

    public IIcon[][] textureSideStates;

    private static final String EXCURSION_BLOCK_NAME = "ExcursionBlock";
    private static final Float EXCURSION_BLOCK_HARDNESS = 55.0F;
    private static final Float EXCURSION_BLOCK_LIGHT_LEVEL = 0.0F;
    private static final int EXCURSION_BLOCK_HARVEST_LEVEL = 3;
    private static final Float EXCURSION_BLOCK_RESISTANCE = 2000.0F;

    public static final int UP_OFFSET = 5;
    public static final int DOWN_OFFSET = 9;

    protected ExcursionBlock(Material materialIn) {
        super(materialIn);

        setBlockName(EXCURSION_BLOCK_NAME);
        setBlockTextureName(EmptyHorizons.MODID + ":" + getUnlocalizedName());
        setCreativeTab(CreativeTabs.tabTransport);

        setHardness(EXCURSION_BLOCK_HARDNESS);
        setLightLevel(EXCURSION_BLOCK_LIGHT_LEVEL);
        setHarvestLevel("pickaxe", EXCURSION_BLOCK_HARVEST_LEVEL);
        setResistance(EXCURSION_BLOCK_RESISTANCE);
        setStepSound(soundTypeMetal);

    }

    public void registerBlockIcons(IIconRegister icon) {
        ExcursionBlockFront = icon.registerIcon(EmptyHorizons.MODID + ":ExcursionBlockFront");
        ExcursionBlockFrontEast = icon.registerIcon(EmptyHorizons.MODID + ":ExcursionBlockFrontEast");
        ExcursionBlockFrontSouth = icon.registerIcon(EmptyHorizons.MODID + ":ExcursionBlockFrontSouth");
        ExcursionBlockFrontWest = icon.registerIcon(EmptyHorizons.MODID + ":ExcursionBlockFrontWest");
        ExcursionBlockSide = icon.registerIcon(EmptyHorizons.MODID + ":ExcursionBlockSide");

        textureSideStates = new IIcon[][] {
            // {UP, DOWN, SOUTH, NORTH, EAST, WEST}
            { ExcursionBlockSide, ExcursionBlockSide, ExcursionBlockSide, ExcursionBlockFront, ExcursionBlockSide,
                ExcursionBlockSide }, // Default State
            { ExcursionBlockSide, ExcursionBlockSide, ExcursionBlockFront, ExcursionBlockSide, ExcursionBlockSide,
                ExcursionBlockSide }, // Block Placed South
            { ExcursionBlockSide, ExcursionBlockSide, ExcursionBlockSide, ExcursionBlockFront, ExcursionBlockSide,
                ExcursionBlockSide }, // Block Placed North
            { ExcursionBlockSide, ExcursionBlockSide, ExcursionBlockSide, ExcursionBlockSide, ExcursionBlockFront,
                ExcursionBlockSide }, // Block Placed East
            { ExcursionBlockSide, ExcursionBlockSide, ExcursionBlockSide, ExcursionBlockSide, ExcursionBlockSide,
                ExcursionBlockFront }, // Block Placed West
            // Up values are inverted
            { ExcursionBlockFront, ExcursionBlockSide, ExcursionBlockSide, ExcursionBlockSide, ExcursionBlockSide,
                ExcursionBlockSide }, // Block Placed Up - Player South
            { ExcursionBlockFrontEast, ExcursionBlockSide, ExcursionBlockSide, ExcursionBlockSide, ExcursionBlockSide,
                ExcursionBlockSide }, // Block Placed Up - Player West
            { ExcursionBlockFrontSouth, ExcursionBlockSide, ExcursionBlockSide, ExcursionBlockSide, ExcursionBlockSide,
                ExcursionBlockSide }, // Block Placed Up - Player North
            { ExcursionBlockFrontWest, ExcursionBlockSide, ExcursionBlockSide, ExcursionBlockSide, ExcursionBlockSide,
                ExcursionBlockSide }, // Block Placed Up - Player East
            { ExcursionBlockSide, ExcursionBlockFrontSouth, ExcursionBlockSide, ExcursionBlockSide, ExcursionBlockSide,
                ExcursionBlockSide }, // Block Placed Down - Player South
            { ExcursionBlockSide, ExcursionBlockFrontWest, ExcursionBlockSide, ExcursionBlockSide, ExcursionBlockSide,
                ExcursionBlockSide }, // Block Placed Down - Player West
            { ExcursionBlockSide, ExcursionBlockFront, ExcursionBlockSide, ExcursionBlockSide, ExcursionBlockSide,
                ExcursionBlockSide }, // Block Placed Down - Player North
            { ExcursionBlockSide, ExcursionBlockFrontEast, ExcursionBlockSide, ExcursionBlockSide, ExcursionBlockSide,
                ExcursionBlockSide } // Block Placed Down - Player East
        };

    }

    public IIcon getIcon(int side, int meta) {

        return textureSideStates[meta][side];
    }

    @Override
    public int onBlockPlaced(World worldIn, int x, int y, int z, int side, float subX, float subY, float subZ,
        int meta) {

        if (side > 1) {
            return side - 1;
        } else if (side == 0) {
            return UP_OFFSET;
        } else if (side == 1) {
            return DOWN_OFFSET;
        } else {
            return 0;
        }
    }

    public void onBlockPlacedBy(World worldIn, int x, int y, int z, EntityLivingBase placer, ItemStack itemIn) {
        int currentMetaData = worldIn.getBlockMetadata(x, y, z);

        if (currentMetaData == UP_OFFSET || currentMetaData == DOWN_OFFSET) {
            int directionFacing = MathHelper.floor_double((double) (placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            int newMetaData = currentMetaData + directionFacing;

            // x, y, z, metadata, flag
            worldIn.setBlockMetadataWithNotify(x, y, z, newMetaData, 2);
        }
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7,
        float par8, float par9) {

        // Get Tile Entity

        TileEntityExcursionBlock tileEntityExcursionBlock = (TileEntityExcursionBlock) world.getTileEntity(x, y, z);

        if (tileEntityExcursionBlock != null) {

            // Check Player Names
            if (!tileEntityExcursionBlock.inUse
                || (tileEntityExcursionBlock.currentUser.equals(player.getDisplayName()))) {

                if (tileEntityExcursionBlock.attemptsRemaining < 1) {
                    if (!world.isRemote) {
                        EntityPlayerMP playerMP = (EntityPlayerMP) player;

                        WorldServer overworld = playerMP.mcServer.worldServerForDimension(0);
                        overworld.setBlockToAir(
                            tileEntityExcursionBlock.targetBlockX,
                            tileEntityExcursionBlock.targetBlockY,
                            tileEntityExcursionBlock.targetBlockZ);

                        overworld.notifyBlockChange(
                            tileEntityExcursionBlock.targetBlockX,
                            tileEntityExcursionBlock.targetBlockY,
                            tileEntityExcursionBlock.targetBlockZ,
                            getBlockById(0));

                        overworld.removeTileEntity(
                            tileEntityExcursionBlock.targetBlockX,
                            tileEntityExcursionBlock.targetBlockY,
                            tileEntityExcursionBlock.targetBlockZ);
                    }

                    tileEntityExcursionBlock.reset();
                }

                if (world.isRemote) {
                    Minecraft.getMinecraft()
                        .displayGuiScreen(new ExcursionBlockGUI(x, y, z));
                }

                return true;
            }
        }

        return false;
    }

    @Override
    public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta) {
        super.breakBlock(worldIn, x, y, z, blockBroken, meta);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityExcursionBlock();
    }
}
