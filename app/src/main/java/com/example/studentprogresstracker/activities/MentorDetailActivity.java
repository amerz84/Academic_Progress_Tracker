package com.example.studentprogresstracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.studentprogresstracker.R;
import com.example.studentprogresstracker.database.MentorEntity;
import com.example.studentprogresstracker.viewmodel.CourseViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.studentprogresstracker.utilities.Constants.COURSE_ID_KEY;
import static com.example.studentprogresstracker.utilities.Constants.EDITING_KEY;
import static com.example.studentprogresstracker.utilities.Constants.MENTOR_ID_KEY;
import static com.example.studentprogresstracker.utilities.Constants.NEW_MENTOR;

public class MentorDetailActivity extends AppCompatActivity {

    @BindView(R.id.mentor_detail_name)
    EditText mentorNameTextView;
    @BindView(R.id.mentor_detail_email)
    EditText mentorEmailTextView;
    @BindView(R.id.mentor_detail_phone)
    EditText mentorPhoneTextView;

    private CourseViewModel mentorViewModel;
    private boolean newMentor, editing;
    private static int courseId, mentorId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_detail);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_check);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            editing = savedInstanceState.getBoolean(EDITING_KEY);
        }

        initViewModel();
    }

    private void initViewModel() {
        mentorViewModel = ViewModelProviders.of(this)
                .get(CourseViewModel.class);

        mentorViewModel.liveMentor.observe(this, new Observer<MentorEntity>() {
            @Override
            public void onChanged(@Nullable MentorEntity mentorEntity) {
                if (mentorEntity != null && !editing) {
                    courseId = mentorEntity.getCourseId();
                    mentorId = mentorEntity.getId();
                    mentorNameTextView.setText(mentorEntity.getMentorName());
                    mentorEmailTextView.setText(mentorEntity.getMentorEmail());
                    mentorPhoneTextView.setText(mentorEntity.getMentorPhone());
                }
            }
        });

        Bundle extras = getIntent().getExtras();
        courseId = extras.getInt(COURSE_ID_KEY);
        if (extras.getBoolean(NEW_MENTOR)) {
            setTitle(getString(R.string.new_mentor));
            newMentor = true;
        } else {
            setTitle(getString(R.string.edit_note));
            mentorId = extras.getInt(MENTOR_ID_KEY);
            mentorViewModel.loadMentor(mentorId);
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
            mentorViewModel.deleteMentor();
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
        mentorViewModel.saveMentor(mentorNameTextView.getText().toString(), mentorEmailTextView.getText().toString(),
                mentorPhoneTextView.getText().toString(), courseId, newMentor);
        finish();
        Intent i = new Intent(this, CourseDetailActivity.class);
        i.putExtra(COURSE_ID_KEY, courseId);
        startActivity(i);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(EDITING_KEY, true);
        super.onSaveInstanceState(outState);
    }
}
