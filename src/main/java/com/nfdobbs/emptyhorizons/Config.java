package com.nfdobbs.emptyhorizons;

import java.io.File;
import java.util.HashMap;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class Config {

    // Quest Line Multipliers
    private final static String QUEST_LINE_MULTIPLIER_CATEGORY = "quest_line_multipliers";
    private final static Float MIN_FLOAT_VALUE = 0.25f;
    private final static Float MAX_FLOAT_VALUE = 100f;
    private final static Float DEFAULT_FLOAT_VALUE = 1f;
    private final static String QUEST_LINE_MULTIPLIER_COMMENT = "BQ QuestLine names here will have their quest time reward multiplied by the given float value (0.25f - 100f).";

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
    public static int baseMainQuestReward = 10;
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

        configuration = configuration.setCategoryComment(QUEST_LINE_MULTIPLIER_CATEGORY, QUEST_LINE_MULTIPLIER_COMMENT);

        var questLineMultipliersConfig = configuration.getCategory(QUEST_LINE_MULTIPLIER_CATEGORY);

        if (questLineMultipliers.isEmpty()) {
            questLineMultipliersConfig.put("Tier 1 - LV", new Property("Tier 1 - LV", "1.5", Property.Type.DOUBLE));
            questLineMultipliersConfig.put("Tier 2 - MV", new Property("Tier 2 - MV", "2", Property.Type.DOUBLE));
            questLineMultipliersConfig.put("Tier 3 - HV", new Property("Tier 3 - HV", "3", Property.Type.DOUBLE));
            questLineMultipliersConfig.put("Tier 4 - EV", new Property("Tier 4 - EV", "4", Property.Type.DOUBLE));
            questLineMultipliersConfig.put("Tier 5 - IV", new Property("Tier 5 - IV", "5", Property.Type.DOUBLE));
            questLineMultipliersConfig.put("Tier 6 - LuV", new Property("Tier 6 - LuV", "6", Property.Type.DOUBLE));
            questLineMultipliersConfig.put("Tier 7 - ZPM", new Property("Tier 7 - ZPM", "7", Property.Type.DOUBLE));
            questLineMultipliersConfig.put("Tier 8 - UV", new Property("Tier 8 - UV", "8", Property.Type.DOUBLE));
            questLineMultipliersConfig.put("Tier 9 - UHV", new Property("Tier 9 - UHV", "14", Property.Type.DOUBLE));
            questLineMultipliersConfig.put("Tier 10 - UEV", new Property("Tier 10 - UEV", "15", Property.Type.DOUBLE));
            questLineMultipliersConfig.put("Tier 11 - UIV", new Property("Tier 11 - UIV", "16", Property.Type.DOUBLE));
            questLineMultipliersConfig.put("Tier 12 - UMV", new Property("Tier 12 - UMV", "17", Property.Type.DOUBLE));
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
                .put("Novice Thaumaturgy", new Property("Novice Thaumaturgy", "2", Property.Type.DOUBLE));
            questLineMultipliersConfig
                .put("Adept Thaumaturgy", new Property("Adept Thaumaturgy", "4", Property.Type.DOUBLE));
            questLineMultipliersConfig
                .put("Kaaami, Haaaami, ... HA!", new Property("Kaaami, Haaaami, ... HA!", "5.5", Property.Type.DOUBLE));
            questLineMultipliersConfig
                .put("Focus on Wand Foci & EMT", new Property("Focus on Wand Foci & EMT", "2", Property.Type.DOUBLE));
            questLineMultipliersConfig.put("Flower Power", new Property("Flower Power", "3", Property.Type.DOUBLE));
            questLineMultipliersConfig
                .put("Look to the Edges", new Property("Look to the Edges", "2.5", Property.Type.DOUBLE));
            questLineMultipliersConfig
                .put("Paying the Highest Price", new Property("Paying the Highest Price", "4", Property.Type.DOUBLE));
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

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }
}
