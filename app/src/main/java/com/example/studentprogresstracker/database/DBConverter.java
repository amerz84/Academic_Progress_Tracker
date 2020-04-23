package com.example.studentprogresstracker.database;

import androidx.room.TypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

//Compatibility conversions for Date/enum objects between Java and SQLite DB
public class DBConverter {

    @androidx.room.TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @androidx.room.TypeConverter
    public static Long toTimestamp (Date date) {
        return date == null ? null : date.getTime();
    }

    @androidx.room.TypeConverter
    public static CourseEntity.Status toStatus(int status) {
        if (status == CourseEntity.Status.IN_PROGRESS.getCode()) {
            return CourseEntity.Status.IN_PROGRESS;
        }
        else if (status == CourseEntity.Status.COMPLETED.getCode()) {
            return CourseEntity.Status.COMPLETED;
        }
        else if (status == CourseEntity.Status.DROPPED.getCode()) {
            return CourseEntity.Status.DROPPED;
        }
        else if (status == CourseEntity.Status.PLAN_TO_TAKE.getCode()) {
            return CourseEntity.Status.PLAN_TO_TAKE;
        }
        else throw new IllegalArgumentException("Course Status Unknown");
    }

    @TypeConverter
    public static int toInteger(CourseEntity.Status status) {
        return status.getCode();
    }

    public static CourseEntity.Status valueOf(String status) {
        if (status.equalsIgnoreCase(CourseEntity.Status.IN_PROGRESS.toString()))
            return CourseEntity.Status.IN_PROGRESS;
        if (status.equalsIgnoreCase(CourseEntity.Status.PLAN_TO_TAKE.toString()))
            return CourseEntity.Status.PLAN_TO_TAKE;
        if (status.equalsIgnoreCase(CourseEntity.Status.DROPPED.toString()))
            return CourseEntity.Status.DROPPED;
        if (status.equalsIgnoreCase(CourseEntity.Status.COMPLETED.toString()))
            return CourseEntity.Status.COMPLETED;
        else
            return null;
    }

    public static Date stringToDate(String strDate) {
        Date formattedDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
        try {
            formattedDate = sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

    public static String dateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
        String dateString = sdf.format(date);
        return dateString;
    }

    //return int value for setting spinner selection on course edit activity
    public static int statusPosition(String status) {
        switch(status) {
            case "In Progress":
                return 0;
            case "Plan To Take":
                return 1;
            case "Dropped":
                return 2;
            case "Completed":
                return 3;
            default:
                return 0;
        }
    }

}

