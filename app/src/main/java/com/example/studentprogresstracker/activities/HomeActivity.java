package com.example.studentprogresstracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.studentprogresstracker.R;
import com.example.studentprogresstracker.viewmodel.MainViewModel;

public class HomeActivity extends AppCompatActivity {

    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle(getString(R.string.title_activity_home));

/*        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        initViewModel();
    }

    private void initViewModel() {
        mainViewModel = ViewModelProviders.of(this)
                .get(MainViewModel.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add_sample_data) {
            populateData(1);
            populateData(2);
            populateData(3);
            return true;
        } else if (id == R.id.action_delete_all) {
            deleteAll();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAll() {
        mainViewModel.deleteAll();
    }

    private void populateData(int position) {
        mainViewModel.populateData(position);
    }

    public void launchTermView(View v) {
            Intent i = new Intent(this, TermListActivity.class);
            startActivity(i);
        }

}
