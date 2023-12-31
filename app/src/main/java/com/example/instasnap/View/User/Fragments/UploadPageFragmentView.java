package com.example.instasnap.View.User.Fragments;


import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.instasnap.Model.Post;
import com.example.instasnap.Model.User;
import com.example.instasnap.R;
import com.example.instasnap.Utils.FirebaseHandler;
import com.example.instasnap.Utils.Tools;
import com.example.instasnap.ViewModel.HomePageViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@SuppressLint("InlinedApi")
public class UploadPageFragmentView extends Fragment {

    private HomePageViewModel _homePageViewModel;
    private ActivityResultLauncher<String[]> _mPermissionResultLauncher;
    private boolean _isReceiveSMSPermissionGranted = false;
    private boolean _isReadSMSPermissionGranted = false;
    private boolean _isReadMediaImagesPermissionGranted = false;
    private ProgressBar _progressBar;
    private  FirebaseUser _currentUser;

    private Button _uploadButton;
    private View _permissionTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initialize(view);
        initiatePermissionLauncher();
        requestPermissions();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        _progressBar.setVisibility(View.VISIBLE);

        if (requestCode == 1234) { // Request code to select and upload an image
            if (resultCode == RESULT_OK) { // If an image was selected, it will enter here

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                String filePath = getFilePathFromDevice(selectedImage, filePathColumn);

                _currentUser = FirebaseAuth.getInstance().getCurrentUser();
                StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images").child(_currentUser.getUid()).child("posts").child(Tools.generateRandomString());

            // Replace 'user_id' with the actual user ID to create a unique path for each user's profile picture
                Uri imageUri = Uri.fromFile(new File(filePath));
                UploadTask uploadTask = storageRef.putFile(imageUri);

                uploadTask.addOnSuccessListener(taskSnapshot -> {
                    // Image uploaded successfully
                    // You can get the URL of the uploaded image using taskSnapshot.getDownloadUrl()
                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {

                        // Save the 'imageUrl' to the user's document in Firestore
                        // Add New Post here to the recyclerView through the HomePageViewModel
                        String imageUrl = uri.toString();

                        _homePageViewModel = new ViewModelProvider(requireActivity()).get(HomePageViewModel.class);
                        Post post = uploadPostToFirebase(imageUrl);
                        _homePageViewModel.addMutablePost(post); // Alert observer about new post

                        _progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "The post was uploaded!", Toast.LENGTH_SHORT).show();

                    }).addOnFailureListener(e -> {
                        _progressBar.setVisibility(View.GONE);

                    });
                }).addOnFailureListener(e -> {
                    _progressBar.setVisibility(View.GONE);

                });


            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.upload_page_fragment, container, false);
    }

    private void initialize(@NonNull View view) {
        _progressBar = view.findViewById(R.id.upload_progressBar);
        _uploadButton = view.findViewById(R.id.upload_button);
        _uploadButton.setEnabled(false);

        _uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                final int ACTIVITY_SELECT_IMAGE = 1234;
                startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
            }
        });
        _permissionTextView = view.findViewById(R.id.permission_denied_text);
        _permissionTextView.setVisibility(View.GONE);
    }


    @NonNull
    private Post uploadPostToFirebase(String imageUrl) {
        User postingUser = _homePageViewModel.getUserFromList(_currentUser.getUid());
        Post post = new Post(postingUser.getUsername(),postingUser.getProfilePictureURL() ,postingUser.getUniqueID(), imageUrl, 0);
        postingUser.getPosts().add(post);
        FirebaseHandler firebaseHandler = new FirebaseHandler();
        firebaseHandler.addPostOnFirebase(postingUser, post);
        return post;
    }

    private String getFilePathFromDevice(Uri selectedImage, String[] filePathColumn) {
        Cursor cursor = getContext().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }

    private void initiatePermissionLauncher() {

        _mPermissionResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions()
                , new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> result) {

                if (result.get(Manifest.permission.RECEIVE_SMS) != null)
                    _isReceiveSMSPermissionGranted = Boolean.TRUE.equals(result.get(Manifest.permission.RECEIVE_SMS));

                if (result.get(Manifest.permission.READ_SMS) != null)
                    _isReadSMSPermissionGranted = Boolean.TRUE.equals(result.get(Manifest.permission.READ_SMS));

                if (result.get(Manifest.permission.READ_MEDIA_IMAGES) != null)
                    _isReadMediaImagesPermissionGranted = Boolean.TRUE.equals(result.get(Manifest.permission.READ_MEDIA_IMAGES));

                if(!_isReadMediaImagesPermissionGranted){
                    _uploadButton.setEnabled(false);
                    _permissionTextView.setVisibility(View.VISIBLE);
                } else{
                    _uploadButton.setEnabled(true);
                    _permissionTextView.setVisibility(View.GONE);
                }

            }
        });

    }

    private void requestPermissions(){

        checkGrantedPermissions();

        List<String> permissionRequest = new ArrayList<>();

        if (!_isReceiveSMSPermissionGranted)
            permissionRequest.add(Manifest.permission.RECEIVE_SMS);

        if (!_isReadSMSPermissionGranted)
            permissionRequest.add(Manifest.permission.READ_SMS);

        if(!_isReadMediaImagesPermissionGranted){
            permissionRequest.add(Manifest.permission.READ_MEDIA_IMAGES);
        } else{
            _uploadButton.setEnabled(true);
            _permissionTextView.setVisibility(View.GONE);
        }

        if (!permissionRequest.isEmpty())
            _mPermissionResultLauncher.launch(permissionRequest.toArray(new String[0]));

    }

    private void checkGrantedPermissions() {
        _isReceiveSMSPermissionGranted = ContextCompat.checkSelfPermission(
                getContext(),
                Manifest.permission.RECEIVE_SMS
        ) == PackageManager.PERMISSION_GRANTED;

        _isReadSMSPermissionGranted = ContextCompat.checkSelfPermission(
                getContext(),
                Manifest.permission.READ_SMS
        ) == PackageManager.PERMISSION_GRANTED;

        _isReadMediaImagesPermissionGranted = ContextCompat.checkSelfPermission(
                getContext(),
                Manifest.permission.READ_MEDIA_IMAGES
        ) == PackageManager.PERMISSION_GRANTED;
    }



}