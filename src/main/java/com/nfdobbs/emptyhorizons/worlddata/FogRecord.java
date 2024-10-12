package com.nfdobbs.emptyhorizons.worlddata;

public class FogRecord {

    private float fogDensity = -10F;
    private float fogColorR = -10F;
    private float fogColorG = -10F;
    private float fogColorB = -10F;

    public FogRecord(float fogDensity, float fogColorR, float fogColorG, float fogColorB) {
        this.fogDensity = fogDensity;
        this.fogColorR = fogColorR;
        this.fogColorG = fogColorG;
        this.fogColorB = fogColorB;
    }

    public float getFogDensity() {
        return fogDensity;
    }

    public float getFogColorR() {
        return fogColorR;
    }

    public float getFogColorG() {
        return fogColorG;
    }

    public float getFogColorB() {
        return fogColorB;
    }
}
