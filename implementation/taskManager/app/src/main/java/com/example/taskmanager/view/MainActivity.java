package com.example.taskmanager.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.taskmanager.R;
import com.example.taskmanager.model.customobservers.CustomObserver;
import com.example.taskmanager.model.database.Converter;
import com.example.taskmanager.model.database.TaskDataBaseProxy;
import com.example.taskmanager.model.database.DataBase;
import com.example.taskmanager.model.taskdata.AppointmentTask;
import com.example.taskmanager.model.taskdata.CheckListComponent;
import com.example.taskmanager.model.taskdata.CheckListTask;
import com.example.taskmanager.model.taskdata.CheckListTaskGroup;
import com.example.taskmanager.model.taskdata.Task;
import com.example.taskmanager.view.adapters.ViewPagerAdapter;
import com.example.taskmanager.viewmodel.CenterControl;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CustomObserver, AdapterView.OnItemSelectedListener {
    public static final int ADD_TASK_REQUEST = 1;
    public static final int EDIT_TASK_REQUEST = 2;
    public static final int ADD_CHILD_REQUEST = 3;
    public static final int UPDATE_CHILD_REQUEST = 4;

    private List<Task> allTasks;
    private List<AppointmentTask> allAppointmentTasks;
    private List<CheckListTaskGroup> allCheckListTaskGroups;
    private List<CheckListTask> allCheckListTasks;

    private CenterControl centerControl;

    private Spinner allStatusPicker;
    private String allStatus = "TODO";

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpTabs();

        //initializing database and center control here
        //DataBase dbs = new ViewModelProvider(this).get(TaskDataBaseReal.class);
        //using proxy instead of the real dbs
//        DataBase proxy = new ViewModelProvider(this).get(TaskDataBaseProxy.class);
//        this.centerControl = new CenterControl(new UI(this));
//        this.centerControl.setDataBase(proxy);

        setUpDB();

        //set observers on liveData, so any changes happened on the liveData, the recyclerView get updated
//        setupLiveDataObserver();

        //set up the onSwipe used with ItemTouchHelper
//        setupSwipeToDelete(recyclerView);

        //button to add a new task
        setUpAddTaskButton();

        //spinner for choosing different task status (common property)
        initializeSpinner(R.id.all_status_spinner, R.array.status_types);

        //button to set status for all tasks
        setUpSetAllStatusButton();

        setupTaskObservers();

        setupButton1();
    }

    private void setUpDB() {
        DataBase proxy = new ViewModelProvider(this).get(TaskDataBaseProxy.class);
        this.centerControl = new CenterControl(new UI(this));
        this.centerControl.setDataBase(proxy);
    }

    private void setUpTabs() {
        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.view_pager);
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(viewPagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });
    }

//
//    private void setupSwipeToDelete(RecyclerView recyclerView){
//        //swipe left to delete, can also add the function to swipe right and drag and drop here
//        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                //drag and drop happens here
//                return false;
//            }
//
//            //disable the swipe on checkListTaskGroupAdapter, to be able to swipe the children recyclerView inside
//            @Override
//            public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
//                String swipedAdapter = viewHolder.getBindingAdapter().getClass().toString();
//                if(swipedAdapter.equals(checkListTaskGroupAdapter.getClass().toString())){
//                    return 0;
//                }
//                return super.getSwipeDirs(recyclerView, viewHolder);
//            }
//
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//                //find and use the correct adapter
//                //then find the task and delete it using centerControl
//                //note: getAdapterPosition() gets the relative position of the task, no need to do extra calculations
//                String swipedAdapter = viewHolder.getBindingAdapter().getClass().toString();
//
//                if(swipedAdapter.equals(AppointmentTaskAdapter.class.toString())){
//                    AppointmentTask task = appointmentTaskAdapter.getTaskAt(viewHolder.getAdapterPosition());
//                    centerControl.deleteTask(task);
//                }
//
//                if(swipedAdapter.equals(CheckListTaskAdapter.class.toString())){
//                    CheckListTask task = (CheckListTask) checkListTaskAdapter.getTaskAt(viewHolder.getAdapterPosition());
//                    centerControl.deleteTask(task);
//                }
//
//                if(swipedAdapter.equals(TaskAdapter.class.toString())){
//                    Task task = taskAdapter.getTaskAt(viewHolder.getAdapterPosition());
//                    centerControl.deleteTask(task);
//                }
//            }
//        }).attachToRecyclerView(recyclerView);
//    }


    private void setUpAddTaskButton(){
        Button buttonAddTask = findViewById(R.id.button_add_task);
        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAddTaskActivity();
            }
        });
    }

    private void setupButton1(){
        Button buttonAddTask = findViewById(R.id.button_1);
        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("clicked", "********");

                for (Task task : allTasks){
                    Log.d("tasks", task.getTitle());
                }
                for(AppointmentTask app: allAppointmentTasks){
                    Log.d("appointments", app.getTitle());
                }
                for(CheckListTaskGroup group: allCheckListTaskGroups){
                    Log.d("groups", group.getTitle());
                    Log.d("subtask size:", ""+ group.getSubCheckListTasks().size());
                }

                /// does not return the children! but they are in the subtasks of the group anyway
                for(CheckListTask checkListTask: allCheckListTasks){
                    Log.d("checklisttasks", checkListTask.getTitle());
                }
            }
        });
    }

    //setup the spinner depends on the resource, requires the xml source id for the spinner and the array resource that feeds into the spinner
    private void initializeSpinner(int spinnerId, int arrayResource){
        allStatusPicker = (Spinner) findViewById(spinnerId);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                arrayResource, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        allStatusPicker.setAdapter(adapter);
        allStatusPicker.setOnItemSelectedListener(this);
    }

    private void setUpSetAllStatusButton(){
        Button buttonSetAllStatus = findViewById(R.id.button_set_all_status);
        buttonSetAllStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                centerControl.updateAllStatus(allStatus);
            }
        });
    }

    //get the passed intent from add activity here and handles them
    //insert should be handled here
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("req/res", resultCode + "/" + resultCode);
        if(requestCode == ADD_TASK_REQUEST && resultCode == RESULT_OK){
            String taskType = data.getStringExtra(IntentKeys.EXTRA_TASK_TYPE);

            //depending on different taskType, centerControl create and insert them differently
            this.centerControl.addTask(taskType, data);

        }else if(requestCode == EDIT_TASK_REQUEST && resultCode == RESULT_OK){
            String taskType = data.getStringExtra(IntentKeys.EXTRA_UPDATE_TASK_TYPE);

            //depending on different taskType, centerControl updates them differently
            this.centerControl.updateTask(taskType, data);

        }else if(requestCode == ADD_CHILD_REQUEST && resultCode == RESULT_OK){
            Log.e("requestcode", "incorrect");
            //add new task under the group works differently than normal addTask, therefore a separate method is called
            this.centerControl.addChild(data);

        }else if(requestCode == UPDATE_CHILD_REQUEST && resultCode == RESULT_OK){
            Log.e("requestcode", "correct");
            this.centerControl.updateChild(data);

        }else{
            //RESULT_CANCELED HERE
            //TODO throw exception here maybe
            this.centerControl.displayMessageOnScreen("update unsuccessful");
            Log.e("Data passing error", "RESULT_CANCELED");
        }
    }

    public void startAddTaskActivity(){
        Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);

        //requestCode tells the different requests(from different activities)
        startActivityForResult(intent, ADD_TASK_REQUEST);
    }

    @Override
    public void update() {

    }

    //TODO String to enum
    //TODO CALL other method in the update method, about the meaning, update can not present everything, reduce the comments
    @Override
    public void update(Task task, String type) {
//        startUpdatingActivity(task, type);
    }

    @Override
    public void update(int CheckListGroupAdapterPosition) {
//        CheckListTaskGroup task = checkListTaskGroupAdapter.getTaskAt(CheckListGroupAdapterPosition);
//        centerControl.deleteTask(task);
    }

    @Override
    public void update(Task task, List<CheckListTask> children, CheckListTaskGroup parent, int childPosition) {
//        Log.e("location test", "correct");
//        startUpdatingChildActivity(task, children, parent, childPosition);
    }

    //override methods for the spinner
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        this.allStatus = allStatusPicker.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void setupTaskObservers() {
        this.centerControl.getAllTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                allTasks = tasks;
            }
        });

        this.centerControl.getAllAppointmentTasks().observe(this, new Observer<List<AppointmentTask>>() {
            @Override
            public void onChanged(List<AppointmentTask> appointmentTasks) {
                allAppointmentTasks = appointmentTasks;
            }
        });

        this.centerControl.getAllCheckListTaskGroups().observe(this, new Observer<List<CheckListTaskGroup>>() {
            @Override
            public void onChanged(List<CheckListTaskGroup> checkListTaskGroups) {
                allCheckListTaskGroups = (checkListTaskGroups);
            }
        });

        this.centerControl.getAllCheckListTasks().observe(this, new Observer<List<CheckListTask>>() {
            @Override
            public void onChanged(List<CheckListTask> checkListTasks) {
                allCheckListTasks = checkListTasks;
            }
        });
    }


//    this.dataBaseControl.getAllTasks().observe(listViewFragment.getViewLifecycleOwner(), new Observer<List<Task>>() {
//        @Override
//        public void onChanged(List<Task> tasks) {
//            taskAdapter.setAllTasks(tasks);
//
//        }
//    });
}