package com.example.taskmanager.viewmodel;

import com.example.taskmanager.model.database.CheckListTaskGroupDao;
import com.example.taskmanager.model.taskdata.AppointmentTask;
import com.example.taskmanager.model.database.DataBase;
import com.example.taskmanager.model.taskdata.CheckListTask;
import com.example.taskmanager.model.taskdata.CheckListTaskGroup;
import com.example.taskmanager.model.taskdata.Task;

import java.util.List;

import androidx.lifecycle.LiveData;

public class DataBaseControl {

    //to determine to use the real database or the proxy
    //to handle all the data from different tables
    //include but not limited to: concat all the data from different tables
    private DataBase dataBase;

    public DataBaseControl(){

    }

    public void setDataBase(DataBase dataBase){
        this.dataBase = dataBase;
    }
    public boolean isDataBaseSet(){
        if(this.dataBase == null){
            return false;
        }
        return true;
    }

    public void insert(Task task){
        this.dataBase.insert(task);
    }
    public void insert(AppointmentTask appointmentTask){
        this.dataBase.insert(appointmentTask);
    }
    public void insert(CheckListTask checkListTask){
        this.dataBase.insert(checkListTask);
    }
    public void insert(CheckListTaskGroup checkListTaskGroup){
        this.dataBase.insert(checkListTaskGroup);
    }

    public void update(Task task){
        this.dataBase.update(task);
    }
    public void update(AppointmentTask appointmentTask){
        this.dataBase.update(appointmentTask);
    }
    public void update(CheckListTask checkListTask){
        this.dataBase.update(checkListTask);
    }
    public void update(CheckListTaskGroup checkListTaskGroup){
        this.dataBase.update(checkListTaskGroup);
    }

    public void delete(Task task){
        this.dataBase.delete(task);
    }
    public void delete(AppointmentTask appointmentTask){
        this.dataBase.delete(appointmentTask);
    }
    public void delete(CheckListTask checkListTask){
        this.dataBase.delete(checkListTask);
    }
    public void delete(CheckListTaskGroup checkListTaskGroup){
        this.dataBase.delete(checkListTaskGroup);
    }

    public void updateAllStatus(String status){
        this.dataBase.updateAppointmentTableStatus(status);
        this.dataBase.updateCheckListTableStatus(status);
        this.dataBase.updateCheckListGroupTableStatus(status);
    }

    public LiveData<List<Task>> getAllTasks(){
        return this.dataBase.getAllTasks();
    }
    public LiveData<List<AppointmentTask>> getAllAppointmentTasks(){
        return this.dataBase.getAllAppointmentTasks();
    }
    public LiveData<List<CheckListTask>> getAllCheckListTasks(){
        return this.dataBase.getAllCheckListTasks();
    }
    public LiveData<List<CheckListTaskGroup>> getAllCheckListTaskGroups(){
        return this.dataBase.getAllCheckListTaskGroups();
    }
}