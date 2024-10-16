package com.github.voxxin.spellbrookplus.core.discord;

import com.github.voxxin.spellbrookplus.SpellbrookPlus;
import com.github.voxxin.spellbrookplus.core.mixin.extenders.BossHealthOverlayExtender;
import com.github.voxxin.spellbrookplus.core.utilities.Static;
import net.minecraft.client.Minecraft;

public enum Location {

    UNKNOWN(
            "Using Spellbrook+",
            "Exploring the blocks... 🚀",
            PresenceImage.Large.SCENE_DARK
    ),

    SPAWN (
            "At spawn",
            "Awaiting adventure... 🔮",
            PresenceImage.Large.SCENE
    ),

    SURVIVAL_WORLD(
            "In the Survival Realm",
            "Trying out new spells! 🪄",
            PresenceImage.Large.SCENE
    ),

    IN_TOWN(
            "Set below :3",
            "Casting friendship! 🫂",
            PresenceImage.Large.SCENE
    ),

    RESOURCE_WORLD(
            "In the Recourse Realm",
            "Excavation magic! 🧨",
            PresenceImage.Large.SCENE
    ),

    THE_NETHER(
            "In the Nether",
            "Screw the Nether 🎶",
            PresenceImage.Large.SCENE
    ),

    THE_END(
            "In the End",
            "Is this really it? 🐲",
            PresenceImage.Large.SCENE
    ),
    ;

    public String name;
    public final String description;
    public final PresenceImage.Large largeIcon;

    Location(
            String name,
            String description,
            PresenceImage.Large largeIcon
    ) {
        this.name = name;
        this.description = description;
        this.largeIcon = largeIcon;
    }

    public static void check() {
        if (!SpellbrookPlus.connected()) {
            Static.Location = UNKNOWN;
            return;
        }

        Minecraft client = Minecraft.getInstance();
        String dimension = client.level.dimension().location().toString();
        String bossBarName = ((BossHealthOverlayExtender) client.gui.getBossOverlay()).getBossBarName();

        if (bossBarName != null && bossBarName.contains("\uE021") && !bossBarName.contains("Wilderness")) {
            if (bossBarName.matches(".*(?<= )(.*?)(?= ).*")) {
                String townName = bossBarName.replaceAll(".*(?<= )(.*?)(?= ).*", "$1");
                Static.Location = IN_TOWN;
                Static.Location.name = ("Visiting " + townName);
                return;
            }
        }

        switch (dimension) {
            case "minecraft:spawn" -> Static.Location = SPAWN;
            case "minecraft:overworld" -> Static.Location = SURVIVAL_WORLD;
            case "minecraft:resources" -> Static.Location = RESOURCE_WORLD;
            case "minecraft:the_nether" -> Static.Location = THE_NETHER;
            case "minecraft:the_end" -> Static.Location = THE_END;
        }
    }


}
