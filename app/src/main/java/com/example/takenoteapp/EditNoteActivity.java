package com.example.takenoteapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.takenoteapp.InitDatabase.Note;
import com.example.takenoteapp.Interface.NotesDao;
import com.example.takenoteapp.RoomDatabase.NotesDB;

import java.util.Date;

public class EditNoteActivity extends AppCompatActivity {
    private EditText inputNote, inputTitle, inputTag;
    private NotesDao dao;
    private Note tmpNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        Toolbar toolbar = findViewById(R.id.edit_note_activity_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create/Edit note");
        inputNote = findViewById(R.id.input_note);
        inputTitle = findViewById(R.id.input_title);
        inputTag = findViewById(R.id.input_tag);
        dao = NotesDB.getInstance(this).notesDao();
        //show title, tag and content of exist note
        if(getIntent().getExtras() != null) {
            tmpNote = dao.getNoteById(getIntent().getExtras().getInt("noteID", 0));
            inputNote.setText(tmpNote.getNoteText());
            inputTitle.setText(tmpNote.getTitleText());
            inputTag.setText(tmpNote.getTagText());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.save_note) {
            SaveEditNote();
        }
        return super.onOptionsItemSelected(item);
    }

    private void SaveEditNote() {
        String text = inputNote.getText().toString();
        String title = inputTitle.getText().toString();
        String tag = inputTag.getText().toString();
        if(TextUtils.isEmpty(title)){
            Toast.makeText(this, "Title can't be blank", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(tag)){
            Toast.makeText(this, "Tag can't be blank", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(text)){
            Toast.makeText(this, "Content can't be blank", Toast.LENGTH_SHORT).show();
        }
        else{
            long date = new Date().getTime();
            //insert new note to database
            if(tmpNote == null){
                tmpNote = new Note(text, date, title, tag);
                dao.insertNote(tmpNote);
                Toast.makeText(this, "Create new note successfully!!!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(EditNoteActivity.this, MainActivity.class));
            }
            //edit exist note to database
            else {
                tmpNote.setNoteText(text);
                tmpNote.setNoteDate(date);
                tmpNote.setTitleText(title);
                tmpNote.setTagText(tag);
                dao.updateNote(tmpNote);
                Toast.makeText(this, "Edit note successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(EditNoteActivity.this, MainActivity.class));
            }
        }
    }
}