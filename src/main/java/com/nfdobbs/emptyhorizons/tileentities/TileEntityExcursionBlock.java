package com.nfdobbs.emptyhorizons.tileentities;

import net.minecraft.tileentity.TileEntity;

import com.nfdobbs.emptyhorizons.util.ExcursionCoords;

public class TileEntityExcursionBlock extends TileEntity {

    public TileEntityExcursionBlock() {
        super();
    }

    public void markDirty() {
        super.markDirty();
    }

    public static ExcursionCoords getExcursionCoords(double day) {
        int decimal = (int) day;
        double fraction = day - decimal;

        double degrees = (decimal % 60) * 6 + fraction;

        int centerCircleX = 0;
        int centerCircleZ = 0;
        int circleRadius = 750;

        int x = (int) (centerCircleX + circleRadius * Math.cos(Math.toRadians(degrees)));
        int z = (int) (centerCircleZ + circleRadius * Math.sin(Math.toRadians(degrees)));

        return new ExcursionCoords(x, z);
    }
}
