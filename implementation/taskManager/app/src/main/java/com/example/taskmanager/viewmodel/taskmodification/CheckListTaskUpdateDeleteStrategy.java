package com.example.taskmanager.viewmodel.taskmodification;

import com.example.taskmanager.model.database.DataBase;
import com.example.taskmanager.model.taskdata.CheckListTask;
import com.example.taskmanager.model.taskdata.Task;

import java.util.List;

public class CheckListTaskUpdateDeleteStrategy implements UpdateDeleteStrategy {

    private DataBase dataBase;

    public CheckListTaskUpdateDeleteStrategy(DataBase dataBase) {
        this.dataBase = dataBase;
    }

    @Override
    public void deleteTask(Task task) {
        dataBase.delete(task);
    }

    @Override
    public Task updateTask(Task task) {

        dataBase.update(task);

        List<Task> temp = dataBase.getAllTasks().getValue();
        for (Task t : temp) {
            if(t.getId() == task.getId()){
                return t;
            }
        }
        throw new IndexOutOfBoundsException("Task doesn't exist!");
    }
}