package com.example.youtube_android;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtubeandroid.R;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {

    private List<String> commentsList;
    private Context context;
    private Video video;
    private VideoRepository repository;

    public CommentsAdapter(Context context, List<String> commentsList, Video video) {
        this.context = context;
        this.commentsList = commentsList;
        this.repository = new VideoRepository();
        this.video=video;


    }    public CommentsAdapter( List<String> commentsList , Video video) {
        this.context = context;
        this.commentsList = commentsList;
        this.video=video;
        this.repository = new VideoRepository();

    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        String comment = commentsList.get(position);
        holder.commentTextView.setText(comment);

        holder.editButton.setOnClickListener(v -> {
            // Show an EditText to edit the comment
            EditText editText = new EditText(context);
            editText.setText(comment);

            new AlertDialog.Builder(context)
                    .setTitle("Edit Comment")
                    .setView(editText)
                    .setPositiveButton("Save", (dialog, which) -> {
                        String newComment = editText.getText().toString();
                        commentsList.set(position, newComment);
                        notifyItemChanged(position);
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        holder.deleteButton.setOnClickListener(v -> {
            commentsList.remove(position);
            int p = position;
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, commentsList.size());
            repository.GetVideo( video,new VideoRepository.GetVideoCallback() {


                @Override
                public void onGetVideoResponse(Video response) {
                    video = response;
                    video.removeComment(p);
                    repository.UpdateVideo( video,new VideoRepository.UpdateVideosCallback() {

                        @Override
                        public void onUpdateVideosResponse(Video response) {

                            Log.i("response:  ", response.toString());
                            System.out.println( response);

                        }

                        @Override
                        public void onUpdateVideosError(String errorMessage) {
                            // Handle error
                            //android.widget.Toast.makeText(Hom.this, "Failed to delete account: " + errorMessage, android.widget.Toast.LENGTH_SHORT).show();
                        }
                    });

                }

                @Override
                public void onGetVideoError(String errorMessage) {

                }
            });

        });
    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView commentTextView;
        Button editButton;
        Button deleteButton;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commentTextView = itemView.findViewById(R.id.comment_text_view);
            editButton = itemView.findViewById(R.id.edit_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}
