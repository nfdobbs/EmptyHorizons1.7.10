package com.nfdobbs.emptyhorizons.EmptyDimension;

import net.minecraftforge.common.DimensionManager;

public class EmptyDimRegister {

    public static final String EMPTY_DIMENSION_NAME = "EmptyDimension";
    public static final int EMPTY_DIMENSION_ID = -404;

    public static final void registerDimensions() {
        DimensionManager.registerProviderType(EMPTY_DIMENSION_ID, EmptyDimProvider.class, false);
        DimensionManager.registerDimension(EMPTY_DIMENSION_ID, EMPTY_DIMENSION_ID);
    }
}
