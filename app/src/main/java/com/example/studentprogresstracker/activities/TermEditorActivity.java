package com.example.studentprogresstracker.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
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
import com.example.studentprogresstracker.database.DBConverter;
import com.example.studentprogresstracker.database.TermEntity;
import com.example.studentprogresstracker.viewmodel.TermViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.studentprogresstracker.utilities.Constants.EDITING_KEY;
import static com.example.studentprogresstracker.utilities.Constants.TERM_ID_KEY;

public class TermEditorActivity extends AppCompatActivity {

    @BindView(R.id.term_editor_title)
    EditText termTitleTextView;
    @BindView(R.id.term_editor_start)
    TextView termStartDate;
    @BindView(R.id.term_editor_end)
    TextView termEndDate;

    final Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TermViewModel termViewModel;
    private boolean newTerm, editing, startDateSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_editor);
        setTitle(getString(R.string.new_term));

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_check);

        ButterKnife.bind(this);

        if(savedInstanceState != null) {
            editing = savedInstanceState.getBoolean(EDITING_KEY);
        }

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                TermEditorActivity.this.updateLabel(startDateSelected);

            }
        };

        termStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(TermEditorActivity.this, dateSetListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                startDateSelected = true;
            }
        });

        termEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(TermEditorActivity.this, dateSetListener, myCalendar
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
            termStartDate.setText(sdf.format(myCalendar.getTime()));
        }
        else {
            termEndDate.setText(sdf.format(myCalendar.getTime()));
        }
    }

    private void initViewModel() {

        termViewModel = ViewModelProviders.of(this)
                .get(TermViewModel.class);
        termViewModel.liveTerm.observe(this, new Observer<TermEntity>() {
            @Override
            public void onChanged(TermEntity termEntity) {
                if (termEntity != null && !editing) {
                    termTitleTextView.setText(termEntity.getTitle());
                    termStartDate.setText(DBConverter.dateToString(termEntity.getStartDate()));
                    termEndDate.setText(DBConverter.dateToString(termEntity.getEndDate()));
                }
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            newTerm = true;
            setTitle(getString(R.string.new_term));
        }
        else {
            int termId = extras.getInt(TERM_ID_KEY);
            termViewModel.loadTerm(termId);
            setTitle("Edit " + "Term ...");
        }

    }

    //Prevents loss of input text during portrait/landscape change
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(EDITING_KEY, true);
        super.onSaveInstanceState(outState);
    }

    //Save data when UP button pressed
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            saveAndReturn();
        }
        return super.onOptionsItemSelected(item);
    }

    //Save data when BACK button pressed
    @Override
    public void onBackPressed() {
        saveAndReturn();
    }

    private void saveAndReturn() {
        Log.i("term", Boolean.toString(newTerm));
        termViewModel.saveTerm(termTitleTextView.getText().toString(), DBConverter.stringToDate(termStartDate.getText().toString()), DBConverter.stringToDate(termEndDate.getText().toString()), newTerm);
        finish();
    }

}
