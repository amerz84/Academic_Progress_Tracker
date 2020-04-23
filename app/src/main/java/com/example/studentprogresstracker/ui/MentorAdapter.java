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
import com.example.studentprogresstracker.activities.MentorDetailActivity;
import com.example.studentprogresstracker.database.MentorEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.studentprogresstracker.utilities.Constants.COURSE_ID_KEY;
import static com.example.studentprogresstracker.utilities.Constants.MENTOR_ID_KEY;

public class MentorAdapter extends RecyclerView.Adapter<MentorAdapter.ViewHolder> {
    private final List<MentorEntity> mentors;
    private final Context mentorContext;

    public MentorAdapter(List<MentorEntity> mentors, Context mentorContext) {
        this.mentors = mentors;
        this.mentorContext = mentorContext;
    }

    @NonNull
    @Override
    public MentorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new MentorAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MentorAdapter.ViewHolder holder, int position) {
        final MentorEntity mentor = mentors.get(position);
        holder.mentorTextView.setText(mentor.getMentorName());

        holder.mentorConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mentorContext, MentorDetailActivity.class);
                intent.putExtra(MENTOR_ID_KEY, mentor.getId());
                intent.putExtra(COURSE_ID_KEY, mentor.getCourseId());
                mentorContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mentors.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.list_text)
        TextView mentorTextView;
        @BindView(R.id.list_item_constraint_layout)
        ConstraintLayout mentorConstraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

