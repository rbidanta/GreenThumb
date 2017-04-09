package pervasive.iu.com.greenthumb.MyPlants;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import pervasive.iu.com.greenthumb.R;

/**
 * Created by deepakasinathan on 3/25/17.
 */

public class plant extends Fragment {
    private ImageButton btnAddPlant;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       // btnAddPlant = (ImageButton) getView().findViewById(R.id.btnAdd);
        getActivity().setTitle("My plant");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.plant,container,false);
        View view = inflater.inflate(R.layout.plant,container,false);

        btnAddPlant = (ImageButton) view.findViewById(R.id.btnAdd);

        btnAddPlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddPlantActivity.class));
            }
        });

        return view;
    }
   /* @Override
    public void onClick(View v) {
        Toast toast = Toast.makeText(getActivity(), "Hello", Toast.LENGTH_SHORT);
        toast.show();
        if(v == btnAddPlant) {
            //getActivity().finish();
            startActivity(new Intent(getActivity(), AddPlantActivity.class));
        }
    }*/
}
