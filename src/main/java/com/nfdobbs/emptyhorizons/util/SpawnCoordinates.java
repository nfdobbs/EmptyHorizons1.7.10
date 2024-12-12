package com.nfdobbs.emptyhorizons.util;

import com.nfdobbs.emptyhorizons.Config;

public class SpawnCoordinates {

    // n should start with 1 and not the usual 0
    public static ExcursionCoords getSpawnCoords(int n) {

        if (n < 1) {
            return new ExcursionCoords(0, 0);
        }

        int spawnMultiplier = Config.challengeSpawnSeparation;

        int x = getXCoord(n) * spawnMultiplier;
        int z = getZCoord(n) * spawnMultiplier;

        return new ExcursionCoords(x, z);
    }

    private static int getXCoord(int n) {
        if (n == 1) {
            return 0;
        } else {
            return (int) (getXCoord(n - 1) + Math.sin(Math.floor(Math.sqrt(4 * n - 7)) * Math.PI / 2));
        }
    }

    private static int getZCoord(int n) {
        if (n == 1) {
            return 0;
        } else {
            return (int) (getZCoord(n - 1) + Math.cos(Math.floor(Math.sqrt(4 * n - 7)) * Math.PI / 2));
        }
    }
}
