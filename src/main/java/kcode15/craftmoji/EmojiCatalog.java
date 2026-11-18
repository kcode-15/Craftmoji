package dev.kcode15.craftmoji;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/** Loads the public emoji list without blocking Minecraft's client thread. */
public final class EmojiCatalog {
    private static final String EMOJI_LIST_URL =
            "https://raw.githubusercontent.com/iamcal/emoji-data/master/emoji.json";

    private static volatile List<Emoji> emojis = List.of();
    private static volatile boolean loading;
    private static volatile String error;

    private EmojiCatalog() {
    }

    public static void load() {
        if (loading) {
            return;
        }

        loading = true;
        error = null;
        CompletableFuture.runAsync(EmojiCatalog::fetchEmojis);
    }

    private static void fetchEmojis() {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) URI.create(EMOJI_LIST_URL).toURL().openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Craftmoji/1.0 (Fabric client mod)");
            connection.setConnectTimeout(10_000);
            connection.setReadTimeout(20_000);

            try (InputStreamReader reader = new InputStreamReader(
                    connection.getInputStream(), StandardCharsets.UTF_8)) {
                JsonArray entries = JsonParser.parseReader(reader).getAsJsonArray();
                List<Emoji> loaded = new ArrayList<>(entries.size());

                for (JsonElement element : entries) {
                    if (!element.isJsonObject()) {
                        continue;
                    }

                    JsonObject entry = element.getAsJsonObject();
                    if (!entry.has("unified") || !entry.has("name")) {
                        continue;
                    }

                    String glyph = toUnicode(entry.get("unified").getAsString());
                    String name = entry.get("name").getAsString();
                    if (!glyph.isEmpty()) {
                        loaded.add(new Emoji(glyph, name));
                    }
                }

                if (loaded.isEmpty()) {
                    throw new IllegalStateException("The emoji service returned no usable emojis.");
                }

                emojis = List.copyOf(loaded);
            }
        } catch (Exception exception) {
            error = exception.getMessage() == null
                    ? "Could not load the emoji list. Check your internet connection."
                    : exception.getMessage();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            loading = false;
        }
    }

    private static String toUnicode(String codePoints) {
        StringBuilder result = new StringBuilder();
        for (String codePoint : codePoints.split("-")) {
            try {
                result.appendCodePoint(Integer.parseInt(codePoint, 16));
            } catch (IllegalArgumentException exception) {
                return "";
            }
        }
        return result.toString();
    }

    public static List<Emoji> getEmojis() {
        return emojis;
    }

    public static boolean isLoading() {
        return loading;
    }

    public static String getError() {
        return error;
    }

    public static String getStatus() {
        if (loading) {
            return "loading";
        }
        if (error != null) {
            return "error";
        }
        return emojis.isEmpty() ? "not-loaded" : "ready";
    }

    public record Emoji(String glyph, String name) {
    }
}
