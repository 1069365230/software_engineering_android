package com.example.taskmanager.viewmodel.taskmodification;

import com.example.taskmanager.model.database.DataBase;
import com.example.taskmanager.model.taskdata.Task;

public interface UpdateDeleteStrategy {

    public void deleteTask(Task task);

    public Task updateTask(Task task);
}