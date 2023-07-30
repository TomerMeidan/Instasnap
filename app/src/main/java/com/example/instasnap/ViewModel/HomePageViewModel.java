package com.example.instasnap.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.instasnap.Model.Post;
import com.example.instasnap.Model.Story;
import com.example.instasnap.Model.User;

import java.util.ArrayList;

public class HomePageViewModel extends AndroidViewModel {

    //region Variables

    private ArrayList<User> _users;

    //----------------- Story variables -------------------
    private MutableLiveData<ArrayList<Story>> _mutableStoryList = new MutableLiveData<>();
    private ArrayList<Story> _storyList; //TODO add here creation of users
    private MutableLiveData<Integer> _mutableStoryIndex = new MutableLiveData<>();
    private Integer _storyIndex = new Integer(-1); // Default value when no user displayed

    //----------------- Post variables -------------------
    private MutableLiveData<ArrayList<Post>> _mutablePostList = new MutableLiveData<>();
    private ArrayList<Post> _postList; //TODO add here creation of users
    private MutableLiveData<Integer> _mutablePostIndex = new MutableLiveData<>();
    private Integer _postIndex = new Integer(-1); // Default value when no user displayed

    //endregion

    public HomePageViewModel(@NonNull Application application) { super(application); }

    public void setPostList(ArrayList<Post> posts){
        _postList = posts;

        // Set post mutable variables
        _mutablePostList.setValue(_postList);
        _mutablePostIndex.setValue(_postIndex);
    }

    public void setStoryList(ArrayList<Story> story){
        _storyList = story;

        // Set story mutable variables
        _mutableStoryList.setValue(_storyList);
        _mutableStoryIndex.setValue(_storyIndex);
    }


    public void addMutablePost(Post post){
        _postList.add(post);

        // Set post mutable variables
        _mutablePostList.setValue(_postList);
    }
    public MutableLiveData<ArrayList<Story>> getMutableUserStoryList(){ return _mutableStoryList; }

    public MutableLiveData<Integer> getMutableUserStoryIndex(){ return _mutableStoryIndex; }

    public MutableLiveData<ArrayList<Post>> getMutableUserPostList(){ return _mutablePostList; }

    public MutableLiveData<Integer> getMutableUserPostIndex(){ return _mutablePostIndex; }

    public void addLike(Post post) {
        int position = _postList.indexOf(post);

        if(position >= 0) {
            _postList.get(position).likes++;
            _mutablePostList.setValue(_postList);
        }
    }

    public void setUserList(ArrayList<User> users) {
        _users = users;
    }

    public User getUserFromList(String uniqueID){
        for(User user : _users){
            if (user.getUniqueID().equals(uniqueID))
                return user;
        }
        return null;
    }
}
