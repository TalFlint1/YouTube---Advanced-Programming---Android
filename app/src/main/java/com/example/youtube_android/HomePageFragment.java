package com.example.youtube_android;

import android.content.Intent;
import android.os.Bundle;
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

import java.io.Serializable;
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

        // Initialize VideoRepository
        repository = new VideoRepository(getContext());

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

        // Load data from the database
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
        repository.getAllVideos(new VideoRepository.GetVideosCallback() {

            @Override
            public void onGetVideosResponse(List<Video> response) {
                // Ensure UI updates are on the main thread
                requireActivity().runOnUiThread(() -> {
                    videoList.clear();
                    filteredVideoList.clear();
                    videoList.addAll(response);
                    filteredVideoList.addAll(response); // Initially, show all videos
                    videoAdapter.notifyDataSetChanged();
                });
            }

            @Override
            public void onGetVideosError(String errorMessage) {
                // Handle error
                Log.e(TAG, "Error fetching videos: " + errorMessage);
                // You might want to show a toast or error message to the user
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
        Log.i("videoItem", videoItem.toString());
        Log.i("video_title", videoItem.getTitle());
        Log.i("getUsername", videoItem.getUsername());
        Log.i("getViews", videoItem.getViews());
        Log.i("getTime", videoItem.getTime());
        Log.i("getVideoUrl", videoItem.getVideoUrl());
        intent.putExtra("comments_list", (Serializable) videoItem.getComments());

        intent.putExtra("video_title", videoItem.getTitle());
        intent.putExtra("video_username", videoItem.getUsername());
        intent.putExtra("video_views", videoItem.getViews());
        intent.putExtra("video_time", videoItem.getTime());
        intent.putExtra("video_url", videoItem.getVideoUrl());
        intent.putExtra("id", videoItem.getId());
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
