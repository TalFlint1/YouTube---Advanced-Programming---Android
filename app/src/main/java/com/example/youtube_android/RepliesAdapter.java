package com.example.youtube_android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RepliesAdapter extends RecyclerView.Adapter<RepliesAdapter.ReplyViewHolder> {

    private List<Comment> repliesList;

    public RepliesAdapter(List<Comment> repliesList) {
        this.repliesList = repliesList;
    }

    @NonNull
    @Override
    public ReplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ReplyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReplyViewHolder holder, int position) {
        Comment reply = repliesList.get(position);
        holder.replyTextView.setText(reply.getMessage());
    }

    @Override
    public int getItemCount() {
        return repliesList.size();
    }

    static class ReplyViewHolder extends RecyclerView.ViewHolder {
        TextView replyTextView;

        public ReplyViewHolder(@NonNull View itemView) {
            super(itemView);
            replyTextView = itemView.findViewById(android.R.id.text1);
        }
    }
}
