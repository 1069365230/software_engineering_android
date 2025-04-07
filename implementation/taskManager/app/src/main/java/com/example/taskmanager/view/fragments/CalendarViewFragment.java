package com.example.taskmanager.view.fragments;

import static com.example.taskmanager.view.CalendarUtils.selectedDate;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.taskmanager.R;
import com.example.taskmanager.model.taskdata.AppointmentTask;
import com.example.taskmanager.model.taskdata.CheckListTask;
import com.example.taskmanager.model.taskdata.CheckListTaskGroup;
import com.example.taskmanager.model.taskdata.Task;
import com.example.taskmanager.view.CalendarUtils;
import com.example.taskmanager.view.adapters.CalendarAdapter;
import com.example.taskmanager.view.viewsetuptemplate.CalenderViewSetupHandler;

import java.time.LocalDate;

public class CalendarViewFragment extends Fragment implements CalendarAdapter.OnItemListener {

    private CalenderViewSetupHandler calenderViewSetupHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.calenderViewSetupHandler = new CalenderViewSetupHandler(this, view);
        calenderViewSetupHandler.loadView();
        nextWeekAction(view);
        previousWeekAction(view);
    }

    public void nextWeekAction(View view){
        Button buttonAddTask = view.findViewById(R.id.next_week);
        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDate = selectedDate.plusWeeks(1);
                calenderViewSetupHandler.setWeekView();
            }
        });
    }

    public void previousWeekAction(View view){
        Button buttonAddTask = view.findViewById(R.id.previous_week);
        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDate = selectedDate.minusWeeks(1);
                calenderViewSetupHandler.setWeekView();
            }
        });
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        selectedDate = date;
        calenderViewSetupHandler.setWeekView();
    }
}