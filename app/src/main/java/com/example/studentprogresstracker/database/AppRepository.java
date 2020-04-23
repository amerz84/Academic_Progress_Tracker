package com.example.studentprogresstracker.database;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.studentprogresstracker.utilities.SampleData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

//Repository to control data source
public class AppRepository {
    private static AppRepository instance;
    private AppDatabase appDatabase;
    private int termId;
    private int courseId;

    //Create background thread for Room to run operations
    private Executor executor = Executors.newSingleThreadExecutor();

    public LiveData<List<AssessmentEntity>> modelAssessments;
    public LiveData<List<CourseEntity>> modelCourses;
    public LiveData<List<TermEntity>> modelTerms;
    public LiveData<List<MentorEntity>> modelMentors;
    public LiveData<List<CourseNoteEntity>> modelNotes;

    public static AppRepository getInstance(Context context) {
        if (instance == null) {
            instance = new AppRepository(context);
        }
        return instance; }

    private AppRepository(Context context) {
        appDatabase = AppDatabase.getInstance(context);

        modelTerms = getTerms();
        modelCourses = getCourses(termId);
        modelAssessments = getAssessments(courseId);
        modelMentors = getMentors(courseId);
        modelNotes = getNotes(courseId);
    }

    //Method to add sample data
    public void populateData(int position) {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        switch (position) {
                            case 1:
                                appDatabase.termDao().insertAll(SampleData.getTerms());
                                break;
                            case 2:
                                appDatabase.courseDao().insertAll(SampleData.getCourses());
                                break;
                            case 3:
                                appDatabase.mentorDao().insertAll(SampleData.getMentors());
                                appDatabase.assessmentDao().insertAll(SampleData.getAssessments());
                                appDatabase.noteDao().insertAll(SampleData.getNotes());
                                break;
                            default:
                                return;
                        }
                    }
                });
    }

    ///////////////////////List activity methods
    public LiveData<List<AssessmentEntity>> getAssessments(int courseId) {
        this.courseId = courseId;
        return appDatabase.assessmentDao().getAssessmentsByCourse(courseId);
    }

    public LiveData<List<CourseEntity>> getCourses(int termId) {
        this.termId = termId;
        return appDatabase.courseDao().getCoursesbyTerm(termId);
    }

    public LiveData<List<MentorEntity>> getMentors(int courseId) {
        this.courseId = courseId;
        return appDatabase.mentorDao().getMentors(courseId);
    }

    private LiveData<List<TermEntity>> getTerms() {
        return appDatabase.termDao().getAll();
    }

    public LiveData<List<CourseNoteEntity>> getNotes(int courseId) {
        return appDatabase.noteDao().getNotes(courseId);
    }

    //Delete all data from database
    public void deleteAll() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                appDatabase.assessmentDao().deleteAll();
                appDatabase.mentorDao().deleteAll();
                appDatabase.courseDao().deleteAll();
                appDatabase.termDao().deleteAll();
                appDatabase.noteDao().deleteAll();
            }
        });
    }

    /////////////////////////Detail activity methods
    public TermEntity getTermById(int termId) {
        return appDatabase.termDao().getTermById(termId);
    }

    public CourseEntity getCourseById(int courseId) {
        return appDatabase.courseDao().getCourseById(courseId);
    }

    public AssessmentEntity getAssessmentById(int assessmentId) {
        return appDatabase.assessmentDao().getAssessmentById(assessmentId);
    }

    public CourseNoteEntity getNoteById(int noteId) {
        return appDatabase.noteDao().getNoteById(noteId);
    }

    public MentorEntity getMentorById(int id) {
        return appDatabase.mentorDao().getMentorById(id);
    }

    ////////////////////////Course methods
    public void insertCourse(final CourseEntity course) {
        executor.execute(() -> { appDatabase.courseDao().insertCourse(course);});
    }

    public void updateCourse(final CourseEntity course) {
        executor.execute(() -> { appDatabase.courseDao().updateCourse(course);});
    }

    public void deleteCourse(final CourseEntity course) {
        executor.execute(() -> appDatabase.courseDao().deleteCourse(course));
    }

    ////////////////////////////////Assessment methods
    public void insertAssessment(final AssessmentEntity assessment) {
        executor.execute(() -> { appDatabase.assessmentDao().insertAssessment(assessment);});
    }

    public void updateAssessment(final AssessmentEntity assessment) {
        executor.execute(() -> { appDatabase.assessmentDao().updateAssessment(assessment);});
    }

    public void deleteAssessment(final AssessmentEntity assessment) {
        executor.execute(() -> appDatabase.assessmentDao().deleteAssessment(assessment));
    }

    //////////////////////////////Term methods
    public void insertTerm(final TermEntity term) {
        executor.execute(() -> { appDatabase.termDao().insertTerm(term);});
    }

    public void updateTerm(final TermEntity term) {
        executor.execute(() -> { appDatabase.termDao().updateTerm(term);});
    }

    public void deleteTerm(final TermEntity term) {
        executor.execute(() -> appDatabase.termDao().deleteTerm(term));
    }

    ///////////////////////////Note methods
    public void deleteNote(final CourseNoteEntity note) {
        executor.execute(() -> appDatabase.noteDao().deleteNote(note));
    }

    public void insertNote(final CourseNoteEntity note) {
        executor.execute(() -> { appDatabase.noteDao().insertNote(note);});
    }

    public void updateNote(final CourseNoteEntity note) {
        executor.execute(() -> { appDatabase.noteDao().updateNote(note);});
    }

    ////////////////////////////Mentor methods
    public void deleteMentor(MentorEntity mentor) {
        executor.execute(() -> appDatabase.mentorDao().deleteMentor(mentor));
    }

    public void insertMentor(MentorEntity mentor) {
        executor.execute(() -> appDatabase.mentorDao().insertMentor(mentor));
    }

    public void updateMentor(MentorEntity mentor) {
        executor.execute(() -> appDatabase.mentorDao().updateMentor(mentor));
    }
}
