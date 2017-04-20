package pervasive.iu.com.greenthumb.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.content.DialogInterface;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import pervasive.iu.com.greenthumb.DBHandler.GardenInfo;
import pervasive.iu.com.greenthumb.R;

/**
 * Created by Rashmi on 4/9/17.
 */

public class GardenListViewAdapter extends ArrayAdapter<GardenInfo> implements DialogInterface.OnClickListener {


    private ArrayList<GardenInfo> dataSet;
    Context mContext;








    public GardenListViewAdapter(ArrayList<GardenInfo> data, Context context) {

        super(context, R.layout.garden_list_view, data);

        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }


    public static class ViewHolder{

        public TextView gardenName;
        public TextView gardenAddress;
        public TextView gardenId;
        public de.hdodenhof.circleimageview.CircleImageView gardenImageView;
        public ImageView statusView;



    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        GardenInfo dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.garden_list_view, parent, false);
            viewHolder.gardenName = (TextView) convertView.findViewById(R.id.glname);
            viewHolder.gardenId = (TextView) convertView.findViewById(R.id.guniqeid);
            viewHolder.gardenAddress = (TextView) convertView.findViewById(R.id.gaddress);
            viewHolder.gardenImageView = (de.hdodenhof.circleimageview.CircleImageView) convertView.findViewById(R.id.garden_thumbnail);
            viewHolder.statusView = (ImageView) convertView.findViewById(R.id.statustype) ;

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference gardenStorageRef = storage.getReference(dataModel.getgFireBasePath().toString());


        viewHolder.gardenName.setText(dataModel.getgName());
        viewHolder.gardenId.setText(dataModel.getgId());
        viewHolder.gardenAddress.setText(dataModel.getgAddress());


        List<String> members = dataModel.getgMembers();

        if(!members.isEmpty()) {

            if (members.get(1).equalsIgnoreCase("owner")) {

                Glide.with(getContext())
                        .load(R.mipmap.ic_owner)
                        .into(viewHolder.statusView);

            } else if (members.get(1).equalsIgnoreCase("false")) {
                Glide.with(getContext())
                        .load(R.mipmap.ic_nonmember)
                        .into(viewHolder.statusView);
            } else if (members.get(1).equalsIgnoreCase("true")) {
                Glide.with(getContext())
                        .load(R.mipmap.ic_grantedmember)
                        .into(viewHolder.statusView);
            }
        }


        Glide.with(getContext())
                .using(new FirebaseImageLoader())
                .load(gardenStorageRef)
                .into(viewHolder.gardenImageView);




        // Return the completed view to render on screen
        return convertView;
    }


}
