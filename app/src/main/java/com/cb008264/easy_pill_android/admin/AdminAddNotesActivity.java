package com.cb008264.easy_pill_android.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cb008264.easy_pill_android.R;
import com.cb008264.easy_pill_android.model.Note;
import com.cb008264.easy_pill_android.utilities.NotesIdGenerator;
import com.cb008264.easy_pill_android.utilities.NotesProvider;

import static com.cb008264.easy_pill_android.admin.AdminCustomersFragment.customerAdapter;
import static com.cb008264.easy_pill_android.admin.AdminNotesFragment.notesAdapter;

public class AdminAddNotesActivity extends AppCompatActivity {
    EditText title, description;
    Button addNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_notes);
        title = findViewById(R.id.add_title);
        description = findViewById(R.id.add_description);
        addNote = findViewById(R.id.addNote);
        String[] projection = new String[]{NotesProvider._ID, NotesProvider.NAME, NotesProvider.DESCRIPTION};
        ContentResolver contentResolver = this.getContentResolver();
        Cursor rs = contentResolver.query(NotesProvider.CONTENT_URI, projection, null, null, null);

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(title.getText().toString()) && !TextUtils.isEmpty(description.getText())) {
                    ContentValues cv = new ContentValues();
                    cv.put(NotesProvider.NAME, title.getText().toString());
                    cv.put(NotesProvider.DESCRIPTION, description.getText().toString());
                    contentResolver.insert(NotesProvider.CONTENT_URI, cv);
                    Toast.makeText(getApplicationContext(), "Note Added", Toast.LENGTH_SHORT).show();
                    notesAdapter.addItem(new Note(new NotesIdGenerator().generateNumber(),title.getText().toString(), description.getText().toString()));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Both Fields Required", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}