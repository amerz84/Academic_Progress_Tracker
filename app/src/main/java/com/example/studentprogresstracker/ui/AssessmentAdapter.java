package com.example.studentprogresstracker.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentprogresstracker.R;
import com.example.studentprogresstracker.activities.AssessmentDetailActivity;
import com.example.studentprogresstracker.database.AssessmentEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.studentprogresstracker.utilities.Constants.ASSESSMENT_ID_KEY;
import static com.example.studentprogresstracker.utilities.Constants.COURSE_ID_KEY;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.ViewHolder> {

    private final List<AssessmentEntity> assessments;
    private final Context assessmentContext;

    public AssessmentAdapter(List<AssessmentEntity> assessments, Context assessmentContext) {
        this.assessments = assessments;
        this.assessmentContext = assessmentContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final AssessmentEntity assessment = assessments.get(position);
        holder.assessmentTextView.setText(assessment.getTitle());

        holder.termConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(assessmentContext, AssessmentDetailActivity.class);
                intent.putExtra(ASSESSMENT_ID_KEY, assessment.getId());
                intent.putExtra(COURSE_ID_KEY, assessment.getCourseId());
                assessmentContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return assessments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.list_text)
        TextView assessmentTextView;
        @BindView(R.id.list_item_constraint_layout)
        ConstraintLayout termConstraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
