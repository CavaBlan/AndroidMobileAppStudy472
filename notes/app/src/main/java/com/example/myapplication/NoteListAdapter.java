package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.databinding.NoteItemBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.MemoViewHolder> {
    private final ANoteViewModel noteViewModel;
    private OnNoteTappedListener noteTappedListener, noteLongTappedListener;

    public NoteListAdapter(ANoteViewModel viewModel) {
        noteViewModel = viewModel;
    }

    /**
     * set note tapped listener
     */
    public void setNoteTappedListener(OnNoteTappedListener noteTappedListener) {
        this.noteTappedListener = noteTappedListener;
    }

    /**
     * set note long tapped listener
     */
    public void setNoteLongTappedListener(OnNoteTappedListener noteLongTappedListener) {
        this.noteLongTappedListener = noteLongTappedListener;
    }

    /**
     * note tapped listener
     */
    public static interface OnNoteTappedListener {
        void onTap(ANote note);
    }

    /**
     * view holder of RecyclerView
     */
    public static class MemoViewHolder extends RecyclerView.ViewHolder {
        public final NoteItemBinding binding;
        private ANote note;

        public MemoViewHolder(@NonNull NoteItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ANote getMemo() {
            return note;
        }

        /**
         * bind data with view
         */
        public void bind(@NonNull ANote note) {
            this.note = note;
            binding.tvNoteTitle.setText(note.getTitle());
            String content = note.getContent();
            if (content.length() < 80) {
                binding.tvNoteContent.setText(content);
            } else {
                binding.tvNoteContent.setText(String.format("%s...", content.substring(0, 80)));
            }
            Date millisecondDate = new Date(note.getModDate());
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss",
                    Locale.US);
            String millisecondStrings = formatter.format(millisecondDate);
            binding.tvNoteUpdateDate.setText(millisecondStrings);
        }
    }

    @NonNull
    @Override
    public MemoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NoteItemBinding noteItemBinding = NoteItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent, false);
        return new MemoViewHolder(noteItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MemoViewHolder holder, int position) {
        ArrayList<ANote> notes = noteViewModel.getNoteList().getValue();
        assert notes != null;
        ANote note = notes.get(position);
        holder.bind(note);
        //edit note
        holder.binding.getRoot().setOnClickListener(view -> {
            if (!Objects.equals(noteTappedListener, null)) {
                noteTappedListener.onTap(note);
            }
        });
        holder.binding.getRoot().setOnLongClickListener(view -> {
            if (!Objects.equals(noteLongTappedListener, null)) {
                noteLongTappedListener.onTap(note);
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        ArrayList<ANote> notes = noteViewModel.getNoteList().getValue();
        assert notes != null;
        return notes.size();
    }
}
