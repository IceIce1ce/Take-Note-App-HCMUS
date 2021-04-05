package com.example.takenoteapp.RoomDatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.takenoteapp.Interface.NotesDao;
import com.example.takenoteapp.InitDatabase.Note;

//create room database
@Database(entities = Note.class, version = 1, exportSchema = false)
public abstract class NotesDB extends RoomDatabase {
    public abstract NotesDao notesDao();

    public static NotesDB getInstance(Context context){
        return Room.databaseBuilder(context, NotesDB.class, "mydb").allowMainThreadQueries().build();
    }
}