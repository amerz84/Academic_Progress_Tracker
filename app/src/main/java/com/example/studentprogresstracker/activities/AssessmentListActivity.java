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
import com.example.studentprogresstracker.database.AssessmentEntity;
import com.example.studentprogresstracker.ui.AssessmentAdapter;
import com.example.studentprogresstracker.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.studentprogresstracker.utilities.Constants.COURSE_ID_KEY;
import static com.example.studentprogresstracker.utilities.Constants.NEW_ASSESSMENT;
import static com.example.studentprogresstracker.utilities.Constants.TERM_ID_KEY;

public class AssessmentListActivity extends AppCompatActivity {

    @BindView(R.id.assessment_recycler_view)
    RecyclerView assessmentRecyclerView;

    private List<AssessmentEntity> assessmentData = new ArrayList<>();
    private AssessmentAdapter assessmentAdapter;
    private MainViewModel mainViewModel;
    private static int courseId, termId;

    @OnClick(R.id.assessment_add_fab)
    void addClickHandler() {
        Intent intent = new Intent(this, AssessmentEditorActivity.class);
        intent.putExtra(COURSE_ID_KEY, courseId);
        intent.putExtra(NEW_ASSESSMENT, true);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_list);

        ButterKnife.bind(this);

        initRecyclerView();
        initViewModel();

    }

    private void initViewModel() {

        final Observer<List<AssessmentEntity>> assessmentObserver = new Observer<List<AssessmentEntity>>() {
            @Override
            public void onChanged(List<AssessmentEntity> assessmentEntities) {
                assessmentData.clear();
                assessmentData.addAll(assessmentEntities);

                if(assessmentAdapter == null) {
                    assessmentAdapter = new AssessmentAdapter(assessmentData, AssessmentListActivity.this);
                    assessmentRecyclerView.setAdapter(assessmentAdapter);
                }
                else {
                    assessmentAdapter.notifyDataSetChanged();
                }
            }
        };
        mainViewModel = ViewModelProviders.of(this)
                .get(MainViewModel.class);

        Bundle extras = getIntent().getExtras();
        courseId = extras.getInt(COURSE_ID_KEY);
        termId = extras.getInt(TERM_ID_KEY);

        mainViewModel.getAssessmentsByCourse(courseId).observe(this, assessmentObserver);
    }

    private void initRecyclerView() {
        assessmentRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        assessmentRecyclerView.setLayoutManager(layoutManager);

        assessmentAdapter = new AssessmentAdapter(assessmentData, this);
        assessmentRecyclerView.setAdapter(assessmentAdapter);

        DividerItemDecoration divider = new DividerItemDecoration(
                assessmentRecyclerView.getContext(), layoutManager.getOrientation());

        assessmentRecyclerView.addItemDecoration(divider);
    }
}
