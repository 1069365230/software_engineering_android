package com.example.taskmanager.model.taskdata;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.taskmanager.model.customiterators.CheckListTaskGroupIterable;
import com.example.taskmanager.model.customiterators.CheckListTaskGroupIterator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "group_checklist_task_table")
public class CheckListTaskGroup extends CheckListComponent implements CheckListTaskGroupIterable {
    //id (group), checklist_id ref checklist_table.id
    private List<CheckListComponent> subCheckListTasks = new ArrayList<CheckListComponent>();

    @Ignore
    public CheckListTaskGroup(){}

    public CheckListTaskGroup(String title, TaskStatus status, Date deadline) {
        this.setTitle(title);
        this.setStatus(status);
        this.setDeadline(deadline);
    }

    @Override
    public void addCheckListComponent(CheckListComponent checkListComponent){
        //checklist could be casted into checklistcomponent
        this.subCheckListTasks.add(checkListComponent);
    }

    @Override
    public void removeCheckListComponent(CheckListComponent checkListComponent){
        this.subCheckListTasks.remove(checkListComponent);
    }
    public void setSubCheckListTasks(List<CheckListComponent> checkListTaskGroups){
        this.subCheckListTasks = checkListTaskGroups;
    }

    public List<CheckListComponent> getSubCheckListTasks() {
        return subCheckListTasks;
    }

    @Override
    public CheckListTaskGroupIterator createIterator() {
        return new CheckListTaskGroupIterator(this);
    }

    //TODO need to be tested
    public void updateAllSubStatus(TaskStatus status){
        CheckListTaskGroupIterator iterator = this.createIterator();

        while(iterator.hasNext()){
            iterator.getNext().setStatus(status);
        }
    }

}
