package com.example.taskmanager.model.customiterators;

import com.example.taskmanager.model.taskdata.CheckListComponent;
import com.example.taskmanager.model.taskdata.CheckListTaskGroup;

public class CheckListTaskGroupIterator implements MyIterator{
    private int index;
    private CheckListTaskGroup checkListTaskGroup;

    public CheckListTaskGroupIterator(CheckListTaskGroup checkListTaskGroup) {
        this.checkListTaskGroup = checkListTaskGroup;
        this.index = 0;
    }

    @Override
    public boolean hasNext() {
        return index < checkListTaskGroup.getSubCheckListTasks().size();
    }

    @Override
    public CheckListComponent getNext() {
        CheckListComponent checkListComponent = checkListTaskGroup.getSubCheckListTasks().get(index);
        this.index += 1;
        return checkListComponent;
    }

    public void resetIndex(){
        this.index = 0;
    }
}
