package com.example.taskmanager.model.database;

import com.example.taskmanager.model.taskdata.AppointmentTask;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface AppointmentTaskDao {
    @Insert
    void insert(AppointmentTask appointmentTask);

    @Update
    void update(AppointmentTask appointmentTask);

    @Delete
    void delete(AppointmentTask appointmentTask);

    @Query("DELETE FROM appointment_task_table")
    void deleteAllTasks();

    @Query("SELECT * FROM appointment_task_table")
    LiveData<List<AppointmentTask>> getAllTasks();

    @Query("UPDATE appointment_task_table SET status =:status")
    void updateStatus(String status);
}
