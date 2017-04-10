package pervasive.iu.com.greenthumb.GardenPartner;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pervasive.iu.com.greenthumb.DBHandler.GardenInfo;
import pervasive.iu.com.greenthumb.R;

public class GardenOverview extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.activity_garden_overview,container,false);

        Bundle bundle = getArguments();

        GardenInfo gInfo = (GardenInfo)bundle.getSerializable("gInfo");

        String gardenName = gInfo.getgName().toString();

        TextView textView = (TextView) view.findViewById(R.id.ovrvwg_name);

        textView.setText(gardenName);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
