package com.example.taskmanager.model.recyclerviewadapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.taskmanager.R;
import com.example.taskmanager.model.customobservers.CustomObservable;
import com.example.taskmanager.model.customobservers.CustomObserver;
import com.example.taskmanager.model.taskdata.AppointmentTask;
import com.example.taskmanager.model.taskdata.CheckListTask;
import com.example.taskmanager.model.taskdata.CheckListTaskGroup;
import com.example.taskmanager.model.taskdata.Task;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class AppointmentTaskAdapter extends RecyclerView.Adapter<AppointmentTaskAdapter.AppointmentTaskHolder> implements CustomObservable {
    private List<AppointmentTask> allTasks = new ArrayList<>();
    private List<CustomObserver> customObservers = new ArrayList<>();

    @NonNull
    @Override
    public AppointmentTaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_task, parent, false);
        return new AppointmentTaskAdapter.AppointmentTaskHolder(itemView);
    }

    //binding process, put the correct information into the correct place (title to title, stats to stats...).
    @Override
    public void onBindViewHolder(@NonNull AppointmentTaskHolder holder, int position) {
        AppointmentTask current = allTasks.get(position);
        holder.textViewTitle.setText(current.getTitle());
        holder.textViewStatus.setText(current.getStatus().toString());
        holder.textViewDeadline.setText(current.getDeadlineToString());
        holder.textViewLocation.setText(current.getLocation());
        holder.textViewTaskType.setText(current.getClass().getSimpleName().toString().replace("Task",""));
        holder.textViewPriority.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return allTasks.size();
    }

    class AppointmentTaskHolder extends RecyclerView.ViewHolder{
        private TextView textViewTitle;
        private TextView textViewStatus;
        private TextView textViewDeadline;
        private TextView textViewLocation;
        private TextView textViewTaskType;
        private TextView textViewPriority;

        public AppointmentTaskHolder(@NonNull View itemView) {
            super(itemView);
            itemView.findViewById(R.id.text_view_priority).setVisibility(View.GONE);

            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewStatus = itemView.findViewById(R.id.text_view_status);
            textViewDeadline = itemView.findViewById(R.id.text_view_deadline);
            textViewLocation = itemView.findViewById(R.id.text_view_location);
            textViewTaskType = itemView.findViewById(R.id.text_view_task_type);
            textViewPriority = itemView.findViewById(R.id.text_view_priority_text);
            //for clicking on a task
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //make sure addObserver has been called before
                    //make sure the position is valid,(the animation for deleting an invalid position still happens)
                    //send the task to mainactivity and handles there
                    int position = getAdapterPosition();
                    if(customObservers.size()>0 && position != RecyclerView.NO_POSITION){
                        Log.d("task on that position", allTasks.get(position).getTitle());
                        notifyObserver(allTasks.get(position));
                    }
                    //TODO exception needed?
                    //throw new NoTaskFoundPosition("No Task found at position:" + String.valueOf(position) + "in adapter: " + this.getClass().toString());


                }
            });
        }
    }

    public AppointmentTask getTaskAt(int position){
        return allTasks.get(position);
    }

    public void setAllTasks(List<AppointmentTask> allTasks){
        this.allTasks = allTasks;
        notifyDataSetChanged();
    }

    @Override
    public void addObserver(CustomObserver customObserver) {
        this.customObservers.add(customObserver);
    }

    @Override
    public void removeObserver(CustomObserver customObserver) {
        this.customObservers.remove(customObserver);
    }

    @Override
    public void notifyObserver() {
        //do nothing
    }

    @Override
    public void notifyObserver(Task task) {
        for(CustomObserver o: customObservers){
            o.update(task, "APPOINTMENT");
        }
    }

    @Override
    public void notifyObserver(int adapterPosition) {
        //do nothing
    }

    @Override
    public void notifyObserver(Task task, List<CheckListTask> children, CheckListTaskGroup parent, int childPosition) {
        //do nothing only used for updating the children
    }
}
