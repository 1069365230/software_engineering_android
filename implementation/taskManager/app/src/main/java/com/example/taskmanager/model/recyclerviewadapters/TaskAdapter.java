package com.example.taskmanager.model.recyclerviewadapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.taskmanager.R;
import com.example.taskmanager.model.taskdata.AppointmentTask;
import com.example.taskmanager.model.taskdata.Task;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
//adapters are for getting the data to the recycler view
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {
    private List<Task> allTasks = new ArrayList<>();

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_task, parent, false);
        return new TaskHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        Task current = allTasks.get(position);
        holder.textViewTitle.setText(current.getTitle());
        holder.textViewStatus.setText(current.getStatus().toString());
        holder.textViewDeadline.setText(current.getDeadlineToString());
        holder.textViewTaskType.setText(current.getClass().getSimpleName().toString().replace("Task",""));


    }

    @Override
    public int getItemCount() {
        return allTasks.size();
    }

    public Task getTaskAt(int position){
        return allTasks.get(position);
    }

    public void setAllTasks(List<Task> allTasks){
        this.allTasks = allTasks;
        notifyDataSetChanged();

    }

    class TaskHolder extends RecyclerView.ViewHolder{
        private TextView textViewTitle;
        private TextView textViewStatus;
        private TextView textViewDeadline;
        private TextView textViewLocation;
        private TextView textViewTaskType;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewStatus = itemView.findViewById(R.id.text_view_status);
            textViewDeadline = itemView.findViewById(R.id.text_view_deadline);
            textViewLocation = itemView.findViewById(R.id.text_view_location);
            textViewTaskType = itemView.findViewById(R.id.text_view_task_type);
        }
    }
}
