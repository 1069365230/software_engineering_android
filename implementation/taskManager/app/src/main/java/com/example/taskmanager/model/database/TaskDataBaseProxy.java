package com.example.taskmanager.model.database;

import android.app.Application;

import com.example.taskmanager.model.taskdata.AppointmentTask;
import com.example.taskmanager.model.taskdata.CheckListTask;
import com.example.taskmanager.model.taskdata.CheckListTaskGroup;
import com.example.taskmanager.model.taskdata.Task;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class TaskDataBaseProxy extends AndroidViewModel implements DataBase{

    private DataBase dataBase;
    private LiveData<List<Task>> allTasks;
    private LiveData<List<AppointmentTask>> allAppointmentTasks;
    private LiveData<List<CheckListTask>> allCheckListTasks;
    private LiveData<List<CheckListTaskGroup>> allCheckListGroups;

    public TaskDataBaseProxy(@NonNull Application application){
        super(application);
        this.dataBase = new TaskDataBaseReal(application);
    }

    @Override
    public void insert(Task task) {
        this.dataBase.insert(task);
    }

    @Override
    public void insert(AppointmentTask appointmentTask) {
        this.dataBase.insert(appointmentTask);
    }

    @Override
    public void insert(CheckListTask checkListTask) {
        this.dataBase.insert(checkListTask);
    }

    @Override
    public void insert(CheckListTaskGroup checkListTaskGroup) {
        this.dataBase.insert(checkListTaskGroup);
    }


    @Override
    public void update(Task task) {
        this.dataBase.update(task);
    }

    @Override
    public void update(AppointmentTask appointmentTask) {
        this.dataBase.update(appointmentTask);
    }

    @Override
    public void update(CheckListTask checkListTask) {
        this.dataBase.update(checkListTask);
    }

    @Override
    public void update(CheckListTaskGroup checkListTaskGroup) {
        this.dataBase.insert(checkListTaskGroup);
    }

    @Override
    public void delete(Task task) {
        this.dataBase.delete(task);
    }

    @Override
    public void delete(AppointmentTask appointmentTask) {
        this.dataBase.delete(appointmentTask);
    }

    @Override
    public void delete(CheckListTask checkListTask) {
        this.dataBase.delete(checkListTask);
    }

    @Override
    public void delete(CheckListTaskGroup checkListTaskGroup) {
        this.dataBase.delete(checkListTaskGroup);
    }

    @Override
    public void updateAppointmentTableStatus(String status) {
        this.dataBase.updateAppointmentTableStatus(status);
    }

    @Override
    public void updateCheckListTableStatus(String status) {
        this.dataBase.updateCheckListTableStatus(status);
    }

    @Override
    public void updateCheckListGroupTableStatus(String status) {
        this.dataBase.updateCheckListGroupTableStatus(status);
    }


    @Override
    public LiveData<List<Task>> getAllTasks() {
        if(this.allTasks == null){
            this.allTasks = dataBase.getAllTasks();
        }
        return this.allTasks;
    }

    @Override
    public LiveData<List<AppointmentTask>> getAllAppointmentTasks() {
        if(this.allAppointmentTasks == null){
            this.allAppointmentTasks = dataBase.getAllAppointmentTasks();
        }
        return this.allAppointmentTasks;
    }

    @Override
    public LiveData<List<CheckListTask>> getAllCheckListTasks() {
        if(this.allCheckListTasks == null){
            this.allCheckListTasks = dataBase.getAllCheckListTasks();
        }
        return this.allCheckListTasks;
    }

    @Override
    public LiveData<List<CheckListTaskGroup>> getAllCheckListTaskGroups() {
        if(this.allCheckListGroups == null){
            this.allCheckListGroups = dataBase.getAllCheckListTaskGroups();
        }
        return this.allCheckListGroups;
    }


}
