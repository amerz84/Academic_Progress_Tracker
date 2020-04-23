package com.example.studentprogresstracker.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentprogresstracker.R;
import com.example.studentprogresstracker.database.CourseEntity;
import com.example.studentprogresstracker.database.DBConverter;
import com.example.studentprogresstracker.database.MentorEntity;
import com.example.studentprogresstracker.ui.MentorAdapter;
import com.example.studentprogresstracker.utilities.Receiver;
import com.example.studentprogresstracker.viewmodel.CourseViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.studentprogresstracker.utilities.Constants.COURSE_END;
import static com.example.studentprogresstracker.utilities.Constants.COURSE_ID_KEY;
import static com.example.studentprogresstracker.utilities.Constants.COURSE_START;
import static com.example.studentprogresstracker.utilities.Constants.COURSE_STATUS;
import static com.example.studentprogresstracker.utilities.Constants.COURSE_TITLE;
import static com.example.studentprogresstracker.utilities.Constants.NEW_MENTOR;
import static com.example.studentprogresstracker.utilities.Constants.TERM_ID_KEY;

public class CourseDetailActivity extends AppCompatActivity {

    @BindView(R.id.course_detail_title)
    TextView courseTitleTextView;
    @BindView(R.id.course_detail_start_date)
    TextView courseStartTextView;
    @BindView(R.id.course_detail_end_date)
    TextView courseEndTextView;
    @BindView(R.id.course_detail_status)
    TextView courseStatusTextView;
    @BindView(R.id.course_mentor_recycler)
    RecyclerView courseMentorRecycler;

    private List<MentorEntity> mentorData = new ArrayList<>();
    private MentorAdapter mentorAdapter;
    public CourseViewModel courseViewModel, mentorViewModel;
    private static int termId, courseId;
    //startDate and endDate used for sending notification alerts
    private long startDate, endDate;

    @OnClick(R.id.add_mentor_button)
    void addClickHandler() {
        Intent intent = new Intent(this, MentorDetailActivity.class);
        intent.putExtra(COURSE_ID_KEY, courseId);
        intent.putExtra(NEW_MENTOR, true);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        ButterKnife.bind(this);

        initRecyclerView();
        initViewModel();
        initMentorViewModel();

    }

    private void initMentorViewModel() {
        mentorViewModel = ViewModelProviders.of(this)
                .get(CourseViewModel.class);

        Observer<List<MentorEntity>> mentorsObserver = new Observer<List<MentorEntity>>() {
            @Override
            public void onChanged(List<MentorEntity> mentorEntities) {
                mentorData.clear();
                mentorData.addAll(mentorEntities);

                if (mentorAdapter == null) {
                    mentorAdapter = new MentorAdapter(mentorData, courseMentorRecycler.getContext());
                    courseMentorRecycler.setAdapter(mentorAdapter);
                } else {
                    mentorAdapter.notifyDataSetChanged();
                }
            }
        };

        mentorViewModel.getModelMentors(courseId).observe(this, mentorsObserver);
    }

    private void initViewModel() {

        Bundle extras = getIntent().getExtras();
        termId = extras.getInt(TERM_ID_KEY);
        courseId = extras.getInt(COURSE_ID_KEY);

        courseViewModel = ViewModelProviders.of(this)
                .get(CourseViewModel.class);
        courseViewModel.liveCourse.observe(this, new Observer<CourseEntity>() {
            @Override
            public void onChanged(CourseEntity courseEntity) {
                courseId = courseEntity.getId();
                termId = courseEntity.getTermId();
                startDate = courseEntity.getStartDate().getTime();
                endDate = courseEntity.getAnticipatedEndDate().getTime();
                courseTitleTextView.setText(courseEntity.getTitle());
                courseStartTextView.setText("Start: " + DBConverter.dateToString(courseEntity.getStartDate()));
                courseEndTextView.setText("Anticipated End: " + DBConverter.dateToString(courseEntity.getAnticipatedEndDate()));
                courseStatusTextView.setText(courseEntity.getStatus().toString());
            }
        });

        courseViewModel.loadCourse(courseId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_course_detail, menu);
        menu.findItem(R.id.action_share).setVisible(true);
        menu.findItem(R.id.action_edit_data).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.action_edit_data:
                Intent i = new Intent(this, CourseEditorActivity.class);
                i.putExtra(COURSE_TITLE, courseTitleTextView.getText());
                i.putExtra(COURSE_START, courseStartTextView.getText().toString());
                i.putExtra(COURSE_END, courseEndTextView.getText().toString());
                i.putExtra(COURSE_STATUS, courseStatusTextView.getText().toString());
                i.putExtra(TERM_ID_KEY, termId);
                i.putExtra(COURSE_ID_KEY, courseId);
                startActivity(i);
                break;
            case R.id.action_delete:
                courseViewModel.deleteCourse();
                finish();
                break;
            case R.id.action_share:
                    i = new Intent();
                    i.setAction(Intent.ACTION_SEND).putExtra(Intent.EXTRA_TEXT, "Hello")
                            .putExtra(Intent.EXTRA_TITLE, "Notes for " + courseTitleTextView.getText().toString()).setType("text/plain");
                    Intent shareIntent = Intent.createChooser(i, null);
                    startActivity(shareIntent);
                break;
            case R.id.action_notify:
                boolean includeStart = DBConverter.toDate(startDate).after(yesterday());

                //Only set alert for startDate if startDate hasn't occurred yet
                if (includeStart == true) {
                    i = new Intent(this, Receiver.class);
                    i.putExtra(COURSE_ID_KEY, courseId);
                    i.putExtra("notify_key", courseTitleTextView.getText() + " starting today");
                    PendingIntent sender = PendingIntent.getBroadcast(this, 0, i, 0);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, startDate, sender);
                }

                i = new Intent(this, Receiver.class);
                i.putExtra(COURSE_ID_KEY, courseId);
                i.putExtra("notify_key", courseTitleTextView.getText() + " scheduled to end today");
                PendingIntent senderTwo = PendingIntent.getBroadcast(this, 1, i, 0);
                AlarmManager alarmManagerTwo = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManagerTwo.set(AlarmManager.RTC_WAKEUP, endDate, senderTwo);
                break;
            case android.R.id.home:
                i = new Intent(this, CourseListActivity.class);
                i.putExtra(TERM_ID_KEY, termId);
                i.putExtra(COURSE_ID_KEY, courseId);
                startActivity(i);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void initRecyclerView() {
        courseMentorRecycler.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        courseMentorRecycler.setLayoutManager(layoutManager);

        DividerItemDecoration divider = new DividerItemDecoration(
                courseMentorRecycler.getContext(), layoutManager.getOrientation());

        courseMentorRecycler.addItemDecoration(divider);
    }

    public void launchAssessmentList(View v) {
        Intent i = new Intent(this, AssessmentListActivity.class);
        i.putExtra(COURSE_ID_KEY, courseId);
        i.putExtra(TERM_ID_KEY, termId);
        startActivity(i);
    }

    public void launchNotesList(View view) {
        Intent i = new Intent(this, CourseNotesActivity.class);
            i.putExtra(COURSE_ID_KEY, courseId);
        startActivity(i);
    }

    //Get date for day before current date
    private Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }
}
