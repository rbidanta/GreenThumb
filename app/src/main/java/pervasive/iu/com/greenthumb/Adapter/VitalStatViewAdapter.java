package pervasive.iu.com.greenthumb.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

import pervasive.iu.com.greenthumb.Model.Plants;
import pervasive.iu.com.greenthumb.R;

/**
 * Created by madrinathapa on 4/16/17.
 */

public class VitalStatViewAdapter extends BaseAdapter{

    private final ArrayList mData;

    public VitalStatViewAdapter(HashMap<String, String> map) {
        mData = new ArrayList();
        mData.addAll(map.entrySet());
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public HashMap.Entry<String, String> getItem(int position) {
        return (HashMap.Entry) mData.get(position);
    }

    public static class ViewHolder{

        public ImageView imgViewStat;
        public TextView textViewStatName;
        public TextView textViewStatVal;
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;
        VitalStatViewAdapter.ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new VitalStatViewAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.plant_vital_stat_list_view, parent, false);
            viewHolder.textViewStatName = (TextView) convertView.findViewById(R.id.tvStatName);
            viewHolder.textViewStatVal = (TextView) convertView.findViewById(R.id.tvStatValue);
            viewHolder.imgViewStat = (ImageView) convertView.findViewById(R.id.ivStat);

            if(position == 0){
                Glide.with(parent.getContext())
                        .load(R.mipmap.ic_wb_sunny)
                        .into(viewHolder.imgViewStat);

            }else if(position == 1){
                Glide.with(parent.getContext())
                        .load(R.drawable.ic_moist)
                        .into(viewHolder.imgViewStat);
            }else if(position == 2){
                Glide.with(parent.getContext())
                        .load(R.mipmap.ic_thermometer)
                        .into(viewHolder.imgViewStat);
            } else{
                Glide.with(parent.getContext())
                        .load(R.mipmap.ic_fertilizer)
                        .into(viewHolder.imgViewStat);
            }

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (VitalStatViewAdapter.ViewHolder) convertView.getTag();
            result=convertView;
        }

        HashMap.Entry<String, String> item = getItem(position);
        viewHolder.textViewStatName.setText(item.getKey());
        viewHolder.textViewStatVal.setText(item.getValue());
        return convertView;
    }
}