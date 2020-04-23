package com.example.studentprogresstracker.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.studentprogresstracker.database.AppRepository;
import com.example.studentprogresstracker.database.AssessmentEntity;
import com.example.studentprogresstracker.database.CourseEntity;
import com.example.studentprogresstracker.database.CourseNoteEntity;
import com.example.studentprogresstracker.database.TermEntity;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    public LiveData<List<AssessmentEntity>> modelAssessments;
    public LiveData<List<CourseEntity>> modelCourses;
    public LiveData<List<TermEntity>> modelTerms;
    public LiveData<List<CourseNoteEntity>> modelNotes;

    private AppRepository repository;

    public MainViewModel(@NonNull Application application) {
        super(application);

        repository = AppRepository.getInstance(application.getApplicationContext());
        modelAssessments = repository.modelAssessments;
        modelCourses = repository.modelCourses;
        modelTerms = repository.modelTerms;
        modelNotes = repository.modelNotes;

    }

    public void populateData(int position) {
        repository.populateData(position);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public LiveData<List<CourseEntity>> getCoursesbyTerm(int termId) {
        return repository.getCourses(termId);
    }

    public LiveData<List<AssessmentEntity>> getAssessmentsByCourse(int courseId) {
        return repository.getAssessments(courseId);
    }

    public LiveData<List<CourseNoteEntity>> getNotesByCourse(int courseId) {
        return repository.getNotes(courseId);
    }
}
