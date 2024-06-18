package com.example.youtube_android;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtubeandroid.R;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;

    private Context context;
    private List<VideoItem> videoList;

    public VideoAdapter(Context context, List<VideoItem> videoList, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.videoList = videoList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_item, parent, false);
        return new VideoViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        VideoItem videoItem = videoList.get(position);
        holder.title.setText(videoItem.getTitle());
        holder.username.setText(videoItem.getUsername());
        holder.views.setText(videoItem.getViews());
        holder.time.setText(videoItem.getTime());

        // Set up the VideoView
        Uri videoUri = Uri.parse(videoItem.getVideoUrl());
        holder.videoView.setVideoURI(videoUri);
        MediaController mediaController = new MediaController(context);
        holder.videoView.setMediaController(mediaController);
        holder.videoView.setOnPreparedListener(mp -> {
            // Start playing the video
            holder.videoView.start();
        });

        holder.videoView.setOnErrorListener((mp, what, extra) -> {
            Log.e("VideoAdapter", "Error playing video: " + videoUri);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        TextView title, username, views, time;
        VideoView videoView;
        ImageView userIcon;

        public VideoViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            title = itemView.findViewById(R.id.video_title);
            username = itemView.findViewById(R.id.video_username);
            views = itemView.findViewById(R.id.video_views);
            time = itemView.findViewById(R.id.video_time);
            videoView = itemView.findViewById(R.id.video_view);
            userIcon = itemView.findViewById(R.id.user_icon);

            itemView.setOnClickListener(v -> {
                if (recyclerViewInterface != null) {
                    int pos = getAdapterPosition();

                    if (pos != RecyclerView.NO_POSITION) {
                        recyclerViewInterface.onItemClick(pos);
                    }
                }
            });
        }
    }
}
