package pervasive.iu.com.greenthumb.TasksRecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.Date;
import java.util.List;

import pervasive.iu.com.greenthumb.DBHandler.GardenInfo;
import pervasive.iu.com.greenthumb.GardenPartner.TabLayoutFragment.TaskUpdateActivity;
import pervasive.iu.com.greenthumb.Model.TasksPOJO;
import pervasive.iu.com.greenthumb.R;

/**
 * Created by madrinathapa on 3/25/17.
 */

public class TasksRecyclerViewAdapter extends RecyclerView.Adapter<TasksRecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private Activity activity;
    private List<TasksPOJO> taskList;
    private String module = "";
    private GardenInfo gardenInfo;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tasksdescription, taskDeadLine;
        public LinearLayout tasksCardParent;
        public de.hdodenhof.circleimageview.CircleImageView  taskicon;

        public MyViewHolder(View view) {
            super(view);
            tasksCardParent = (LinearLayout) view.findViewById(R.id.tasksCardParent);
            tasksdescription = (TextView) view.findViewById(R.id.tasksdescription);
            taskDeadLine = (TextView) view.findViewById(R.id.taskDeadLine);
            tasksCardParent.setOnClickListener(this);
            taskicon = (de.hdodenhof.circleimageview.CircleImageView ) view.findViewById(R.id.taskicon);

        }

        @Override
        public void onClick(View view) {
            TasksPOJO task = taskList.get(getAdapterPosition());
            Intent intent = new Intent(mContext, TaskUpdateActivity.class);
            intent.putExtra("task", task);
            intent.putExtra("gInfo", gardenInfo);

            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(activity, (View)taskicon, "farmingImageTransition");
            mContext.startActivity(intent, options.toBundle());
        }
    }

    public TasksRecyclerViewAdapter(String module, Context mContext, List<TasksPOJO> taskList, GardenInfo ginfo) {
        this.mContext = mContext;
        this.taskList = taskList;
        this.module = module;
        this.activity = (Activity) mContext;
        this.gardenInfo = ginfo;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.taskscarditem, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        TasksPOJO task = taskList.get(position);

        long millis = System.currentTimeMillis();

        if(!(task.getTaskDeadLine().equals("") || task.getTaskDeadLine() == null)){
            if(module.equals("backlog")){
                if (millis > Long.parseLong(task.getTaskDeadLine())){
                    holder.tasksdescription.setText(task.getTaskDescription());
                    Date d = new Date(Long.parseLong(task.getTaskDeadLine()) * 1000);
                    holder.taskDeadLine.setText(d.toString());
                    if (task.getTaskDescription().equalsIgnoreCase("watering")){
                        holder.taskicon.setImageResource(R.drawable.ic_watering);
                    }
                    else if(task.getTaskDescription().equalsIgnoreCase("pruning")){
                        holder.taskicon.setImageResource(R.drawable.ic_pruning);
                    }
                    else if(task.getTaskDescription().equalsIgnoreCase("weeding")){
                        holder.taskicon.setImageResource(R.drawable.ic_weeding);
                    }
                    else if(task.getTaskDescription().equalsIgnoreCase("mulching")){
                        holder.taskicon.setImageResource(R.drawable.ic_mulching);
                    }
                    else if(task.getTaskDescription().equalsIgnoreCase("harvesting")){
                        holder.taskicon.setImageResource(R.drawable.ic_harvesting);
                    }
                    else if(task.getTaskDescription().equalsIgnoreCase("planting")){
                        holder.taskicon.setImageResource(R.drawable.ic_planting);
                    }
                    else if(task.getTaskDescription().equalsIgnoreCase("manuring")){
                        holder.taskicon.setImageResource(R.drawable.ic_manuring);
                    }
                }
            }
            else{
                if (millis < Long.parseLong(task.getTaskDeadLine())){
                    holder.tasksdescription.setText(task.getTaskDescription());
                    Date d = new Date(Long.parseLong(task.getTaskDeadLine()) * 1000);
                    holder.taskDeadLine.setText(d.toString());
                    if (task.getTaskDescription().equalsIgnoreCase("watering")){
                        holder.taskicon.setImageResource(R.drawable.ic_watering);
                    }
                    else if(task.getTaskDescription().equalsIgnoreCase("pruning")){
                        holder.taskicon.setImageResource(R.drawable.ic_pruning);
                    }
                    else if(task.getTaskDescription().equalsIgnoreCase("weeding")){
                        holder.taskicon.setImageResource(R.drawable.ic_weeding);
                    }
                    else if(task.getTaskDescription().equalsIgnoreCase("mulching")){
                        holder.taskicon.setImageResource(R.drawable.ic_mulching);
                    }
                    else if(task.getTaskDescription().equalsIgnoreCase("harvesting")){
                        holder.taskicon.setImageResource(R.drawable.ic_harvesting);
                    }
                    else if(task.getTaskDescription().equalsIgnoreCase("planting")){
                        holder.taskicon.setImageResource(R.drawable.ic_planting);
                    }
                    else if(task.getTaskDescription().equalsIgnoreCase("manuring")){
                        holder.taskicon.setImageResource(R.drawable.ic_manuring);
                    }
                }
            }
        }

    }

    @Override
    public int getItemCount(){
        Log.e("size ", taskList.size()+" size");
        return taskList.size();
    }
}
