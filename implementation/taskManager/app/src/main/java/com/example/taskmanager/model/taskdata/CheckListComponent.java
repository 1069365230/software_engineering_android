package com.example.taskmanager.model.taskdata;

import java.util.ArrayList;
import java.util.List;

public class CheckListComponent extends Task{
    //TODO think if this should have "private boolean check" here instead of only in checkListTask,
    // other words should a group have a "check box too"

    public void addCheckListComponent(CheckListComponent checkListComponent){
        //TODO create custom exception class
        //throw custom exception
    }

    public void removeCheckListComponent(CheckListComponent checkListComponent){
        //TODO create custom exception class
        //throw custom exception
    }


    public boolean hasSubTask(){
        return false;
    }

}
