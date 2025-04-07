package com.example.taskmanager.model.taskdata;


import android.graphics.Color;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.taskmanager.model.taskdata.attachment.AttachmentFactory;
import com.example.taskmanager.model.taskdata.attachment.AttachmentType;
import com.example.taskmanager.model.taskdata.attachment.FileAttachment;

@Entity(tableName = "task_table")
//@TypeConverters(Converter.class)
public class Task {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private TaskStatus status;

    private Date deadline;

    //TODO need to think how this should be stored in dbs, use type converter
    @Ignore
    private Color color;
    
    private List<FileAttachment> attachedFiles = new ArrayList<FileAttachment>();

    public void setAttachedFiles(List<FileAttachment> attachedFiles) {
        this.attachedFiles = attachedFiles;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public Date getDeadline() {
        return deadline;
    }

    public String getDeadlineToString() {
        DateFormat formatter = new SimpleDateFormat("yyyy/M/d");
        return formatter.format(this.deadline);
    }

    public Color getColor() {
        return color;
    }

    public List<FileAttachment> getAttachedFiles() {
        return attachedFiles;
    }

    public void attachFile(String path) {
        attachedFiles.add(AttachmentFactory.createAttachment(AttachmentType.FILE, path));
    }

    public void attachSketch(String path) {
        attachedFiles.add(AttachmentFactory.createAttachment(AttachmentType.SKETCH, path));
    }

    public boolean checkDeadline(){
        //TODO check if the current date is close to private Date, gonna use the observers here
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(title, task.title) && status == task.status && Objects.equals(deadline, task.deadline) && Objects.equals(color, task.color) && Objects.equals(attachedFiles, task.attachedFiles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, status, deadline, color, attachedFiles);
    }
}
