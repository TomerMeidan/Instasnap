package com.example.instasnap.View.HomePage;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instasnap.R;

public class StoryRecyclerViewAdapter extends RecyclerView.Adapter<StoryRecyclerViewAdapter.StoryViewHolder> {
    @NonNull
    @Override
    public StoryRecyclerViewAdapter.StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull StoryRecyclerViewAdapter.StoryViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    // -------------------------- Inner class ------------------------------
    protected class StoryViewHolder extends RecyclerView.ViewHolder{

        private ImageView _userProfileImageView;
        private TextView _userNameTextView;


        public StoryViewHolder(@NonNull View itemView) {
            super(itemView);

            _userProfileImageView = itemView.findViewById(R.id.user_imageView);
            _userNameTextView = itemView.findViewById(R.id.user_name_textView);
        }
    }
}
