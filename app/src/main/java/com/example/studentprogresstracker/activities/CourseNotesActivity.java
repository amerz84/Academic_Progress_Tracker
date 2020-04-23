package com.example.studentprogresstracker.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentprogresstracker.R;
import com.example.studentprogresstracker.database.CourseNoteEntity;
import com.example.studentprogresstracker.ui.NotesAdapter;
import com.example.studentprogresstracker.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.studentprogresstracker.utilities.Constants.COURSE_ID_KEY;
import static com.example.studentprogresstracker.utilities.Constants.NEW_NOTE;

public class CourseNotesActivity extends AppCompatActivity {

    private static int courseId;
    private List<CourseNoteEntity> notesList = new ArrayList<CourseNoteEntity>();
    private NotesAdapter notesAdapter;
    private MainViewModel mainViewModel;

    @BindView(R.id.note_recycler_view)
    RecyclerView noteRecyclerView;

    @OnClick(R.id.note_add_fab)
    void addClickHandler() {
        Intent intent = new Intent(this, CourseNoteDetailActivity.class);
        intent.putExtra(COURSE_ID_KEY, courseId);
        intent.putExtra(NEW_NOTE, true);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        ButterKnife.bind(this);

        initRecyclerView();
        initViewModel();

    }

    private void initViewModel() {

        final Observer<List<CourseNoteEntity>> notesObserver = new Observer<List<CourseNoteEntity>>() {
            @Override
            public void onChanged(List<CourseNoteEntity> courseNoteEntities) {
                notesList.clear();
                notesList.addAll(courseNoteEntities);

                if (notesAdapter == null) {
                    notesAdapter = new NotesAdapter(notesList, CourseNotesActivity.this);
                    noteRecyclerView.setAdapter(notesAdapter);
                } else {
                    notesAdapter.notifyDataSetChanged();
                }
            }
        };

        mainViewModel = ViewModelProviders.of(this)
                .get(MainViewModel.class);

        Bundle extras = getIntent().getExtras();
        courseId = extras.getInt(COURSE_ID_KEY);

        mainViewModel.getNotesByCourse(courseId).observe(this, notesObserver);

    }

    private void initRecyclerView() {
        noteRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        noteRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration divider = new DividerItemDecoration(
                noteRecyclerView.getContext(), layoutManager.getOrientation());

        noteRecyclerView.addItemDecoration(divider);
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }
}
