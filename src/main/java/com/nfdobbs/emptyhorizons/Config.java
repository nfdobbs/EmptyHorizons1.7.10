package com.nfdobbs.emptyhorizons;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import com.nfdobbs.emptyhorizons.EmptyDimension.EmptyDimRegister;

public class Config {

    // Quest Line Multipliers
    private final static String QUEST_LINE_MULTIPLIER_CATEGORY = "quest_line_multipliers";
    private final static Float MIN_FLOAT_VALUE = 0.25f;
    private final static Float MAX_FLOAT_VALUE = 100f;
    private final static Float DEFAULT_FLOAT_VALUE = 1f;
    private final static String QUEST_LINE_MULTIPLIER_COMMENT = "BQ QuestLine names here will have their quest time reward multiplied by the given float value (0.25f - 100f).";

    // Safe Dimensions
    private final static String SAFE_DIMENSIONS_CATEGORY = "safe_dimensions";
    private final static int MIN_DIM_VALUE = -100000;
    private final static int MAX_DIM_VALUE = 100000;
    private final static int DEFAULT_DIM_VALUE = EmptyDimRegister.EMPTY_DIMENSION_ID;
    private final static String SAFE_DIMENSIONS_COMMENT = "Dimensions IDs here will be safe from building exposure.";

    // Fuzzy Safe Dimensions Name
    private final static String SAFE_DIM_NAMES_CATEGORY = "safe_dim_names";
    private final static String DEFAULT_SAFE_DIM_NAME = "";
    private final static String SAFE_DIM_NAMES_COMMENT = "Dimension Names and Partial Dimension Names will be safe from building exposure. EX: 'Space Station' should make all the Galacticraft Space Station dimensions safe.";

    // Dimension Multipliers
    private final static String DIMENSION_MULTIPLIERS_CATEGORY = "dimension_multipliers";
    private final static Float MIN_MULTIPLIER = 0.25f;
    private final static Float MAX_MULTIPLIER = 100f;
    private final static Float DEFAULT_MULTIPLIER = 1f;
    private final static String DIMENSION_MULTIPLIERS_COMMENT = "Dimensions IDs here will have their exposure buildup rate changed by the provided Float";

    // Challenge Spawn Separation
    public static int challengeSpawnSeparation = 1000;
    private final static int MIN_CHALLENGE_SPAWN_SEPARATION = 50;
    private final static int MAX_CHALLENGE_SPAWN_SEPARATION = 10000;
    private final static String CHALLENGE_SPAWN_SEPARATION_COMMENT = "Distance that challenge players will spawn away from each other.";

    // Starting Time Values
    public static int startingExposureTime = 20;
    private final static int MIN_STARTING_EXPOSURE = 1;
    private final static int MAX_STARTING_EXPOSURE = 3600;
    private final static String STARTING_EXPOSURE_TIME_COMMENT = "Initial Max Exposure Time.";

    // Max Excursion Attempts
    public static int maxExcursionAttempts = 5;
    private final static int MIN_EXCURSION_ATTEMPTS = 1;
    private final static int MAX_EXCURSION_ATTEMPTS = 1000;
    private final static String EXCURSION_ATTEMPTS_COMMENT = "Number of attempts to allow per location.";

    // Base Main Quest Rewards
    public static int baseMainQuestReward = 9;
    private final static int MIN_BASE_MAIN_QUEST_REWARD = 0;
    private final static int MAX_BASE_MAIN_QUEST_REWARD = 600;
    private final static String BASE_MAIN_QUEST_REWARD_COMMENT = "Base time to give before multipliers for main quests.";

    // Base Optional Quest Rewards
    public static int baseOptionalQuestReward = 3;
    private final static int MIN_BASE_OPTIONAL_QUEST_REWARD = 0;
    private final static int MAX_BASE_OPTIONAL_QUEST_REWARD = 600;
    private final static String BASE_OPTIONAL_QUEST_REWARD_COMMENT = "Base time to give before multipliers for optional quests.";

    // Achievement Rewards
    public static int achievementReward = 5;
    private final static int MIN_ACHIEVEMENT_REWARD = 0;
    private final static int MAX_ACHIEVEMENT_REWARD = 600;
    private final static String BASE_ACHIEVEMENT_REWARD_COMMENT = "Base time to give for completing an achievement.";

    public static String greeting = "Welcome to suffering";

    public static HashMap<String, Float> questLineMultipliers = new HashMap<>();

    public static HashMap<Integer, Float> dimensionMultipliers = new HashMap<>();

    public static HashMap<String, Integer> safeDimensions = new HashMap<String, Integer>();

    public static List<String> safeFuzzyDimNames = new ArrayList<>();

    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);

        greeting = configuration.getString("greeting", Configuration.CATEGORY_GENERAL, greeting, "How shall I greet?");

        challengeSpawnSeparation = configuration.getInt(
            "challengeSpawnSeparation",
            Configuration.CATEGORY_GENERAL,
            challengeSpawnSeparation,
            MIN_CHALLENGE_SPAWN_SEPARATION,
            MAX_CHALLENGE_SPAWN_SEPARATION,
            CHALLENGE_SPAWN_SEPARATION_COMMENT);

        startingExposureTime = configuration.getInt(
            "startingExposureTime",
            Configuration.CATEGORY_GENERAL,
            startingExposureTime,
            MIN_STARTING_EXPOSURE,
            MAX_STARTING_EXPOSURE,
            STARTING_EXPOSURE_TIME_COMMENT);

        maxExcursionAttempts = configuration.getInt(
            "maxExcursionAttempts",
            Configuration.CATEGORY_GENERAL,
            maxExcursionAttempts,
            MIN_EXCURSION_ATTEMPTS,
            MAX_EXCURSION_ATTEMPTS,
            EXCURSION_ATTEMPTS_COMMENT);

        baseMainQuestReward = configuration.getInt(
            "baseMainQuestReward",
            Configuration.CATEGORY_GENERAL,
            baseMainQuestReward,
            MIN_BASE_MAIN_QUEST_REWARD,
            MAX_BASE_MAIN_QUEST_REWARD,
            BASE_MAIN_QUEST_REWARD_COMMENT);

        baseOptionalQuestReward = configuration.getInt(
            "baseOptionalQuestReward",
            Configuration.CATEGORY_GENERAL,
            baseOptionalQuestReward,
            MIN_BASE_OPTIONAL_QUEST_REWARD,
            MAX_BASE_OPTIONAL_QUEST_REWARD,
            BASE_OPTIONAL_QUEST_REWARD_COMMENT);

        achievementReward = configuration.getInt(
            "achievementReward",
            Configuration.CATEGORY_GENERAL,
            achievementReward,
            MIN_ACHIEVEMENT_REWARD,
            MAX_ACHIEVEMENT_REWARD,
            BASE_ACHIEVEMENT_REWARD_COMMENT);

        // Safe Dimensions
        configuration = configuration.setCategoryComment(SAFE_DIMENSIONS_CATEGORY, SAFE_DIMENSIONS_COMMENT);

        var safeDimensionsConfig = configuration.getCategory(SAFE_DIMENSIONS_CATEGORY);

        if (safeDimensionsConfig.isEmpty()) {
            safeDimensionsConfig.put("Empty Dimension", new Property("Empty Dimension", "404", Property.Type.INTEGER));
        }

        // Get all the multipliers
        for (var entry : safeDimensionsConfig.entrySet()) {
            Integer safeDim = configuration
                .getInt(entry.getKey(), SAFE_DIMENSIONS_CATEGORY, DEFAULT_DIM_VALUE, MIN_DIM_VALUE, MAX_DIM_VALUE, "");

            safeDimensions.put(entry.getKey(), safeDim);
        }

        // Quest Line Multipliers
        configuration = configuration.setCategoryComment(QUEST_LINE_MULTIPLIER_CATEGORY, QUEST_LINE_MULTIPLIER_COMMENT);

        var questLineMultipliersConfig = configuration.getCategory(QUEST_LINE_MULTIPLIER_CATEGORY);

        if (questLineMultipliers.isEmpty()) {
            questLineMultipliersConfig
                .put("Tier 0 - Stone Age", new Property("Tier 0 - Stone Age", "0.5", Property.Type.DOUBLE));
            questLineMultipliersConfig
                .put("Tier 0.5 - Steam", new Property("Tier 0.5 - Steam", "0.75", Property.Type.DOUBLE));
            questLineMultipliersConfig.put("Tier 1 - LV", new Property("Tier 1 - LV", "1", Property.Type.DOUBLE));
            questLineMultipliersConfig.put("Tier 2 - MV", new Property("Tier 2 - MV", "1.5", Property.Type.DOUBLE));
            questLineMultipliersConfig.put("Tier 3 - HV", new Property("Tier 3 - HV", "2", Property.Type.DOUBLE));
            questLineMultipliersConfig.put("Tier 4 - EV", new Property("Tier 4 - EV", "3", Property.Type.DOUBLE));
            questLineMultipliersConfig.put("Tier 5 - IV", new Property("Tier 5 - IV", "4", Property.Type.DOUBLE));
            questLineMultipliersConfig.put("Tier 6 - LuV", new Property("Tier 6 - LuV", "5", Property.Type.DOUBLE));
            questLineMultipliersConfig.put("Tier 7 - ZPM", new Property("Tier 7 - ZPM", "6", Property.Type.DOUBLE));
            questLineMultipliersConfig.put("Tier 8 - UV", new Property("Tier 8 - UV", "7", Property.Type.DOUBLE));
            questLineMultipliersConfig.put("Tier 9 - UHV", new Property("Tier 9 - UHV", "13", Property.Type.DOUBLE));
            questLineMultipliersConfig.put("Tier 10 - UEV", new Property("Tier 10 - UEV", "14", Property.Type.DOUBLE));
            questLineMultipliersConfig.put("Tier 11 - UIV", new Property("Tier 11 - UIV", "15", Property.Type.DOUBLE));
            questLineMultipliersConfig.put("Tier 12 - UMV", new Property("Tier 12 - UMV", "16", Property.Type.DOUBLE));
            questLineMultipliersConfig.put("Endgame Goals", new Property("Endgame Goals", "200", Property.Type.DOUBLE));
            questLineMultipliersConfig.put(
                "Fishing, Farming, Cooking",
                new Property("Fishing, Farming, Cooking", "1.25", Property.Type.DOUBLE));
            questLineMultipliersConfig
                .put("Getting Around...", new Property("Getting Around...", "2", Property.Type.DOUBLE));
            questLineMultipliersConfig
                .put("Forestry and Multifarms", new Property("Forestry and Multifarms", "1.5", Property.Type.DOUBLE));
            questLineMultipliersConfig
                .put("Multiblock Goals", new Property("Multiblock Goals", "5", Property.Type.DOUBLE));
            questLineMultipliersConfig
                .put("Mass Processing", new Property("Mass Processing", "8", Property.Type.DOUBLE));
            questLineMultipliersConfig
                .put("How to Generate Power", new Property("How to Generate Power", "3", Property.Type.DOUBLE));
            questLineMultipliersConfig.put(
                "Storing and Transforming EU",
                new Property("Storing and Transforming EU", "4", Property.Type.DOUBLE));
            questLineMultipliersConfig
                .put("Working with Oil", new Property("Working with Oil", "2.5", Property.Type.DOUBLE));
            questLineMultipliersConfig
                .put("Bio for the Masses", new Property("Bio for the Masses", "2.5", Property.Type.DOUBLE));
            questLineMultipliersConfig
                .put("Powerful Nuclear Physics", new Property("Powerful Nuclear Physics", "7", Property.Type.DOUBLE));
            questLineMultipliersConfig.put("Space Race", new Property("Space Race", "4.5", Property.Type.DOUBLE));
            questLineMultipliersConfig
                .put("Basic Automation", new Property("Basic Automation", "1.5", Property.Type.DOUBLE));
            questLineMultipliersConfig
                .put("SFM and Computers", new Property("SFM and Computers", "3", Property.Type.DOUBLE));
            questLineMultipliersConfig.put(
                "Handle Logistics with Pipes",
                new Property("Handle Logistics with Pipes", "2.5", Property.Type.DOUBLE));
            questLineMultipliersConfig
                .put("Applied Energistics", new Property("Applied Energistics", "5", Property.Type.DOUBLE));
            questLineMultipliersConfig.put("Be(e) Breeding", new Property("Be(e) Breeding", "3", Property.Type.DOUBLE));
            questLineMultipliersConfig.put(
                "Hardcore End(er) Expansion",
                new Property("Hardcore End(er) Expansion", "4.5", Property.Type.DOUBLE));
            questLineMultipliersConfig
                .put("Novice Thaumaturgy", new Property("Novice Thaumaturgy", "1.5", Property.Type.DOUBLE));
            questLineMultipliersConfig
                .put("Adept Thaumaturgy", new Property("Adept Thaumaturgy", "3", Property.Type.DOUBLE));
            questLineMultipliersConfig
                .put("Kaaami, Haaaami, ... HA!", new Property("Kaaami, Haaaami, ... HA!", "5", Property.Type.DOUBLE));
            questLineMultipliersConfig
                .put("Focus on Wand Foci & EMT", new Property("Focus on Wand Foci & EMT", "2", Property.Type.DOUBLE));
            questLineMultipliersConfig.put("Flower Power", new Property("Flower Power", "3", Property.Type.DOUBLE));
            questLineMultipliersConfig
                .put("Look to the Edges", new Property("Look to the Edges", "2.5", Property.Type.DOUBLE));
            questLineMultipliersConfig
                .put("Paying the Highest Price", new Property("Paying the Highest Price", "3.5", Property.Type.DOUBLE));
        }

        // Get all the multipliers
        for (var entry : questLineMultipliersConfig.entrySet()) {
            Float questMultiplier = configuration.getFloat(
                entry.getKey(),
                QUEST_LINE_MULTIPLIER_CATEGORY,
                DEFAULT_FLOAT_VALUE,
                MIN_FLOAT_VALUE,
                MAX_FLOAT_VALUE,
                "");
            questLineMultipliers.put(entry.getKey(), questMultiplier);
        }

        // Dimension Multipliers
        configuration = configuration.setCategoryComment(DIMENSION_MULTIPLIERS_CATEGORY, DIMENSION_MULTIPLIERS_COMMENT);

        var dimensionMultiplierConfig = configuration.getCategory(DIMENSION_MULTIPLIERS_CATEGORY);

        if (dimensionMultiplierConfig.isEmpty()) {
            dimensionMultiplierConfig.put("1", new Property("1", "1.5", Property.Type.DOUBLE));
        }

        // Get Rate Multipliers
        for (var entry : dimensionMultiplierConfig.entrySet()) {
            String dimIdString = entry.getKey();

            Float dimMultiplier = configuration.getFloat(
                dimIdString,
                DIMENSION_MULTIPLIERS_CATEGORY,
                DEFAULT_MULTIPLIER,
                MIN_MULTIPLIER,
                MAX_MULTIPLIER,
                "");

            Integer dimension = null;

            try {
                dimension = Integer.parseInt(dimIdString);
            } catch (NumberFormatException ex) {
                EmptyHorizons.LOG.warn("Failed to convert '{}' to dimension ID.", entry.getKey());
            }

            if (dimension != null && !dimensionMultipliers.containsKey(dimension)) {
                dimensionMultipliers.put(dimension, dimMultiplier);
            }
        }

        // Fuzzy Dim Names
        configuration = configuration.setCategoryComment(SAFE_DIM_NAMES_CATEGORY, SAFE_DIM_NAMES_COMMENT);

        var safeFuzzyDimNamesConfig = configuration.getCategory(SAFE_DIM_NAMES_CATEGORY);

        if (safeFuzzyDimNamesConfig.isEmpty()) {
            safeFuzzyDimNamesConfig.put(
                "Space Stations",
                new Property("Galacticraft Space Stations", "Space Station", Property.Type.STRING));
            safeFuzzyDimNamesConfig
                .put("Storage Cells", new Property("AE2 Storage Cells", "Storage Cell", Property.Type.STRING));
            safeFuzzyDimNamesConfig
                .put("SpectreWorlds", new Property("RandomThings SpectreWorld", "SpectreWorld", Property.Type.STRING));
            safeFuzzyDimNamesConfig
                .put("SpiritWorlds", new Property("Witchery Spirit World", "Spirit World", Property.Type.STRING));
        }

        for (var entry : safeFuzzyDimNamesConfig.entrySet()) {
            String fuzzyDimName = configuration
                .getString(entry.getKey(), SAFE_DIM_NAMES_CATEGORY, DEFAULT_SAFE_DIM_NAME, "");

            safeFuzzyDimNames.add(fuzzyDimName);
        }

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }
}
