package com.example.youtube_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import java.util.Locale;

public class HomePageFragment extends Fragment implements RecyclerViewInterface {
    private VideoRepository repository;

    private RecyclerView videoRecyclerView;
    private VideoAdapter videoAdapter;
    private List<Video> videoList;
    private List<Video> filteredVideoList;
    private EditText searchBar;
    private static final String TAG = "HomePageFragment";
    private boolean isSearchActive = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
// Initialize UserRepository
        repository = new VideoRepository();

        // Initialize the RecyclerView and Search Bar
        videoRecyclerView = view.findViewById(R.id.video_recycler_view);
        searchBar = view.findViewById(R.id.search_bar);

        // Set the Layout Manager for the RecyclerView
        videoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the video list and adapter
        videoList = new ArrayList<>();
        filteredVideoList = new ArrayList<>();
        videoAdapter = new VideoAdapter(getContext(), filteredVideoList, this);

        // Attach the adapter to the RecyclerView
        videoRecyclerView.setAdapter(videoAdapter);

        // Load data from the JSON file
        loadJsonData();

        // Add text change listener to the search bar
        searchBar.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isSearchActive) {
                    filterVideos(s.toString());
                }
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        return view;
    }

    private void loadJsonData() {
        repository.getAllVideos( new VideoRepository.GetVideosCallback() {

            @Override
            public void onGetVideosResponse(List<Video> response) {
                for (Video item : response) {
                    System.out.println("Element: " + item);
                    videoList.add(item);
                    filteredVideoList.add(item); // Initially, show all videos

                }
                Log.i("response:  ", response.toString());
                System.out.println( response);
                videoAdapter.notifyDataSetChanged();

            }

            @Override
            public void onGetVideosError(String errorMessage) {
                // Handle error
                //android.widget.Toast.makeText(Hom.this, "Failed to delete account: " + errorMessage, android.widget.Toast.LENGTH_SHORT).show();
            }
        });
//        try {
//            InputStream inputStream = getResources().openRawResource(R.raw.data); // Ensure you have a data.json file in res/raw folder
//            JsonReader reader = new JsonReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
//            reader.beginArray();
//
//            while (reader.hasNext()) {
//                reader.beginObject();
//
//                String title = null;
//                String username = null;
//                String views = null;
//                String time = null;
//                String videoUrl = null;
//
//                while (reader.hasNext()) {
//                    String name = reader.nextName();
//                    switch (name) {
//                        case "title":
//                            title = reader.nextString();
//                            break;
//                        case "username":
//                            username = reader.nextString();
//                            break;
//                        case "views":
//                            views = reader.nextString();
//                            break;
//                        case "time":
//                            time = reader.nextString();
//                            break;
//                        case "video_url":
//                            videoUrl = reader.nextString();
//                            break;
//                        default:
//                            reader.skipValue();
//                            break;
//                    }
//                }
//                Video videoItem = new Video(title, username, views, time, videoUrl, 0);
//                videoList.add(videoItem);
//                filteredVideoList.add(videoItem); // Initially, show all videos
//                reader.endObject();
//            }
//            reader.endArray();
//            reader.close();
//
//            videoAdapter.notifyDataSetChanged();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e(TAG, "Error loading data", e);
//        }
    }

    private void filterVideos(String query) {
        filteredVideoList.clear();
        for (Video video : videoList) {
            if (video.getTitle().toLowerCase(Locale.getDefault()).contains(query.toLowerCase(Locale.getDefault()))) {
                filteredVideoList.add(video);
            }
        }
        videoAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), VideoPage.class);
        Video videoItem = filteredVideoList.get(position);
        intent.putExtra("video_title", videoItem.getTitle());
        intent.putExtra("video_username", videoItem.getUsername());
        intent.putExtra("video_views", videoItem.getViews());
        intent.putExtra("video_time", videoItem.getTime());
        intent.putExtra("video_url", videoItem.getVideoUrl());
        startActivity(intent);
    }

    public void toggleSearchBar() {
        if (searchBar.getVisibility() == View.GONE) {
            searchBar.setVisibility(View.VISIBLE);
            isSearchActive = true;
        } else {
            searchBar.setVisibility(View.GONE);
            searchBar.setText(""); // Clear search bar
            isSearchActive = false;
            filteredVideoList.clear();
            filteredVideoList.addAll(videoList);
            videoAdapter.notifyDataSetChanged();
        }
    }
}
