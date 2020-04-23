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
import com.example.studentprogresstracker.database.CourseEntity;
import com.example.studentprogresstracker.ui.CourseAdapter;
import com.example.studentprogresstracker.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.studentprogresstracker.utilities.Constants.NEW_COURSE;
import static com.example.studentprogresstracker.utilities.Constants.TERM_ID_KEY;

public class CourseListActivity extends AppCompatActivity {

    @BindView(R.id.course_recycler_view)
    RecyclerView courseRecyclerView;

    @OnClick(R.id.course_add_fab)
    void addClickHandler() {
        Intent intent = new Intent(this, CourseEditorActivity.class);
        intent.putExtra(TERM_ID_KEY, termId);
        intent.putExtra(NEW_COURSE, true);
                startActivity(intent);
    }

    private List<CourseEntity> courseData = new ArrayList<>();
    private CourseAdapter courseAdapter;
    private MainViewModel mainViewModel;
    private static int termId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        ButterKnife.bind(this);

        initRecyclerView();
        initViewModel();

    }

    private void initViewModel() {

        final Observer<List<CourseEntity>> coursesObserver = new Observer<List<CourseEntity>>() {
            @Override
            public void onChanged(List<CourseEntity> courseEntities) {
                courseData.clear();
                courseData.addAll(courseEntities);

                if(courseAdapter == null) {
                    courseAdapter = new CourseAdapter(courseData, CourseListActivity.this);
                    courseRecyclerView.setAdapter(courseAdapter);
                }
                else {
                    courseAdapter.notifyDataSetChanged();
                }
            }
        };

        mainViewModel = ViewModelProviders.of(this)
                .get(MainViewModel.class);

        Bundle extras = getIntent().getExtras();
            if (extras != null) {
                termId = extras.getInt(TERM_ID_KEY);
            }

        mainViewModel.getCoursesbyTerm(termId).observe(this, coursesObserver);

    }

    private void initRecyclerView() {
        courseRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        courseRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration divider = new DividerItemDecoration(
                courseRecyclerView.getContext(), layoutManager.getOrientation());

        courseRecyclerView.addItemDecoration(divider);
    }

}
