package com.example.instasnap.View.User;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.instasnap.R;
import com.example.instasnap.Utils.LikeBroadcastReceiver;
import com.example.instasnap.Utils.LikeService;
import com.example.instasnap.View.User.Fragments.HomePageFragmentView;
import com.example.instasnap.View.User.Fragments.ScreenModeDialogView;
import com.example.instasnap.View.LoginView;
import com.example.instasnap.View.User.Fragments.SettingsFragmentView;
import com.example.instasnap.View.User.Fragments.UploadPageFragmentView;
import com.google.firebase.auth.FirebaseAuth;

public class UserView extends AppCompatActivity {

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);
        getSavedTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_view);

        initializeReceiver();
        initializeWorkManagerService();

        initiateMainPageView();

    }

    private void initializeReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.instasnap.LIKE_NOTIFICATION");

        LikeBroadcastReceiver likeBroadcastReceiver = LikeBroadcastReceiver.getInstance();
        registerReceiver(likeBroadcastReceiver, intentFilter);
    }

    private void initializeWorkManagerService() {
        if(!isMyServiceRunning(LikeService.class)) {
            Intent intent = new Intent(this, LikeService.class);
            startService(intent);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void initiateMainPageView() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.user_container_view, HomePageFragmentView.class, null)
                .commit();
        getSupportFragmentManager().executePendingTransactions();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_logout:
                FirebaseAuth.getInstance().signOut();
                Intent loginIntent = new Intent(getApplicationContext(), LoginView.class);
                startActivity(loginIntent);
                finish();
                return true;
            case R.id.menu_screen_mode:
                FragmentManager fm = getSupportFragmentManager();
                ScreenModeDialogView screenModeView = ScreenModeDialogView.newInstance("Theme Choice");
                screenModeView.show(fm, "screen_mode_fragment");
                return true;
            case R.id.menu_settings:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.user_container_view, new SettingsFragmentView(), "SFV")
                        .addToBackStack("SettingsView")
                        .commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onHomepageButtonClick(View view){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.user_container_view, new HomePageFragmentView())
                .addToBackStack("HomePage")
                .commit();
    }

    public void onPostButtonClick(View view){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.user_container_view, new UploadPageFragmentView(), "UPFV")
                .addToBackStack("Upload")
                .commit();
    }


    private void getSavedTheme() {
        // Retrieve the theme from shared preference
        int defaultTheme = com.google.android.material.R.style.Base_V24_Theme_Material3_Light; // Default theme if no theme is found
        SharedPreferences sharedPreferences = getSharedPreferences("ThemePrefs", Context.MODE_PRIVATE);
        int currentTheme = sharedPreferences.getInt("current_theme", defaultTheme);
        setTheme(currentTheme);
    }

}