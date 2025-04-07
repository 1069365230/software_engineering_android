package com.example.taskmanager.viewmodel.taskcreation;

import com.example.taskmanager.model.taskdata.AppointmentTask;
import com.example.taskmanager.model.taskdata.CheckListPriority;
import com.example.taskmanager.model.taskdata.CheckListTask;
import com.example.taskmanager.model.taskdata.CheckListTaskGroup;
import com.example.taskmanager.model.taskdata.Task;
import com.example.taskmanager.model.taskdata.TaskStatus;

import java.util.Date;

public class TaskFactory {

    public AppointmentTask produceTask(String title, TaskStatus status, Date deadline, String location){
        return new AppointmentTaskCreator().createTask(title, status, deadline, location);
    }

    public CheckListTask produceTask(String title, TaskStatus status, Date deadline, CheckListPriority priority){
        return new CheckListTaskCreator().createTask(title, status, deadline, priority);
    }

    public CheckListTaskGroup produceTask(String title, TaskStatus status, Date deadline){
        return new CheckListTaskGroupCreator().createTask(title, status, deadline);
    }


}
