package com.asmt.civiladvocacy.data.entity;

import org.json.JSONObject;

import java.io.Serializable;

public class Address implements Serializable {
    public final String line1;
    public final String line2;
    public final String city;
    public final String state;
    public final String zip;

    public Address(String line1, String line2, String city, String state, String zip) {
        this.line1 = line1;
        this.line2 = line2;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    public static Address fromJson(JSONObject jo) {
        String line1 = jo.optString("line1");
        String line2 = jo.optString("line2");
        String city = jo.optString("city");
        String state = jo.optString("state");
        String zip = jo.optString("zip");
        return new Address(line1, line2, city, state, zip);
    }

    public String getAddress() {
        StringBuilder sb = new StringBuilder();
        if (!line1.isEmpty()) sb.append(line1);
        if (!line2.isEmpty()) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(line2);
        }
        if (!city.isEmpty()) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(city);
        }
        if (!state.isEmpty()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(state);
        }
        if (!zip.isEmpty()) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(zip);
        }
        return sb.toString();
    }
}
