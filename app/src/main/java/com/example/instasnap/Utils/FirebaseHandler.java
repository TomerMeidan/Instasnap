package com.example.instasnap.Utils;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.instasnap.Model.Post;
import com.example.instasnap.Model.Story;
import com.example.instasnap.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FirebaseHandler {
    private  FirebaseFirestore _db;
    public FirebaseHandler(){
        _db = FirebaseFirestore.getInstance();
    }

    public void setUserInFirestoreDataBase(User user){

        // Set the document with a custom ID
        _db.collection("users")
                .document(user.getUniqueID())  // Set the custom document ID here
                .set(user)  // Use set() to add the user object to the specified document
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot added with custom ID: " + user.getUniqueID());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public void addPostOnFirebase(User user, Post post) {
        // Convert the Post object to a map
        HashMap<String, Object> postMap = Tools.convertPostToHashMap(post);

        // Get a reference to the document you want to update
        DocumentReference docRef = _db.collection("users").document(user.getUniqueID());

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Get the existing list of posts from the "posts" field
                        ArrayList<HashMap<String, Object>> existingPosts = (ArrayList<HashMap<String, Object>>) document.get("posts");

                        // If no posts exist yet, create a new list
                        if (existingPosts == null) {
                            existingPosts = new ArrayList<>();
                        }

                        // Add the new post to the existing list
                        existingPosts.add(postMap);

                        // Update the "posts" field with the updated list
                        docRef.update("posts", existingPosts)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "Document updated successfully.");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error updating document", e);
                                    }
                                });
                    } else {
                        Log.d(TAG, "Document does not exist");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public ArrayList<User> readDataFromFirebase() {
        final JSONArray usersJson = new JSONArray();

        Task<QuerySnapshot> task = _db.collection("users").get();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            // CompletableFuture to wait for the result on a background thread
            CompletableFuture<QuerySnapshot> completableFuture = new CompletableFuture<>();
            task.addOnCompleteListener(executor, new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(Task<QuerySnapshot> task) {
                    completableFuture.complete(task.getResult());
                }
            });

            QuerySnapshot querySnapshot = completableFuture.get();
            if (querySnapshot != null && !querySnapshot.isEmpty()) {

                for (DocumentSnapshot document : querySnapshot.getDocuments())
                    addUserDocumentToJson(usersJson, document);

            } else {
                Log.w(TAG, "No documents found.");
            }
        } catch (ExecutionException | InterruptedException e) {
            Log.e(TAG, "Error waiting for Firestore operation.", e);
        }

        return Parser.parseUsers(usersJson);
    }

    private void addUserDocumentToJson(JSONArray usersJson, DocumentSnapshot document) {
        Map<String, Object> dataMap = document.getData();
        JSONObject jsonObject = new JSONObject(dataMap);
        usersJson.add(jsonObject);
        Log.d(TAG, document.getId() + " => " + document.getData());
    }

}
