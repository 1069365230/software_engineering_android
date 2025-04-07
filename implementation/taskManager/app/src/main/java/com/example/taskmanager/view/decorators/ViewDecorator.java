package com.example.taskmanager.view.decorators;


import android.view.View;

import com.example.taskmanager.view.fragments.ListViewFragment;
import com.example.taskmanager.view.viewsetuptemplate.ListViewSetupHandler;
import com.example.taskmanager.view.viewsetuptemplate.ViewSetupHandler;

public class ViewDecorator extends ListViewSetupHandler {
    ListViewSetupHandler listViewSetupHandler;

    public ViewDecorator(ListViewFragment listViewFragment, View view, ListViewSetupHandler listViewSetupHandler) {
        super(listViewFragment, view);
        this.listViewSetupHandler = listViewSetupHandler;
    }

    @Override
    public void loadView(){
        listViewSetupHandler.loadView();
    }
}
