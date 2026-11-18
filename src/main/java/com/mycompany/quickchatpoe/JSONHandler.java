package com.mycompany.quickchatpoe;

import com.google.gson.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class JSONHandler {
    private static final String FILE = "messages.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void save(List<Message> messages) {
        try {
            List<Map<String, String>> data = new ArrayList<>();
            for (Message m : messages) {
                Map<String, String> map = new LinkedHashMap<>();
                map.put("messageID", m.getMessageID());
                map.put("recipient", m.getRecipient());
                map.put("message",   m.getMessageText());
                map.put("hash",      m.getMessageHash());
                data.add(map);
            }
            Files.writeString(Paths.get(FILE), gson.toJson(data));
        } catch (IOException e) {
            System.err.println("Could not save messages: " + e.getMessage());
        }
    }

    public static List<StoredMessage> load() {
        List<StoredMessage> out = new ArrayList<>();
        try {
            File f = new File(FILE);
            if (!f.exists()) return out;
            String json = Files.readString(f.toPath());
            JsonArray arr = JsonParser.parseString(json).getAsJsonArray();
            for (JsonElement e : arr) {
                JsonObject o = e.getAsJsonObject();
                out.add(new StoredMessage(
                        o.get("messageID").getAsString(),
                        o.get("recipient").getAsString(),
                        o.get("message").getAsString(),
                        o.get("hash").getAsString()));
            }
        } catch (Exception e) {
            System.err.println("Could not load messages: " + e.getMessage());
        }
        return out;
    }

    public static boolean deleteByHash(String hash) {
        List<StoredMessage> all = load();
        boolean removed = all.removeIf(s -> s.getHash().equals(hash));
        if (removed) {
            try {
                List<Map<String, String>> data = new ArrayList<>();
                for (StoredMessage s : all) {
                    Map<String, String> map = new LinkedHashMap<>();
                    map.put("messageID", s.getMessageID());
                    map.put("recipient", s.getRecipient());
                    map.put("message",   s.getMessage());
                    map.put("hash",      s.getHash());
                    data.add(map);
                }
                Files.writeString(Paths.get(FILE), gson.toJson(data));
            } catch (IOException e) {
                System.err.println("Could not save after delete: " + e.getMessage());
            }
        }
        return removed;
    }
}
