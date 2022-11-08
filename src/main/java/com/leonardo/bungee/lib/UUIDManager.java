package com.leonardo.bungee.lib;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.leonardo.bungee.Main;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.UUID;

/**
 * Minecraft AdvancedBans plugin inspired codes, an adaptation was made for the Punishments plugin.
 * Original source: https://github.com/DevLeoko/AdvancedBan/blob/master/src/main/java/me/leoko/advancedban/manager/UUIDManager.java
 */
public class UUIDManager {

    private static UUIDManager instance;
    private final HashMap<String, String> activeUUIDs = Maps.newHashMap();

    public static UUIDManager get() {
        return instance == null ? instance = new UUIDManager() : instance;
    }

    public String getOriginalUUID(String name) {
        name = name.toLowerCase();
        if (Main.getInstance().getProxy().getPlayer(name) != null) {
            final String uuid = Main.getInstance().getProxy().getPlayer(name).toString();
            supplyInternUUID(name, fromString(uuid));
            return uuid;
        }
        if (activeUUIDs.containsKey(name)) return getUUID(name);
        String uuid = "";
        try {
            uuid = askAPI(name);
        } catch (IOException ignored) {
        }
        supplyInternUUID(name, fromString(uuid));
        return fromString(uuid).toString();
    }

    public void supplyInternUUID(String name, UUID uuid) {
        activeUUIDs.put(name.toLowerCase(), uuid.toString().replace("-", ""));
    }

    public UUID fromString(String uuid) {
        if (!uuid.contains("-") && uuid.length() == 32)
            uuid = uuid
                    .replaceFirst(
                            "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5");
        return uuid.length() == 36 && uuid.contains("-") ? UUID.fromString(uuid) : null;
    }

    public String getUUID(String name) {
        String inMemoryUuid = activeUUIDs.get(name.toLowerCase());
        return (inMemoryUuid != null) ? inMemoryUuid : getOriginalUUID(name);
    }

    public String getInMemoryName(String uuid) {
        for (Entry<String, String> rs : activeUUIDs.entrySet()) {
            if (rs.getValue().equalsIgnoreCase(uuid)) {
                return rs.getKey();
            }
        }
        return null;
    }

    public String getNameFromUUID(String uuid, boolean forceInitial) {
        if (!forceInitial) {
            String inMemoryName = getInMemoryName(uuid);
            if (inMemoryName != null) {
                return inMemoryName;
            }
        }
        try (Scanner scanner = new Scanner(new URL("https://api.mojang.com/user/profiles/" + uuid + "/names").openStream(), "UTF-8")) {
            String s = scanner.useDelimiter("\\A").next();
            s = s.substring(s.lastIndexOf('{'), s.lastIndexOf('}') + 1);
            return parseJSON(s, "name");
        } catch (Exception exc) {
            return null;
        }
    }

    private String askAPI(String name) throws IOException {
        name = name.toLowerCase();
        final HttpURLConnection request = (HttpURLConnection) new URL("https://api.mojang.com/users/profiles/minecraft/%NAME%?at=%TIMESTAMP%".replaceAll("%NAME%", name).replaceAll("%TIMESTAMP%", new Date().getTime() + "")).openConnection();
        request.connect();
        String uuid = parseJSON(new InputStreamReader(request.getInputStream()), "id");
        if (uuid == null) {
            System.out.println("!! Failed fetching UUID of " + name);
            System.out.println("!! Could not find key 'id' in the servers response");
            System.out.println("!! Response: " + request.getResponseMessage());
        } else {
            activeUUIDs.put(name, uuid);
        }
        return uuid;
    }

    public String parseJSON(String json, String key) {
        final JsonElement element = new JsonParser().parse(json);
        if (element instanceof JsonNull) {
            return null;
        }
        final JsonElement obj = ((JsonObject) element).get(key);
        return obj != null ? obj.toString().replaceAll("\"", "") : null;
    }

    public String parseJSON(InputStreamReader json, String key) {
        final JsonElement element = new JsonParser().parse(json);
        if (element instanceof JsonNull) {
            return null;
        }
        final JsonElement obj = ((JsonObject) element).get(key);
        return obj != null ? obj.toString().replaceAll("\"", "") : null;
    }
}
