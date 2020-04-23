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
import com.example.studentprogresstracker.activities.CourseNoteDetailActivity;
import com.example.studentprogresstracker.database.CourseNoteEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.studentprogresstracker.utilities.Constants.NOTE_ID_KEY;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private List<CourseNoteEntity> notes;
    private final Context noteContext;

    public NotesAdapter(List<CourseNoteEntity> notes, Context noteContext) {
        this.notes = notes;
        this.noteContext = noteContext;
    }

    @NonNull
    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new NotesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.ViewHolder holder, int position) {
        final CourseNoteEntity note = notes.get(position);
        holder.noteTextView.setText(note.getNote());

        holder.noteConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(noteContext, CourseNoteDetailActivity.class);
                intent.putExtra(NOTE_ID_KEY, note.getId());
                noteContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.list_text)
        TextView noteTextView;
        @BindView(R.id.list_item_constraint_layout)
        ConstraintLayout noteConstraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
