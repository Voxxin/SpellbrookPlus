package com.github.voxxin.spellbrookplus.core.discord;

public class PresenceImage {
    public enum Large {
        SCENE("hat"),
        SCENE_DARK("hat_dark"),
        HAT("wizard_hat_512"),
        ROUNDEL("roundel_512");

        Large(String key) { this.key = key; }

        private final String key;

        public String key() { return key; }
    }

    public enum Small {
        WIZARD_HAT("wizard_hat_512"),
        ROUNDEL("roundel_512");

        Small(String key) { this.key = key; }

        private final String key;
        public String key() { return key; }
    }
}
