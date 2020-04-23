package com.example.studentprogresstracker.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.studentprogresstracker.utilities.Constants;

import java.util.Date;

@Entity(tableName = "assessments",
        foreignKeys = { @ForeignKey(
            entity = CourseEntity.class,
            parentColumns = Constants.COURSE_COLUMN_ID,
            childColumns = Constants.COURSE_FOREIGN_ID,
            onDelete = ForeignKey.CASCADE)},
        indices = { @Index(value = Constants.COURSE_FOREIGN_ID)})

public class AssessmentEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = Constants.ASSESSMENT_COLUMN_ID)
    private int id;
    @ColumnInfo(name = Constants.ASSESSMENT_COLUMN_TITLE)
    private String title;
    @ColumnInfo(name = Constants.ASSESSMENT_COLUMN_DUEDATE)
    private Date dueDate;
    @ColumnInfo(name = Constants.COURSE_FOREIGN_ID)
    private int courseId;

    @Ignore
    public AssessmentEntity() {
    }

    public AssessmentEntity(String title, Date dueDate, int courseId) {

        this.title = title;
        this.dueDate = dueDate;
        this.courseId = courseId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}
