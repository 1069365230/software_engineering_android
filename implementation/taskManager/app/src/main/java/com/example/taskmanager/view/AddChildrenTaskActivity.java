package com.example.taskmanager.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;

import com.example.taskmanager.R;
import com.example.taskmanager.exception.EmptyInputException;
import com.example.taskmanager.exception.InvalidDateException;
import com.example.taskmanager.model.taskdata.TaskStatus;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class AddChildrenTaskActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText titleEditText;
    private NumberPicker statusPicker;
    private Spinner prioritySpinner;

    private EditText yearEditText;
    private EditText monthEditText;
    private EditText dayEditText;

    private String subTasksJson;
    private int groupPosition;
    private String groupTitle;
    private TaskStatus groupStatus;
    private String groupDate;

    private UI ui;

    private static final int DONE_number = 1;
    private static final int TODO_number = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_children_task);

        this.ui = new UI(this);
        linkResourcesFromXml();
        initializeSpinner(R.id.priority_child_spinner, R.array.priority_types);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_1);

        setTitle("ADD CHECKLIST");

        Log.e("addCHILD0", "get intent" );
        Intent intentFromMain = getIntent();
        //int testID = intentFromMain.getIntExtra(IntentKeys.EXTRA_ID, 10);
        String testJson = intentFromMain.getStringExtra(IntentKeys.EXTRA_SUBTASKS);

        this.subTasksJson = intentFromMain.getStringExtra(IntentKeys.EXTRA_SUBTASKS);
        this.groupPosition = intentFromMain.getIntExtra(IntentKeys.EXTRA_ID, 10);
        this.groupTitle = intentFromMain.getStringExtra(IntentKeys.EXTRA_TITLE);
        this.groupStatus = (TaskStatus) intentFromMain.getSerializableExtra(IntentKeys.EXTRA_STATUS);
        this.groupDate = intentFromMain.getStringExtra(IntentKeys.EXTRA_DATE);

        Log.e("addCHILD1",  String.valueOf(groupPosition));
        Log.e("addCHILD2", testJson);

    }

    private void save(){
        String title = titleEditText.getText().toString();
        int status = statusPicker.getValue();
        String year = yearEditText.getText().toString();
        String month = monthEditText.getText().toString();
        String day = dayEditText.getText().toString();
        String date = year+"/"+month+"/"+day;
        String priority = prioritySpinner.getSelectedItem().toString();
        //String taskType = taskSpinner.getSelectedItem().toString();

        try {
            inputValidation(title, year, month, day, date);
        }catch (InvalidDateException e){
            throw e;
        }

        //Intent intent = getIntent();

        Intent data = new Intent();

        data.putExtra(IntentKeys.EXTRA_TITLE, groupTitle);
        data.putExtra(IntentKeys.EXTRA_STATUS, groupStatus);
        data.putExtra(IntentKeys.EXTRA_DATE, groupDate);

        data.putExtra(IntentKeys.EXTRA_CHILD_TITLE, title);
        data.putExtra(IntentKeys.EXTRA_CHILD_STATUS, getStatusFromInt(status));
        data.putExtra(IntentKeys.EXTRA_CHILD_DATE, date);
        data.putExtra(IntentKeys.EXTRA_CHILD_PRIORITY, priority);

        data.putExtra(IntentKeys.EXTRA_SUBTASKS, this.subTasksJson);
        data.putExtra(IntentKeys.EXTRA_ID, this.groupPosition);

        setResult(RESULT_OK, data);
        finish();

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

    //return the TaskStatus from int position from spinner
    private TaskStatus getStatusFromInt(int status){
        if(status == DONE_number){
            return TaskStatus.DONE;
        }
        return TaskStatus.TODO;
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
    //link the fields in xml files
    private void linkResourcesFromXml(){
        titleEditText = findViewById(R.id.enter_child_title);

        statusPicker = findViewById(R.id.status_child_picker);
        statusPicker.setMinValue(0);
        statusPicker.setMaxValue(1);
        statusPicker.setDisplayedValues(new String []{"TODO", "DONE"});

        prioritySpinner = findViewById(R.id.priority_child_spinner);

        yearEditText = findViewById(R.id.enter_child_year);
        monthEditText = findViewById(R.id.enter_child_month);
        dayEditText = findViewById(R.id.enter_child_day);
    }

    private void initializeSpinner(int spinnerId, int arrayResource){
        Spinner Spinner = findViewById(spinnerId);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, arrayResource, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner.setAdapter(adapter);
        Spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}