package com.example.instasnap.View.User.Fragments;


import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.content.Context;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.instasnap.R;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@SuppressLint("InlinedApi")
public class UploadPageFragmentView extends Fragment {

    private ActivityResultLauncher<String[]> _mPermissionResultLauncher;

    private boolean _isReceiveSMSPermissionGranted = false;

    private boolean _isReadSMSPermissionGranted = false;

    private boolean _isReadMediaImagesPermissionGranted = false;

    public UploadPageFragmentView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initiatePermissionLauncher();

        requestPermissions();

        Button uploadButton = view.findViewById(R.id.upload_button);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                final int ACTIVITY_SELECT_IMAGE = 1234;
                startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1234) {
            if (resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContext().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                cursor.close();


                Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);

                // Save the Bitmap to internal storage
                saveBitmapToInternalStorage(yourSelectedImage);

            }
        }

    }

    private void saveBitmapToInternalStorage(Bitmap bitmap) {
        File directory = getContext().getFilesDir();
        String fileName = "my_bitmap.png";
        File file = new File(directory, fileName);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            // The bitmap is now saved to internal storage at the specified location
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.upload_page_fragment, container, false);
    }

    private void initiatePermissionLauncher() {
        _mPermissionResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> result) {

                if (result.get(Manifest.permission.RECEIVE_SMS) != null)
                    _isReceiveSMSPermissionGranted = Boolean.TRUE.equals(result.get(Manifest.permission.RECEIVE_SMS));

                if (result.get(Manifest.permission.READ_SMS) != null)
                    _isReadSMSPermissionGranted = Boolean.TRUE.equals(result.get(Manifest.permission.READ_SMS));

                if (result.get(Manifest.permission.READ_MEDIA_IMAGES) != null)
                    _isReadMediaImagesPermissionGranted = Boolean.TRUE.equals(result.get(Manifest.permission.READ_MEDIA_IMAGES));

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

        if (!_isReadMediaImagesPermissionGranted)
            permissionRequest.add(Manifest.permission.READ_MEDIA_IMAGES);

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