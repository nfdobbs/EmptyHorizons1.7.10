package com.nfdobbs.emptyhorizons.core;

import java.util.List;
import java.util.Set;

import com.gtnewhorizon.gtnhlib.mixin.IMixins;
import com.gtnewhorizon.gtnhmixins.ILateMixinLoader;
import com.nfdobbs.emptyhorizons.mixins.Mixins;

public class EmptyHorizonsLateMixins implements ILateMixinLoader {

    @Override
    public String getMixinConfig() {
        return "mixins.emptyhorizons.late.json";
    }

    @Override
    public List<String> getMixins(Set<String> loadedMods) {
        return IMixins.getLateMixins(Mixins.class, loadedMods);
    }
}
