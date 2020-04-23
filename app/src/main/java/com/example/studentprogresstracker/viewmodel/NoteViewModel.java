package com.example.studentprogresstracker.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.studentprogresstracker.database.AppRepository;
import com.example.studentprogresstracker.database.CourseNoteEntity;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NoteViewModel extends AndroidViewModel {

    public MutableLiveData<CourseNoteEntity> liveNote = new MutableLiveData<>();
    private AppRepository noteRepository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public NoteViewModel(@NonNull Application application) {
        super(application);
        noteRepository = AppRepository.getInstance(getApplication());
    }

    public void loadNote(final int noteId) {
        executor.execute(() -> {
            CourseNoteEntity note = noteRepository.getNoteById(noteId);
            liveNote.postValue(note);
        });
    }

    //Create a new course object or save over existing object and add to repository (from CourseEditorActivity)
    public void saveNote(String noteText, int courseId, boolean newNote) {
        CourseNoteEntity note = liveNote.getValue();

        if (note == null) {
            if (TextUtils.isEmpty(noteText.trim())) {
                return;
            }
            else if (newNote) {
                note = new CourseNoteEntity(noteText, courseId);
                noteRepository.insertNote(note);
            }
        }
        else {
            note.setNote(noteText);
            noteRepository.updateNote(note);
        }
    }

    public void deleteNote() {
        noteRepository.deleteNote(liveNote.getValue());
    }

}
