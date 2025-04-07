package com.example.taskmanager.view.decorators;

import android.graphics.Color;
import android.view.View;
import android.view.ViewParent;
import android.widget.Adapter;
import android.widget.RelativeLayout;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.R;
import com.example.taskmanager.model.recyclerviewadapters.AppointmentTaskAdapter;
import com.example.taskmanager.model.taskdata.AppointmentTask;
import com.example.taskmanager.model.taskdata.Task;
import com.example.taskmanager.view.fragments.ListViewFragment;
import com.example.taskmanager.view.viewsetuptemplate.ListViewSetupHandler;

import java.util.List;

public class BackgroundColorDecorator extends ViewDecorator{
    private View view;
    private ListViewFragment listViewFragment;

    public BackgroundColorDecorator(ListViewFragment listViewFragment, View view, ListViewSetupHandler listViewSetupHandler) {
        super(listViewFragment, view, listViewSetupHandler);
        this.view = view;
        this.listViewFragment = listViewFragment;
    }

    @Override
    public void loadView(){
        listViewSetupHandler.loadView();
        setBackGroundColor();
    }

    private void setBackGroundColor(){
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.recycler_view);

        rv.setBackgroundColor(Color.DKGRAY);
    }
}
