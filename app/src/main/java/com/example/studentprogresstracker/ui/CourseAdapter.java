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
import com.example.studentprogresstracker.activities.CourseDetailActivity;
import com.example.studentprogresstracker.database.CourseEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.studentprogresstracker.utilities.Constants.COURSE_ID_KEY;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    private List<CourseEntity> courses;
    private final Context courseContext;

    public CourseAdapter(List<CourseEntity> courses, Context courseContext) {
        this.courses = courses;
        this.courseContext = courseContext;
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
        final CourseEntity course = courses.get(position);
        holder.courseTextView.setText(course.getTitle());

        holder.courseConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(courseContext, CourseDetailActivity.class);
                intent.putExtra(COURSE_ID_KEY, course.getId());
                courseContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.list_text)
        TextView courseTextView;
        @BindView(R.id.list_item_constraint_layout)
        ConstraintLayout courseConstraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //Create list of courses associated with term (to prevent deletion of term if courses present per requirements)
    public void setTermCourses(List<CourseEntity> courses) {
        this.courses = courses;
        notifyDataSetChanged();
    }
}