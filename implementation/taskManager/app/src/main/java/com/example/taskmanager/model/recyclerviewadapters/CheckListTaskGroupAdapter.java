package com.example.taskmanager.model.recyclerviewadapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.taskmanager.R;
import com.example.taskmanager.model.customobservers.CustomObservable;
import com.example.taskmanager.model.customobservers.CustomObserver;
import com.example.taskmanager.model.taskdata.CheckListTask;
import com.example.taskmanager.model.taskdata.CheckListTaskGroup;
import com.example.taskmanager.model.taskdata.Task;
import com.example.taskmanager.view.MainActivity;
import com.example.taskmanager.view.fragments.ListViewFragment;
import com.example.taskmanager.view.viewsetuptemplate.ListViewSetupHandler;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class CheckListTaskGroupAdapter extends RecyclerView.Adapter<CheckListTaskGroupAdapter.CheckListTaskGroupHolder> implements CustomObservable {

    private List<CheckListTaskGroup> allGroups = new ArrayList<>();
    private List<CustomObserver> customObservers = new ArrayList<>();
    private Context context;
    private ListViewSetupHandler listViewSetupHandler;

    public CheckListTaskGroupAdapter() {
    }

    public CheckListTaskGroupAdapter(ListViewSetupHandler listViewSetupHandler) {
        this.listViewSetupHandler = listViewSetupHandler;
    }


    @NonNull
    @Override
    public CheckListTaskGroupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_task, parent, false);
//        context = parent.getContext();
        return new CheckListTaskGroupAdapter.CheckListTaskGroupHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckListTaskGroupHolder holder, int position) {
        CheckListTaskGroup current = allGroups.get(position);


        //errors will appear of it contains a CheckListGroup. could be extended in the future
        List<CheckListTask> subCheckListTasks = (List<CheckListTask>)(List<?>) current.getSubCheckListTasks();

        holder.textViewTitle.setText(current.getTitle());
        holder.textViewStatus.setText(current.getStatus().toString());

        //can be used to update with the customObserver here
        //sudo code:
        //overwrite observer update in main here
        //subCheckListTasks remove clicked task
        //centercontrol send update in dbs
        CheckListTaskAdapter childAdapter = new CheckListTaskAdapter();
        childAdapter.addObserver((CustomObserver) listViewSetupHandler);
        childAdapter.setLocation(Location.CHECKLISTGROUP);


        childAdapter.setAllTasks(subCheckListTasks);
        childAdapter.setParent(current);

        holder.recyclerViewChildren.setAdapter(childAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                //TODO deleting children
                Log.e("children swipe", "onSwiped: ");
            }
        }).attachToRecyclerView(holder.recyclerViewChildren);


    }

    @Override
    public int getItemCount() {
        return this.allGroups.size();
    }

    class CheckListTaskGroupHolder extends RecyclerView.ViewHolder{
        private TextView textViewTitle;
        private RecyclerView recyclerViewChildren;
        private TextView textViewStatus;

        public CheckListTaskGroupHolder(@NonNull View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.text_view_group_title);
            textViewStatus = itemView.findViewById(R.id.text_view_group_status);
            recyclerViewChildren = itemView.findViewById(R.id.recycler_view_children);

            //button for deleting
            Button buttonDeleteGroup = itemView.findViewById(R.id.button_delete_group);
            buttonDeleteGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //since the button is here now, it needs to send back the position to main and use the centercontrol there to delete it
                    notifyObserver(getAdapterPosition());
                }
            });

            //button for adding inside of the group
            Button buttonAddInGroup = itemView.findViewById(R.id.button_add_task_in_group);
            buttonAddInGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if(customObservers.size()>0 && position != RecyclerView.NO_POSITION){
                        Log.d("group on that position", allGroups.get(position).getTitle());

                        listViewSetupHandler.startAddChildActivity(allGroups.get(position).getSubCheckListTasks(), allGroups.get(position));
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(customObservers.size()>0 && position != RecyclerView.NO_POSITION){
                        Log.d("group on that position", allGroups.get(position).getTitle());
                        notifyObserver(allGroups.get(position));
                    }
                    //TODO throw exception here, not finding the task on the position
                }
            });

        }
    }
    public CheckListTaskGroup getGroupAt(int position){
        return allGroups.get(position);
    }

    public CheckListTaskGroup getTaskAt(int position){
        return allGroups.get(position);
    }

    public void setAllGroups(List<CheckListTaskGroup> allGroups){
        this.allGroups = allGroups;
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

    //used for passing the correct group, thus for updating in mainActivity
    @Override
    public void notifyObserver(Task task) {
        for(CustomObserver o: customObservers){
            o.update(task, "CHECKLISTGROUP");
        }
    }

    //used for passing the position of the group, thus for deleting the group in mainActivity
    @Override
    public void notifyObserver(int adapterPosition) {
        for(CustomObserver o: customObservers){
            o.update(adapterPosition);
        }
    }

    @Override
    public void notifyObserver(Task task, List<CheckListTask> children, CheckListTaskGroup parent, int childPosition) {
        //do nothing, used only for updating children
    }
}
