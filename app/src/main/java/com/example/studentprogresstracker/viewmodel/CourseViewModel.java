package com.example.studentprogresstracker.viewmodel;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studentprogresstracker.database.AppRepository;
import com.example.studentprogresstracker.database.CourseEntity;
import com.example.studentprogresstracker.database.CourseNoteEntity;
import com.example.studentprogresstracker.database.DBConverter;
import com.example.studentprogresstracker.database.MentorEntity;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CourseViewModel extends AndroidViewModel {

    public LiveData<List<MentorEntity>> modelMentors;
    public LiveData<List<CourseEntity>> modelCourses;
    public LiveData<List<CourseNoteEntity>> modelNotes;
    public MutableLiveData<CourseEntity> liveCourse = new MutableLiveData<>();
    public MutableLiveData<MentorEntity> liveMentor = new MutableLiveData<>();
    private AppRepository courseRepository;
    private Executor executor = Executors.newSingleThreadExecutor();
    private static int courseId, mentorId;

    public CourseViewModel(@NonNull Application application) {
            super(application);

            courseRepository = AppRepository.getInstance(application.getApplicationContext());
            modelMentors = courseRepository.getMentors(courseId);
            modelCourses = courseRepository.modelCourses;
            modelNotes = courseRepository.getNotes(courseId);

    }

    ////////////////Load single course for detail activity
    public void loadCourse(int courseId) {
        this.courseId = courseId;
        executor.execute(new Runnable() {
            @Override
            public void run() {
                CourseEntity course = courseRepository.getCourseById(courseId);
                liveCourse.postValue(course);
            }
        });
    }

    public void loadMentor(int mentorId) {
        this.mentorId = mentorId;
        executor.execute(new Runnable() {
            @Override
            public void run() {
                MentorEntity mentor = courseRepository.getMentorById(mentorId);
                liveMentor.postValue(mentor);
            }
        });
    }

    public LiveData<List<MentorEntity>> getModelMentors(int courseId) {
        this.courseId = courseId;
        return courseRepository.getMentors(courseId);
    }

    //Create a new course object or save over existing object and add to repository (from CourseEditorActivity)
    public void saveCourse(String title, Date start, Date end, String status, int termId, boolean newCourse) {
        CourseEntity course = liveCourse.getValue();

        if (course == null) {
            if (TextUtils.isEmpty(title.trim()) && TextUtils.isEmpty(start.toString().trim())
                    && TextUtils.isEmpty(end.toString().trim()) && TextUtils.isEmpty(status.trim())) {
                return;
            }
            else if (newCourse) {
                course = new CourseEntity(title, start, end, DBConverter.valueOf(status), termId);
                courseRepository.insertCourse(course);
            }
        }
        else {
            course.setTitle(title);
            course.setStartDate(start);
            course.setAnticipatedEndDate(end);
            course.setStatus(DBConverter.valueOf(status));
            course.setTermId(termId);
            courseRepository.updateCourse(course);
        }
    }

    public void deleteCourse() {
        courseRepository.deleteCourse(liveCourse.getValue());
    }

    public void deleteMentor() {
        Context context = getApplication().getApplicationContext();
         if (liveMentor.getValue() == null) {
             Toast.makeText(context, "Only valid mentor entities may be deleted", Toast.LENGTH_SHORT).show();
             return;
         }
         else {
             courseRepository.deleteMentor(liveMentor.getValue());
         }
    }

    public void saveMentor(String mentorName, String mentorEmail, String mentorPhone, int courseId, boolean newMentor) {
        MentorEntity mentor = liveMentor.getValue();

        if (mentor == null) {
            if (TextUtils.isEmpty(mentorName.trim()) && TextUtils.isEmpty(mentorEmail.trim())
                    && TextUtils.isEmpty(mentorPhone.trim())) {
                return;
            }
            else if (newMentor) {
                mentor = new MentorEntity(mentorName, mentorEmail, mentorPhone, courseId);
                courseRepository.insertMentor(mentor);
            }
        }
        else {
            mentor.setMentorName(mentorName);
            mentor.setMentorEmail(mentorEmail);
            mentor.setMentorPhone(mentorPhone);
            mentor.setCourseId(courseId);
            courseRepository.updateMentor(mentor);
        }
    }
}
