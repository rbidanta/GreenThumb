package pervasive.iu.com.greenthumb.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

import pervasive.iu.com.greenthumb.DBHandler.GardenInfo;
import pervasive.iu.com.greenthumb.GardenPartner.TabLayoutFragment.TaskUpdateActivity;
import pervasive.iu.com.greenthumb.Model.Plants;
import pervasive.iu.com.greenthumb.Model.TasksPOJO;
import pervasive.iu.com.greenthumb.MyPlants.AddPlantActivity;
import pervasive.iu.com.greenthumb.R;

/**
 * Created by madrinathapa on 4/15/17.
 */

public class PlantListViewAdapter extends ArrayAdapter<Plants> implements DialogInterface.OnClickListener  {


    private ArrayList<Plants> dataSet;
    private Context mContext;
    private Activity activity;


    public PlantListViewAdapter(ArrayList<Plants> data, Context context) {

        super(context, R.layout.plant_list_view, data);

        this.dataSet = data;
        this.mContext=context;

        this.activity = (Activity)context;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public de.hdodenhof.circleimageview.CircleImageView plantImage, plantStat;
        public TextView plantName;
        public TextView plantId;
        public RelativeLayout plantlistviewparent;
        private int position = 0;

        public ViewHolder(View convertView){
            super(convertView);

            plantlistviewparent = (RelativeLayout) convertView.findViewById(R.id.plantlistviewparent);
            plantName = (TextView) convertView.findViewById(R.id.plName);
            plantId = (TextView) convertView.findViewById(R.id.plId);
            plantImage = (de.hdodenhof.circleimageview.CircleImageView ) convertView.findViewById(R.id.plImage);
            plantStat = (de.hdodenhof.circleimageview.CircleImageView) convertView.findViewById(R.id.plStat);

            plantlistviewparent.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            Plants plantInfo = dataSet.get(position);

            Intent myIntent = new Intent(mContext, AddPlantActivity.class);
            myIntent.putExtra("plantInfo", plantInfo);
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(activity, (View)plantImage, "plantImageTransition");
            mContext.startActivity(myIntent,options.toBundle());
        }

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        try{
            // Get the data item for this position
            Plants dataModel = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            PlantListViewAdapter.ViewHolder viewHolder; // view lookup cache stored in tag

            if (dataModel != null) {
                final View result;

                if (convertView == null) {

                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    convertView = inflater.inflate(R.layout.plant_list_view, parent, false);
                    viewHolder = new PlantListViewAdapter.ViewHolder(convertView);

                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (PlantListViewAdapter.ViewHolder) convertView.getTag();
                    result = convertView;
                }

                HashMap<String, String> values = dataModel.getThresholdValues();
                boolean isSafe = true;
                viewHolder.position = position;
                for(HashMap.Entry<String, String> statEntry : values.entrySet()) {
                    if (statEntry.getKey().equals("sunglight")) {
                        if (Double.parseDouble(statEntry.getValue()) > 50000 || Double.parseDouble(statEntry.getValue()) < 25000) {
                            isSafe = false;
                            break;
                        }
                    }else if (statEntry.getKey().equals("moisture")) {
                        if (Double.parseDouble(statEntry.getValue()) > 50) {
                            isSafe = false;
                            break;
                        }
                    }else if(statEntry.getKey().equals("ph")) {
                        if (Double.parseDouble(statEntry.getValue()) > 2) {
                            isSafe = false;
                            break;
                        }
                    }else if(statEntry.getKey().equals("temperature")) {
                        if (Double.parseDouble(statEntry.getValue()) > 100 || Double.parseDouble(statEntry.getValue()) < 30) {
                            isSafe = false;
                            break;
                        }
                    }
                }

                if(!isSafe){
                    Glide.with(getContext())
                            .load(R.mipmap.ic_danger)
                            .into(viewHolder.plantStat);
                }else{
                    Glide.with(getContext())
                            .load(R.mipmap.ic_safe)
                            .into(viewHolder.plantStat);
                }

                viewHolder.plantName.setText(dataModel.getPlantName());
                viewHolder.plantId.setText(dataModel.getPlantId());

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference plantImagesRef = storage.getReference(dataModel.getPlantImagePath());

                Glide.with(getContext())
                        .using(new FirebaseImageLoader())
                        .load(plantImagesRef)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(viewHolder.plantImage);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return convertView;
    }
}
