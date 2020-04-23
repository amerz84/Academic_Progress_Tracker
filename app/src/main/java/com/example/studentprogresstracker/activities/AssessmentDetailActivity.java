package com.example.studentprogresstracker.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.studentprogresstracker.R;
import com.example.studentprogresstracker.database.AssessmentEntity;
import com.example.studentprogresstracker.database.DBConverter;
import com.example.studentprogresstracker.utilities.Receiver;
import com.example.studentprogresstracker.viewmodel.AssessmentViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.studentprogresstracker.utilities.Constants.ASSESSMENT_DATE;
import static com.example.studentprogresstracker.utilities.Constants.ASSESSMENT_ID_KEY;
import static com.example.studentprogresstracker.utilities.Constants.ASSESSMENT_TITLE;
import static com.example.studentprogresstracker.utilities.Constants.COURSE_ID_KEY;

public class AssessmentDetailActivity extends AppCompatActivity {

    @BindView(R.id.assessment_detail_title)
    TextView assessmentTitleTextView;
    @BindView(R.id.assessment_detail_due_date)
    TextView assessmentDueDateTextView;

    private AssessmentViewModel assessmentViewModel;
    private static int courseId, assessmentId;
    private long dueDate;

    @OnClick(R.id.assessment_detail_notes_button)
    void addClickHandler() {
        Intent intent = new Intent(this, CourseNotesActivity.class);
        intent.putExtra(COURSE_ID_KEY, courseId);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_detail);

        ButterKnife.bind(this);
        initViewModel();
    }

    private void initViewModel() {
        assessmentViewModel = ViewModelProviders.of(this)
                .get(AssessmentViewModel.class);
        assessmentViewModel.liveAssessment.observe(this, new Observer<AssessmentEntity>() {
            @Override
            public void onChanged(AssessmentEntity assessmentEntity) {
                assessmentId = assessmentEntity.getId();
                dueDate = assessmentEntity.getDueDate().getTime();
                assessmentTitleTextView.setText(assessmentEntity.getTitle());
                assessmentDueDateTextView.setText("Due Date: " + DBConverter.dateToString(assessmentEntity.getDueDate()));
            }
        });

        Bundle extras = getIntent().getExtras();
        courseId = extras.getInt(COURSE_ID_KEY);
        assessmentId = extras.getInt(ASSESSMENT_ID_KEY);

        assessmentViewModel.loadAssessment(assessmentId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_course_detail, menu);
        menu.findItem(R.id.action_share).setVisible(false);
        menu.findItem(R.id.action_edit_data).setVisible(true);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.action_edit_data:
                Intent i = new Intent(this, AssessmentEditorActivity.class);
                i.putExtra(ASSESSMENT_TITLE, assessmentTitleTextView.getText());
                i.putExtra(ASSESSMENT_DATE, assessmentDueDateTextView.getText().toString());
                i.putExtra(COURSE_ID_KEY, courseId);
                i.putExtra(ASSESSMENT_ID_KEY, assessmentId);
                startActivity(i);
                break;
            case R.id.action_delete:
                assessmentViewModel.deleteAssessment();
                finish();
                break;
            case R.id.action_notify:
                i = new Intent(this, Receiver.class);
                i.putExtra(ASSESSMENT_ID_KEY, assessmentId);
                i.putExtra("notify_key", assessmentTitleTextView.getText() + " - goal date today");
                PendingIntent sender = PendingIntent.getBroadcast(this, 2, i, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, dueDate, sender);
                break;
            case android.R.id.home:
                i = new Intent(this, AssessmentListActivity.class);
                i.putExtra(COURSE_ID_KEY, courseId);
                startActivity(i);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
