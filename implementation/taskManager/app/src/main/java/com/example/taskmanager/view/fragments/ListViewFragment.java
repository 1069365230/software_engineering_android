package com.example.taskmanager.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.taskmanager.R;
import com.example.taskmanager.view.decorators.BackgroundColorDecorator;
import com.example.taskmanager.view.viewsetuptemplate.ListViewSetupHandler;


public class ListViewFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


//        ListViewSetupHandler listViewSetupHandler = new ListViewSetupHandler(this, view);
//        listViewSetupHandler.loadView();

        ListViewSetupHandler listViewSetupHandler =
                new BackgroundColorDecorator(this, view, new ListViewSetupHandler(this, view));

        listViewSetupHandler.loadView();
    }
}