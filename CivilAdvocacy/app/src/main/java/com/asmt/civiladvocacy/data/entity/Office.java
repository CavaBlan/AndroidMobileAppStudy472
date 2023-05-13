package com.asmt.civiladvocacy.data.entity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Office implements Serializable {
    public final String name;
    public final List<Official> officials;
    public Office(String name, List<Official> officials) {
        this.name = name;
        this.officials = officials;
    }

    public Official getOfficial() {
        return officials.get(0);
    }

    public static Office fromJson(JSONObject jo, List<Official> officialsCache) {
        String name = jo.optString("name");
        JSONArray jaIndices = jo.optJSONArray("officialIndices");
        List<Official> officials = new ArrayList<>();
        if (jaIndices != null) {
            for (int i = 0; i < jaIndices.length(); i++) {
                int index = jaIndices.optInt(i);
                officials.add(officialsCache.get(index));
            }
        }
        return new Office(name, officials);
    }
}
