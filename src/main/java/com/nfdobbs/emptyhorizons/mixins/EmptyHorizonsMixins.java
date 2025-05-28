package com.nfdobbs.emptyhorizons.mixins;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.gtnewhorizon.gtnhmixins.ILateMixinLoader;
import com.gtnewhorizon.gtnhmixins.LateMixin;

import cpw.mods.fml.common.FMLCommonHandler;

@LateMixin
public class EmptyHorizonsMixins implements ILateMixinLoader {

    @Override
    public String getMixinConfig() {
        return "mixins.emptyhorizons.late.json";
    }

    @Override
    public List<String> getMixins(Set<String> loadedMods) {
        List<String> mixins = new ArrayList<>();

        if (FMLCommonHandler.instance()
            .getSide()
            .isClient()) {
            mixins.add("MixinGuiQuest");
            mixins.add("MixinGuiQuestLines");
        }

        return mixins;
    }
}
