package com.example.takenoteapp.Listener;

import com.example.takenoteapp.InitDatabase.Note;

public interface ItemClickListener {
    void StartNoteClick(Note note);
    void StartNoteLongClick(Note note);
}