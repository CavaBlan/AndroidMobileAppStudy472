package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import com.example.myapplication.databinding.ActivityEditBinding;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class EditActivity extends AppCompatActivity {
    public final static String KEY_NOTE = "note";
    //indicate if need to save current input
    private boolean unSaved = false;
    private ActivityEditBinding binding;
    //current editing note
    private ANote currentNote;
    private ANoteViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = MainActivity.viewModel;
        binding = ActivityEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbarEdit);
        //note passed by note list in MainActivity
        Intent intent = getIntent();
        ANote editingNote = (ANote) intent.getSerializableExtra(KEY_NOTE);
        if (Objects.equals(editingNote, null)) {
            // This is creating a new note.
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.create_new_note);
        } else {
            //This is editing a note.
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.edit_note);
            currentNote = editingNote;
            binding.edittextTitle.setText(currentNote.getTitle());
            binding.editTextEditor.setText(currentNote.getContent());
        }
        setUnSavedListener();
        binding.toolbarEdit.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_save) {
                    saveAndBack();
                }
                return true;
            }
        });
    }

    /**
     * check if need to save
     */
    private void setUnSavedListener() {
        binding.edittextTitle.addTextChangedListener(new TextWatcher() {
            private String string;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                string = charSequence.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String newString = editable.toString();
                unSaved = !Objects.equals(string, newString);
            }
        });
        binding.editTextEditor.addTextChangedListener(new TextWatcher() {
            private String string;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                string = charSequence.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String newString = editable.toString();
                unSaved = !Objects.equals(string, newString);
            }
        });
    }

    /**
     * save input and exit
     */
    private void saveAndBack() {
        String title = binding.edittextTitle.getText().toString();
        String content = binding.editTextEditor.getText().toString();
        //save but without title
        if (unSaved && Objects.equals(title, "")) {
            new AlertDialog.Builder(this).setTitle(getString(R.string.hint))
                    .setMessage(R.string.note_save_warning)
                    .setPositiveButton(R.string.ok, (dialogInterface, i) -> {
                        back();
                    }).setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> {
                    }).show();
            return;
        }
        //save and exit
        if (unSaved && !Objects.equals(title, "")) {
            if (Objects.equals(currentNote, null)) {
                viewModel.addNote(title, content);
            } else {
                viewModel.updateNote(currentNote.id, title, content);
            }
        }
        back();
    }

    /**
     * confirm save when back button is down.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        String title = binding.edittextTitle.getText().toString();
        if (Objects.equals(title, "")) {
            title = "it?";
        } else {
            title = String.format("'%s'?", title);
        }
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (unSaved) {
                //confirm save
                new AlertDialog.Builder(this).setTitle(R.string.hint)
                        .setMessage(String.format("%s %s", getString(R.string.confirm_exit), title))
                        .setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                            saveAndBack();
                        }).setNegativeButton(R.string.no, (dialogInterface, i) -> {
                        }).show();
                return false;
            } else {
                back();
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    private void back() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public void onPause() {
        //hide soft keyboard when exit.
        InputMethodManager im = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(binding.editTextEditor.getWindowToken(), 0);
        im.hideSoftInputFromWindow(binding.edittextTitle.getWindowToken(), 0);
        super.onPause();
    }
}