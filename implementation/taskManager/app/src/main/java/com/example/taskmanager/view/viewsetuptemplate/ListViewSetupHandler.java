package com.example.taskmanager.view.viewsetuptemplate;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.R;
import com.example.taskmanager.model.customobservers.CustomObserver;
import com.example.taskmanager.model.database.Converter;
import com.example.taskmanager.model.database.DataBase;
import com.example.taskmanager.model.database.TaskDataBaseProxy;
import com.example.taskmanager.model.recyclerviewadapters.AppointmentTaskAdapter;
import com.example.taskmanager.model.recyclerviewadapters.CheckListTaskAdapter;
import com.example.taskmanager.model.recyclerviewadapters.CheckListTaskGroupAdapter;
import com.example.taskmanager.model.recyclerviewadapters.TaskAdapter;
import com.example.taskmanager.model.taskdata.AppointmentTask;
import com.example.taskmanager.model.taskdata.CheckListComponent;
import com.example.taskmanager.model.taskdata.CheckListTask;
import com.example.taskmanager.model.taskdata.CheckListTaskGroup;
import com.example.taskmanager.model.taskdata.Task;
import com.example.taskmanager.model.taskdata.TaskType;
import com.example.taskmanager.view.AddChildrenTaskActivity;
import com.example.taskmanager.view.AddEditTaskActivity;
import com.example.taskmanager.view.IntentKeys;
import com.example.taskmanager.view.MainActivity;
import com.example.taskmanager.view.fragments.ListViewFragment;
import com.example.taskmanager.viewmodel.DataBaseControl;

import java.util.List;

public class ListViewSetupHandler extends ViewSetupHandler implements CustomObserver {

    public static final int ADD_TASK_REQUEST = 1;
    public static final int EDIT_TASK_REQUEST = 2;
    public static final int ADD_CHILD_REQUEST = 3;
    public static final int UPDATE_CHILD_REQUEST = 4;

    private final ListViewFragment listViewFragment;
    private final View view;

    private DataBaseControl dataBaseControl;
    private RecyclerView recyclerView;

    private TaskAdapter taskAdapter;
    private CheckListTaskGroupAdapter checkListTaskGroupAdapter;
    private AppointmentTaskAdapter appointmentTaskAdapter;
    private CheckListTaskAdapter checkListTaskAdapter;
    private ConcatAdapter concatAdapter;



    public ListViewSetupHandler(ListViewFragment listViewFragment, View view) {
        this.listViewFragment = listViewFragment;
        this.view = view;
    }


    @Override
    void setUpAdapters() {
        this.taskAdapter = new TaskAdapter();
        this.checkListTaskGroupAdapter = new CheckListTaskGroupAdapter(this);
        this.appointmentTaskAdapter= new AppointmentTaskAdapter();
        this.checkListTaskAdapter = new CheckListTaskAdapter();

        //observer used here to populate the edit task page
        this.appointmentTaskAdapter.addObserver(this);
        this.checkListTaskAdapter.addObserver(this);
        this.checkListTaskGroupAdapter.addObserver(this);

        //using concat adapters to avoid overwrites of adapters in recyclerView
        this.concatAdapter = new ConcatAdapter(taskAdapter, appointmentTaskAdapter, checkListTaskAdapter, checkListTaskGroupAdapter);
    }

    @Override
    void setUpDataBase() {
        DataBase proxy = new ViewModelProvider(listViewFragment).get(TaskDataBaseProxy.class);
        this.dataBaseControl = new DataBaseControl();
        this.dataBaseControl.setDataBase(proxy);
    }

    @Override
    void setUpLayout() {
        recyclerView = this.view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(listViewFragment.getContext()));
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(concatAdapter);
    }

    @Override
    public void setUpLiveDataObservers() {
        this.dataBaseControl.getAllTasks().observe(listViewFragment.getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                taskAdapter.setAllTasks(tasks);
            }
        });

        this.dataBaseControl.getAllAppointmentTasks().observe(listViewFragment.getViewLifecycleOwner(), new Observer<List<AppointmentTask>>() {
            @Override
            public void onChanged(List<AppointmentTask> appointmentTasks) {
                appointmentTaskAdapter.setAllTasks(appointmentTasks);
            }
        });

        this.dataBaseControl.getAllCheckListTasks().observe(listViewFragment.getViewLifecycleOwner(), new Observer<List<CheckListTask>>() {
            @Override
            public void onChanged(List<CheckListTask> checkListTasks) {
                checkListTaskAdapter.setAllTasks(checkListTasks);
            }
        });

        this.dataBaseControl.getAllCheckListTaskGroups().observe(listViewFragment.getViewLifecycleOwner(), new Observer<List<CheckListTaskGroup>>() {
            @Override
            public void onChanged(List<CheckListTaskGroup> checkListTaskGroups) {
                checkListTaskGroupAdapter.setAllGroups(checkListTaskGroups);
            }
        });
    }

    public void startUpdatingActivity(Task task, String type){
        Intent intent = new Intent(listViewFragment.getActivity(), AddEditTaskActivity.class);
        intent.putExtra(IntentKeys.EXTRA_ID, task.getId());
        intent.putExtra(IntentKeys.EXTRA_TITLE, task.getTitle());
        intent.putExtra(IntentKeys.EXTRA_STATUS, task.getStatus().toString());
        intent.putExtra(IntentKeys.EXTRA_DATE, task.getDeadlineToString());

        if(type.equals(TaskType.APPOINTMENT.toString())){
            AppointmentTask appointmentType = (AppointmentTask) task;
            intent.putExtra(IntentKeys.EXTRA_LOCATION, appointmentType.getLocation());
        }

        if(type.equals(TaskType.CHECKLIST.toString())){
            CheckListTask checkListType = (CheckListTask) task;
            intent.putExtra(IntentKeys.EXTRA_PRIORITY, checkListType.getPriority().toString());
        }

        if(type.equals(TaskType.CHECKLISTGROUP.toString())){
            String convertedSubTasks = Converter.fromCheckListComponentList(((CheckListTaskGroup) task).getSubCheckListTasks());
            intent.putExtra(IntentKeys.EXTRA_SUBTASKS, convertedSubTasks);
        }

        intent.putExtra(IntentKeys.EXTRA_UPDATE_TASK_TYPE, type);
        listViewFragment.getActivity().startActivityForResult(intent, EDIT_TASK_REQUEST);
    }

    public void startUpdatingChildActivity(Task task, List<CheckListTask> children, CheckListTaskGroup parent, int childPosition){
        Intent intent = new Intent(listViewFragment.getContext(), AddEditTaskActivity.class);
        //this id here needs to be group id
        intent.putExtra(IntentKeys.EXTRA_ID, parent.getId());

        intent.putExtra(IntentKeys.EXTRA_TITLE, task.getTitle());
        intent.putExtra(IntentKeys.EXTRA_STATUS, task.getStatus().toString());
        intent.putExtra(IntentKeys.EXTRA_DATE, task.getDeadlineToString());
        CheckListTask checkListType = (CheckListTask) task;
        intent.putExtra(IntentKeys.EXTRA_PRIORITY, checkListType.getPriority().toString());
        intent.putExtra(IntentKeys.EXTRA_UPDATE_TASK_TYPE, TaskType.CHILDINGROUP.toString());

        intent.putExtra(IntentKeys.EXTRA_GROUP_TITLE, parent.getTitle());
        intent.putExtra(IntentKeys.EXTRA_GROUP_STATUS, parent.getStatus().toString());
        intent.putExtra(IntentKeys.EXTRA_GROUP_DATE, parent.getDeadlineToString());
        intent.putExtra(IntentKeys.EXTRA_CHILD_POSITION, childPosition);

        List<CheckListComponent> subCheckListTasks = (List<CheckListComponent>)(List<?>) children;
        String convertedSubTasks = Converter.fromCheckListComponentList(subCheckListTasks);
        intent.putExtra(IntentKeys.EXTRA_SUBTASKS, convertedSubTasks);

        listViewFragment.getActivity().startActivityForResult(intent, UPDATE_CHILD_REQUEST);
    }

    public void startAddChildActivity(List<CheckListComponent> children, CheckListTaskGroup task){
        Intent intent = new Intent(listViewFragment.getContext(), AddChildrenTaskActivity.class);
        intent.putExtra(IntentKeys.EXTRA_ID, task.getId());
        intent.putExtra(IntentKeys.EXTRA_TITLE, task.getTitle());
        intent.putExtra(IntentKeys.EXTRA_STATUS, task.getStatus());
        intent.putExtra(IntentKeys.EXTRA_DATE, task.getDeadlineToString());


        String jsonChildren = Converter.fromCheckListComponentList(children);
        intent.putExtra(IntentKeys.EXTRA_SUBTASKS, jsonChildren);

        listViewFragment.getActivity().startActivityForResult(intent, ADD_CHILD_REQUEST);
    }

    @Override
    public void update() {

    }

    @Override
    public void update(Task task, String type) {
        startUpdatingActivity(task, type);
    }

    @Override
    public void update(int CheckListGroupAdapterPosition) {
        CheckListTaskGroup task = checkListTaskGroupAdapter.getTaskAt(CheckListGroupAdapterPosition);
        dataBaseControl.delete(task);
    }

    @Override
    public void update(Task task, List<CheckListTask> children, CheckListTaskGroup parent, int childPosition) {
        startUpdatingChildActivity(task, children, parent, childPosition);
    }


}
