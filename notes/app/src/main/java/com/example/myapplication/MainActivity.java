package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.myapplication.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    public static ANoteViewModel viewModel;
    private ActivityMainBinding binding;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbarMain);

        viewModel = new ViewModelProvider(this).get(ANoteViewModel.class);
        File file = new File(getFilesDir(), ANoteViewModel.notesFileName);
        //load notes from json file
        try {
            //read file
            Scanner scanner = new Scanner(file);
            StringBuilder jsonStr = new StringBuilder("");
            while (scanner.hasNextLine()) {
                jsonStr.append(scanner.nextLine());
            }
            //parse json
            JSONObject root = new JSONObject(jsonStr.toString());
            JSONArray notesList = (JSONArray) root.get(ANoteViewModel.KEY_NOTE_LIST);
            ArrayList<ANote> notes = new ArrayList<>();
            for (int i = 0; i < notesList.length(); i++) {
                notes.add(ANote.fromJsonObj(notesList.getJSONObject(i)));
            }
            viewModel.setNoteList(notes);
        } catch (FileNotFoundException | JSONException e) {
            e.printStackTrace();
        }
        //show note in list
        NoteListAdapter noteListAdapter = new NoteListAdapter(viewModel);
        binding.recyclerviewNotes.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerviewNotes.setAdapter(noteListAdapter);
        //switch to edit activity when tap a note in list.
        noteListAdapter.setNoteTappedListener(note -> {
            Intent intent = new Intent(MainActivity.this, EditActivity.class);
            intent.putExtra(EditActivity.KEY_NOTE, note);
            startActivity(intent);
        });
        //delete note.
        MainActivity mainActivity = this;
        noteListAdapter.setNoteLongTappedListener(note -> {
            //confirm delete
            new AlertDialog.Builder(mainActivity).setTitle(R.string.hint)
                    .setMessage(getString(R.string.delete_confirm) + note.getTitle() + "?")
                    .setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                        viewModel.deleteNote(note);
                    }).setNegativeButton(R.string.no, (dialogInterface, i) -> {
                    }).show();
        });
        //update note list after editing or creating
        viewModel.getNoteList().observe(this, aNotes -> {
            if (aNotes.size() > 0) {
                binding.tvNothing.setVisibility(View.GONE);
            } else {
                binding.tvNothing.setVisibility(View.VISIBLE);
            }
            noteListAdapter.notifyDataSetChanged();
            //show note number in title
            binding.toolbarMain.setTitle(String.format("%s(%s)",
                    getString(R.string.app_name),
                    aNotes.size()));
        });
        //set click listener for info and add menu item
        binding.toolbarMain.setOnMenuItemClickListener(item -> {
            // go to about activity
            if (item.getItemId() == R.id.action_about) {
                switchToAbout();
            }
            // go to create new note
            if (item.getItemId() == R.id.action_add) {
                switchToCreateNote();
            }
            return true;
        });
    }

    /**
     * switch to about activity
     */
    private void switchToAbout() {
        Intent intent = new Intent(MainActivity.this, AboutActivity.class);
        startActivity(intent);
    }

    /**
     * open edit activity
     */
    private void switchToCreateNote() {
        Intent intent = new Intent(MainActivity.this, EditActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<ANote> notes = viewModel.getNoteList().getValue();
        assert notes != null;
        binding.toolbarMain.setTitle(String.format("%s(%s)",
                getString(R.string.app_name),
                notes.size()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_memu, menu);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        //save notes to file
        File file = new File(getFilesDir(), ANoteViewModel.notesFileName);
        try {
            if (!file.exists()) {
                boolean res = file.createNewFile();
            }
            viewModel.saveToFile(file);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}