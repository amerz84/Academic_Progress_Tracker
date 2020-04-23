package com.example.studentprogresstracker.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studentprogresstracker.database.AppRepository;
import com.example.studentprogresstracker.database.CourseEntity;
import com.example.studentprogresstracker.database.TermEntity;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TermViewModel extends AndroidViewModel {

    public MutableLiveData<TermEntity> liveTerm = new MutableLiveData<>();
    private AppRepository termRepository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public TermViewModel(@NonNull Application application) {
        super(application);
        termRepository = AppRepository.getInstance(getApplication());
    }

    public void loadTerm(int termId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                TermEntity term = termRepository.getTermById(termId);
                liveTerm.postValue(term);
            }
        });
    }

    public void deleteTerm() {
        termRepository.deleteTerm(liveTerm.getValue());
    }

    public void saveTerm(String title, Date start, Date end, boolean newTerm) {
        TermEntity term = liveTerm.getValue();

        if (term == null) {
            if (TextUtils.isEmpty(title.trim()) && TextUtils.isEmpty(start.toString().trim())
                    && TextUtils.isEmpty(end.toString().trim())) {
                return;
            }
            else if (newTerm) {
                term = new TermEntity(title, start, end);
                termRepository.insertTerm(term);
            }
        }
        else {
            term.setTitle(title.trim());
            term.setStartDate(start);
            term.setEndDate(end);
            termRepository.updateTerm(term);
        }
    }

    //Get list of courses by term - used to check for courses before deleting a term entity
    public LiveData<List<CourseEntity>> getAllCourses(int termId) {
        return termRepository.getCourses(termId);
    }

}
