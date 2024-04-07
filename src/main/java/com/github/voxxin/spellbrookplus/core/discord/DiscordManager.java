package com.github.voxxin.spellbrookplus.core.discord;

import com.github.voxxin.spellbrookplus.SpellBrookPlus;
import com.github.voxxin.spellbrookplus.core.utilities.Constants;
import com.github.voxxin.spellbrookplus.core.utilities.Static;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.Packet;
import com.jagrosh.discordipc.entities.RichPresence;
import com.jagrosh.discordipc.entities.User;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Objects;

public class DiscordManager {
    public static boolean active = false;
    private static IPCClient client;
    private static Instant start;

    public DiscordManager start() {
        if (!active) {
            SpellBrookPlus.logger().info("Starting Discord RPC client...");
            client = new IPCClient(1226293239552806972L);
            client.setListener(new IPCListener() {
                @Override
                public void onPacketSent(IPCClient client, Packet packet) {
                }

                @Override
                public void onPacketReceived(IPCClient client, Packet packet) {
                }

                @Override
                public void onActivityJoin(IPCClient client, String secret) {
                }

                @Override
                public void onActivitySpectate(IPCClient client, String secret) {
                }

                @Override
                public void onActivityJoinRequest(IPCClient client, String secret, User user) {
                }

                @Override
                public void onReady(IPCClient client) {
                    SpellBrookPlus.logger().info("Discord RPC client connected!");
                    active = true;
                    start = Instant.now();
                    update();
                }

                @Override
                public void onClose(IPCClient client, JsonObject json) {
                    active = false;
                }

                @Override
                public void onDisconnect(IPCClient client, Throwable t) {
                    active = false;
                }
            });

            try {
                client.connect();
            } catch (Exception ignored) {}
        }
        return this;
    }

    public void update() {
        if (active) {
            Location location = Static.Location;
            RichPresence.Builder builder = new RichPresence.Builder();

            JsonArray buttonsArray = new JsonArray();

            JsonObject discordLinkButton = new JsonObject();
            discordLinkButton.addProperty("label", "Discord");
            discordLinkButton.addProperty("url", "https://discord.gg/UktczTSdB4");

            JsonObject githubLinkButton = new JsonObject();
            githubLinkButton.addProperty("label", "GitHub");
            githubLinkButton.addProperty("url", "https://github.com/Voxxin/SpellbrookPlus");


            buttonsArray.add(discordLinkButton);
            buttonsArray.add(githubLinkButton);

            builder.setState(location.description)
                    .setDetails(location.name)
                    .setStartTimestamp(Instant.ofEpochSecond(start.toEpochMilli()).atOffset(ZoneOffset.UTC).toEpochSecond())
                    .setLargeImage(location.largeIcon.key(), "HideawayPlus v" + Constants.MOD_VERSION)
                    .setSmallImage(location.smallIcon.key(), "Nothing to see here...")
                    .setButtons(buttonsArray);
            client.sendRichPresence(builder.build());
        } else start();
    }

    public void stop() {
        client.sendRichPresence(null);
        active = false;
    }
}
