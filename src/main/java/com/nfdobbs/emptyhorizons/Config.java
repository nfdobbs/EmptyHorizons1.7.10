package com.nfdobbs.emptyhorizons;

import java.io.File;
import java.util.HashMap;

import net.minecraftforge.common.config.Configuration;

public class Config {

    // Quest Line Multipliers
    private final static String QUEST_LINE_MULTIPLIER_CATEGORY = "quest_line_multipliers";
    private final static Float MIN_FLOAT_VALUE = 0.25f;
    private final static Float MAX_FLOAT_VALUE = 100f;
    private final static Float DEFAULT_FLOAT_VALUE = 1f;
    private final static String QUEST_LINE_MULTIPLIER_COMMENT = "BQ QuestLine names here will have their quest time reward multiplied by the given float value (0.25f - 100f).";

    // Starting Time Values
    public static int startingExposureTime = 20;
    private final static int MIN_STARTING_EXPOSURE = 1;
    private final static int MAX_STARTING_EXPOSURE = 3600;
    private final static String STARTING_EXPOSURE_TIME_COMMENT = "Initial Max Exposure Time.";

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

    public static String greeting = "Welcome to suffering";

    public static HashMap<String, Float> questLineMultipliers = new HashMap<>();

    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);

        greeting = configuration.getString("greeting", Configuration.CATEGORY_GENERAL, greeting, "How shall I greet?");

        startingExposureTime = configuration.getInt(
            "startingExposureTime",
            Configuration.CATEGORY_GENERAL,
            startingExposureTime,
            MIN_STARTING_EXPOSURE,
            MAX_STARTING_EXPOSURE,
            STARTING_EXPOSURE_TIME_COMMENT);

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

        configuration = configuration.setCategoryComment(QUEST_LINE_MULTIPLIER_CATEGORY, QUEST_LINE_MULTIPLIER_COMMENT);

        var questLineMultipliersConfig = configuration.getCategory(QUEST_LINE_MULTIPLIER_CATEGORY);

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
