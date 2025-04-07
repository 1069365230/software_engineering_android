package com.example.taskmanager.viewmodel.taskcreation;

import com.example.taskmanager.model.taskdata.AppointmentTask;
import com.example.taskmanager.model.taskdata.TaskStatus;

import java.util.Date;

public class AppointmentTaskCreator implements TaskCreator{

    @Override
    public AppointmentTask createTask() {
        return new AppointmentTask();
    }

    public AppointmentTask createTask(String title, TaskStatus status, Date deadline, String location) {
        return new AppointmentTask(title, status, deadline, location);
    }
}
