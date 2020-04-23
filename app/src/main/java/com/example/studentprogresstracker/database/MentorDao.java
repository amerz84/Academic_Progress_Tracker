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
public interface MentorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMentor(MentorEntity mentorEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<MentorEntity> mentors);

    @Delete
    void deleteMentor(MentorEntity mentorEntity);

    @Query("SELECT * FROM mentors")
    LiveData<List<MentorEntity>> getAll();

    @Query("DELETE FROM mentors")
    int deleteAll();

    @Query("SELECT COUNT (*) FROM mentors")
    int getCount();

    @Query("SELECT * FROM mentors WHERE courseId = :courseId")
    LiveData<List<MentorEntity>> getMentors(int courseId);

    @Query("SELECT * FROM mentors where id = :id")
    MentorEntity getMentorById(int id);

    @Update
    void updateMentor(MentorEntity mentor);
}
