package com.example.studentprogresstracker.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.studentprogresstracker.database.AppRepository;
import com.example.studentprogresstracker.database.AssessmentEntity;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AssessmentViewModel extends AndroidViewModel {


    public MutableLiveData<AssessmentEntity> liveAssessment = new MutableLiveData<>();
    private AppRepository assessmentRepository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public AssessmentViewModel(@NonNull Application application) {
        super(application);
        assessmentRepository = AppRepository.getInstance(getApplication());
    }

    public void loadAssessment(int assessmentId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                AssessmentEntity assessment = assessmentRepository.getAssessmentById(assessmentId);
                liveAssessment.postValue(assessment);
            }
        });
    }

    public void deleteAssessment() {
        assessmentRepository.deleteAssessment(liveAssessment.getValue());
    }

    public void saveAssessment(String title, Date dueDate, int courseId, boolean newAssessment) {
        AssessmentEntity assessment = liveAssessment.getValue();

        if (assessment == null) {
            if (TextUtils.isEmpty(title.trim()) && TextUtils.isEmpty(dueDate.toString().trim())) {
                return;
            }
            else if (newAssessment) {
                assessment = new AssessmentEntity(title, dueDate, courseId);
                assessmentRepository.insertAssessment(assessment);
            }
        }
        else {
            assessment.setTitle(title);
            assessment.setDueDate(dueDate);
            assessment.setCourseId(courseId);
            assessmentRepository.updateAssessment(assessment);
        }
    }
}
