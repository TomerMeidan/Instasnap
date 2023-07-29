package com.example.instasnap.Utils;

import android.content.Context;
import android.content.res.AssetManager;

import androidx.annotation.NonNull;

import com.example.instasnap.Model.Post;
import com.example.instasnap.Model.Story;
import com.example.instasnap.Model.User;

;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Parser {

    public static ArrayList<Post> getAllPosts(ArrayList<User> users) {
        ArrayList<Post> allPosts = new ArrayList<>();
        for(User user : users)
            allPosts.addAll(user.getPosts());
        return allPosts;
    }

    public static ArrayList<Story> getAllStories(ArrayList<User> users) {
        ArrayList<Story> allStories = new ArrayList<>();
        for(User user : users){
            allStories.addAll(user.getStories());
        }
        return allStories;
    }

    public static ArrayList<User> parseUsers(Context context) {

        ArrayList<User> allUsers = new ArrayList<>(); // All users

        InputStream inputStream = openJsonFile(context, "users.json");
        JSONParser jsonParser = new JSONParser();
        JSONObject jo;

        try {
            jo = (JSONObject) jsonParser.parse(
                    new InputStreamReader(inputStream, "UTF-8"));
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }

        JSONArray usersJson = (JSONArray) jo.get("users");

        for (Object temp1 : usersJson) { // Parsing the users

            JSONObject singleUser = (JSONObject) temp1;
            JSONArray postsJson = (JSONArray) singleUser.get("posts");
            JSONArray storiesJson = (JSONArray) singleUser.get("stories");

            String email = (String) singleUser.get("email");
            String username = (String) singleUser.get("username");
            String password = (String) singleUser.get("password");
            String profilePictureID = (String) singleUser.get("profilePictureID");

            ArrayList<Post> userPosts = getUserPosts(postsJson, username, profilePictureID);
            ArrayList<Story> userStories = getUserStories(storiesJson, username, profilePictureID);

            User userInfo = new User(email, password, profilePictureID, userPosts, userStories);
            allUsers.add(userInfo);
        }

        return allUsers;
    }

    @NonNull
    private static ArrayList<Post> getUserPosts(JSONArray postsJson, String username, String profilePictureID) {
        ArrayList<Post> userPosts = new ArrayList<>(); // Array of a single user's posts

        for (Object temp2 : postsJson) { // Parsing the user's posts
            JSONObject jsonPost = (JSONObject) temp2;
            String postPictureID = (String) jsonPost.get("postPictureID");
            Long temp = (Long) jsonPost.get("likes");
            int likes = temp.intValue();
            Post post = new Post(new User(username, profilePictureID), postPictureID, likes);
            userPosts.add(post);
        }
        return userPosts;
    }

    private static ArrayList<Story> getUserStories(JSONArray storiesJson, String username, String profilePictureID) {
        ArrayList<Story> userStories = new ArrayList<>(); // Array of a single user's posts

        for (Object temp2 : storiesJson){
            JSONObject jsonPost = (JSONObject) temp2;
            String storyPictureID = (String) jsonPost.get("storyPictureID");
            Story story = new Story(new User(username, profilePictureID), storyPictureID);
            userStories.add(story);
        }

        return userStories;
    }

    private static InputStream openJsonFile(Context context, String fileName) {
        AssetManager assetManager = context.getAssets();
        InputStream in =null;
        try {
            in = assetManager.open(fileName);
        } catch (IOException e) {e.printStackTrace();}
        return in;
    }
}
