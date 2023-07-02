package com.example.instasnap;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class LoginViewModel extends AndroidViewModel {

    private static final String PREF_NAME = "LoginPreferences";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";

    private SharedPreferences _sharedPreferences;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        _sharedPreferences = application.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveLoginCredentials(String email, String password) {
        SharedPreferences.Editor editor = _sharedPreferences.edit();
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD, password);
        editor.apply();
    }

    public void clearLoginCredentials() {
        SharedPreferences.Editor editor = _sharedPreferences.edit();
        editor.remove(KEY_EMAIL);
        editor.remove(KEY_PASSWORD);
        editor.apply();
    }

    public String getEmail() {
        return _sharedPreferences.getString(KEY_EMAIL, "");
    }

    public String getPassword() {
        return _sharedPreferences.getString(KEY_PASSWORD, "");
    }

    public boolean isLoginCredentialsSaved() {
        return _sharedPreferences.contains(KEY_EMAIL) && _sharedPreferences.contains(KEY_PASSWORD);
    }
}
