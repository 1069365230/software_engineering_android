package com.example.taskmanager.model.taskdata;

import java.util.Date;
import java.util.Objects;

import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "appointment_task_table")
public class AppointmentTask extends Task{

    private String location;

    @Ignore
    public AppointmentTask(){}

    public AppointmentTask(String title, TaskStatus status, Date deadline, String location) {
        this.setTitle(title);
        this.setStatus(status);
        this.setDeadline(deadline);
        this.location = location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AppointmentTask that = (AppointmentTask) o;
        return Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), location);
    }
}
