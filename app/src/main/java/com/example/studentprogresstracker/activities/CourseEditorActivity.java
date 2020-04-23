package com.example.studentprogresstracker.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.studentprogresstracker.R;
import com.example.studentprogresstracker.database.CourseEntity;
import com.example.studentprogresstracker.database.DBConverter;
import com.example.studentprogresstracker.viewmodel.CourseViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.studentprogresstracker.utilities.Constants.COURSE_ID_KEY;
import static com.example.studentprogresstracker.utilities.Constants.COURSE_STATUS;
import static com.example.studentprogresstracker.utilities.Constants.COURSE_TITLE;
import static com.example.studentprogresstracker.utilities.Constants.EDITING_KEY;
import static com.example.studentprogresstracker.utilities.Constants.NEW_COURSE;
import static com.example.studentprogresstracker.utilities.Constants.TERM_ID_KEY;

public class CourseEditorActivity extends AppCompatActivity {

    @BindView(R.id.course_editor_title)
    EditText courseTitleTextView;
    @BindView(R.id.course_editor_start)
    TextView courseStartDate;
    @BindView(R.id.course_editor_end)
    TextView courseEndDate;
    @BindView(R.id.course_ed_status_spinner)
    Spinner courseStatusSpinner;

    private CourseViewModel courseViewModel;
    private boolean newCourse, editing, startDateSelected;
    private static int termId, courseId;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_editor);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_check);

        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            editing = savedInstanceState.getBoolean(EDITING_KEY);
        }

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                CourseEditorActivity.this.updateLabel(startDateSelected);

            }
        };

        courseStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CourseEditorActivity.this, dateSetListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                startDateSelected = true;
            }
        });

        courseEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CourseEditorActivity.this, dateSetListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                startDateSelected = false;
            }
        });

        initViewModel();

    }

    private void updateLabel(boolean firstSelected) {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        if(firstSelected) {
            courseStartDate.setText(sdf.format(myCalendar.getTime()));
        }
        else {
            courseEndDate.setText(sdf.format(myCalendar.getTime()));
        }
    }

    private void initViewModel() {

        courseViewModel = ViewModelProviders.of(this)
            .get(CourseViewModel.class);
        courseViewModel.liveCourse.observe(this, new Observer<CourseEntity>() {
            @Override
            public void onChanged(CourseEntity courseEntity) {
                if (courseEntity != null && !editing) {
                    Intent intent = getIntent();
                    courseTitleTextView.setText(intent.getStringExtra(COURSE_TITLE));
                    courseStartDate.setText(DBConverter.dateToString(courseEntity.getStartDate()));
                    courseEndDate.setText(DBConverter.dateToString(courseEntity.getAnticipatedEndDate()));
                    courseStatusSpinner.setSelection(DBConverter.statusPosition(intent.getStringExtra(COURSE_STATUS)));
                }
            }
        });

        Bundle extras = getIntent().getExtras();
        termId = extras.getInt(TERM_ID_KEY);
        if(extras.getBoolean(NEW_COURSE)) {
            newCourse = true;
            setTitle(getString(R.string.new_course));
        }
        else {
            courseId = extras.getInt(COURSE_ID_KEY);
            courseViewModel.loadCourse(courseId);
            setTitle("Edit " + extras.getString(COURSE_TITLE));
        }
    }

    //Save data when UP button pressed
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            saveAndReturn();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Save data when BACK button pressed
    @Override
    public void onBackPressed() {
        saveAndReturn();
    }

    private void saveAndReturn() {
        courseViewModel.saveCourse(courseTitleTextView.getText().toString(), DBConverter.stringToDate(courseStartDate.getText().toString()),
            DBConverter.stringToDate(courseEndDate.getText().toString()), courseStatusSpinner.getSelectedItem().toString(), termId, newCourse);
        finish();
        Intent i = new Intent(this, CourseListActivity.class);
        startActivity(i);
    }

    //Prevents loss of input text during portrait/landscape change
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(EDITING_KEY, true);
        super.onSaveInstanceState(outState);
    }
}
