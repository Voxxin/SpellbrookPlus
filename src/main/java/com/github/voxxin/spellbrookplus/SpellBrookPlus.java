package com.github.voxxin.spellbrookplus;

import com.github.voxxin.spellbrookplus.core.client.gui.conifg.ConfigManager;
import com.github.voxxin.spellbrookplus.core.discord.DiscordManager;
import com.github.voxxin.spellbrookplus.core.discord.Location;
import com.github.voxxin.spellbrookplus.core.level.HandleMagicEvents;
import com.github.voxxin.spellbrookplus.core.lifecycle.Lifecycle;
import com.github.voxxin.spellbrookplus.core.lifecycle.Task;
import com.github.voxxin.spellbrookplus.core.utilities.Constants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Environment(EnvType.CLIENT)
public class SpellBrookPlus implements ClientModInitializer {
    private static final Logger LOGGER = LogManager.getLogger(Constants.MOD_NAME);
    public static DiscordManager DISCORD_MANAGER;
    private static Lifecycle LIFECYCLE;
    @Override
    public void onInitializeClient() {
        Constants.MOD_MENU_PRESENT = FabricLoader.getInstance().isModLoaded("modmenu");

        LIFECYCLE = new Lifecycle();

        // Lifecycle tasks should be initialized here.
        lifecycle()
                .add(Task.of(Location::check, 20))
                .add(Task.of(() -> {
                    try {
                        if (connected() && ConfigManager.discordRPC.getValue() && DISCORD_MANAGER == null)
                            DISCORD_MANAGER = new DiscordManager().start();

                        if (DiscordManager.active) {
                            DISCORD_MANAGER.update();
                            if (!connected() || !ConfigManager.discordRPC.getValue()) DISCORD_MANAGER.stop();
                        } else if (connected() && ConfigManager.discordRPC.getValue())
                            DISCORD_MANAGER.start();
                    } catch (Error err) { logger().error(err); }

                }, 10))
                .add(Task.of(HandleMagicEvents::tick, 1))
        ;

        ConfigManager.initalizeConfig();

    }

    public static boolean connected() {
        ServerData server = Minecraft.getInstance().getCurrentServer();
        if (server != null) {
            return server.ip.toLowerCase().endsWith("spellbrook.gg");
        } else return false;
    }

    public static Logger logger() { return LOGGER; }
    public static Lifecycle lifecycle() { return LIFECYCLE; }
}
