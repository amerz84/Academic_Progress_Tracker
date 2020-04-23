package com.example.studentprogresstracker.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.studentprogresstracker.utilities.Constants;

@Entity(tableName = "course_notes",
        foreignKeys = { @ForeignKey(
        entity = CourseEntity.class,
        parentColumns = Constants.COURSE_COLUMN_ID,
        childColumns = Constants.COURSE_FOREIGN_ID,
        onDelete = ForeignKey.CASCADE)},
        indices = { @Index(value = Constants.COURSE_FOREIGN_ID)})
public class CourseNoteEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = Constants.COURSE_NOTES_ID)
    private int id;
    @ColumnInfo(name = Constants.COURSE_NOTES_NOTE)
    private String note;
    @ColumnInfo(name = Constants.COURSE_FOREIGN_ID)
    private int courseId;

    @Ignore
    public CourseNoteEntity() {}

    public CourseNoteEntity(String note, int courseId) {
        this.note = note;
        this.courseId = courseId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}
