package com.example.instasnap.View.User.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instasnap.R;

public class MainPageFragmentView extends Fragment {

    private HomePageFragmentView homePageFragmentView;

    public MainPageFragmentView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        getChildFragmentManager().beginTransaction()
                .replace(R.id.main_container_view, HomePageFragmentView.class, null)
                .commit();
        getChildFragmentManager().executePendingTransactions();

    }

        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.main_page_fragment, container, false);
    }

}