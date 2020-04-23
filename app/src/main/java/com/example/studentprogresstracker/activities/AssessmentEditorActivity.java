package com.example.studentprogresstracker.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.studentprogresstracker.R;
import com.example.studentprogresstracker.database.AssessmentEntity;
import com.example.studentprogresstracker.database.DBConverter;
import com.example.studentprogresstracker.viewmodel.AssessmentViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.studentprogresstracker.utilities.Constants.ASSESSMENT_ID_KEY;
import static com.example.studentprogresstracker.utilities.Constants.ASSESSMENT_TITLE;
import static com.example.studentprogresstracker.utilities.Constants.COURSE_ID_KEY;
import static com.example.studentprogresstracker.utilities.Constants.EDITING_KEY;
import static com.example.studentprogresstracker.utilities.Constants.NEW_ASSESSMENT;

public class AssessmentEditorActivity extends AppCompatActivity {

    @BindView(R.id.assessment_editor_title)
    EditText assessmentTitleTextView;
    @BindView(R.id.assessment_editor_due_date)
    TextView assessmentDueDate;

    private AssessmentViewModel assessmentViewModel;
    private boolean newAssessment, editing;
    private static int courseId, assessmentId;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_editor);

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
                AssessmentEditorActivity.this.updateLabel();

            }
        };

        assessmentDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AssessmentEditorActivity.this, dateSetListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        initViewModel();

    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        assessmentDueDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void initViewModel() {


        assessmentViewModel = ViewModelProviders.of(this)
                .get(AssessmentViewModel.class);
        assessmentViewModel.liveAssessment.observe(this, new Observer<AssessmentEntity>() {
            @Override
            public void onChanged(AssessmentEntity assessmentEntity) {
                courseId = assessmentEntity.getCourseId();

                if (assessmentEntity != null && !editing) {
                    Intent intent = getIntent();
                    assessmentTitleTextView.setText(intent.getStringExtra(ASSESSMENT_TITLE));
                    assessmentDueDate.setText(DBConverter.dateToString(assessmentEntity.getDueDate()));
                }
            }
        });

        Bundle extras = getIntent().getExtras();
        courseId = extras.getInt(COURSE_ID_KEY);

        if(extras.getBoolean(NEW_ASSESSMENT)) {
            newAssessment = true;
            setTitle(getString(R.string.new_assessment));
        }
        else {
            assessmentId = extras.getInt(ASSESSMENT_ID_KEY);
            assessmentViewModel.loadAssessment(assessmentId);
            setTitle("Edit " + "Assessment ...");
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
        assessmentViewModel.saveAssessment(assessmentTitleTextView.getText().toString(), DBConverter.stringToDate(assessmentDueDate.getText().toString()), courseId, newAssessment);
        finish();
        Intent i = new Intent(this, AssessmentListActivity.class);
        i.putExtra(COURSE_ID_KEY, courseId);
        startActivity(i);
    }

    //Prevents loss of input text during portrait/landscape change
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(EDITING_KEY, true);
        super.onSaveInstanceState(outState);
    }
}
