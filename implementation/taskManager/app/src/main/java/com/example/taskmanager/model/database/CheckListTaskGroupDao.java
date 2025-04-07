package com.example.taskmanager.model.database;

import com.example.taskmanager.model.taskdata.CheckListComponent;
import com.example.taskmanager.model.taskdata.CheckListTask;
import com.example.taskmanager.model.taskdata.CheckListTaskGroup;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface CheckListTaskGroupDao {

    @Insert(onConflict = REPLACE)
    void insert(CheckListTaskGroup checkListTaskGroup);

    @Update(onConflict = REPLACE)
    void update(CheckListTaskGroup checkListTaskGroup);

    @Delete
    void delete(CheckListTaskGroup checkListTaskGroup);

    @Query("DELETE FROM group_checklist_task_table")
    void deleteAllTasks();

    @Query("SELECT * FROM group_checklist_task_table")
    LiveData<List<CheckListTaskGroup>> getAllTasks();

    @Query("UPDATE group_checklist_task_table SET status =:status")
    void updateStatus(String status);
}
