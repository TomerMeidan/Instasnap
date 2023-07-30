package com.example.instasnap.Utils;

import androidx.annotation.NonNull;
import com.example.instasnap.Model.Post;
import com.example.instasnap.Model.Story;
import com.example.instasnap.Model.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

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
    public static ArrayList<User> parseUsers(JSONArray context) {

        ArrayList<User> allUsers = new ArrayList<>(); // All users

        JSONArray usersJson = context;

        for (Object temp1 : usersJson) { // Parsing the users

            JSONObject singleUser = (JSONObject) temp1;
            JSONArray postsJson = Tools.convertArrayListToJsonArray((ArrayList<?>) singleUser.get("posts"));
            JSONArray storiesJson = Tools.convertArrayListToJsonArray((ArrayList<?>) singleUser.get("stories"));

            String email = (String) singleUser.get("email");
            String username = (String) singleUser.get("username");
            String password = (String) singleUser.get("password");
            String profilePictureURL = (String) singleUser.get("profilePictureURL");
            String userUniqueID = (String) singleUser.get("uniqueID");

            ArrayList<Post> userPosts = getUserPosts(postsJson, username, profilePictureURL);
            ArrayList<Story> userStories = getUserStories(storiesJson, username, profilePictureURL);

            User userInfo = new User(email, password, profilePictureURL, userPosts, userStories, userUniqueID);
            allUsers.add(userInfo);
        }

        return allUsers;
    }

    @NonNull
    private static ArrayList<Post> getUserPosts(JSONArray postsJson, String username, String profilePictureURL) {
        ArrayList<Post> userPosts = new ArrayList<>(); // Array of a single user's posts

        for (Object temp2 : postsJson) { // Parsing the user's posts
            JSONObject jsonPost = Tools.convertHashMapToJsonObject((HashMap<String, Object>) temp2);
            String postPictureURL = (String) jsonPost.get("postPictureURL");
            String userUniqueID = (String) jsonPost.get("uniqueID");
            Long temp = (Long) jsonPost.get("likes");
            int likes = temp.intValue();
            Post post = new Post(username, profilePictureURL, userUniqueID, postPictureURL, likes);
            userPosts.add(post);
        }
        return userPosts;
    }

    private static ArrayList<Story> getUserStories(JSONArray storiesJson, String username, String profilePictureID) {
        ArrayList<Story> userStories = new ArrayList<>(); // Array of a single user's posts

        for (Object temp2 : storiesJson){
            JSONObject jsonPost = Tools.convertHashMapToJsonObject((HashMap<String, Object>) temp2);
            String storyPictureID = (String) jsonPost.get("storyPictureID");
            String userUniqueID = (String) jsonPost.get("uniqueID");

            Story story = new Story(username, profilePictureID, userUniqueID, storyPictureID);
            userStories.add(story);
        }

        return userStories;
    }
}
