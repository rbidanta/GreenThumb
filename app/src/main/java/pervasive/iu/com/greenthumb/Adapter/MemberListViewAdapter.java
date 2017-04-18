package pervasive.iu.com.greenthumb.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.content.DialogInterface;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import pervasive.iu.com.greenthumb.DBHandler.GardenInfo;
import pervasive.iu.com.greenthumb.DBHandler.MemberInfo;
import pervasive.iu.com.greenthumb.DBHandler.saveInfo;
import pervasive.iu.com.greenthumb.R;

/**
 * Created by Rashmi on 4/17/17.
 */

public class MemberListViewAdapter extends ArrayAdapter<MemberInfo> implements DialogInterface.OnClickListener {




    private ArrayList dataSet;
    Context mContext;





    public MemberListViewAdapter(ArrayList data, Context context) {

        super(context, R.layout.member_list_view, data);

        this.dataSet = data;
        this.mContext=context;

    }


    @Override
    public void onClick(DialogInterface dialog, int which) {

    }


    public static class ViewHolder{

        public TextView memberName;
        public TextView memberId;
        public CheckBox selectMember;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        MemberInfo dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        MemberListViewAdapter.ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new MemberListViewAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.member_list_view, parent, false);
            viewHolder.memberName = (TextView) convertView.findViewById(R.id.memberName);
            viewHolder.memberId = (TextView) convertView.findViewById(R.id.memberId);
            viewHolder.selectMember = (CheckBox) convertView.findViewById(R.id.memCheckBox) ;
            /*viewHolder.txtVersion = (TextView) convertView.findViewById(R.id.version_number);
            viewHolder.info = (ImageView) convertView.findViewById(R.id.item_info);
*/
            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MemberListViewAdapter.ViewHolder) convertView.getTag();
            result=convertView;
        }


        viewHolder.memberName.setText(dataModel.getFirstname()+" "+dataModel.getLastname());
        viewHolder.memberId.setText(dataModel.getUserId());

        // Return the completed view to render on screen
        return convertView;
    }


}
