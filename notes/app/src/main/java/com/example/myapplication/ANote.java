package com.example.myapplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class ANote implements Comparable<ANote>,Serializable {
    public static final String KEY_TITLE = "title";
    public static final String KEY_ID = "id";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_MOD_DATE = "mode_date";

    public final int id;
    private String title;
    private String content;
    private long modDate;

    public ANote(int id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.modDate = System.currentTimeMillis();
    }

    public ANote(int id, String title, String content, long modDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.modDate = modDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setModDate(long modDate) {
        this.modDate = modDate;
    }

    public String getTitle() {
        return title;
    }

    public long getModDate() {
        return modDate;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    /**
     * serialize to json
     */
    public JSONObject toJsonObj() throws JSONException {
        JSONObject object = new JSONObject();
        object.put(KEY_ID, id);
        object.put(KEY_TITLE, title);
        object.put(KEY_CONTENT, content);
        object.put(KEY_MOD_DATE, modDate);
        return object;
    }

    /**
     * unSerialize from json
     */
    public static ANote fromJsonObj(JSONObject object) throws JSONException {
        int id = object.getInt(KEY_ID);
        String title = object.getString(KEY_TITLE);
        String content = object.getString(KEY_CONTENT);
        long modDate = object.getLong(KEY_MOD_DATE);
        return new ANote(id, title, content, modDate);
    }

    @Override
    public int compareTo(ANote aNote) {
        return (int) (aNote.modDate-this.modDate );
    }
}
