package com.example.taskmanager.model.database;

import com.example.taskmanager.model.taskdata.CheckListTask;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface CheckListTaskDao {
    @Insert
    void insert(CheckListTask checkListTask);

    @Update
    void update(CheckListTask checkListTask);

    @Delete
    void delete(CheckListTask checkListTask);

    @Query("DELETE FROM checklist_task_table")
    void deleteAllTasks();

    @Query("SELECT * FROM checklist_task_table")
    LiveData<List<CheckListTask>> getAllTasks();

    @Query("UPDATE checklist_task_table SET status =:status")
    void updateStatus(String status);

}
