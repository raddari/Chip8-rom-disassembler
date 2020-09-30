package me.raddari.chip8.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class JsonConfiguration extends FileConfiguration {

    private static final Logger LOGGER = LogManager.getLogger();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public JsonConfiguration() {
        super('.');
    }

    @Override
    public void loadFromString(@NotNull String contents) {
        var mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> configMap = gson.fromJson(contents, mapType);
        for (var entry : configMap.entrySet()) {
            pullNestedValues("", entry.getKey(), entry.getValue(), pathMap);
        }
    }

    private void pullNestedValues(String parent, String key, Object value, Map<String, Object> dest) {
        if (value instanceof Map) {
            parent += key + pathSeparator;
            for (var entry : ((Map<?, ?>) value).entrySet()) {
                pullNestedValues(parent, (String) entry.getKey(), entry.getValue(), dest);
            }
        } else {
            var path = parent + key;
            LOGGER.debug("Added (\"{}\" => \"{}\") to config entries", path, value);
            dest.put(path, value);
        }
    }

}
