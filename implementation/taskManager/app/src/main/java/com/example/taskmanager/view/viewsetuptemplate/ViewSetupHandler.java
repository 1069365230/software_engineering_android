package com.example.taskmanager.view.viewsetuptemplate;

import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.model.recyclerviewadapters.AppointmentTaskAdapter;
import com.example.taskmanager.model.recyclerviewadapters.CheckListTaskAdapter;
import com.example.taskmanager.model.recyclerviewadapters.CheckListTaskGroupAdapter;
import com.example.taskmanager.model.recyclerviewadapters.TaskAdapter;

import com.example.taskmanager.viewmodel.DataBaseControl;

public abstract class ViewSetupHandler {

    abstract void setUpDataBase();
    abstract void setUpAdapters();
    abstract void setUpLayout();
    abstract void setUpLiveDataObservers();

    public void loadView() {
        setUpDataBase();
        setUpAdapters();
        setUpLayout();
        setUpLiveDataObservers();
    }
}
