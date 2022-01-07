package com.cb008264.easy_pill_android.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cb008264.easy_pill_android.R;
import com.cb008264.easy_pill_android.model.Note;
import com.cb008264.easy_pill_android.utilities.NotesProvider;

import static com.cb008264.easy_pill_android.admin.AdminNotesFragment.notesAdapter;

public class AdminUpdateNoteActivity extends AppCompatActivity {
    EditText title, description;
    Button updateBtn;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_update_note);
        title = findViewById(R.id.update_title);
        description = findViewById(R.id.update_description);
        updateBtn = findViewById(R.id.updateNote);

        Intent intent = getIntent();
        title.setText(intent.getStringExtra("title"));
        description.setText(intent.getStringExtra("description"));
        id = intent.getStringExtra("id");
        String[] projection = new String[]{NotesProvider._ID, NotesProvider.NAME, NotesProvider.DESCRIPTION};
        ContentResolver contentResolver = this.getContentResolver();
        Cursor rs = contentResolver.query(NotesProvider.CONTENT_URI, projection, null, null, null);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(title.getText().toString()) && !TextUtils.isEmpty(description.getText())) {
                    ContentValues cv = new ContentValues();
                    cv.put(NotesProvider.DESCRIPTION, description.getText().toString());
                    contentResolver.update(NotesProvider.CONTENT_URI, cv, "_id = ?", new String[]{id});
                    Toast.makeText(getApplicationContext(), "Note Updated", Toast.LENGTH_SHORT).show();
                    notesAdapter.updateItem(id,title,description);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Both Fields Required", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}