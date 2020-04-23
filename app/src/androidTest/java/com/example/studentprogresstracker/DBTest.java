package com.example.studentprogresstracker;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.studentprogresstracker.database.AppDatabase;
import com.example.studentprogresstracker.database.AssessmentDao;
import com.example.studentprogresstracker.database.CourseDao;
import com.example.studentprogresstracker.database.MentorDao;
import com.example.studentprogresstracker.database.TermDao;
import com.example.studentprogresstracker.database.TermEntity;
import com.example.studentprogresstracker.utilities.SampleData;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DBTest {

    public static final String TAG = "Junit";
    private AppDatabase testDB;
    private AssessmentDao assessmentDao;
    private CourseDao courseDao;
    private TermDao termDao;
    private MentorDao mentorDao;

    @Before
    public void createDB() {
        Context context = ApplicationProvider.getApplicationContext();
        testDB = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();

        assessmentDao = testDB.assessmentDao();
        courseDao = testDB.courseDao();
        termDao = testDB.termDao();
        mentorDao = testDB.mentorDao();
        Log.i(TAG, "createDB executed");
    }

    @After
    public void closeDB() {
        testDB.close();
        Log.i(TAG, "closeDB exectued");
    }

    @Test
    public void createAndRetrieveTerms() {
        SampleData.populateData();
        termDao.insertAll(SampleData.getTerms());
        int count = termDao.getCount();
        Log.i(TAG, "createAndRetrieveTerms: count = " + count);
        assertEquals(SampleData.getTerms().size(), count);
    }

    @Test
    public void compareStrings() {
        SampleData.populateData();
        termDao.insertAll(SampleData.getTerms());
        TermEntity original = SampleData.getTerms().get(0);
        TermEntity fromDb = termDao.getTermById(1);
        assertEquals(original.getTitle(), fromDb.getTitle());

    }
}
