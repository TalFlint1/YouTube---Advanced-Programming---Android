package com.example.youtube_android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtubeandroid.R;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {

    private List<Comment> commentsList;

    public CommentsAdapter(List<Comment> commentsList) {
        this.commentsList = commentsList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = commentsList.get(position);
        holder.commentTextView.setText(comment.getMessage());
        holder.likeCountTextView.setText(comment.getLikes() + " likes");

        RepliesAdapter repliesAdapter = new RepliesAdapter(comment.getReplies());
        holder.repliesRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.repliesRecyclerView.setAdapter(repliesAdapter);

        holder.addReplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String replyText = holder.replyEditText.getText().toString();
                if (!replyText.isEmpty()) {
                    Comment reply = new Comment(replyText, 0); // Assuming 0 as default user image
                    comment.addReply(reply);
                    holder.replyEditText.setText("");
                    repliesAdapter.notifyItemInserted(comment.getReplies().size() - 1);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView commentTextView;
        TextView likeCountTextView;
        ImageButton likeButton;
        ImageButton replyButton;
        EditText replyEditText;
        Button addReplyButton;
        RecyclerView repliesRecyclerView;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commentTextView = itemView.findViewById(R.id.comment_text);
            likeCountTextView = itemView.findViewById(R.id.comment_like_count);
            likeButton = itemView.findViewById(R.id.like_comment_button);
            //replyButton = itemView.findViewById(R.id.reply_button);
            replyEditText = itemView.findViewById(R.id.reply_edit_text);
            addReplyButton = itemView.findViewById(R.id.add_reply_button);
            repliesRecyclerView = itemView.findViewById(R.id.replies_recycler_view);
        }
    }
}
