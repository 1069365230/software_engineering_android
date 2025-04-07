package com.example.taskmanager.model.database;

import com.example.taskmanager.model.taskdata.AppointmentTask;
import com.example.taskmanager.model.taskdata.CheckListTask;
import com.example.taskmanager.model.taskdata.CheckListTaskGroup;
import com.example.taskmanager.model.taskdata.Task;

import java.util.List;

import androidx.lifecycle.LiveData;

public interface DataBase {
    public void insert(Task task);
    public void insert(AppointmentTask appointmentTask);
    public void insert(CheckListTask checkListTask);
    public void insert(CheckListTaskGroup checkListTaskGroup);

    public void update(Task task);
    public void update(AppointmentTask appointmentTask);
    public void update(CheckListTask checkListTask);
    public void update(CheckListTaskGroup checkListTaskGroup);

    public void delete(Task task);
    public void delete(AppointmentTask appointmentTask);
    public void delete(CheckListTask checkListTask);
    public void delete(CheckListTaskGroup checkListTaskGroup);

    public void updateAppointmentTableStatus(String status);
    public void updateCheckListTableStatus(String status);
    public void updateCheckListGroupTableStatus(String status);

    public LiveData<List<Task>> getAllTasks();
    public LiveData<List<AppointmentTask>> getAllAppointmentTasks();
    public LiveData<List<CheckListTask>> getAllCheckListTasks();
    public LiveData<List<CheckListTaskGroup>> getAllCheckListTaskGroups();

}
