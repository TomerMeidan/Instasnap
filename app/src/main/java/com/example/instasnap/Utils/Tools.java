package com.example.instasnap.Utils;

import com.example.instasnap.Model.Post;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;

public class Tools {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom random = new SecureRandom();

    public static String generateRandomString() {
        int minLength = 10;
        int maxLength = 20;
        int randomLength = random.nextInt(maxLength - minLength + 1) + minLength;

        StringBuilder sb = new StringBuilder(randomLength);
        for (int i = 0; i < randomLength; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }

    public static JSONArray convertArrayListToJsonArray(ArrayList<?> arrayList) {
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(arrayList);
        return jsonArray;
    }

    public static JSONObject convertHashMapToJsonObject(HashMap<String, Object> hashMap) {
        JSONObject jsonObject = new JSONObject();
        for (String key : hashMap.keySet()) {
            jsonObject.put(key, hashMap.get(key));
        }
        return jsonObject;
    }

    public static HashMap<String, Object> convertPostToHashMap(Post post) {
        HashMap<String, Object> postMap = new HashMap<>();
            postMap.put("likes", post.getLikes());
            postMap.put("postPictureURL", post.getPostPictureURL());
            postMap.put("username", post.getUsername());
            postMap.put("profilePictureURL",post.getProfilePictureURL());
            postMap.put("userUniqueID", post.getUserUniqueID());
        return postMap;
    }

}
