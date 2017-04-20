package pervasive.iu.com.greenthumb.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
import pervasive.iu.com.greenthumb.Model.Plants;
import pervasive.iu.com.greenthumb.R;

/**
 * Created by madrinathapa on 4/15/17.
 */

public class PlantListViewAdapter extends ArrayAdapter<Plants> implements DialogInterface.OnClickListener  {


    private ArrayList<Plants> dataSet;
    Context mContext;


    public PlantListViewAdapter(ArrayList<Plants> data, Context context) {

        super(context, R.layout.plant_list_view, data);

        this.dataSet = data;
        this.mContext=context;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }


    public static class ViewHolder{

        public de.hdodenhof.circleimageview.CircleImageView plantImage;
        public TextView plantName;
        public TextView plantId;
        public de.hdodenhof.circleimageview.CircleImageView plantStat;

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

                    viewHolder = new PlantListViewAdapter.ViewHolder();
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    convertView = inflater.inflate(R.layout.plant_list_view, parent, false);
                    viewHolder.plantName = (TextView) convertView.findViewById(R.id.plName);
                    viewHolder.plantId = (TextView) convertView.findViewById(R.id.plId);
                    viewHolder.plantImage = (de.hdodenhof.circleimageview.CircleImageView ) convertView.findViewById(R.id.plImage);
                    viewHolder.plantStat = (de.hdodenhof.circleimageview.CircleImageView) convertView.findViewById(R.id.plStat);
                    result = convertView;

                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (PlantListViewAdapter.ViewHolder) convertView.getTag();
                    result = convertView;
                }

                HashMap<String, String> values = dataModel.getThresholdValues();
                boolean isSafe = true;
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
