package com.example.takenoteapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.takenoteapp.Adapter.NotesAdapter;
import com.example.takenoteapp.InitDatabase.Note;
import com.example.takenoteapp.Interface.NotesDao;
import com.example.takenoteapp.Listener.ItemClickListener;
import com.example.takenoteapp.RoomDatabase.NotesDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemClickListener {
    private RecyclerView recyclerView;
    private ArrayList<Note> notes;
    private NotesAdapter adapter;
    private NotesDao dao;
    private FloatingActionButton fab;
    private EditText search_note_txt, search_note1_txt;
    private ActionMode act;
    private ActionMode.Callback mActionModeCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");
        recyclerView = findViewById(R.id.notes_list);
        //todo: change layout of recyclerview
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, EditNoteActivity.class));
            }
        });
        dao = NotesDB.getInstance(this).notesDao();
        //search multiple notes by their title
        search_note_txt = findViewById(R.id.search_note_txt);
        search_note_txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterByTitle(s.toString());
            }
        });
        /*use code bellow with sql query
        search_note_txt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    String res = search_note_txt.getText().toString().trim();
                    notes = new ArrayList<>();
                    notes.addAll(NotesDB.getInstance(MainActivity.this).notesDao().getNoteByTitle(res));
                    adapter = new NotesAdapter(MainActivity.this, notes);
                    adapter.setItemClickListener(MainActivity.this);
                    recyclerView.setAdapter(adapter);
                    hideKeyboardFromSearch();
                    return true;
                }
                return false;
            }
        });*/
        //search multiple notes by their string
        search_note1_txt = findViewById(R.id.search_note_txt1);
        search_note1_txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterByString(s.toString());
            }
        });
    }

    private void filterByTitle(String title){
        ArrayList<Note> filteredListTitle = new ArrayList<>();
        for(Note note: notes){
            if(note.getTitleText().toLowerCase().contains(title.toLowerCase())){
                filteredListTitle.add(note);
            }
        }
        adapter = new NotesAdapter(this, filteredListTitle);
        adapter.setItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    private void filterByString(String text){
        ArrayList<Note> filteredListText = new ArrayList<>();
        for(Note note: notes){
            if(note.getNoteText().toLowerCase().contains(text.toLowerCase())){
                filteredListText.add(note);
            }
        }
        adapter = new NotesAdapter(this, filteredListText);
        adapter.setItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    /*
    private void hideKeyboardFromSearch(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }*/

    private void loadNotes() {
        //get all notes from room database and add data to array list
        notes = new ArrayList<>();
        List<Note> list = dao.getNotes();
        notes.addAll(list);
        adapter = new NotesAdapter(this, notes);
        adapter.setItemClickListener(this);
        recyclerView.setAdapter(adapter);
        displayEmptyNote();
    }

    private void displayEmptyNote() {
        if(notes.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            findViewById(R.id.empty_notes_view).setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            findViewById(R.id.empty_notes_view).setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_layout1) {
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(mLayoutManager);
            return true;
        }
        if(id == R.id.action_layout2){
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(mLayoutManager);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //update when creating new note
    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
    }

    @Override
    public void StartNoteClick(Note note) {
        Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
        intent.putExtra("noteID", note.getId()); //get id of note when click to edit
        startActivity(intent);
    }

    @Override
    public void StartNoteLongClick(Note note) {
        fab.setVisibility(View.GONE);
        note.setChecked(true); //set current note selected as checked
        adapter.setMultiCheckMode(true);
        adapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void StartNoteClick(Note note) {
                note.setChecked(!note.isChecked());
                List<Note> selectedNote = adapter.getCheckedNotes();
                if(selectedNote.size() == 0) {
                    act.finish();
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void StartNoteLongClick(Note note) {

            }
        });
        mActionModeCallback = new ActionMode.Callback() {
            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch(item.getItemId()){
                    case R.id.action_delete_notes:
                        DeleteMultipleNotes();
                        mode.finish();
                        return true;
                    default: return false;
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                actionMode.getMenuInflater().inflate(R.menu.main_action_mode, menu);
                act = actionMode;
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                adapter.setMultiCheckMode(false);
                adapter.setItemClickListener(MainActivity.this);
                fab.setVisibility(View.VISIBLE);
                mode.finish();
            }
        };
        startActionMode(mActionModeCallback);
    }

    private void DeleteMultipleNotes() {
        List<Note> selectedNote = adapter.getCheckedNotes();
        if(selectedNote.size() != 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete note");
            if(selectedNote.size() == 1){
                builder.setMessage("Do you want to delete this note?");
            }
            else{
                builder.setMessage("Do you want to delete " + selectedNote.size() + " notes");
            }
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    for(Note note: selectedNote){
                        dao.deleteNote(note);
                    }
                    loadNotes(); //update list note after deleting
                    Toast.makeText(MainActivity.this, "Delete note successfully!!!", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
}