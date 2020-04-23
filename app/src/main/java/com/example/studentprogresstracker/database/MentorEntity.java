package com.example.studentprogresstracker.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.studentprogresstracker.utilities.Constants;

@Entity(tableName = "mentors",
    foreignKeys = { @ForeignKey(
        entity = CourseEntity.class,
        parentColumns = Constants.COURSE_COLUMN_ID,
        childColumns = Constants.COURSE_FOREIGN_ID,
        onDelete = ForeignKey.CASCADE)},
    indices = { @Index(value = Constants.COURSE_FOREIGN_ID)})
public class MentorEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = Constants.MENTORS_COLUMN_ID)
    private int id;
    @ColumnInfo(name = Constants.MENTORS_COLUMN_NAME)
    private String mentorName;
    @ColumnInfo(name = Constants.MENTORS_COLUMN_EMAIL)
    private String mentorEmail;
    @ColumnInfo(name = Constants.MENTORS_COLUMN_PHONE)
    private String mentorPhone;
    @ColumnInfo(name = Constants.COURSE_FOREIGN_ID)
    private int courseId;

    public MentorEntity(String mentorName, String mentorEmail, String mentorPhone, int courseId) {

        this.mentorName = mentorName;
        this.mentorEmail = mentorEmail;
        this.mentorPhone = mentorPhone;
        this.courseId = courseId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMentorName() {
        return mentorName;
    }

    public void setMentorName(String mentorName) {
        this.mentorName = mentorName;
    }

    public String getMentorEmail() {
        return mentorEmail;
    }

    public void setMentorEmail(String mentorEmail) {
        this.mentorEmail = mentorEmail;
    }

    public String getMentorPhone() {
        return mentorPhone;
    }

    public void setMentorPhone(String mentorPhone) {
        this.mentorPhone = mentorPhone;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}
