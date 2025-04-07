package com.example.taskmanager.viewmodel.taskcreation;

import com.example.taskmanager.model.taskdata.CheckListPriority;
import com.example.taskmanager.model.taskdata.CheckListTask;
import com.example.taskmanager.model.taskdata.TaskStatus;

import java.util.Date;

public class CheckListTaskCreator implements TaskCreator{
    @Override
    public CheckListTask createTask() {
        return new CheckListTask();
    }

    public CheckListTask createTask(String title, TaskStatus status, Date deadline, CheckListPriority priority){
        return new CheckListTask(title, status, deadline, priority);
    }
}
