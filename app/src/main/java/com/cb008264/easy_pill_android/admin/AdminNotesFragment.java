package com.cb008264.easy_pill_android.admin;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cb008264.easy_pill_android.NotesAdapter;
import com.cb008264.easy_pill_android.R;
import com.cb008264.easy_pill_android.model.Note;
import com.cb008264.easy_pill_android.utilities.NotesProvider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;


public class AdminNotesFragment extends Fragment {
    View v;
     RecyclerView recyclerView;
    FloatingActionButton fab;
    public static NotesAdapter notesAdapter;
    List<Note> notesList;
    CoordinatorLayout coordinatorLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_admin_notes, container, false);
        recyclerView = v.findViewById(R.id.note_recycler);
        fab = v.findViewById(R.id.fab);
        coordinatorLayout= v.findViewById(R.id.frameLayout);
        notesList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        notesAdapter = new NotesAdapter(getActivity().getApplicationContext(), getActivity(), notesList);
        recyclerView.setAdapter(notesAdapter);

        String[] projection = new String[]{NotesProvider._ID, NotesProvider.NAME, NotesProvider.DESCRIPTION};
        ContentResolver contentResolver = getContext().getContentResolver();
        Cursor rs = contentResolver.query(NotesProvider.CONTENT_URI, projection, null, null, null);
        if (rs.getCount() == 0) {
            Toast.makeText(getActivity().getApplicationContext(), "No Note Available", Toast.LENGTH_SHORT).show();
        } else {
            while (rs.moveToNext()) {
                notesList.add(new Note(rs.getString(0), rs.getString(1), rs.getString(2)));
            }

        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AdminAddNotesActivity.class));
            }
        });

        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Note note = notesAdapter.getList().get(position);

                notesAdapter.removeItem(viewHolder.getAdapterPosition());
                Snackbar snackbar = Snackbar.make(coordinatorLayout,"Item Deleted",Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        notesAdapter.restoreItem(note,position);
                        recyclerView.scrollToPosition(position);
                    }
                }).addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        if(!(event == DISMISS_EVENT_ACTION))
                        {
                           contentResolver.delete(NotesProvider.CONTENT_URI,"TITLE = ?", new String[]{note.getTitle()});
                            Toast.makeText(getActivity().getApplicationContext(), "Note Deleted", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            }
        };
        ItemTouchHelper helper =  new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);

        return v;
    }

}