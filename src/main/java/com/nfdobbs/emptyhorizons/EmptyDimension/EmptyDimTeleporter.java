package com.nfdobbs.emptyhorizons.EmptyDimension;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class EmptyDimTeleporter extends Teleporter {

    private double teleportX;
    private double teleportY;
    private double teleportZ;
    private float teleportYaw;
    private float teleportPitch;

    public EmptyDimTeleporter(WorldServer worldIn, double tX, double tY, double tZ, float yaw, float pitch) {
        super(worldIn);

        teleportX = tX;
        teleportY = tY;
        teleportZ = tZ;
        teleportYaw = yaw;
        teleportPitch = pitch;
    }

    @Override
    public void placeInPortal(Entity entityIn, double x, double y, double z, float r) {
        entityIn
            .setLocationAndAngles(this.teleportX, this.teleportY, this.teleportZ, this.teleportYaw, this.teleportPitch);
    }

    @Override
    public void removeStalePortalLocations(long par1) {

    }

    @Override
    public boolean placeInExistingPortal(Entity entityIn, double x, double y, double z, float r) {
        return false;
    }

    public static void teleportToEmptyDim(EntityPlayer player, double x, double y, double z, float yaw, float pitch) {
        if (player instanceof EntityPlayerMP playerMP) {
            int dim = EmptyDimRegister.EMPTY_DIMENSION_ID;

            if (playerMP.dimension == dim) {
                player.setLocationAndAngles(x, y, z, yaw, pitch);
                player.setPositionAndUpdate(x, y, z);
            } else {

                double xCenter = 0;
                double zCenter = 0;

                if(x < 0)
                {
                    xCenter = -0.5;
                }
                else
                {
                    xCenter = 0.5;
                }

                if(z < 0)
                {
                    zCenter = -0.5;
                }
                else
                {
                    zCenter = 0.5;
                }

                playerMP.mcServer.getConfigurationManager()
                    .transferPlayerToDimension(
                        playerMP,
                        dim,
                        new EmptyDimTeleporter(
                            playerMP.mcServer.worldServerForDimension(dim),
                            x + xCenter,
                            y,
                            z + zCenter,
                            yaw,
                            pitch));
            }
        }
    }
}
