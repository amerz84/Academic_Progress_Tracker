package com.example.studentprogresstracker.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentprogresstracker.R;
import com.example.studentprogresstracker.database.TermEntity;
import com.example.studentprogresstracker.ui.TermAdapter;
import com.example.studentprogresstracker.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TermListActivity extends AppCompatActivity {

    @BindView(R.id.term_recycler_view)
    RecyclerView termRecyclerView;

    @OnClick(R.id.term_add_fab)
    void addClickHandler() {
        Intent intent = new Intent(this, TermEditorActivity.class);
        startActivity(intent);
    }

    private List<TermEntity> termData = new ArrayList<>();
    private TermAdapter termAdapter;
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);

        ButterKnife.bind(this);

        initRecyclerView();
        initViewModel();

    }

    private void initViewModel() {

        final Observer<List<TermEntity>> termsObserver = new Observer<List<TermEntity>>() {
            @Override
            public void onChanged(List<TermEntity> termEntities) {
                termData.clear();
                termData.addAll(termEntities);

                if(termAdapter == null) {
                    termAdapter = new TermAdapter(termData, TermListActivity.this);
                    termRecyclerView.setAdapter(termAdapter);
                }
                else {
                    termAdapter.notifyDataSetChanged();
                }
            }
        };

        mainViewModel = ViewModelProviders.of(this)
                .get(MainViewModel.class);
        mainViewModel.modelTerms.observe(this, termsObserver);
    }

    private void initRecyclerView() {
        termRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        termRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration divider = new DividerItemDecoration(
                termRecyclerView.getContext(), layoutManager.getOrientation());

        termRecyclerView.addItemDecoration(divider);
    }

}
