package pervasive.iu.com.greenthumb.MyPlants;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pervasive.iu.com.greenthumb.GardenPartner.GardenOverview;
import pervasive.iu.com.greenthumb.R;

/**
 * Created by deepakasinathan on 3/25/17.
 */

public class plant extends Fragment {
    private ImageButton btnAddPlant;
    private ListView lv;
    private List<String> plantList;
    private DatabaseReference plantReference;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("My plant");
        plantReference = FirebaseDatabase.getInstance().getReference("Plants");

        lv = (ListView) view.findViewById(R.id.plantList);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.plant,container,false);
        View view = inflater.inflate(R.layout.activity_main_plant, container, false);

        btnAddPlant = (ImageButton) view.findViewById(R.id.btnAdd);

        btnAddPlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddPlantActivity.class));
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        plantReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                plantList = new ArrayList<String>();

                for (DataSnapshot gardenSnapshot : dataSnapshot.getChildren()) {
                    Map<String, Object> gInfoMap = (HashMap<String, Object>) gardenSnapshot.getValue();

                    plantList.add(gInfoMap.get("plantName").toString());
                }


                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        getActivity(),
                        android.R.layout.simple_list_item_1,
                        plantList);
                lv.setAdapter(arrayAdapter);


                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        String name = (String) parent.getItemAtPosition(position);

                        Fragment frag = new GardenOverview();
                        Bundle bundle = new Bundle();
                        bundle.putString("gardenName", name);
                        frag.setArguments(bundle);

                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.content_navigation, frag); // give your fragment container id in first parameter
                        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                        transaction.commit();
                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });


    }


    @Override
    public void onOptionsMenuClosed(Menu menu) {
        super.onOptionsMenuClosed(menu);

    }
}