package com.example.takenoteapp.InitDatabase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

//init database
@Entity(tableName = "notes")
public class Note {
    @PrimaryKey(autoGenerate = true) private int id; //key of each note
    @ColumnInfo(name = "text") private String noteText; //content of note
    @ColumnInfo(name = "date") private long noteDate; //latest date of note
    @ColumnInfo(name = "title") private String titleText; //title of note
    @ColumnInfo(name = "tag") private String tagText; //tag of note
    @Ignore private boolean checked = false; //checkbox to delete multiple notes, no need to store its state in DB

    public Note() {
        //default constructor
    }

    public Note(String noteText, long noteDate, String titleText, String tagText) {
        this.noteText = noteText;
        this.noteDate = noteDate;
        this.titleText = titleText;
        this.tagText = tagText;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public long getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(long noteDate) {
        this.noteDate = noteDate;
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public String getTagText() {
        return tagText;
    }

    public void setTagText(String tagText) {
        this.tagText = tagText;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}