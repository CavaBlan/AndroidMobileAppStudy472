package com.asmt.civiladvocacy.data.entity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Official implements Serializable {

    public final String name;
    public final List<Address> address;
    public final String party;
    public final List<String> phones;
    public final List<String> urls;
    public final List<String> emails;
    public final String photoUrl;
    public final List<Channel> channels;

    public Official(String name, List<Address> address, String party, List<String> phones,
                    List<String> urls, List<String> emails, String photoUrl, List<Channel> channels) {
        this.name = name;
        this.address = address;
        this.party = party;
        this.phones = phones;
        this.urls = urls;
        this.emails = emails;
        this.photoUrl = photoUrl;
        this.channels = channels;
    }

    public String getAddress() {
        if (address.isEmpty()) {
            return "Unknown Address";
        }
        return address.get(0).getAddress();
    }

    public String getPhone() {
        if (phones.isEmpty()) {
            return "Unknown Phone";
        }
        return phones.get(0);
    }

    public String getEmail() {
        if (emails.isEmpty()) {
            return "Unknown Email";
        }
        return emails.get(0);
    }

    public String getUrl() {
        if (urls.isEmpty()) {
            return "Unknown Email";
        }
        return urls.get(0);
    }

    public static Official fromJson(JSONObject jo)  {
        String name = jo.optString("name");

        List<Address> address = new ArrayList<>();
        JSONArray jaAddress = jo.optJSONArray("address");
        if (jaAddress != null) {
            for (int i = 0; i < jaAddress.length(); i++) {
                address.add(Address.fromJson(jaAddress.optJSONObject(i)));
            }
        }

        String party = jo.optString("party");
        List<String> phones = new ArrayList<>();
        JSONArray jaPhones = jo.optJSONArray("phones");
        if (jaPhones != null) {
            for (int i = 0; i < jaPhones.length(); i++) {
                phones.add(jaPhones.optString(i));
            }
        }

        List<String> urls = new ArrayList<>();
        JSONArray jaUrls = jo.optJSONArray("urls");
        if (jaUrls != null) {
            for (int i = 0; i < jaUrls.length(); i++) {
                urls.add(jaUrls.optString(i));
            }
        }

        List<String> emails = new ArrayList<>();
        JSONArray jaEmails = jo.optJSONArray("emails");
        if (jaEmails != null) {
            for (int i = 0; i < jaEmails.length(); i++) {
                emails.add(jaEmails.optString(i));
            }
        }

        String photoUrl = jo.optString("photoUrl");

        List<Channel> channels = new ArrayList<>();
        JSONArray jaChannels = jo.optJSONArray("channels");
        if (jaChannels != null) {
            for (int i = 0; i < jaChannels.length(); i++) {
                channels.add(Channel.fromJson(jaChannels.optJSONObject(i)));
            }
        }

        return new Official(name, address, party, phones, urls, emails, photoUrl, channels);
    }

    public static class Channel implements Serializable  {
        public final String type;
        public final String id;

        public Channel(String type, String id) {
            this.type = type;
            this.id = id;
        }

        public static Channel fromJson(JSONObject jo) {
            String type = jo.optString("type");
            String id = jo.optString("id");
            return new Channel(type, id);
        }
    }
}
