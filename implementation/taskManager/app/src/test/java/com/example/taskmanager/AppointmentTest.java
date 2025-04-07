package com.example.taskmanager;

import com.example.taskmanager.model.taskdata.AppointmentTask;
import com.example.taskmanager.model.taskdata.TaskStatus;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class AppointmentTest {
    @Test
    public void appointmentConstructorTest() {
        AppointmentTask appointmentTask = new AppointmentTask("appointment", TaskStatus.TODO, new Date("2022/3/3"), "location");
        String expectedTitle = "appointment";
        String actualTitle = appointmentTask.getTitle();
        assertEquals(expectedTitle, actualTitle);

        TaskStatus expectedStatus = TaskStatus.TODO;
        TaskStatus actualStatus = appointmentTask.getStatus();
        assertEquals(expectedStatus, actualStatus);

        Date expectedDeadline = new Date("2022/3/3");
        Date actualDeadline = appointmentTask.getDeadline();
        assertEquals(expectedDeadline, actualDeadline);

        String expectedLocation = "location";
        String actualLocation = appointmentTask.getLocation();
        assertEquals(expectedLocation, actualLocation);
    }

    @Test
    public void appointmentModificationTest(){
        AppointmentTask appointmentTask = new AppointmentTask();
        appointmentTask.setTitle("appointment");
        appointmentTask.setStatus(TaskStatus.DONE);
        appointmentTask.setDeadline(new Date("2022/3/3"));
        appointmentTask.setLocation("location");

        String expectedTitle = "appointment";
        String actualTitle = appointmentTask.getTitle();
        assertEquals(expectedTitle, actualTitle);

        TaskStatus expectedStatus = TaskStatus.DONE;
        TaskStatus actualStatus = appointmentTask.getStatus();
        assertEquals(expectedStatus, actualStatus);

        Date expectedDeadline = new Date("2022/3/3");
        Date actualDeadline = appointmentTask.getDeadline();
        assertEquals(expectedDeadline, actualDeadline);

        String expectedStringDeadline = ("2022/3/3");
        String actualStringDeadline = appointmentTask.getDeadlineToString();
        assertEquals(expectedStringDeadline, actualStringDeadline);

        String expectedLocation = "location";
        String actualLocation = appointmentTask.getLocation();
        assertEquals(expectedLocation, actualLocation);
    }

    @Test
    public void appointmentComparatorTest(){
        AppointmentTask expected = new AppointmentTask("appointment", TaskStatus.TODO, new Date("2022/3/3"), "location");
        AppointmentTask actual = new AppointmentTask("appointment", TaskStatus.TODO, new Date("2022/3/3"), "location");
        assertEquals(expected, actual);

    }
}
