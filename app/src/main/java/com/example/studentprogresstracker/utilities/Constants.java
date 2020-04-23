package com.example.studentprogresstracker.utilities;

public class Constants {
    public static final String TERM_ID_KEY = "term_id_key";
    public static final String COURSE_ID_KEY = "course_id_key";
    public static final String ASSESSMENT_ID_KEY = "assessment_id_key";
    public static final String NOTE_ID_KEY = "note_id_key";
    public static final String MENTOR_ID_KEY = "mentor_id_key";
    public static final String EDITING_KEY = "editing_key";
    public static final String COURSE_TITLE = "course_title";
    public static final String COURSE_START = "course_start";
    public static final String COURSE_END = "course_end";
    public static final String COURSE_STATUS = "course_status";
    public static final String ASSESSMENT_TITLE = "assessment_title";
    public static final String ASSESSMENT_DATE = "assessment_date";
    public static final String ASSESSMENT_SHARING = "sharing_features";
    public static final String TERM_TITLE = "term_title";
    public static final String TERM_START = "term_start";
    public static final String TERM_END = "term_end";
    public static final String NEW_COURSE = "new_course";
    public static final String NEW_ASSESSMENT = "new_assessment";
    public static final String NEW_NOTE = "new_note";
    public static final String NEW_MENTOR = "new_mentor";

    //Terms Table
    public static final String TERM_COLUMN_ID = "id";
    public static final String TERM_COLUMN_TITLE = "title";
    public static final String TERM_COLUMN_START = "startDate";
    public static final String TERM_COLUMN_END = "endDate";

    //Courses table
    public static final String TERM_FOREIGN_ID = "termId";
    public static final String COURSE_COLUMN_ID = "id";
    public static final String COURSE_COLUMN_TITLE = "title";
    public static final String COURSE_COLUMN_START = "startDate";
    public static final String COURSE_COLUMN_END = "anticipatedEndDate";
    public static final String COURSE_COLUMN_STATUS = "status";

    //CourseNotes table
    public static final String COURSE_NOTES_ID = "id";
    public static final String COURSE_NOTES_NOTE = "note";

    //Assessments table
    public static final String ASSESSMENT_COLUMN_ID = "id";
    public static final String COURSE_FOREIGN_ID = "courseId";
    public static final String ASSESSMENT_COLUMN_TITLE = "title";
    public static final String ASSESSMENT_COLUMN_DUEDATE = "dueDate";
    public static final String ASSESSMENT_COLUMN_SHARING = "sharingFeatures";

    //Mentors table
    public static final String MENTORS_COLUMN_ID = "id";
    public static final String MENTORS_COLUMN_NAME = "mentorName";
    public static final String MENTORS_COLUMN_EMAIL = "mentorEmail";
    public static final String MENTORS_COLUMN_PHONE = "mentorPhone";
}
