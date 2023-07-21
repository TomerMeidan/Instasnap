package com.example.instasnap.View;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class ScreenModeDialog extends DialogFragment {
    private static final String[] THEME_NAMES = {"Light Theme", "Dark Theme"};
    private static final int[] THEME_VALUES = {com.google.android.material.R.style.Base_V24_Theme_Material3_Light,
            com.google.android.material.R.style.Base_V24_Theme_Material3_Dark};

    public static ScreenModeDialog newInstance(String title) {
        ScreenModeDialog frag = new ScreenModeDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Theme")
                .setItems(THEME_NAMES, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        int selectedTheme = THEME_VALUES[which];
                        applyTheme(selectedTheme);
                    }
                });
        return builder.create();
    }

    private void applyTheme(int theme) {
        // Save the chosen theme to a shared preference
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ThemePrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("current_theme", theme);
        editor.apply();

        // Recreate the theme in the homepage screen
        getActivity().setTheme(theme);
        getActivity().recreate();
        dismiss();
    }

}
