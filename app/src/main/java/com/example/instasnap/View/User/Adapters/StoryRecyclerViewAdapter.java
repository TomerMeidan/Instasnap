package com.example.instasnap.View.User.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instasnap.Model.Story;
import com.example.instasnap.R;
import com.example.instasnap.ViewModel.HomePageViewModel;

import java.util.ArrayList;

public class StoryRecyclerViewAdapter extends RecyclerView.Adapter<StoryRecyclerViewAdapter.StoryViewHolder> {

    private ArrayList<Story> _storys;
    private HomePageViewModel _homeHomePageViewModel;

    public StoryRecyclerViewAdapter(ArrayList<Story> allStories, HomePageViewModel homePageViewModel){
        _storys = allStories;
        _homeHomePageViewModel = homePageViewModel;
    }

    @NonNull
    @Override
    public StoryRecyclerViewAdapter.StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View countryView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.story_recycler_view_item, parent, false);
        return new StoryViewHolder(countryView);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryRecyclerViewAdapter.StoryViewHolder holder, int position) {
        Story currentStory = _storys.get(position);

        holder._story = currentStory;
        holder._userNameTextView.setText(currentStory.user.username);
        holder._userProfileImageView.setImageResource(
                holder._userNameTextView.getResources().getIdentifier(
                        currentStory.user.profilePictureId,"drawable", holder._userNameTextView.getContext().getOpPackageName()
                )
        );
    }

    @Override
    public int getItemCount() { return _storys.size(); }

    // -------------------------- Inner class ------------------------------
    protected class StoryViewHolder extends RecyclerView.ViewHolder{

        private Story _story;
        private ImageView _userProfileImageView;
        private TextView _userNameTextView;


        public StoryViewHolder(@NonNull View itemView) {
            super(itemView);

            _userProfileImageView = itemView.findViewById(R.id.user_imageView);
            _userNameTextView = itemView.findViewById(R.id.user_name_textView);
        }
    }
}
