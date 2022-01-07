package com.cb008264.easy_pill_android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cb008264.easy_pill_android.admin.AdminAddNotesActivity;
import com.cb008264.easy_pill_android.admin.AdminUpdateNoteActivity;
import com.cb008264.easy_pill_android.model.Note;

import java.util.List;

public class NotesAdapter  extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {
    Context context;
    Activity activity;
    List<Note> noteList;

    public NotesAdapter(Context context, Activity activity, List<Note> noteList) {
        this.context = context;
        this.activity = activity;
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_list_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(noteList.get(position).getTitle());
        holder.description.setText(noteList.get(position).getDescription());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(context, AdminUpdateNoteActivity.class);
               intent.putExtra("title",noteList.get(position).getTitle());
               intent.putExtra("description",noteList.get(position).getDescription());
               intent.putExtra("id",noteList.get(position).getId());
               activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public void addItem(Note note) {
        noteList.add(note);
        notifyDataSetChanged();
    }

    public void updateItem(String id, EditText title, EditText description) {
        for (Note note: noteList)
        {
            if(note.getId().equalsIgnoreCase(id))
            {
                note.setTitle(title.getText().toString());
                note.setDescription(description.getText().toString());
            }
        }
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title,description;
        RelativeLayout layout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.note_title);
            description = itemView.findViewById(R.id.note_description);
            layout = itemView.findViewById(R.id.note_layout);
        }
    }

    public List<Note> getList()
    {
        return noteList;
    }

    public void removeItem(int position)
    {
        noteList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Note note, int position)
    {
        noteList.add(position,note);
        notifyItemInserted(position);
    }
}
