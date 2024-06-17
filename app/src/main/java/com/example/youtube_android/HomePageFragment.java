package com.example.youtube_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.youtubeandroid.R;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HomePageFragment extends Fragment implements RecyclerViewInterface {

    private RecyclerView videoRecyclerView;
    private VideoAdapter videoAdapter;
    private List<VideoItem> videoList;
    private static final String TAG = "HomePageFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        // Initialize the RecyclerView
        videoRecyclerView = view.findViewById(R.id.video_recycler_view);

        // Set the Layout Manager for the RecyclerView
        videoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the video list and adapter
        videoList = new ArrayList<>();
        videoAdapter = new VideoAdapter(getContext(), videoList, this);

        // Attach the adapter to the RecyclerView
        videoRecyclerView.setAdapter(videoAdapter);

        // Load data from the JSON file
        loadJsonData();

        return view;
    }

    private void loadJsonData() {
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.data); // Ensure you have a data.json file in res/raw folder
            JsonReader reader = new JsonReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            reader.beginArray();

            while (reader.hasNext()) {
                reader.beginObject();

                String title = null;
                String username = null;
                String views = null;
                String time = null;
                String videoUrl = null;

                while (reader.hasNext()) {
                    String name = reader.nextName();
                    switch (name) {
                        case "title":
                            title = reader.nextString();
                            break;
                        case "username":
                            username = reader.nextString();
                            break;
                        case "views":
                            views = reader.nextString();
                            break;
                        case "time":
                            time = reader.nextString();
                            break;
                        case "video_url":
                            videoUrl = reader.nextString();
                            break;
                        default:
                            reader.skipValue();
                            break;
                    }
                }
                Log.d(TAG, "Loaded video: " + title);
                videoList.add(new VideoItem(title, username, views, time, videoUrl));
                reader.endObject();
            }
            reader.endArray();
            reader.close();

            videoAdapter.notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Error loading data", e);
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), VideoPage.class);
        intent.putExtra("video_title", videoList.get(position).getTitle());
        intent.putExtra("video_username", videoList.get(position).getUsername());
        intent.putExtra("video_views", videoList.get(position).getViews());
        intent.putExtra("video_time", videoList.get(position).getTime());
        intent.putExtra("video_url", videoList.get(position).getVideoUrl());
        startActivity(intent);
    }
}
