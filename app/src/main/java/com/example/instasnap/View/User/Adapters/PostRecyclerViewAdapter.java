package com.example.instasnap.View.User.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instasnap.Model.Post;
import com.example.instasnap.R;
import com.example.instasnap.ViewModel.HomePageViewModel;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostRecyclerViewAdapter extends RecyclerView.Adapter<PostRecyclerViewAdapter.PostViewHolder>{
    private ArrayList<Post> _posts;
    private HomePageViewModel _homeHomePageViewModel;

    public PostRecyclerViewAdapter(ArrayList<Post> allPosts, HomePageViewModel homePageViewModel){
        _posts = allPosts;
        _homeHomePageViewModel = homePageViewModel;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View countryView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_recycler_view_item, parent, false);
        return new PostViewHolder(countryView);
    }

    @Override
    public void onBindViewHolder(@NonNull PostRecyclerViewAdapter.PostViewHolder holder, int position) {
        Post currentPost = _posts.get(position);

        holder._post = currentPost;
        holder._likeTextView.setText(String.valueOf(currentPost.likes));
        holder._usernameTextView.setText(currentPost.username);
        holder._userProfileImageView.setImageResource(
                holder._usernameTextView.getResources().getIdentifier(
                        currentPost.profilePictureURL,"drawable", holder._usernameTextView.getContext().getOpPackageName()
                )
        );
        Picasso.get().load(currentPost.getPostPictureURL()).into(holder._postImageView); // Retrieving the image from the internet
    }

    @Override
    public int getItemCount() {
        return _posts.size();
    }

    public void updatePostList(ArrayList<Post> posts) {
        _posts = posts;
        this.notifyDataSetChanged();
    }

    // -------------------------- Inner class ------------------------------
    protected class PostViewHolder extends RecyclerView.ViewHolder{

        private Post _post;
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

            _likeButton.setOnClickListener(view -> _homeHomePageViewModel.addLike(_post));
        }
    }
}
