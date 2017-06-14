package pervasive.iu.com.greenthumb.TasksRecyclerView;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;
import java.util.List;

import pervasive.iu.com.greenthumb.DBHandler.GardenInfo;
import pervasive.iu.com.greenthumb.GardenPartner.TabLayoutFragment.TaskUpdateActivity;
import pervasive.iu.com.greenthumb.Model.LeaderBoardPojo;
import pervasive.iu.com.greenthumb.Model.TasksPOJO;
import pervasive.iu.com.greenthumb.R;

/**
 * Created by madrinathapa on 4/19/17.
 */

public class RewardsRecyclerViewAdapter extends RecyclerView.Adapter<RewardsRecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<LeaderBoardPojo> leaderBoardPojoList;
    private String module = "";
    private GardenInfo gardenInfo;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tasksrewardname, tasksreward, place;
        public LinearLayout tasksrewardcarditem;
        public ImageView userProfilePic;

        public MyViewHolder(View view) {
            super(view);
            tasksrewardcarditem = (LinearLayout) view.findViewById(R.id.tasksrewardcarditem);
            tasksrewardname = (TextView) view.findViewById(R.id.tasksrewardname);
            tasksreward = (TextView) view.findViewById(R.id.tasksreward);
            place = (TextView) view.findViewById(R.id.place);
            userProfilePic = (ImageView) view.findViewById(R.id.userProfilePic);
        }
    }

    public RewardsRecyclerViewAdapter(Context mContext, List<LeaderBoardPojo> leaderBoardPojoList, GardenInfo ginfo) {
        this.mContext = mContext;
        this.leaderBoardPojoList = leaderBoardPojoList;
        this.gardenInfo = ginfo;
    }

    @Override
    public RewardsRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.taskrewarditem, parent, false);
        return new RewardsRecyclerViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final LeaderBoardPojo leaderBoardPojo = this.leaderBoardPojoList.get(position);
        holder.tasksrewardname.setText(leaderBoardPojo.getFirstName()+" "+leaderBoardPojo.getLastName());
        holder.tasksreward.setText(leaderBoardPojo.getRewardpoints()+"");
        holder.place.setText("Rank: "+leaderBoardPojo.getPosition());


        String token = FirebaseInstanceId.getInstance().getToken();
        final FirebaseStorage storage = FirebaseStorage.getInstance();
        DatabaseReference userref = FirebaseDatabase.getInstance().getReference(leaderBoardPojo.getUid());
        userref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Log.e("extra something else", "in here");
                    if (ds.getKey().equalsIgnoreCase("UserImagePath")) {
                        System.out.println("extra something else User image path" + ds.getValue().toString());
                        String mCurrentPhotoPath = ds.getValue().toString();
                        StorageReference userImageRef = storage.getReference(mCurrentPhotoPath);

                        Glide.with(mContext)
                                .using(new FirebaseImageLoader())
                                .load(userImageRef)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .listener(new RequestListener<StorageReference, GlideDrawable>() {
                                    @Override
                                    public boolean onException(Exception e, StorageReference model, Target<GlideDrawable> target, boolean isFirstResource) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(GlideDrawable resource, StorageReference model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                        holder.userProfilePic.setBackgroundResource(0);
                                        return false;
                                    }
                                })
                                .into(holder.userProfilePic);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Log.e("position ", leaderBoardPojo.getPosition()+"");
    }

    @Override
    public int getItemCount(){
        return leaderBoardPojoList.size();
    }
}
