package com.example.studentprogresstracker.utilities;

import com.example.studentprogresstracker.database.AssessmentEntity;
import com.example.studentprogresstracker.database.CourseEntity;
import com.example.studentprogresstracker.database.CourseNoteEntity;
import com.example.studentprogresstracker.database.MentorEntity;
import com.example.studentprogresstracker.database.TermEntity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class SampleData {
    private static final String SAMPLE_TITLE = "Term";
    private static final String SAMPLE_COURSE_TITLE = "Course";
    private static final String SAMPLE_ASSESSMENT_TITLE = "Assessment";
    public static final String SAMPLE_NOTE_TITLE = "Note";
    private static List<TermEntity> terms = new ArrayList<>();
    private static List<CourseEntity> courses = new ArrayList<>();
    private static List<AssessmentEntity> assessments = new ArrayList<>();
    private static List<MentorEntity> mentors = new ArrayList<>();
    private static List<CourseNoteEntity> notes = new ArrayList<>();

    private static Date getDate(int diff) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.add(Calendar.MILLISECOND, diff);
        return cal.getTime();
    }

    public static List<TermEntity> getTerms() {

        terms.add(new TermEntity(SAMPLE_TITLE + " 1", getDate(0), (getDate(10))));
        terms.add(new TermEntity(SAMPLE_TITLE + " 2", getDate(100), (getDate(10))));
        terms.add(new TermEntity(SAMPLE_TITLE + " 3", getDate(200), (getDate(10))));

        return terms;
    }

    public static List<CourseEntity> getCourses() {

        courses.add(new CourseEntity(SAMPLE_COURSE_TITLE + " 1", getDate(0), (getDate(10)), CourseEntity.Status.IN_PROGRESS, 1));
        courses.add(new CourseEntity(SAMPLE_COURSE_TITLE + " 4", getDate(0), (getDate(10)), CourseEntity.Status.IN_PROGRESS, 1));
        courses.add(new CourseEntity(SAMPLE_COURSE_TITLE + " 2", getDate(100), (getDate(10)), CourseEntity.Status.PLAN_TO_TAKE, 2));
        courses.add(new CourseEntity(SAMPLE_COURSE_TITLE + " 3", getDate(200), (getDate(10)), CourseEntity.Status.COMPLETED, 3));

        return courses;
    }

    public static List<AssessmentEntity> getAssessments() {
        assessments.add(new AssessmentEntity(SAMPLE_ASSESSMENT_TITLE + " 1", getDate(0), 1));
        assessments.add(new AssessmentEntity(SAMPLE_ASSESSMENT_TITLE + " 2", getDate(100), 1));
        assessments.add(new AssessmentEntity(SAMPLE_ASSESSMENT_TITLE + " 3", getDate(200), 2));
        assessments.add(new AssessmentEntity(SAMPLE_ASSESSMENT_TITLE + " 4", getDate(300), 3));

        return assessments;
    }

    public static List<MentorEntity> getMentors() {

        mentors.add(new MentorEntity("John Smith", "jsmith@wgu.edu", "111-123-4455", 1));
        mentors.add(new MentorEntity("Julie Walker", "jwalker@wgu.edu", "111-234-5566", 1));
        mentors.add(new MentorEntity("Peter Thompson", "pthompson@wgu.edu", "111-345-6677", 2));

        return mentors;
    }

    public static List<CourseNoteEntity> getNotes() {

        notes.add(new CourseNoteEntity("This is the first note", 1));
        notes.add(new CourseNoteEntity("This is the second note", 1));
        notes.add(new CourseNoteEntity("This is the third note", 2));
        notes.add(new CourseNoteEntity("This is the fourth note", 3));

        return notes;
    }


}
