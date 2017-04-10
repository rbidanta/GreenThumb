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
import android.widget.TextView;

import java.util.ArrayList;

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
        public TextView gardenId;



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
            /*viewHolder.txtVersion = (TextView) convertView.findViewById(R.id.version_number);
            viewHolder.info = (ImageView) convertView.findViewById(R.id.item_info);
*/
            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }


        viewHolder.gardenName.setText(dataModel.getgName());
        viewHolder.gardenId.setText(dataModel.getgId());

        // Return the completed view to render on screen
        return convertView;
    }




}
