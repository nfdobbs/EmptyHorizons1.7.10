package com.nfdobbs.emptyhorizons.mixins;

import java.util.List;
import java.util.function.Supplier;

import com.gtnewhorizon.gtnhlib.mixin.*;

public enum Mixins implements IMixins {

    EXAMPLE_EARLY(new MixinBuilder("Mixin for adding text to Better Questing QuestLine GUI")
        .addTargetedMod(TargetedModBetterQuesting.BETTER_QUESTING)
        .setSide(Side.CLIENT)
        .setPhase(Phase.EARLY)
        .setApplyIf(() -> true)
        .addMixinClasses("MixinGuiQuestLines"));

    private final List<String> mixinClasses;
    private final Supplier<Boolean> applyIf;
    private final Phase phase;
    private final Side side;
    private final List<ITargetedMod> targetedMods;
    private final List<ITargetedMod> excludedMods;

    Mixins(MixinBuilder builder) {
        this.mixinClasses = builder.mixinClasses;
        this.applyIf = builder.applyIf;
        this.side = builder.side;
        this.targetedMods = builder.targetedMods;
        this.excludedMods = builder.excludedMods;
        this.phase = builder.phase;
        if (this.targetedMods.isEmpty()) {
            throw new RuntimeException("No targeted mods specified for " + this.name());
        }
        if (this.applyIf == null) {
            throw new RuntimeException("No ApplyIf function specified for " + this.name());
        }
    }

    public List<String> getMixinClasses() {
        return mixinClasses;
    }

    public Supplier<Boolean> getApplyIf() {
        return applyIf;
    }

    public Phase getPhase() {
        return phase;
    }

    public Side getSide() {
        return side;
    }

    public List<ITargetedMod> getTargetedMods() {
        return targetedMods;
    }

    public List<ITargetedMod> getExcludedMods() {
        return excludedMods;
    }

}
