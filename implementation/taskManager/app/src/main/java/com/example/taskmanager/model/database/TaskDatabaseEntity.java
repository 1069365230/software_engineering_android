package com.example.taskmanager.model.database;

import android.content.Context;
import android.os.AsyncTask;

import com.example.taskmanager.model.taskdata.AppointmentTask;
import com.example.taskmanager.model.taskdata.CheckListPriority;
import com.example.taskmanager.model.taskdata.CheckListTask;
import com.example.taskmanager.model.taskdata.CheckListTaskGroup;
import com.example.taskmanager.model.taskdata.Task;
import com.example.taskmanager.model.taskdata.TaskStatus;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Task.class, AppointmentTask.class, CheckListTask.class, CheckListTaskGroup.class}, version = 3, exportSchema = false)
@TypeConverters({Converter.class})
public abstract class TaskDatabaseEntity extends RoomDatabase {

    private static TaskDatabaseEntity instance;

    public abstract TaskDao taskDao();
    public abstract AppointmentTaskDao appointmentTaskDao();
    public abstract CheckListTaskDao checkListTaskDao();
    public abstract CheckListTaskGroupDao checkListTaskGroupDao();

    public static synchronized TaskDatabaseEntity getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), TaskDatabaseEntity.class, "task_database")
                    //.addTypeConverter(new Converter())
                    .fallbackToDestructiveMigration()
                    //.addCallback(roomCallBack)
                    .build();
        }
        return instance;
    }


    //TODO only for testing, need to be removed before final release
    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback(){

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new AddsomeTasksBeforeInsertAsyc(instance).execute();
        }
    };

    private static class AddsomeTasksBeforeInsertAsyc extends AsyncTask<Void, Void, Void> {
        private TaskDao taskDao;
        private AppointmentTaskDao appointmentTaskDao;
        private CheckListTaskDao checkListTaskDao;
        private CheckListTaskGroupDao checkListTaskGroupDao;


        public AddsomeTasksBeforeInsertAsyc(TaskDatabaseEntity database){
            this.appointmentTaskDao  = database.appointmentTaskDao();
            this.taskDao = database.taskDao();
            this.checkListTaskDao = database.checkListTaskDao();
            this.checkListTaskGroupDao = database.checkListTaskGroupDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //can not instantiate the factory on the back ground thread

            Task t1 = new AppointmentTask("this is a Task1", TaskStatus.TODO, new Date("2022/11/18"), "Location1");
            Task t2 = new AppointmentTask("this is a Task2", TaskStatus.TODO, new Date("2022/11/19"), "Location2");
            Task t3 = new AppointmentTask("this is a Task3", TaskStatus.TODO, new Date(), "Location3");

            taskDao.insert(t1);
            taskDao.insert(t2);
            taskDao.insert(t3);

            AppointmentTask a1 = new AppointmentTask("this is a title1", TaskStatus.TODO, new Date("2022/11/18"), "Location1");
            AppointmentTask a2 = new AppointmentTask("this is a title2", TaskStatus.TODO, new Date("2022/11/18"), "Location2");
            AppointmentTask a3 = new AppointmentTask("this is a title3", TaskStatus.TODO, new Date("2022/11/18"), "Location3");

            appointmentTaskDao.insert(a1);
            appointmentTaskDao.insert(a2);
            appointmentTaskDao.insert(a3);

            //String title, TaskStatus status, Date deadline, CheckListPriority priority
            CheckListTask c1 = new CheckListTask("c1", TaskStatus.TODO, new Date("2022/12/25"), CheckListPriority.LOW);
            CheckListTask c2 = new CheckListTask("c2", TaskStatus.TODO, new Date("2022/12/25"), CheckListPriority.MEDIUM);
            CheckListTask c3 = new CheckListTask("c3", TaskStatus.TODO, new Date("2022/12/25"), CheckListPriority.HIGH);

            checkListTaskDao.insert(c1);
            checkListTaskDao.insert(c2);
            checkListTaskDao.insert(c3);

            CheckListTaskGroup g1 = new CheckListTaskGroup("GROUP TEST", TaskStatus.TODO, new Date("2022/12/31"));

            g1.addCheckListComponent(c1);
            g1.addCheckListComponent(c2);
            g1.addCheckListComponent(c3);

            CheckListTaskGroup g2 = new CheckListTaskGroup("GROUP TEST", TaskStatus.TODO, new Date("2022/12/31"));

            g2.addCheckListComponent(c1);
            g2.addCheckListComponent(c2);
            g2.addCheckListComponent(c3);

            checkListTaskGroupDao.insert(g1);
            checkListTaskGroupDao.insert(g2);

            return null;
        }
    }

}
