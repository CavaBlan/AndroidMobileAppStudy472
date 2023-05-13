package com.example.myapplication;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

public class ANoteViewModel extends ViewModel {
    public static final String KEY_NOTE_LIST = "note_list";
    public static final String notesFileName = "notes.json";
    private final MutableLiveData<ArrayList<ANote>> noteList;
    private int NoteIdIndex = 0;

    public ANoteViewModel() {
        noteList = new MutableLiveData<>();
        noteList.setValue(new ArrayList<>());
    }

    public void setNoteList(ArrayList<ANote> notes) {
        Collections.sort(notes);
        noteList.postValue(notes);
    }

    public MutableLiveData<ArrayList<ANote>> getNoteList() {
        return noteList;
    }

    /**
     * add new Note
     */
    public void addNote(String title, String content) {
        ArrayList<ANote> notes = noteList.getValue();
        ANote newNote = new ANote(NoteIdIndex, title, content);
        assert notes != null;
        notes.add(0, newNote);
        Collections.sort(notes);
        noteList.postValue(notes);
        NoteIdIndex += 1;
    }

    /**
     * update Note
     */
    public void updateNote(int id, String title, String content) {
        ArrayList<ANote> notes = noteList.getValue();
        assert notes != null;
        for (int i = 0; i < notes.size(); i++) {
            ANote m = notes.get(i);
            if (m.id == id) {
                m.setTitle(title);
                m.setContent(content);
                m.setModDate(System.currentTimeMillis());
                Collections.sort(notes);
                noteList.postValue(notes);
            }
        }
    }

    /**
     * delete Notes
     */
    public void deleteNote(ANote note) {
        ArrayList<ANote> notes = noteList.getValue();
        assert notes != null;
        notes.remove(note);
        Collections.sort(notes);
        noteList.postValue(notes);
    }

    /**
     * save note to json file
     */
    public void saveToFile(File file) throws JSONException, FileNotFoundException {
        ArrayList<ANote> notes = noteList.getValue();
        assert notes != null;
        JSONArray jsonArray = new JSONArray();
        for (ANote note : notes) {
            jsonArray.put(note.toJsonObj());
        }
        PrintWriter printWriter = new PrintWriter(file);
        JSONObject data = new JSONObject();
        data.put(KEY_NOTE_LIST, jsonArray);
        printWriter.println(data);
        printWriter.close();
    }
}
