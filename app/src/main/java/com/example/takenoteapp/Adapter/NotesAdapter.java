package com.example.takenoteapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.takenoteapp.InitDatabase.Note;
import com.example.takenoteapp.Listener.ItemClickListener;
import com.example.takenoteapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Note> notes;
    private ItemClickListener itemClickListener;
    private boolean multiCheckMode = false;

    @SuppressLint("SimpleDateFormat")
    public String displayTime(long time) {
        DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy 'at' hh:mm aaa");
        return format.format(new Date(time));
    }

    //Constructor
    public NotesAdapter(Context context, ArrayList<Note> notes) {
        this.context = context;
        this.notes = notes;
    }

    @NonNull
    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.ViewHolder holder, int position) {
        if(!notes.isEmpty()){
            holder.noteText.setText("Your note: " + notes.get(position).getNoteText());
            holder.noteDate.setText("Last modified date: " + displayTime(notes.get(position).getNoteDate()));
            holder.titleText.setText("Title: " + notes.get(position).getTitleText());
            holder.tagText.setText("Tag: " + notes.get(position).getTagText());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.StartNoteClick(notes.get(position));
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    itemClickListener.StartNoteLongClick(notes.get(position));
                    return false;
                }
            });
            if(multiCheckMode){
                holder.checkBox.setVisibility(View.VISIBLE);
                holder.checkBox.setChecked(notes.get(position).isChecked());
            }
            else{
                holder.checkBox.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public List<Note> getCheckedNotes() {
        List<Note> noteListChecked = new ArrayList<>();
        for (Note note: notes) {
            if (note.isChecked()){
                noteListChecked.add(note);
            }
        }
        return noteListChecked;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView noteText, noteDate, titleText, tagText;
        private CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            noteDate = itemView.findViewById(R.id.note_date);
            noteText = itemView.findViewById(R.id.note_text);
            titleText = itemView.findViewById(R.id.title_text);
            tagText = itemView.findViewById(R.id.tag_text);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setMultiCheckMode(boolean isCheck) {
        this.multiCheckMode = isCheck;
        if (!isCheck) {
            for (Note note: notes) {
                note.setChecked(false);
            }
        }
        notifyDataSetChanged();
    }
}