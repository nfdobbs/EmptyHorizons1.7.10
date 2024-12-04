package com.nfdobbs.emptyhorizons.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class OverworldTeleporter extends Teleporter {

    private double teleportX;
    private double teleportY;
    private double teleportZ;
    private float teleportYaw;
    private float teleportPitch;

    public OverworldTeleporter(WorldServer worldIn, double tX, double tY, double tZ, float yaw, float pitch) {
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

    public static void TeleportToOverworld(int x, int z, EntityPlayer player) {
        if (player instanceof EntityPlayerMP playerMP) {
            int dim = 0;

            if (playerMP.dimension == dim) {
                player.setLocationAndAngles(x, 100, z, playerMP.rotationYaw, playerMP.rotationPitch);
                player.setPositionAndUpdate(x, 100, z);
            } else {
                playerMP.mcServer.getConfigurationManager()
                    .transferPlayerToDimension(
                        playerMP,
                        dim,
                        new OverworldTeleporter(
                            playerMP.mcServer.worldServerForDimension(dim),
                            x,
                            100,
                            z,
                            playerMP.rotationYaw,
                            playerMP.rotationPitch));
            }
        }
    }
}
