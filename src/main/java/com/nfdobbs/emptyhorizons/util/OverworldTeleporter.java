package com.nfdobbs.emptyhorizons.util;

import net.minecraft.entity.Entity;
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

    public static void TeleportToOverworld(int x, int y, int z, WorldServer world, EntityPlayerMP playerMP) {
        int dim = 0;

        if (playerMP.dimension == dim) {
            playerMP.setLocationAndAngles(x, 100, z, playerMP.rotationYaw, playerMP.rotationPitch);
            playerMP.setPositionAndUpdate(x, 100, z);
        } else {

            playerMP.mcServer.getConfigurationManager()
                .transferPlayerToDimension(
                    playerMP,
                    dim,
                    new OverworldTeleporter(world, x + .5, y, z + .5, playerMP.rotationYaw, playerMP.rotationPitch));
        }
    }

    public static int getTopBlock(int x, int z, WorldServer worldServer) {

        int worldHeight = 255;
        int y = worldHeight;
        boolean blockFound = false;

        while (y > 1) {
            if (!worldServer.isAirBlock(x, y, z)) {
                return y;
            }

            y = y - 1;
        }

        return worldHeight;
    }
}
