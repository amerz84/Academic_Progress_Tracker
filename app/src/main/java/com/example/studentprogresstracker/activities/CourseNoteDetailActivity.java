package com.example.studentprogresstracker.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.studentprogresstracker.R;
import com.example.studentprogresstracker.database.CourseNoteEntity;
import com.example.studentprogresstracker.viewmodel.NoteViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.studentprogresstracker.utilities.Constants.COURSE_ID_KEY;
import static com.example.studentprogresstracker.utilities.Constants.EDITING_KEY;
import static com.example.studentprogresstracker.utilities.Constants.NEW_NOTE;
import static com.example.studentprogresstracker.utilities.Constants.NOTE_ID_KEY;

public class CourseNoteDetailActivity extends AppCompatActivity {

    @BindView(R.id.note_text)
    TextView noteTextView;

    private NoteViewModel noteViewModel;
    private boolean newNote, editing;
    private static int courseId, noteId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_check);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            editing = savedInstanceState.getBoolean(EDITING_KEY);
        }

        initViewModel();
    }

    private void initViewModel() {
        noteViewModel = ViewModelProviders.of(this)
                .get(NoteViewModel.class);

        noteViewModel.liveNote.observe(this, new Observer<CourseNoteEntity>() {
            @Override
            public void onChanged(@Nullable CourseNoteEntity courseNoteEntity) {
                if (courseNoteEntity != null && !editing) {
                    noteTextView.setText(courseNoteEntity.getNote());
                }
            }
        });

        Bundle extras = getIntent().getExtras();
        courseId = extras.getInt(COURSE_ID_KEY);
        if (extras.getBoolean(NEW_NOTE)) {
            setTitle(getString(R.string.new_note));
            newNote = true;
        } else {
            setTitle(getString(R.string.edit_note));
            noteId = extras.getInt(NOTE_ID_KEY);
            noteViewModel.loadNote(noteId);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        menu.findItem(R.id.action_edit_data).setVisible(false);
        return true;
    }
    //Save data when UP button pressed
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            saveAndReturn();
            return true;
        }
        else if (item.getItemId() == R.id.action_delete) {
            noteViewModel.deleteNote();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }

    //Save data when BACK button pressed
    @Override
    public void onBackPressed() {
        saveAndReturn();
    }

    private void saveAndReturn() {
        noteViewModel.saveNote(noteTextView.getText().toString(), courseId, newNote);
        finish();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(EDITING_KEY, true);
        super.onSaveInstanceState(outState);
    }
}
