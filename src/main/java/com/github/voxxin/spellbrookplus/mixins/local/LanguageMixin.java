package com.github.voxxin.spellbrookplus.mixins.local;

import com.github.voxxin.spellbrookplus.SpellBrookPlus;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonParseException;
import net.minecraft.locale.Language;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BiConsumer;

import static net.minecraft.locale.Language.loadFromJson;

@Mixin(Language.class)
public abstract class LanguageMixin {

    @Shadow
    private static void parseTranslations(BiConsumer<String, String> output, String languagePath) {
    }

    @Shadow
    public static void loadFromJson(InputStream stream, BiConsumer<String, String> output) {
    }

    @Shadow @Final private static Logger LOGGER;

    @Redirect(at = @At(
            value = "INVOKE",
            target = "Lcom/google/common/collect/ImmutableMap$Builder;build()Lcom/google/common/collect/ImmutableMap;"),
            method = "method_29429", remap = false
    )
    private static ImmutableMap<String, String> loadDefault(ImmutableMap.Builder<String, String> cir) {
        Map<String, String> map = new HashMap<>(cir.buildOrThrow());

        for (Path path : getModLanguageFiles()) {
            loadFromPath(path, map::put);
        }

        return ImmutableMap.copyOf(map);
    }

    @Unique
    private static void loadFromPath(Path path, BiConsumer<String, String> entryConsumer) {
        System.out.println(path);

        try (InputStream stream = Files.newInputStream(path)) {
            LOGGER.debug("Loading translations from {}", path);
            loadFromJson(stream, entryConsumer);
        } catch (JsonParseException | IOException e) {
            LOGGER.error("Couldn't read strings from {}", path, e);
        }
    }

    @Unique
    private static Collection<Path> getModLanguageFiles() {
        Set<Path> paths = new LinkedHashSet<>();
        URL folderUrl = SpellBrookPlus.class.getResource("/assets/spellbrookplus/lang");
        if (folderUrl == null) {
            LOGGER.error("Folder not found: assets/spellbrookplus/lang");
            return Collections.emptySet();
        }

        try {
            Path folderPath = Paths.get(folderUrl.toURI());
            Files.walk(folderPath)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".json"))
                    .forEach(paths::add);
        } catch (URISyntaxException | IOException e) {
            LOGGER.error("Error while retrieving language files", e);
        }
        return Collections.unmodifiableCollection(paths);
    }

}
