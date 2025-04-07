package com.example.taskmanager;

import com.example.taskmanager.model.taskdata.AppointmentTask;
import com.example.taskmanager.model.taskdata.CheckListPriority;
import com.example.taskmanager.model.taskdata.CheckListTask;
import com.example.taskmanager.model.taskdata.CheckListTaskGroup;
import com.example.taskmanager.model.taskdata.TaskStatus;
import com.example.taskmanager.viewmodel.taskcreation.TaskFactory;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class TaskFactoryTest {
    private TaskFactory factory = new TaskFactory();

    @Test
    public void appointmentCreationIsCorrect() {

        AppointmentTask expected = new AppointmentTask("appointment", TaskStatus.TODO, new Date("2022/3/3"), "location");

        assertEquals(expected, factory.produceTask("appointment", TaskStatus.TODO, new Date("2022/3/3"), "location"));
    }

    @Test
    public void checkListCreationIsCorrect() {

        CheckListTask expected = new CheckListTask("checklist", TaskStatus.TODO, new Date("2022/3/3"), CheckListPriority.LOW);

        assertEquals(expected, factory.produceTask("checklist", TaskStatus.TODO, new Date("2022/3/3"), CheckListPriority.LOW));
    }

    @Test
    public void checkListGroupCreationIsCorrect() {

        CheckListTaskGroup expected = new CheckListTaskGroup("checklist", TaskStatus.TODO, new Date("2022/3/3"));

        assertEquals(expected, factory.produceTask("checklist", TaskStatus.TODO, new Date("2022/3/3")));
    }
}
