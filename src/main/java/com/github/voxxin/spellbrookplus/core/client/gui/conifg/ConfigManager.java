package com.github.voxxin.spellbrookplus.core.client.gui.conifg;

import com.github.voxxin.api.config.option.BooleanConfigOption;
import com.github.voxxin.api.config.option.ConfigOption;
import com.github.voxxin.api.config.option.NumberConfigOption;
import com.github.voxxin.spellbrookplus.core.utilities.Constants;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;

public class ConfigManager {
    private static final File configDir = FabricLoader.getInstance().getConfigDir().resolve(Constants.MOD_ID).toFile();
    private static final com.github.voxxin.api.config.ConfigManager MANAGER = new com.github.voxxin.api.config.ConfigManager(configDir);
    private static final String configTA = "config."+Constants.MOD_ID+".";

    public static ConfigOption generalCategory;
    public static BooleanConfigOption assignmentsResetTimer = new BooleanConfigOption(configTA + "option.assignments_reset_timer", true);
    public static BooleanConfigOption assignmentsResetAdvancement = new BooleanConfigOption(configTA + "option.assignments_reset_advancement", true);
    public static BooleanConfigOption assignmentsResetAlwaysNotify = new BooleanConfigOption(configTA + "option.assignments_reset_always", false);
    public static BooleanConfigOption discordRPC = new BooleanConfigOption(configTA + "option.discord_rpc", true);
    public static BooleanConfigOption spellSuggestions = new BooleanConfigOption(configTA + "option.spell_suggestions", true);
    public static BooleanConfigOption magicGlow = new BooleanConfigOption(configTA + "option.magic_glow", true);
    public static BooleanConfigOption magicAdvancement = new BooleanConfigOption(configTA + "option.magic_advancement", true);
    public static BooleanConfigOption magicAlwaysBright = new BooleanConfigOption(configTA + "option.always_bright", true);
    public static BooleanConfigOption fishingPing = new BooleanConfigOption(configTA + "option.fishing_ping", true);
    public static BooleanConfigOption fishingAlert = new BooleanConfigOption(configTA + "option.fishing_alert", true);

    public static ConfigOption valuesCategory;
    public static NumberConfigOption firstAssignmentsTimeWater = new NumberConfigOption(configTA + "values.first_assignments_time.water", -1);
    public static NumberConfigOption firstAssignmentsTimeNature = new NumberConfigOption(configTA + "values.first_assignments_time.nature", -1);
    public static NumberConfigOption firstAssignmentsTimeFire = new NumberConfigOption(configTA + "values.first_assignments_time.fire", -1);

    public static void initalizeConfig() {
        generalCategory = new ConfigOption.Builder(configTA + "category.general")
                .addBoolean(assignmentsResetTimer)
                .addBoolean(assignmentsResetAdvancement)
                .addBoolean(assignmentsResetAlwaysNotify)
                .addBoolean(discordRPC)
                .addBoolean(spellSuggestions)
                .addBoolean(magicGlow)
                .addBoolean(magicAdvancement)
                .addBoolean(magicAlwaysBright)
                .addBoolean(fishingPing)
                .addBoolean(fishingAlert)
                .build();

        valuesCategory = new ConfigOption.Builder(configTA + "category.values")
                .addNumber(firstAssignmentsTimeWater)
                .addNumber(firstAssignmentsTimeNature)
                .addNumber(firstAssignmentsTimeFire)
                .build();

        MANAGER.addOption(generalCategory);
        MANAGER.addOption(valuesCategory);
        MANAGER.runOptions();
    }

    public static void updateConfig() {
        MANAGER.saveOptions();
    }
}
