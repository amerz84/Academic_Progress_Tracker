package com.example.studentprogresstracker.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AssessmentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAssessment(AssessmentEntity assessmentEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<AssessmentEntity> assessments);

    @Delete
    void deleteAssessment(AssessmentEntity assessmentEntity);

    @Query("SELECT * FROM assessments WHERE id = :id")
    AssessmentEntity getAssessmentById(int id);

    @Query("SELECT * FROM assessments ORDER BY dueDate")
    LiveData<List<AssessmentEntity>> getAll();

    @Query("DELETE FROM assessments")
    int deleteAll();

    @Query("SELECT COUNT (*) FROM assessments")
    int getCount();

    @Query("SELECT * FROM assessments WHERE courseId = :courseId ORDER BY dueDate")
    LiveData<List<AssessmentEntity>> getAssessmentsByCourse(int courseId);

    @Update
    void updateAssessment(AssessmentEntity assessment);
}
