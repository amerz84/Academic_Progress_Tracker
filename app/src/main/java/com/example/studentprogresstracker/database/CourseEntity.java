package com.example.studentprogresstracker.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.studentprogresstracker.utilities.Constants;

import java.util.Date;

@Entity(tableName = "courses",
    foreignKeys = { @ForeignKey(
        entity = TermEntity.class,
        parentColumns = Constants.TERM_COLUMN_ID,
        childColumns = Constants.TERM_FOREIGN_ID,
        onDelete = ForeignKey.CASCADE)},
    indices = { @Index(value = Constants.TERM_FOREIGN_ID)})

public class CourseEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = Constants.COURSE_COLUMN_ID)
    private int id;
    @ColumnInfo(name = Constants.COURSE_COLUMN_TITLE)
    private String title;
    @ColumnInfo(name = Constants.COURSE_COLUMN_START)
    private Date startDate;
    @ColumnInfo(name = Constants.COURSE_COLUMN_END)
    private Date anticipatedEndDate;
    @ColumnInfo(name = Constants.TERM_FOREIGN_ID)
    private int termId;

    public enum Status {
        IN_PROGRESS(0),
        COMPLETED(1),
        DROPPED(2),
        PLAN_TO_TAKE(3);

        private int code;

        Status(int code) {
            this.code = code;
        }
        public int getCode() {
            return code;
        }

        @NonNull
        @Override
        public String toString() {
            switch(this) {
                case IN_PROGRESS:
                    return "In Progress";
                case PLAN_TO_TAKE:
                    return "Plan To Take";
                case DROPPED:
                    return "Dropped";
                case COMPLETED:
                    return "Completed";
            }
            return null;
        }
    };

    @ColumnInfo (name = Constants.COURSE_COLUMN_STATUS)
    private Status status;
    //private List<String> courseNotes;

    @Ignore
    public CourseEntity() {
    }

    public CourseEntity(String title, Date startDate, Date anticipatedEndDate, Status status, int termId) {

        this.title = title;
        this.startDate = startDate;
        this.anticipatedEndDate = anticipatedEndDate;
        this.status = status;
        this.termId = termId;
        //courseNotes = new ArrayList<>();

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getAnticipatedEndDate() {
        return anticipatedEndDate;
    }

    public void setAnticipatedEndDate(Date anticipatedEndDate) {
        this.anticipatedEndDate = anticipatedEndDate;
    }

    public int getTermId() {
        return termId;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }

}
