package com.example.taskmanager.model.customobservers;

import com.example.taskmanager.model.taskdata.AppointmentTask;
import com.example.taskmanager.model.taskdata.CheckListTask;
import com.example.taskmanager.model.taskdata.CheckListTaskGroup;
import com.example.taskmanager.model.taskdata.Task;

import java.util.List;

public interface CustomObserver {
    public void update();
    public void update(Task task, String type);
    public void update(int CheckListGroupAdapterPosition);
    public void update(Task task, List<CheckListTask> children, CheckListTaskGroup parent, int childPosition);
}
