package com.example.taskmanager.model.database;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.example.taskmanager.model.taskdata.AppointmentTask;
import com.example.taskmanager.model.taskdata.CheckListTask;
import com.example.taskmanager.model.taskdata.CheckListTaskGroup;
import com.example.taskmanager.model.taskdata.Task;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class TaskDataBaseReal extends AndroidViewModel implements DataBase{

    private TaskDao taskDao;
    private AppointmentTaskDao appointmentTaskDao;
    private CheckListTaskDao checkListTaskDao;
    private CheckListTaskGroupDao checkListTaskGroupDao;

    public TaskDataBaseReal(@NonNull Application application){
        super(application);
        TaskDatabaseEntity db = TaskDatabaseEntity.getInstance(application);
        this.taskDao = db.taskDao();
        this.appointmentTaskDao = db.appointmentTaskDao();
        this.checkListTaskDao = db.checkListTaskDao();
        this.checkListTaskGroupDao = db.checkListTaskGroupDao();
    }

    @Override
    public void insert(Task task){
        new TaskDataBaseReal.InsertTaskAsync(this.taskDao).execute(task);
    }

    @Override
    public void insert(AppointmentTask appointmentTask){
        new TaskDataBaseReal.InsertAppointmentTaskAsync(this.appointmentTaskDao).execute(appointmentTask);
    }

    @Override
    public void insert(CheckListTask checkListTask){
        new TaskDataBaseReal.InsertCheckListTaskAsync(this.checkListTaskDao).execute(checkListTask);
    }

    @Override
    public void insert(CheckListTaskGroup checkListTaskGroup){
        new TaskDataBaseReal.InsertCheckListTaskGroupAsync(this.checkListTaskGroupDao).execute(checkListTaskGroup);
    }

    @Override
    public void update(Task task){
        Log.d("TODO in real dbs", task.getTitle());
        //TODO AsyncTask for update
    }

    @Override
    public void update(AppointmentTask appointmentTask){
        new TaskDataBaseReal.UpdateAppointmentTaskAsync(this.appointmentTaskDao).execute(appointmentTask);
    }

    @Override
    public void update(CheckListTask checkListTask){
        new TaskDataBaseReal.UpdateCheckListTaskAsync(this.checkListTaskDao).execute(checkListTask);
    }

    @Override
    public void update(CheckListTaskGroup checkListTaskGroup){
        new TaskDataBaseReal.UpdateCheckListTaskGroupAsync(this.checkListTaskGroupDao).execute(checkListTaskGroup);
    }


    @Override
    public void delete(Task task){
        Log.d("TODO in real dbs", task.getTitle());
        new TaskDataBaseReal.DeleteTaskAsync(this.taskDao).execute(task);
    }

    @Override
    public void delete(AppointmentTask appointmentTask){
        new TaskDataBaseReal.DeleteAppointmentTaskAsync(this.appointmentTaskDao).execute(appointmentTask);
    }

    @Override
    public void delete(CheckListTask checkListTask){
        new TaskDataBaseReal.DeleteCheckListTaskAsync(this.checkListTaskDao).execute(checkListTask);
    }

    @Override
    public void delete(CheckListTaskGroup checkListTaskGroup){
        new TaskDataBaseReal.DeleteCheckListTaskGroupAsync(this.checkListTaskGroupDao).execute(checkListTaskGroup);
    }

    @Override
    public void updateAppointmentTableStatus(String status) {
        new TaskDataBaseReal.UpdateAppointmentStatusAsync(this.appointmentTaskDao).execute(status);
    }

    @Override
    public void updateCheckListTableStatus(String status) {
        new TaskDataBaseReal.UpdateCheckListStatusAsync(this.checkListTaskDao).execute(status);

    }

    @Override
    public void updateCheckListGroupTableStatus(String status) {
        new TaskDataBaseReal.UpdateCheckListGroupStatusAsync(this.checkListTaskGroupDao).execute(status);
    }

    public LiveData<List<Task>> getAllTasks(){
        return this.taskDao.getAllTasks();
    }

    public LiveData<List<AppointmentTask>> getAllAppointmentTasks() {
        return this.appointmentTaskDao.getAllTasks();
    }

    public LiveData<List<CheckListTask>> getAllCheckListTasks() {
        return this.checkListTaskDao.getAllTasks();
    }

    public LiveData<List<CheckListTaskGroup>> getAllCheckListTaskGroups() {
        return this.checkListTaskGroupDao.getAllTasks();
    }

    //Async task for General task
    private static class InsertTaskAsync extends AsyncTask<Task, Void, Void> {
        private TaskDao taskDao;
        private InsertTaskAsync(TaskDao taskDao){
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.insert(tasks[0]);
            return null;
        }

    }
    private static class UpdateTaskAsync extends AsyncTask<Task, Void, Void> {
        private TaskDao taskDao;
        private UpdateTaskAsync(TaskDao taskDao){
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.update(tasks[0]);
            return null;
        }

    }
    private static class DeleteTaskAsync extends AsyncTask<Task, Void, Void> {
        private TaskDao taskDao;
        private DeleteTaskAsync(TaskDao taskDao){
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.delete(tasks[0]);
            return null;
        }

    }

    //Async task for Appointment task
    private static class InsertAppointmentTaskAsync extends AsyncTask<AppointmentTask, Void, Void> {
        private AppointmentTaskDao appointmentTaskDao;
        private InsertAppointmentTaskAsync(AppointmentTaskDao appointmentTaskDao){
            this.appointmentTaskDao = appointmentTaskDao;
        }

        @Override
        protected Void doInBackground(AppointmentTask... appointmentTasks) {
            appointmentTaskDao.insert(appointmentTasks[0]);
            return null;
        }

    }

    private static class UpdateAppointmentTaskAsync extends AsyncTask<AppointmentTask, Void, Void> {
        private AppointmentTaskDao appointmentTaskDao;
        private UpdateAppointmentTaskAsync(AppointmentTaskDao appointmentTaskDao){
            this.appointmentTaskDao = appointmentTaskDao;
        }

        @Override
        protected Void doInBackground(AppointmentTask... appointmentTasks) {
            appointmentTaskDao.update(appointmentTasks[0]);
            return null;
        }

    }

    private static class DeleteAppointmentTaskAsync extends AsyncTask<AppointmentTask, Void, Void> {
        private AppointmentTaskDao appointmentTaskDao;
        private DeleteAppointmentTaskAsync(AppointmentTaskDao appointmentTaskDao){
            this.appointmentTaskDao = appointmentTaskDao;
        }

        @Override
        protected Void doInBackground(AppointmentTask... appointmentTasks) {
            appointmentTaskDao.delete(appointmentTasks[0]);
            return null;
        }

    }

    private static class UpdateAppointmentStatusAsync extends AsyncTask<String, Void, Void> {
        private AppointmentTaskDao appointmentTaskDao;
        private UpdateAppointmentStatusAsync(AppointmentTaskDao appointmentTaskDao){
            this.appointmentTaskDao = appointmentTaskDao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            appointmentTaskDao.updateStatus(strings[0]);
            return null;
        }
    }

    //
    //Async task for checklist task
    private static class InsertCheckListTaskAsync extends AsyncTask<CheckListTask, Void, Void> {
        private CheckListTaskDao checkListTaskDao;
        private InsertCheckListTaskAsync(CheckListTaskDao checkListTaskDao){
            this.checkListTaskDao = checkListTaskDao;
        }

        @Override
        protected Void doInBackground(CheckListTask... checkListTasks) {
            checkListTaskDao.insert(checkListTasks[0]);
            return null;
        }

    }

    private static class UpdateCheckListTaskAsync extends AsyncTask<CheckListTask, Void, Void> {
        private CheckListTaskDao checkListTaskDao;
        private UpdateCheckListTaskAsync(CheckListTaskDao checkListTaskDao){
            this.checkListTaskDao = checkListTaskDao;
        }

        @Override
        protected Void doInBackground(CheckListTask... checkListTasks) {
            checkListTaskDao.update(checkListTasks[0]);
            return null;
        }

    }

    private static class DeleteCheckListTaskAsync extends AsyncTask<CheckListTask, Void, Void> {
        private CheckListTaskDao checkListTaskDao;
        private DeleteCheckListTaskAsync(CheckListTaskDao checkListTaskDao){
            this.checkListTaskDao = checkListTaskDao;
        }

        @Override
        protected Void doInBackground(CheckListTask... checkListTasks) {
            checkListTaskDao.delete(checkListTasks[0]);
            return null;
        }

    }

    private static class UpdateCheckListStatusAsync extends AsyncTask<String, Void, Void> {
        private CheckListTaskDao checkListTaskDao;
        private UpdateCheckListStatusAsync(CheckListTaskDao checkListTaskDao){
            this.checkListTaskDao = checkListTaskDao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            checkListTaskDao.updateStatus(strings[0]);
            return null;
        }
    }

    //Async task for checklist task group
    private static class InsertCheckListTaskGroupAsync extends AsyncTask<CheckListTaskGroup, Void, Void> {
        private CheckListTaskGroupDao checkListTaskGroupDao;
        private InsertCheckListTaskGroupAsync(CheckListTaskGroupDao checkListTaskGroupDao){
            this.checkListTaskGroupDao = checkListTaskGroupDao;
        }

        @Override
        protected Void doInBackground(CheckListTaskGroup... checkListTaskGroups) {
            checkListTaskGroupDao.insert(checkListTaskGroups[0]);
            return null;
        }
    }

    private static class UpdateCheckListTaskGroupAsync extends AsyncTask<CheckListTaskGroup, Void, Void> {
        private CheckListTaskGroupDao checkListTaskGroupDao;
        private UpdateCheckListTaskGroupAsync(CheckListTaskGroupDao checkListTaskGroupDao){
            this.checkListTaskGroupDao = checkListTaskGroupDao;
        }

        @Override
        protected Void doInBackground(CheckListTaskGroup... checkListTaskGroups) {
            checkListTaskGroupDao.update(checkListTaskGroups[0]);
            return null;
        }

    }

    private static class DeleteCheckListTaskGroupAsync extends AsyncTask<CheckListTaskGroup, Void, Void> {
        private CheckListTaskGroupDao checkListTaskGroupDao;
        private DeleteCheckListTaskGroupAsync(CheckListTaskGroupDao checkListTaskGroupDao){
            this.checkListTaskGroupDao = checkListTaskGroupDao;
        }

        @Override
        protected Void doInBackground(CheckListTaskGroup... checkListTaskGroups) {
            checkListTaskGroupDao.delete(checkListTaskGroups[0]);
            return null;
        }

    }

    private static class UpdateCheckListGroupStatusAsync extends AsyncTask<String, Void, Void> {
        private CheckListTaskGroupDao checkListTaskGroupDao;
        private UpdateCheckListGroupStatusAsync(CheckListTaskGroupDao checkListTaskGroupDao){
            this.checkListTaskGroupDao = checkListTaskGroupDao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            checkListTaskGroupDao.updateStatus(strings[0]);
            return null;
        }
    }

}
