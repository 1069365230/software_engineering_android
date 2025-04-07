package com.example.taskmanager.view.viewsetuptemplate;

import static com.example.taskmanager.view.CalendarUtils.daysInWeekArray;
import static com.example.taskmanager.view.CalendarUtils.monthYearFromDate;
import static com.example.taskmanager.view.CalendarUtils.selectedDate;

import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.R;
import com.example.taskmanager.model.database.DataBase;
import com.example.taskmanager.model.database.TaskDataBaseProxy;
import com.example.taskmanager.model.recyclerviewadapters.AppointmentTaskAdapter;
import com.example.taskmanager.model.recyclerviewadapters.CheckListTaskAdapter;
import com.example.taskmanager.model.recyclerviewadapters.CheckListTaskGroupAdapter;
import com.example.taskmanager.model.recyclerviewadapters.TaskAdapter;
import com.example.taskmanager.view.adapters.CalendarAdapter;
import com.example.taskmanager.view.fragments.CalendarViewFragment;
import com.example.taskmanager.viewmodel.DataBaseControl;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalenderViewSetupHandler extends ViewSetupHandler {

    private final CalendarViewFragment calendarViewFragment;
    private final View view;

    private DataBaseControl dataBaseControl;
    private RecyclerView calendarRecyclerView;
    private TextView monthYearText;

    private TaskAdapter taskAdapter;
    private CheckListTaskGroupAdapter checkListTaskGroupAdapter;
    private AppointmentTaskAdapter appointmentTaskAdapter;
    private CheckListTaskAdapter checkListTaskAdapter;
    private ConcatAdapter concatAdapter;

    public CalenderViewSetupHandler(CalendarViewFragment calendarViewFragment, View view) {
        this.calendarViewFragment = calendarViewFragment;
        this.view = view;
    }

    @Override
    void setUpDataBase() {
        DataBase proxy = new ViewModelProvider(calendarViewFragment).get(TaskDataBaseProxy.class);
        this.dataBaseControl = new DataBaseControl();
        this.dataBaseControl.setDataBase(proxy);
    }

    @Override
    void setUpAdapters() {

    }

    @Override
    void setUpLayout() {
        initWidgets();
        setWeekView();
    }

    @Override
    void setUpLiveDataObservers() {
    }

    private void initWidgets() {
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView);
        monthYearText = view.findViewById(R.id.monthYearTV);
    }

    public void setWeekView() {
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, calendarViewFragment);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(calendarViewFragment.getContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }
}
