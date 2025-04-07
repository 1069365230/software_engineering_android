package com.example.taskmanager.model.taskdata;

import java.util.Date;
import java.util.Objects;

import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "checklist_task_table")
public class CheckListTask extends CheckListComponent{

    private CheckListPriority priority;

    @Ignore
    public CheckListTask(){}

    public CheckListTask(String title, TaskStatus status, Date deadline, CheckListPriority priority) {
        this.setTitle(title);
        this.setStatus(status);
        this.setDeadline(deadline);
        this.priority = priority;
    }

    public CheckListPriority getPriority() {
        return priority;
    }

    public void setPriority(CheckListPriority priority) {
        this.priority = priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CheckListTask that = (CheckListTask) o;
        return priority == that.priority;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), priority);
    }

    @Override
    public boolean hasSubTask() {
        return false;
    }
}
