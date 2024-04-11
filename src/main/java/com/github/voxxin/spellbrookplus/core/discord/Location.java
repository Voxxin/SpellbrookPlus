package com.github.voxxin.spellbrookplus.core.discord;

import com.github.voxxin.spellbrookplus.SpellBrookPlus;
import com.github.voxxin.spellbrookplus.core.mixin.asr.BossHealthOverlayAccessor;
import com.github.voxxin.spellbrookplus.core.utilities.Static;
import net.minecraft.client.Minecraft;

public enum Location {

    UNKNOWN(
            "Using Spellbrook+",
            "Exploring the blocks... 🚀",
            PresenceImage.Large.SCENE_DARK,
            PresenceImage.Small.ROUNDEL
    ),

    SPAWN (
            "At spawn",
            "Awaiting adventure... 🔮",
            PresenceImage.Large.SCENE,
            PresenceImage.Small.ROUNDEL
    ),

    SURVIVAL_WORLD(
            "In the Survival Realm",
            "Trying out new spells! 🪄",
            PresenceImage.Large.SCENE,
            PresenceImage.Small.ROUNDEL
    ),

    IN_TOWN(
            "Set below :3",
            "Casting friendship! 🫂",
            PresenceImage.Large.SCENE,
            PresenceImage.Small.ROUNDEL
    ),

    RESOURCE_WORLD(
            "In the Recourse Realm",
            "Excavation magic! 🧨",
            PresenceImage.Large.SCENE,
            PresenceImage.Small.ROUNDEL
    ),

    THE_NETHER(
            "In the Nether",
            "Screw the Nether 🎶",
            PresenceImage.Large.SCENE,
            PresenceImage.Small.ROUNDEL
    ),

    THE_END(
            "In the End",
            "Is this really it? 🐲",
            PresenceImage.Large.SCENE,
            PresenceImage.Small.ROUNDEL
    ),
    ;

    public String name;
    public final String description;
    public final PresenceImage.Large largeIcon;
    public final PresenceImage.Small smallIcon;

    Location(
            String name,
            String description,
            PresenceImage.Large largeIcon,
            PresenceImage.Small smallIcon
    ) {
        this.name = name;
        this.description = description;
        this.largeIcon = largeIcon;
        this.smallIcon = smallIcon;
    }

    public static void check() {
        if (!SpellBrookPlus.connected()) {
            Static.Location = UNKNOWN;
            return;
        }

        Minecraft client = Minecraft.getInstance();
        String dimension = client.level.dimension().location().toString();
        String bossBarName = ((BossHealthOverlayAccessor) client.gui.getBossOverlay()).getBossBarName();

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
