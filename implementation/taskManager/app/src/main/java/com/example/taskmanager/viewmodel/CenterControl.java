package com.example.taskmanager.viewmodel;

import android.content.Intent;
import android.util.Log;

import com.example.taskmanager.model.customobservers.CustomObserver;
import com.example.taskmanager.model.database.Converter;
import com.example.taskmanager.model.taskdata.AppointmentTask;
import com.example.taskmanager.model.database.DataBase;
import com.example.taskmanager.model.taskdata.CheckListComponent;
import com.example.taskmanager.model.taskdata.CheckListPriority;
import com.example.taskmanager.model.taskdata.CheckListTask;
import com.example.taskmanager.model.taskdata.CheckListTaskGroup;
import com.example.taskmanager.model.taskdata.Task;
import com.example.taskmanager.model.taskdata.TaskStatus;
import com.example.taskmanager.view.IntentKeys;
import com.example.taskmanager.view.UI;
import com.example.taskmanager.viewmodel.taskcreation.TaskFactory;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;

public class CenterControl implements CustomObserver {

    private DataBaseControl dataBaseControl;
    private TaskFactory taskFactory;
    private UI ui;

    public CenterControl(UI ui) {
        this.dataBaseControl = new DataBaseControl();
        this.taskFactory = new TaskFactory();
        this.ui = ui;
    }

    //TODO Toast can be changed by notification strategy later
    //TODO put similar code in one base method
    public void addTask(String taskType, Intent intent){

        switch(taskType){
            case "APPOINTMENT":
                insertTask(createAppointmentFromIntent(intent));
                break;

            case "CHECKLIST":
                insertTask(createCheckListFromIntent(intent));
                break;

            case "CHECKLISTGROUP":
                insertTask(createGroupFromIntent(intent));
                break;

            default:
                displayMessageOnScreen("other task not available yet");
                Log.d("task type required", taskType);

        }
        //TODO notification strategy here maybe
        displayMessageOnScreen(taskType +" added");
    }

    //TODO Toast can be changed by notification strategy later
    public void updateTask(String taskType, Intent intent){
        int id = intent.getIntExtra(IntentKeys.EXTRA_ID, IntentKeys.invalidID);
        if(id == IntentKeys.invalidID){
            //invalid id exception (own exception)
            displayMessageOnScreen("task can not be found");
            Log.e("Primary key issue", "ID: " + id + " can not be found in the table");
            return;
        }

        switch(taskType){
            case "APPOINTMENT":
                AppointmentTask appointmentTask = createAppointmentFromIntent(intent);
                appointmentTask.setId(id);
                updateTask(appointmentTask);
                displayMessageOnScreen("Task updated");
                break;

            case "CHECKLIST":
                CheckListTask checkListTask =  createCheckListFromIntent(intent);
                checkListTask.setId(id);
                updateTask(checkListTask);
                displayMessageOnScreen("Task updated");
                break;

            case "CHECKLISTGROUP":
                List<CheckListComponent> subCheckListTasks = Converter.toCheckListComponentList(intent.getStringExtra(IntentKeys.EXTRA_SUBTASKS));
                CheckListTaskGroup checkListTaskGroup = createGroupFromIntent(intent);
                if(subCheckListTasks != null){
                    Log.e("subchecklist", "is not null");
                    checkListTaskGroup.setSubCheckListTasks(subCheckListTasks);
                }

                checkListTaskGroup.setId(id);
                updateTask(checkListTaskGroup);
                displayMessageOnScreen("Group updated");
                break;

            default:
                displayMessageOnScreen("task type not supported");
                Log.d("task type required", taskType);
                return;
        }
    }

    public void addChild(Intent intent){
        int id = intent.getIntExtra(IntentKeys.EXTRA_ID, IntentKeys.invalidID);

        if(id == IntentKeys.invalidID){
            //invalid id exception (own exception)
            displayMessageOnScreen("task can not be found");
            Log.e("Primary key issue", "ID: " + id + " can not be found in the table");
            return;
        }

        List<CheckListComponent> updatedSubTasks = updateSubTasksFromParent(intent);

        String groupTitle = intent.getStringExtra(IntentKeys.EXTRA_TITLE);
        TaskStatus groupStatus = (TaskStatus) intent.getSerializableExtra(IntentKeys.EXTRA_STATUS);
        String groupDate = intent.getStringExtra(IntentKeys.EXTRA_DATE);
        CheckListTaskGroup checkListTaskGroup = createTask(groupTitle, groupStatus, new Date(groupDate));

        checkListTaskGroup.setSubCheckListTasks(updatedSubTasks);
        checkListTaskGroup.setId(id);

        updateTask(checkListTaskGroup);
        displayMessageOnScreen("sub task inserted");
    }

    public void updateChild(Intent intent){
        int id = intent.getIntExtra(IntentKeys.EXTRA_ID, IntentKeys.invalidID);

        if(id == IntentKeys.invalidID){
            //invalid id exception (own exception)
            displayMessageOnScreen("task can not be found");
            Log.e("Primary key issue", "ID: " + id + " can not be found in the table");
            return;
        }

        List<CheckListComponent> updatedSubTasks = updateSubTasksFromChild(intent);

        String groupTitle = intent.getStringExtra(IntentKeys.EXTRA_GROUP_TITLE);
        String groupStatus = intent.getStringExtra(IntentKeys.EXTRA_GROUP_STATUS);
        Log.e("group status type", groupStatus);
        String groupDate = intent.getStringExtra(IntentKeys.EXTRA_GROUP_DATE);
        CheckListTaskGroup checkListTaskGroup = createTask(groupTitle, TaskStatus.valueOf(groupStatus), new Date(groupDate));

        checkListTaskGroup.setSubCheckListTasks(updatedSubTasks);
        checkListTaskGroup.setId(id);

        updateTask(checkListTaskGroup);
        displayMessageOnScreen("sub task updated");
    }

    public void deleteChild(Intent intent){
        int id = intent.getIntExtra(IntentKeys.EXTRA_ID, IntentKeys.invalidID);

        if(id == IntentKeys.invalidID){
            //invalid id exception (own exception)
            displayMessageOnScreen("task can not be found");
            Log.e("Primary key issue", "ID: " + id + " can not be found in the table");
            return;
        }

        List<CheckListComponent> updatedSubTasks = deleteSubTasksFromParent(intent);

        String groupTitle = intent.getStringExtra(IntentKeys.EXTRA_GROUP_TITLE);
        String groupStatus = intent.getStringExtra(IntentKeys.EXTRA_GROUP_STATUS);
        Log.e("group status type", groupStatus);
        String groupDate = intent.getStringExtra(IntentKeys.EXTRA_GROUP_DATE);
        CheckListTaskGroup checkListTaskGroup = createTask(groupTitle, TaskStatus.valueOf(groupStatus), new Date(groupDate));

        checkListTaskGroup.setSubCheckListTasks(updatedSubTasks);
        checkListTaskGroup.setId(id);

        updateTask(checkListTaskGroup);
        displayMessageOnScreen("sub task deleted");
    }

    private AppointmentTask createAppointmentFromIntent(Intent intent){
        String title = intent.getStringExtra(IntentKeys.EXTRA_TITLE);
        TaskStatus status = (TaskStatus) intent.getSerializableExtra(IntentKeys.EXTRA_STATUS);
        String date = intent.getStringExtra(IntentKeys.EXTRA_DATE);

        String location = intent.getStringExtra(IntentKeys.EXTRA_LOCATION);
        return createTask(title, status, new Date(date), location);
    }

    private CheckListTask createCheckListFromIntent(Intent intent){
        String title = intent.getStringExtra(IntentKeys.EXTRA_TITLE);
        TaskStatus status = (TaskStatus) intent.getSerializableExtra(IntentKeys.EXTRA_STATUS);
        String date = intent.getStringExtra(IntentKeys.EXTRA_DATE);

        CheckListPriority priority = CheckListPriority.valueOf(intent.getStringExtra(IntentKeys.EXTRA_PRIORITY));
        return createTask(title, status, new Date(date), priority);
    }

    private CheckListTaskGroup createGroupFromIntent(Intent intent){
        String title = intent.getStringExtra(IntentKeys.EXTRA_TITLE);
        TaskStatus status = (TaskStatus) intent.getSerializableExtra(IntentKeys.EXTRA_STATUS);
        String date = intent.getStringExtra(IntentKeys.EXTRA_DATE);

        return createTask(title, status, new Date(date));
    }

    private List<CheckListComponent> updateSubTasksFromParent(Intent intent){
        String title = intent.getStringExtra(IntentKeys.EXTRA_CHILD_TITLE);
        TaskStatus status = (TaskStatus) intent.getSerializableExtra(IntentKeys.EXTRA_CHILD_STATUS);
        String date = intent.getStringExtra(IntentKeys.EXTRA_CHILD_DATE);
        CheckListPriority priority = CheckListPriority.valueOf(intent.getStringExtra(IntentKeys.EXTRA_CHILD_PRIORITY));
        CheckListTask checkListTask = createTask(title, status, new Date(date), priority);

        String subTasksJson = intent.getStringExtra(IntentKeys.EXTRA_SUBTASKS);

        List<CheckListComponent> updatedSubTasks = Converter.toCheckListComponentList(subTasksJson);
        updatedSubTasks.add(checkListTask);
        return updatedSubTasks;
    }

    private List<CheckListComponent> updateSubTasksFromChild(Intent intent){
        String title = intent.getStringExtra(IntentKeys.EXTRA_TITLE);
        TaskStatus status = (TaskStatus) intent.getSerializableExtra(IntentKeys.EXTRA_STATUS);
        String date = intent.getStringExtra(IntentKeys.EXTRA_DATE);
        CheckListPriority priority = CheckListPriority.valueOf(intent.getStringExtra(IntentKeys.EXTRA_PRIORITY));
        CheckListTask checkListTask = createTask(title, status, new Date(date), priority);
        int childPosition = intent.getIntExtra(IntentKeys.EXTRA_CHILD_POSITION, IntentKeys.invalidID);
        String subTasksJson = intent.getStringExtra(IntentKeys.EXTRA_SUBTASKS);

        List<CheckListComponent> updatedSubTasks = Converter.toCheckListComponentList(subTasksJson);
        updatedSubTasks.set(childPosition, checkListTask);
        return updatedSubTasks;
    }

    private List<CheckListComponent> deleteSubTasksFromParent(Intent intent){
        String subTasksJson = intent.getStringExtra(IntentKeys.EXTRA_SUBTASKS);

        int childPosition = intent.getIntExtra(IntentKeys.EXTRA_CHILD_POSITION, IntentKeys.invalidID);
        List<CheckListComponent> remainingSubTasks = Converter.toCheckListComponentList(subTasksJson);
        remainingSubTasks.remove(childPosition);
        return remainingSubTasks;
    }

    public void displayMessageOnScreen(String message){
        ui.createMessage(message);
    }

    public AppointmentTask createTask(String title, TaskStatus status, Date deadline, String location){
        return this.taskFactory.produceTask(title, status, deadline, location);
    }

    public CheckListTask createTask(String title, TaskStatus status, Date deadline, CheckListPriority priority){
        return this.taskFactory.produceTask(title, status, deadline, priority);
    }

    public CheckListTaskGroup createTask(String title, TaskStatus status, Date deadline){
        return this.taskFactory.produceTask(title, status, deadline);
    }

    public void setDataBase(DataBase dataBase){
        this.dataBaseControl.setDataBase(dataBase);
    }

    public boolean isDataBaseSet(){
        return this.dataBaseControl.isDataBaseSet();
    }

    public void insertTask(Task task){
        this.dataBaseControl.insert(task);
    }

    public void insertTask(AppointmentTask appointmentTask){
        this.dataBaseControl.insert(appointmentTask);
    }

    public void insertTask(CheckListTask checkListTask){
        this.dataBaseControl.insert(checkListTask);
    }

    public void insertTask(CheckListTaskGroup checkListTaskGroup){
        this.dataBaseControl.insert(checkListTaskGroup);
    }

    public void updateTask(Task task){
        this.dataBaseControl.update(task);
    }

    public void updateTask(AppointmentTask appointmentTask){
        this.dataBaseControl.update(appointmentTask);
    }

    public void updateTask(CheckListTask checkListTask){
        this.dataBaseControl.update(checkListTask);
    }

    public void updateTask(CheckListTaskGroup checkListTaskGroup){
        this.dataBaseControl.update(checkListTaskGroup);
    }

    public void deleteTask(Task task){
        this.dataBaseControl.delete(task);
    }

    //TODO make a big method for all those three, one toast message for all of them
    public void deleteTask(AppointmentTask appointmentTask){
        this.dataBaseControl.delete(appointmentTask);
        displayMessageOnScreen("Task appointment deleted");
    }

    public void deleteTask(CheckListTask checkListTask){
        this.dataBaseControl.delete(checkListTask);
        displayMessageOnScreen("Task checklist deleted");
    }

    public void deleteTask(CheckListTaskGroup checkListTaskGroup){
        this.dataBaseControl.delete(checkListTaskGroup);
        displayMessageOnScreen("group deleted");
    }

    public LiveData<List<Task>>getAllTasks(){
        return this.dataBaseControl.getAllTasks();
    }

    public LiveData<List<AppointmentTask>>getAllAppointmentTasks(){
        return this.dataBaseControl.getAllAppointmentTasks();
    }

    public LiveData<List<CheckListTask>>getAllCheckListTasks(){
        return this.dataBaseControl.getAllCheckListTasks();
    }

    public LiveData<List<CheckListTaskGroup>>getAllCheckListTaskGroups(){
        return this.dataBaseControl.getAllCheckListTaskGroups();
    }

    public void updateAllStatus(String taskStatus){
        this.dataBaseControl.updateAllStatus(taskStatus);
    }

    @Override
    public void update() {
        //TODO inform UI the upcoming appointment task
    }



    //TODO two observers maybe? One for upcoming appointment, the other for updating tasks
    @Override
    public void update(Task task, String type) {
        //do nothing
    }

    @Override
    public void update(int CheckListGroupAdapterPosition) {
        //do nothing
    }

    @Override
    public void update(Task task, List<CheckListTask> children, CheckListTaskGroup parent, int childPosition) {
        //do nothing
    }

}
