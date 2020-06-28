package me.centralworks.punishments.lib;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;

public class MojangAPI {

    protected static MojangAPI instance;

    public static MojangAPI getInstance() {
        if (instance == null) instance = new MojangAPI();
        return instance;
    }

    public UUID getPlayerUUID(String player) {
        try {

            final String responseBody = IOUtils.toString(new URL("https://api.mojang.com/users/profiles/minecraft/" + player));
            final JsonObject json = ((JsonObject) JSONValue.parse(responseBody));
            final JsonElement id = json.get("id");
            return UUID.fromString(id.getAsString());
        } catch (IOException e) {
            return null;
        }
    }

    public String getName(String uuid) {
        try {
            String nameJson = IOUtils.toString(new URL("https://api.mojang.com/user/profiles/" + uuid.replace("-", "") + "/names"));
            JSONArray nameValue = (JSONArray) JSONValue.parse(nameJson);
            String playerSlot = nameValue.get(nameValue.size() - 1).toString();
            JsonObject nameObject = (JsonObject) JSONValue.parse(playerSlot);
            return nameObject.get("name").toString();
        } catch (Exception e) {
            return "error";
        }
    }
}
