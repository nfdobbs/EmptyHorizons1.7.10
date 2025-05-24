package com.nfdobbs.emptyhorizons.mixins;

import com.gtnewhorizon.gtnhlib.mixin.ITargetedMod;

public enum TargetedModBetterQuesting implements ITargetedMod {

    BETTER_QUESTING;

    @Override
    public String getCoreModClass() {
        return null;
    }

    @Override
    public String getModId() {
        return null;
    }

    @Override
    public String getModName() {
        return "BetterQuesting";
    }
}
