package pervasive.iu.com.greenthumb.GardenPartner.TabLayoutFragment;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import pervasive.iu.com.greenthumb.DBHandler.GardenInfo;
import pervasive.iu.com.greenthumb.KanbanActivity;
import pervasive.iu.com.greenthumb.Model.TasksInfo;
import pervasive.iu.com.greenthumb.Model.TasksPOJO;
import pervasive.iu.com.greenthumb.R;

public class TaskUpdateActivity extends AppCompatActivity {

    private TasksPOJO tasksPOJO;
    private TextView taskDescription, taskDeadline;
    private Button taskCompleted;
    private GardenInfo ginfo;
    private DatabaseReference gardenReference;
    private ImageView taskfarmingimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_update);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        toolbar.setTitle("Task Update Activity");

        gardenReference = FirebaseDatabase.getInstance().getReference("taskinstance");

        Bundle bundle = getIntent().getExtras();
        tasksPOJO = (TasksPOJO) bundle.getSerializable("task");
        ginfo = (GardenInfo) bundle.getSerializable("gInfo");

        taskDescription = (TextView) findViewById(R.id.taskDescription);
        taskDeadline = (TextView) findViewById(R.id.taskDeadline);
        taskCompleted = (Button) findViewById(R.id.taskCompleted);
        taskfarmingimage = (ImageView) findViewById(R.id.taskfarmingimage);

        taskDescription.setText(tasksPOJO.getTaskDescription());

        Date d = new Date(Long.parseLong(tasksPOJO.getTaskDeadLine()) * 1000);
        taskDeadline.setText(d.toString());


        if (tasksPOJO.getTaskDescription().equalsIgnoreCase("watering")){
            taskfarmingimage.setImageResource(R.drawable.ic_watering);
        }
        else if(tasksPOJO.getTaskDescription().equalsIgnoreCase("pruning")){
            taskfarmingimage.setImageResource(R.drawable.ic_pruning);
        }
        else if(tasksPOJO.getTaskDescription().equalsIgnoreCase("weeding")){
            taskfarmingimage.setImageResource(R.drawable.ic_weeding);
        }
        else if(tasksPOJO.getTaskDescription().equalsIgnoreCase("mulching")){
            taskfarmingimage.setImageResource(R.drawable.ic_mulching);
        }
        else if(tasksPOJO.getTaskDescription().equalsIgnoreCase("harvesting")){
            taskfarmingimage.setImageResource(R.drawable.ic_harvesting);
        }
        else if(tasksPOJO.getTaskDescription().equalsIgnoreCase("planting")){
            taskfarmingimage.setImageResource(R.drawable.ic_planting);
        }
        else if(tasksPOJO.getTaskDescription().equalsIgnoreCase("manuring")){
            taskfarmingimage.setImageResource(R.drawable.ic_manuring);
        }

        taskCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gardenReference = FirebaseDatabase.getInstance().getReference("taskinstance");

                DatabaseReference taskReference = FirebaseDatabase.getInstance().getReference("tasks");

                taskReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot ds : dataSnapshot.getChildren()){

                            Map<String,String> taskdetail = (HashMap<String, String>) ds.getValue();

                            System.out.println("Task Details:"+taskdetail);

                            //TasksInfo taskInfo = new TasksInfo();
                            DatabaseReference taskGardenRoot = FirebaseDatabase.getInstance().getReference("taskinstance");

                            String taskinstacneId = tasksPOJO.getTaskID();
                            String currenttaskname = tasksPOJO.getTaskDescription();

                            long deadline = Calendar.getInstance().getTimeInMillis();

                            String assignedTo = " ";
                            String completedBy = " ";


                            String taskname = taskdetail.get("description");

                            if(currenttaskname.equals(taskname)){
                                String frequency = taskdetail.get("frequency");
                                Map<String,Long> taskdetail1 = (HashMap<String, Long>) ds.getValue();
                                final long allocatedpoints = taskdetail1.get("reward");

                                if(frequency.equalsIgnoreCase("weekly")){

                                    deadline = deadline + toMilliSeconds(7);

                                }else if(frequency.equalsIgnoreCase("months")){
                                    deadline = deadline + toMilliSeconds(30);

                                }else if(frequency.equalsIgnoreCase("yearly")){
                                    deadline = deadline + toMilliSeconds(180);

                                }else{
                                    deadline = deadline + toMilliSeconds(365);
                                }

                                TasksInfo tasksInfo = new TasksInfo(taskinstacneId,taskname,deadline,assignedTo,completedBy);
                                taskGardenRoot.child(tasksPOJO.getGardenid()).child(taskinstacneId).setValue(tasksInfo);

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("leaderboard");
                                final DatabaseReference ref1 = ref.child(tasksPOJO.getGardenid()).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                ref1.addListenerForSingleValueEvent(new ValueEventListener(){
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String points = "";
                                        points = String.valueOf(dataSnapshot.getValue());
                                        long updatedPoints = 0;

                                        updatedPoints = Integer.parseInt(points) + allocatedpoints;

                                        ref1.setValue(updatedPoints);

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                TaskUpdateActivity.super.onBackPressed();


                //gardenReference.child(tasksPOJO.getGardenid()).child(tasksPOJO.getTaskID()).child("deadline").setValue("");
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        gardenReference = FirebaseDatabase.getInstance().getReference("leaderboard");

        gardenReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot gardenSnapshot : dataSnapshot.getChildren()){

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

                public static long toMilliSeconds(double day) {
        return (long) (day * 24 * 60 * 60 * 1000);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
