package com.example.taskmanager.model.customobservers;

import com.example.taskmanager.model.taskdata.AppointmentTask;
import com.example.taskmanager.model.taskdata.CheckListTask;
import com.example.taskmanager.model.taskdata.CheckListTaskGroup;
import com.example.taskmanager.model.taskdata.Task;

import java.util.List;

public interface CustomObservable {
    public void addObserver(CustomObserver customObserver);
    public void removeObserver(CustomObserver customObserver);
    public void notifyObserver();
    public void notifyObserver(Task task);
    public void notifyObserver(int adapterPosition);
    public void notifyObserver(Task task, List<CheckListTask> children, CheckListTaskGroup parent, int childPosition);

}
