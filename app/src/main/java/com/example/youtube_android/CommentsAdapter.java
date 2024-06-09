package com.example.youtube_android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {

    private List<String> commentsList;

    public CommentsAdapter(List<String> commentsList) {
        this.commentsList = commentsList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        String comment = commentsList.get(position);
        holder.commentTextView.setText(comment);
    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView commentTextView;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commentTextView = itemView.findViewById(android.R.id.text1);
        }
    }
}
