package com.example.youtube_android;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtubeandroid.R;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {

    private List<String> commentsList;
    private Context context;
    private Video video;
    private VideoRepository repository;

    public CommentsAdapter( List<String> commentsList, Video video) {
//        this.context = context;
        this.commentsList = commentsList;
        this.repository = new VideoRepository(context);
        this.video = video;
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


        holder.deleteButton.setOnClickListener(v -> {
            int p = position;
            commentsList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, commentsList.size());
            removeCommentFromRepository(p);
        });
    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    private void updateCommentInRepository(int position, String newComment) {
        repository.getVideo(video, new VideoRepository.GetVideoCallback() {
            @Override
            public void onGetVideoResponse(Video response) {
                video = response;
                video.updateComment(position, newComment);  // Assuming you have an updateComment method in Video class
                repository.updateVideo(video, new VideoRepository.UpdateVideosCallback() {
                    @Override
                    public void onUpdateVideosResponse(Video response) {
                        Log.i("response:  ", response.toString());
                    }

                    @Override
                    public void onUpdateVideosError(String errorMessage) {
                        Log.e("UpdateError", errorMessage);
                    }
                });
            }

            @Override
            public void onGetVideoError(String errorMessage) {
                Log.e("GetVideoError", errorMessage);
            }
        });
    }

    private void removeCommentFromRepository(int position) {
        repository.getVideo(video, new VideoRepository.GetVideoCallback() {
            @Override
            public void onGetVideoResponse(Video response) {
                video = response;
                video.removeComment(position);
                repository.updateVideo(video, new VideoRepository.UpdateVideosCallback() {
                    @Override
                    public void onUpdateVideosResponse(Video response) {
                        Log.i("response:  ", response.toString());
                    }

                    @Override
                    public void onUpdateVideosError(String errorMessage) {
                        Log.e("UpdateError", errorMessage);
                    }
                });
            }

            @Override
            public void onGetVideoError(String errorMessage) {
                Log.e("GetVideoError", errorMessage);
            }
        });
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView commentTextView;
        Button editButton;
        Button deleteButton;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commentTextView = itemView.findViewById(R.id.comment_text_view);
//            editButton = itemView.findViewById(R.id.edit_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}

