package com.example.instasnap.View.HomePage;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instasnap.R;

public class PostRecyclerViewAdapter extends RecyclerView.Adapter<PostRecyclerViewAdapter.PostViewHolder>{
    @NonNull
    @Override
    public PostRecyclerViewAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull PostRecyclerViewAdapter.PostViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    // -------------------------- Inner class ------------------------------
    protected static class PostViewHolder extends RecyclerView.ViewHolder{

        private ImageView _userProfileImageView, _postImageView;
        private ImageButton _likeButton;
        private TextView _usernameTextView, _likeTextView;


        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            _userProfileImageView = itemView.findViewById(R.id.user_profile_imageView);
            _postImageView = itemView.findViewById(R.id.user_post_imageView);
            _likeButton = itemView.findViewById(R.id.like_imageButton);
            _usernameTextView = itemView.findViewById(R.id.username_textView);
            _likeTextView = itemView.findViewById(R.id.like_textView);
        }
    }
}
