package com.example.taskmanager.viewmodel.taskcreation;

import com.example.taskmanager.model.taskdata.CheckListTaskGroup;
import com.example.taskmanager.model.taskdata.TaskStatus;

import java.util.Date;

public class CheckListTaskGroupCreator implements TaskCreator{
    @Override
    public CheckListTaskGroup createTask() {
        return new CheckListTaskGroup();
    }

    public CheckListTaskGroup createTask(String title, TaskStatus status, Date deadline){
        return new CheckListTaskGroup(title, status, deadline);
    }
}
