package com.example.takenoteapp.Interface;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.takenoteapp.InitDatabase.Note;

import java.util.List;

//create DAO interface
@Dao
public interface NotesDao {
    //insert note
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(Note note);

    //delete multiple notes
    @Delete
    void deleteNote(Note... note);

    //edit exist note
    @Update
    void updateNote(Note note);

    //get all notes
    @Query("SELECT * FROM notes")
    List<Note> getNotes();

    //get note by its specific id
    @Query("SELECT * FROM notes WHERE id = :noteId")
    Note getNoteById(int noteId);

    //get multiple notes by their title
    @Query("SELECT * FROM notes WHERE title LIKE :titleSearch")
    List<Note> getNoteByTitle(String titleSearch);

    //get multiple notes by their tag
    @Query("SELECT * FROM notes WHERE tag LIKE :tagSearch")
    List<Note> getNoteByTag(String tagSearch);

    //get multiple notes by their string
    @Query("SELECT * FROM notes WHERE text = :stringSearch")
    List<Note> getNoteByString(String stringSearch);

    //get multiple notes by their date
    @Query("SELECT * FROM notes WHERE date = :dateSearch")
    List<Note> getNoteByDate(long dateSearch);
}