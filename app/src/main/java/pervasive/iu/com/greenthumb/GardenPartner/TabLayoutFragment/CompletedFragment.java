package pervasive.iu.com.greenthumb.GardenPartner.TabLayoutFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pervasive.iu.com.greenthumb.DBHandler.GardenInfo;
import pervasive.iu.com.greenthumb.Model.LeaderBoardPojo;
import pervasive.iu.com.greenthumb.R;
import pervasive.iu.com.greenthumb.TasksRecyclerView.RewardsRecyclerViewAdapter;
import pervasive.iu.com.greenthumb.TasksRecyclerView.TasksRecyclerViewAdapter;

/**
 * Created by raakeshpremkumar on 3/25/17.
 */

public class CompletedFragment extends Fragment {

    private DatabaseReference gardenReference;
    private View view;
    private LayoutInflater inflater;
    private ViewGroup container;
    private RewardsRecyclerViewAdapter rewardsRecyclerViewAdapter;
    private GardenInfo gInfo;
    private RecyclerView completed_recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gardenReference = FirebaseDatabase.getInstance().getReference("leaderboard");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_completed_fragment, container, false);
        completed_recyclerView = (RecyclerView) view.findViewById(R.id.completed_recyclerView);
        this.inflater = inflater;
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        Bundle bundle = getArguments();

        gInfo = (GardenInfo)bundle.getSerializable("gInfo");

        final String gid = gInfo.getgId();

        gardenReference.child(gid);

        gardenReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final ArrayList<LeaderBoardPojo> leaderBoardPojoArrayList = new ArrayList<LeaderBoardPojo>();

                for (DataSnapshot tasksnapshot: dataSnapshot.getChildren()) {
                    Log.e("reward [age", tasksnapshot.getKey()+"");
                    if(tasksnapshot.getKey().equals(gid)){
                        Map<String,Long> leaderboarddetails = (HashMap<String, Long>) tasksnapshot.getValue();

                        for (String uid: leaderboarddetails.keySet()) {
                            final long reward = leaderboarddetails.get(uid);

                            final LeaderBoardPojo leaderBoardPojo = new LeaderBoardPojo();
                            leaderBoardPojo.setUid(uid);
                            leaderBoardPojo.setRewardpoints(reward);
                            leaderBoardPojoArrayList.add(leaderBoardPojo);
                        }
                    }
                }

                Log.e("in here", "in here " + leaderBoardPojoArrayList.size());


                for (int i = 0; i < leaderBoardPojoArrayList.size();i++){
                    LeaderBoardPojo leaderBoardPojo1 = leaderBoardPojoArrayList.get(i);
                    for (int j = i; j< leaderBoardPojoArrayList.size(); j++){
                        LeaderBoardPojo leaderBoardPojo2 = leaderBoardPojoArrayList.get(j);
                        if (leaderBoardPojo1.getRewardpoints() < leaderBoardPojo2.getRewardpoints()){
                            LeaderBoardPojo temp = leaderBoardPojo1;
                            leaderBoardPojoArrayList.set(i, leaderBoardPojo2);
                            leaderBoardPojoArrayList.set(j, temp);
                        }
                    }
                }

                int counter = 0;

                for (int i = 0 ; i < leaderBoardPojoArrayList.size(); i ++){

                    final LeaderBoardPojo leaderBoardPojo = leaderBoardPojoArrayList.get(i);
                    final int count = i;
                    final int countLimit = leaderBoardPojoArrayList.size()-1;

                    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference(leaderBoardPojo.getUid());

                    dbref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.e("in here","in here ");
                            HashMap<String, String> userDetail = (HashMap<String,String>)dataSnapshot.getValue();

                            leaderBoardPojo.setFirstName(userDetail.get("firstname"));
                            leaderBoardPojo.setLastName(userDetail.get("lastname"));
                            leaderBoardPojoArrayList.set(count, leaderBoardPojo);

                            if (count == countLimit){
                                populateList(leaderBoardPojoArrayList);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void populateList(ArrayList<LeaderBoardPojo> leaderBoardPojos){

        int rewardPosition = 0;
        long rewardPoint = 0;

        for (int i = 0; i < leaderBoardPojos.size(); i++){
            LeaderBoardPojo leaderBoardPojo1 = leaderBoardPojos.get(i);

            Log.e("comparison ", leaderBoardPojo1.getRewardpoints()+" "+rewardPoint);
            if (leaderBoardPojo1.getRewardpoints() > rewardPoint){
                rewardPosition = rewardPosition + 1;
                leaderBoardPojo1.setPosition(rewardPosition);

                rewardPoint = leaderBoardPojo1.getRewardpoints();
                leaderBoardPojos.set(i, leaderBoardPojo1);
            }
            else if (leaderBoardPojo1.getRewardpoints() < rewardPoint){
                rewardPosition = rewardPosition + 1;
                leaderBoardPojo1.setPosition(rewardPosition);

                rewardPoint = leaderBoardPojo1.getRewardpoints();
                leaderBoardPojos.set(i, leaderBoardPojo1);
            }
            else {
                rewardPoint = leaderBoardPojo1.getRewardpoints();
                leaderBoardPojo1.setPosition(rewardPosition);
                leaderBoardPojos.set(i, leaderBoardPojo1);
            }
        }

        for (LeaderBoardPojo leaderBoardPojo: leaderBoardPojos){
            Log.e("place ", leaderBoardPojo.getPosition()+"");
        }

        view = inflater.inflate(R.layout.activity_completed_fragment, container, false);
        Context context = getActivity();

        rewardsRecyclerViewAdapter = new RewardsRecyclerViewAdapter(getActivity(), leaderBoardPojos, gInfo);
        //LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);

        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        //mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;

        //mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        completed_recyclerView.setLayoutManager(mLayoutManager);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        completed_recyclerView.setAdapter(rewardsRecyclerViewAdapter);
    }
}
