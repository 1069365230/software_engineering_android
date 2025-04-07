package com.example.taskmanager.view;

import static android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.taskmanager.R;
import com.example.taskmanager.exception.EmptyInputException;
import com.example.taskmanager.exception.InvalidDateException;
import com.example.taskmanager.exception.UnknownTaskTypeException;
import com.example.taskmanager.model.taskdata.TaskStatus;
import com.example.taskmanager.model.taskdata.TaskType;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class AddEditTaskActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText titleEditText;
    private NumberPicker statusPicker;
    private EditText locationEditText;
    private Spinner prioritySpinner;
    private Spinner taskSpinner;

    private EditText yearEditText;
    private EditText monthEditText;
    private EditText dayEditText;

    private String jsonSubTasks;

    private String taskType;
    private String taskTypeForUpdate = "";

    private String parentTitle;
    private String parentStatus;
    private int childPosition;

    private static final int splitStringYearPosition = 0;
    private static final int splitStringMonthPosition = 1;
    private static final int splitStringDayPosition = 2;

    private static final int DONE_number = 1;
    private static final int TODO_number = 0;

    private static final String fillerDate = "2023/1/1";

    private UI ui;
    private String file_path;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        linkResourcesFromXml();

        initializeSpinner(R.id.task_spinner, R.array.task_types);
        initializeSpinner(R.id.priority_spinner, R.array.priority_types);

        this.ui = new UI(this);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_1);

        setAttachFileButton();
        setAttachSketchButton();

        //for updating a task
        //if the intent has id in it, it will be a updating
        Intent intentFromMain = getIntent();
        if(intentFromMain.hasExtra(IntentKeys.EXTRA_ID)){
            setTitle("EDIT A TASK");
            taskSpinner.setVisibility(View.GONE);

            try {
                populateScreen(intentFromMain);
                jsonSubTasks = intentFromMain.getStringExtra(IntentKeys.EXTRA_SUBTASKS);
                if(jsonSubTasks!=null){
                    Log.e("intent passing2", jsonSubTasks);//check
                }
                //for updating children
                parentTitle = intentFromMain.getStringExtra(IntentKeys.EXTRA_GROUP_TITLE);
                parentStatus = intentFromMain.getStringExtra(IntentKeys.EXTRA_GROUP_STATUS);
                childPosition = intentFromMain.getIntExtra(IntentKeys.EXTRA_CHILD_POSITION, IntentKeys.invalidID);

            }catch (UnknownTaskTypeException e){
                this.ui.createMessage(e.getMessage());
            }

        }else{
            setTitle("ADD A TASK");
            taskSpinner.setVisibility(View.VISIBLE);
        }
        pickFiles();
    }

    //link the fields in xml files
    private void linkResourcesFromXml(){
        titleEditText = findViewById(R.id.enter_title);

        statusPicker = findViewById(R.id.status_picker);
        statusPicker.setMinValue(0);
        statusPicker.setMaxValue(1);
        statusPicker.setDisplayedValues(new String []{"TODO", "DONE"});

        locationEditText = findViewById(R.id.enter_location);
        prioritySpinner = findViewById(R.id.priority_spinner);
        taskSpinner = findViewById(R.id.task_spinner);

        yearEditText = findViewById(R.id.enter_year);
        monthEditText = findViewById(R.id.enter_month);
        dayEditText = findViewById(R.id.enter_day);
    }

    //fill in all the fields from the data inside intent
    private void populateScreen(Intent intent){
        titleEditText.setText(intent.getStringExtra(IntentKeys.EXTRA_TITLE));

        statusPicker.setValue(getStatusValueFromString(intent.getStringExtra(IntentKeys.EXTRA_STATUS)));

        //TODO can be improved if theres a date picker in the UI
        String[] dateString = intent.getStringExtra(IntentKeys.EXTRA_DATE).split("/");
        yearEditText.setText(dateString[splitStringYearPosition]);
        monthEditText.setText(dateString[splitStringMonthPosition]);
        dayEditText.setText(dateString[splitStringDayPosition]);
        taskTypeForUpdate = intent.getStringExtra(IntentKeys.EXTRA_UPDATE_TASK_TYPE);

        //adjust the ui depending on the task type, fill the text with corresponding task
        try {
            adjustUIFromTaskType(taskTypeForUpdate, intent);
        }catch (UnknownTaskTypeException e){
            throw e;
        }

    }

    //return the int position for spinner from the string
    private int getStatusValueFromString(String status){
        if(status.equals("DONE")){
            return DONE_number;
        }
        return TODO_number;
    }

    //return the TaskStatus from int position from spinner
    private TaskStatus getStatusFromInt(int status){
        if(status == DONE_number){
            return TaskStatus.DONE;
        }
        return TaskStatus.TODO;
    }

    //get the integer position from the required priority inside a specific spinner
    private int findPriorityPosition(Spinner spinner, String priority){
        for(int i=0;i<spinner.getCount();i++){
            if(spinner.getItemAtPosition(i).toString().equals(priority)){
                return i;
            }
        }
        Log.e("Spinner", "priority value: "+ priority + " not found on spinner");
        return 0;
    }

    //adjust which fields to fill inside of the activity, depending on type of the task
    private void adjustUIFromTaskType(String taskTypeForUpdate, Intent intent){
        Log.d("task type clicked", taskTypeForUpdate);
        if(taskTypeForUpdate.equals(TaskType.APPOINTMENT.toString())){
            displayForAppointment();

            locationEditText.setText(intent.getStringExtra(IntentKeys.EXTRA_LOCATION));

        }else if(taskTypeForUpdate.equals(TaskType.CHECKLIST.toString())){
            displayForCheckList();

            String priority = intent.getStringExtra(IntentKeys.EXTRA_PRIORITY);
            prioritySpinner.setSelection(findPriorityPosition(prioritySpinner, priority));

        }else if (taskTypeForUpdate.equals(TaskType.CHECKLISTGROUP.toString())){
            displayForGroup();
            hideDateInput();

        }else if (taskTypeForUpdate.equals(TaskType.CHILDINGROUP.toString())){
            displayForCheckList();

            String priority = intent.getStringExtra(IntentKeys.EXTRA_PRIORITY);
            prioritySpinner.setSelection(findPriorityPosition(prioritySpinner, priority));

        }else{
            throw new UnknownTaskTypeException("task not supported");
        }

    }

    //setup the spinner depends on the resource, requires the xml source id for the spinner and the array resource that feeds into the spinner
    private void initializeSpinner(int spinnerId, int arrayResource){
        Spinner Spinner = findViewById(spinnerId);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, arrayResource, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner.setAdapter(adapter);
        Spinner.setOnItemSelectedListener(this);
    }

    private void save(){
        String title = titleEditText.getText().toString();
        int status = statusPicker.getValue();
        String year = yearEditText.getText().toString();
        String month = monthEditText.getText().toString();
        String day = dayEditText.getText().toString();
        String date = year+"/"+month+"/"+day;
        String taskType = taskSpinner.getSelectedItem().toString();
        String location = locationEditText.getText().toString();

        try {
            if(taskType.equals(TaskType.CHECKLISTGROUP.toString())){
                inputValidation(title);
            }else{
                inputValidation(title, year, month, day, date);
            }
        }catch (EmptyInputException | InvalidDateException e){
            throw e;
        }

        //send the data back to main activity when this activity is closed
        Intent data = new Intent();
        //TODO for updating children

        data.putExtra(IntentKeys.EXTRA_TITLE, title);
        data.putExtra(IntentKeys.EXTRA_STATUS, getStatusFromInt(status));
        data.putExtra(IntentKeys.EXTRA_DATE, date);
        data.putExtra(IntentKeys.EXTRA_TASK_TYPE, taskType);
        data.putExtra(IntentKeys.EXTRA_LOCATION, location);

        //TODO how easily extendable is this, could have Enum, separate classes
        //TODO could use a try catch here maybe
        Log.d("orphan0", taskType);
        if(taskType.equals(TaskType.CHECKLIST.toString()) || taskTypeForUpdate.equals(TaskType.CHECKLIST.toString())){
            String priority = prioritySpinner.getSelectedItem().toString();
            data.putExtra(IntentKeys.EXTRA_PRIORITY, priority);
        }

        if(taskType.equals(TaskType.CHECKLISTGROUP.toString()) || taskTypeForUpdate.equals(TaskType.CHECKLISTGROUP.toString())){

            if(jsonSubTasks != null){
                data.putExtra(IntentKeys.EXTRA_SUBTASKS, jsonSubTasks);
                Log.e("intent passing3", jsonSubTasks);
            }
            //for constructing a group it needs the date, the date here is hardcoded as a filler
            //can be extended in the future that user could define a date for the group
            data.putExtra(IntentKeys.EXTRA_DATE, fillerDate);
            //String test = data.getStringExtra(IntentKeys.EXTRA_DATE);
            //Log.e("intent test", test);

        }

        if(taskTypeForUpdate.equals(TaskType.CHILDINGROUP.toString())){
            if(jsonSubTasks != null){
                data.putExtra(IntentKeys.EXTRA_SUBTASKS, jsonSubTasks);
                Log.e("intent passing3", jsonSubTasks);
            }
            String priority = prioritySpinner.getSelectedItem().toString();
            data.putExtra(IntentKeys.EXTRA_PRIORITY, priority);

            data.putExtra(IntentKeys.EXTRA_GROUP_DATE, fillerDate);
            data.putExtra(IntentKeys.EXTRA_GROUP_TITLE, parentTitle);
            data.putExtra(IntentKeys.EXTRA_GROUP_STATUS, parentStatus);

            data.putExtra(IntentKeys.EXTRA_CHILD_POSITION, childPosition);
            Log.e("status", getStatusFromInt(status).toString());
        }



        //default value invalidID
        int id = getIntent().getIntExtra(IntentKeys.EXTRA_ID, IntentKeys.invalidID);
        if(id != IntentKeys.invalidID){
            data.putExtra(IntentKeys.EXTRA_ID, id);
            data.putExtra(IntentKeys.EXTRA_UPDATE_TASK_TYPE, taskTypeForUpdate);
        }
        Log.e("string tasktype", taskTypeForUpdate);

        //check if the save is successful
        setResult(RESULT_OK, data);
        finish();
    }

    //checks if the title is empty
    private void inputValidation(String title){
        if(title.trim().isEmpty()){
            throw new EmptyInputException("input is empty");
        }
    }

    //checks if the title, year, month, day is empty, also checks if the entered date is valid
    private void inputValidation(String title, String year, String month, String day, String date){
        //trim down to see if the input is empty
        if(title.trim().isEmpty() || year.trim().isEmpty() || month.trim().isEmpty() || day.trim().isEmpty()){
            throw new EmptyInputException("input is empty");
        }

        DateFormat dateCheck = new SimpleDateFormat("yyyy/M/dd");
        dateCheck.setLenient(false);
        try {
            dateCheck.parse(date);
        }catch (ParseException e){
            throw new InvalidDateException("invalid date");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_task_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.save_task){
            try {
                save();
            }catch (EmptyInputException | InvalidDateException e){
                this.ui.createMessage(e.getMessage());
            }
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        this.taskType = adapterView.getItemAtPosition(i).toString();

        //change the visibility depends on task type
        //TODO other task type need to done here, maybe a checkbox, to be disgusted
        if(this.taskType.equals(TaskType.APPOINTMENT.toString())){
            displayDateInput();
            displayForAppointment();

        } else if(this.taskType.equals(TaskType.CHECKLIST.toString())){
            displayDateInput();
            displayForCheckList();

        } else if(this.taskType.equals(TaskType.CHECKLISTGROUP.toString())){
            hideDateInput();
            displayForGroup();

        } else{
            //locationEditText.setVisibility(TextView.INVISIBLE);
            //TODO exceptions maybe
        }

    }

    // different display adjustments for different task types
    private void displayForAppointment(){
        locationEditText.setVisibility(TextView.VISIBLE);
        prioritySpinner.setVisibility(View.GONE);
    }

    private void displayForCheckList(){
        prioritySpinner.setVisibility(View.VISIBLE);
        locationEditText.setVisibility(TextView.INVISIBLE);
    }

    private void displayForGroup(){
        prioritySpinner.setVisibility(View.GONE);
        locationEditText.setVisibility(TextView.INVISIBLE);
    }

    private void displayDateInput(){
        yearEditText.setVisibility(TextView.VISIBLE);
        monthEditText.setVisibility(TextView.VISIBLE);
        dayEditText.setVisibility(TextView.VISIBLE);
    }

    private void hideDateInput(){
        yearEditText.setVisibility(TextView.INVISIBLE);
        monthEditText.setVisibility(TextView.INVISIBLE);
        dayEditText.setVisibility(TextView.INVISIBLE);
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void pickFiles() {

        // Construct an intent for opening a folder
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("*/*"), "resource/folder");
        // Check that there is an app activity handling that intent on our system
        if (intent.resolveActivityInfo(getBaseContext().getPackageManager(), 0) != null) {
            // Yes there is one start it then
            startActivity(intent);
        } else {
            // Did not find any activity capable of handling that intent on our system
            // TODO: Display error message or something
        }
    }

    private void setAttachFileButton() {
        Button buttonAddTask = findViewById(R.id.button_attach_file);
        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ui.createMessage("attach file");
                Intent intent = new Intent();
                intent.setType("application/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select file"), 300);
            }
        });
    }

    private void setAttachSketchButton() {
        Button buttonAddTask = findViewById(R.id.button_attach_sketch);
        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //MyCanvas myCanvas = new MyCanvas(AddEditTaskActivity.this, null);
                //setContentView(R.layout.activity_add_sketch);

                Intent getSketch = new Intent(AddEditTaskActivity.this, AddSketchActivity.class);
                startActivity(getSketch);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TextView text_view_file = findViewById(R.id.text_view_file);
        Log.e("Data", "--->" + data.getData().toString());
        file_path = FileUtils.getPath(AddEditTaskActivity.this, data.getData());
        if (file_path != null) {
            File imgFile = new File(file_path);
            if (imgFile.exists()) {
                text_view_file.setVisibility(View.VISIBLE);
                text_view_file.setText(imgFile.getName());
            } else {
                text_view_file.setText("");
                ui.createMessage("DocFile 1 is not exist.");
            }
        } else {
            file_path = "";
            text_view_file.setText("");
            ui.createMessage("Error to upload!");
        }
    }
}