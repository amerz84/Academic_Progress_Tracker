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
public interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(CourseNoteEntity courseNoteEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CourseNoteEntity> courseNotes);

    @Query("SELECT * FROM course_notes WHERE courseId = :courseId")
    LiveData<List<CourseNoteEntity>> getNotes(int courseId);

    @Query("SELECT * FROM course_notes WHERE courseId = :courseId")
    List<CourseNoteEntity> getCourseNotes(int courseId);

    @Query("DELETE FROM course_notes")
    int deleteAll();

    @Query("SELECT * FROM course_notes WHERE id = :noteId")
    CourseNoteEntity getNoteById(int noteId);

    @Delete
    void deleteNote(CourseNoteEntity note);

    @Update
    void updateNote(CourseNoteEntity note);
}
