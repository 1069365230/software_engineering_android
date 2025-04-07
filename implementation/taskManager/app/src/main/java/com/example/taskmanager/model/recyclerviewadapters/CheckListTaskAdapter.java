package com.example.taskmanager.model.recyclerviewadapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.taskmanager.R;
import com.example.taskmanager.model.customobservers.CustomObservable;
import com.example.taskmanager.model.customobservers.CustomObserver;
import com.example.taskmanager.model.taskdata.CheckListComponent;
import com.example.taskmanager.model.taskdata.CheckListTask;
import com.example.taskmanager.model.taskdata.CheckListTaskGroup;
import com.example.taskmanager.model.taskdata.Task;
import com.example.taskmanager.view.fragments.ListViewFragment;
import com.example.taskmanager.view.viewsetuptemplate.ListViewSetupHandler;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CheckListTaskAdapter extends RecyclerView.Adapter<CheckListTaskAdapter.CheckListTaskHolder> implements CustomObservable {

    private Location location = Location.MAIN;
    private CheckListTaskGroup parent;
    private List<CheckListTask> allTasks = new ArrayList<>();
    private List<CustomObserver> customObservers = new ArrayList<>();

    public void setLocation(Location location){
        this.location = location;
    }

    public void setParent(CheckListTaskGroup parent){
        this.parent = parent;
    }

    @NonNull
    @Override
    public CheckListTaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_task, parent, false);
        return new CheckListTaskAdapter.CheckListTaskHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckListTaskHolder holder, int position) {
        CheckListTask current = allTasks.get(position);
        holder.textViewTitle.setText(current.getTitle());
        holder.textViewStatus.setText(current.getStatus().toString());
        holder.textViewDeadline.setText(current.getDeadlineToString());
        holder.textViewPriority.setText(current.getPriority().toString());
        holder.textViewTaskType.setText(current.getClass().getSimpleName().toString().replace("Task",""));
        holder.textViewLocation.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return allTasks.size();
    }

    class CheckListTaskHolder extends RecyclerView.ViewHolder{
         private TextView textViewTitle;
         private TextView textViewStatus;
         private TextView textViewDeadline;
         private TextView textViewPriority;
         private TextView textViewTaskType;
         private TextView textViewLocation;

         public CheckListTaskHolder(@NonNull View itemView) {
             super(itemView);
             itemView.findViewById(R.id.text_view_location).setVisibility(View.GONE);

             textViewTitle = itemView.findViewById(R.id.text_view_title);
             textViewStatus = itemView.findViewById(R.id.text_view_status);
             textViewDeadline = itemView.findViewById(R.id.text_view_deadline);
             textViewPriority = itemView.findViewById(R.id.text_view_priority);
             textViewTaskType = itemView.findViewById(R.id.text_view_task_type);
             textViewLocation = itemView.findViewById(R.id.text_view_location_text);

             //for clicking on a task
             itemView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     int position = getAdapterPosition();

                     if(customObservers.size()>0 && position != RecyclerView.NO_POSITION){
                         if(location == Location.MAIN){
                             Log.d("task on that position", allTasks.get(position).getTitle());
                             notifyObserver(allTasks.get(position));
                         }

                         if(location == Location.CHECKLISTGROUP){
                             Log.d("child position", String.valueOf(position));
                             notifyObserver(allTasks.get(position), allTasks, parent, position);
                         }
                     }
                     //TODO throw exception here, not finding the task on the position
                 }
             });
         }
     }

    public CheckListComponent getTaskAt(int position){
        return allTasks.get(position);
    }

    public void setAllTasks(List<CheckListTask> allTasks){
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
        //differentiate between the on click is called in main or inside of a group
        //group needs a different update method
        for(CustomObserver o: customObservers){
                o.update(task, "CHECKLIST");
        }

    }

    @Override
    public void notifyObserver(int adapterPosition) {
        //do nothing
    }

    @Override
    public void notifyObserver(Task task, List<CheckListTask> children, CheckListTaskGroup parent, int childPosition) {
        for(CustomObserver o: customObservers){
            o.update(task, children, parent, childPosition);
        }
    }
}
