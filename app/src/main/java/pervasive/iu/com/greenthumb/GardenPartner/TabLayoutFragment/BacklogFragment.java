package pervasive.iu.com.greenthumb.GardenPartner.TabLayoutFragment;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import pervasive.iu.com.greenthumb.DBHandler.DataBaseManager;
import pervasive.iu.com.greenthumb.DBHandler.DataBaseQuery;
import pervasive.iu.com.greenthumb.DBHandler.GardenInfo;
import pervasive.iu.com.greenthumb.Model.TasksPOJO;
import pervasive.iu.com.greenthumb.R;
import pervasive.iu.com.greenthumb.TasksRecyclerView.TasksRecyclerViewAdapter;

/**
 * Created by raakeshpremkumar on 3/25/17.
 */

public class BacklogFragment extends Fragment {

    private RecyclerView recyclerView;
    private TasksRecyclerViewAdapter tasksRecyclerViewAdapter;
    private DataBaseManager databaseManager;
    private ArrayList<TasksPOJO> task_list;
    private GardenInfo gInfo;
    private DatabaseReference gardenReference;
    private View view;
    private LayoutInflater inflater;
    private ViewGroup container;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gardenReference = FirebaseDatabase.getInstance().getReference("taskinstance");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.activity_backlog_fragment, container, false);

        this.inflater = inflater;
        this.container = container;

        recyclerView = (RecyclerView) view.findViewById(R.id.breaklogfragment_recyclerView);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        gardenReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                task_list = new ArrayList<TasksPOJO>();

                Bundle bundle = getArguments();

                GardenInfo gInfo = (GardenInfo)bundle.getSerializable("gInfo");

                String gid = gInfo.getgId();

                for (DataSnapshot tasksnapshot: dataSnapshot.getChildren()){
                    Map<String,Object> gInfoMap = (HashMap<String, Object>) tasksnapshot.getValue();
                    TasksPOJO tasksPOJO = new TasksPOJO();

                    Log.e("key&gid ", tasksnapshot.getKey()+" "+gid);

                    if(tasksnapshot.getKey().equals(gid)){
                        for (String key: gInfoMap.keySet()){
                            tasksPOJO = new TasksPOJO();
                            HashMap<String, String> taskmap = (HashMap<String, String>)gInfoMap.get(key);
                            tasksPOJO.setGardenid(key);
                            tasksPOJO.setTaskDescription(taskmap.get("taskname"));
                            tasksPOJO.setTaskDeadLine(String.valueOf(taskmap.get("deadline")));
                            tasksPOJO.setTaskID(taskmap.get("taskId"));
                            tasksPOJO.setGardenid(gid);
                            task_list.add(tasksPOJO);
                        }
                    }
                }

                //populate(view);
                view = inflater.inflate(R.layout.activity_backlog_fragment, container, false);
                Context context = getActivity();

                tasksRecyclerViewAdapter = new TasksRecyclerViewAdapter("backlog", getActivity(), task_list, gInfo);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
                mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(mLayoutManager);
                //recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(tasksRecyclerViewAdapter);
                Log.e("tasksPojo ", task_list.toString()+ " " +task_list.size());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateOrInsertDb(int sno){
        ContentValues contentValues = new ContentValues();

        contentValues.put(DataBaseQuery.S_NO, sno);
        contentValues.put(DataBaseQuery.TASK_STATUS, "Backlog");
        contentValues.put(DataBaseQuery.TASK_DESCRIPTION, "my task");
        contentValues.put(DataBaseQuery.TASK_DEADLINE, "22/07/2015");

        databaseManager.getInstance(getActivity()).insertTaskDetails(contentValues);
    }


}

