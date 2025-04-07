package com.example.taskmanager.viewmodel.filehandling;

import java.util.List;
import com.example.taskmanager.model.taskdata.Task;

public interface ListOfTask {
    public List<Task> request(String fileContent);
}
