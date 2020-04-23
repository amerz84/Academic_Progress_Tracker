package com.example.studentprogresstracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.studentprogresstracker.R;
import com.example.studentprogresstracker.database.CourseEntity;
import com.example.studentprogresstracker.database.DBConverter;
import com.example.studentprogresstracker.database.TermEntity;
import com.example.studentprogresstracker.ui.CourseAdapter;
import com.example.studentprogresstracker.viewmodel.TermViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.studentprogresstracker.utilities.Constants.TERM_END;
import static com.example.studentprogresstracker.utilities.Constants.TERM_ID_KEY;
import static com.example.studentprogresstracker.utilities.Constants.TERM_START;
import static com.example.studentprogresstracker.utilities.Constants.TERM_TITLE;

public class TermDetailActivity extends AppCompatActivity {

    @BindView(R.id.term_detail_title)
    TextView termTitleTextView;
    @BindView(R.id.term_detail_start_date)
    TextView termStartDateTextView;
    @BindView(R.id.term_detail_end_date)
    TextView termEndDateTextView;

    @OnClick(R.id.term_detail_view_courses)
    void addClickHandler() {
        Intent intent = new Intent(this, CourseListActivity.class);
        intent.putExtra(TERM_ID_KEY, termId);
        Log.i("Intent", String.valueOf(termId));
        startActivity(intent);
    }

    private TermViewModel termViewModel;
    private CourseAdapter courseAdapter;
    private boolean newTerm;
    private static int termId, numCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);

        ButterKnife.bind(this);

        initViewModel();

        //Monitor number of child courses associated with parent term
        termViewModel.getAllCourses(termId).observe(this, new Observer<List<CourseEntity>>() {
            @Override
            public void onChanged(List<CourseEntity> courseEntities) {
                List<CourseEntity> filteredCourses = new ArrayList<>();
                for(CourseEntity course : courseEntities) {
                    if (course.getTermId() == getIntent().getIntExtra(TERM_ID_KEY, 0)) {
                        filteredCourses.add(course);
                    }
                }
                courseAdapter = new CourseAdapter(filteredCourses, TermDetailActivity.this);
                courseAdapter.setTermCourses(filteredCourses);
                numCourses = filteredCourses.size();
            }
        });
    }

    private void initViewModel() {
        termViewModel = ViewModelProviders.of(this)
                .get(TermViewModel.class);

        termViewModel.liveTerm.observe(this, new Observer<TermEntity>() {
            @Override
            public void onChanged(TermEntity termEntity) {
                termId = termEntity.getId();
                termTitleTextView.setText(termEntity.getTitle());
                termStartDateTextView.setText("Start: " + DBConverter.dateToString(termEntity.getStartDate()));
                termEndDateTextView.setText("End: " + DBConverter.dateToString(termEntity.getEndDate()));
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            newTerm = true;
        }
        else {
            termId = extras.getInt(TERM_ID_KEY);
        }

        termViewModel.loadTerm(termId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        menu.findItem(R.id.action_edit_data).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.action_edit_data:
                Intent i = new Intent(this, TermEditorActivity.class);
                i.putExtra(TERM_ID_KEY, termId);
                i.putExtra(TERM_TITLE, termTitleTextView.getText());
                i.putExtra(TERM_START, termStartDateTextView.getText().toString());
                i.putExtra(TERM_END, termEndDateTextView.getText().toString());
                startActivity(i);
                break;
            case R.id.action_delete:
                if (numCourses == 0) {
                    termViewModel.deleteTerm();
                    Toast.makeText(this, "Term Deleted", Toast.LENGTH_LONG).show();
                    finish();
                    break;
                }
                else {
                    Toast.makeText(this, "Please delete all associated courses before deleting the term", Toast.LENGTH_LONG).show();
                    break;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

}
