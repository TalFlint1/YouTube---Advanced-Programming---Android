package com.example.youtube_android;

import android.content.Context;
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
import java.io.Serializable;
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
        Context c = getContext();
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
                    Log.i("response:  ", item.toString());
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
        Log.i("videoItem" ,videoItem.toString());
        Log.i("video_title" ,videoItem.getTitle());
        Log.i("getUsername" ,videoItem.getUsername());
        Log.i("getViews" , String.valueOf(videoItem.getViews()));
        Log.i("getTime" ,videoItem.getTime());
        Log.i("getVideoUrl" ,videoItem.getVideoUrl());

        intent.putExtra("video_title", videoItem.getTitle());
        intent.putExtra("video_username", videoItem.getUsername());
        intent.putExtra("video_views", String.valueOf(videoItem.getViews()));
        intent.putExtra("video_time", videoItem.getTime());
        intent.putExtra("video_time_type", videoItem.getTime_type());
        intent.putExtra("video_url", videoItem.getVideoUrl());
        intent.putExtra("id", videoItem.getId());
        Log.e("here",";");
        intent.putExtra("comments_list", (Serializable) videoItem.getComments());

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
