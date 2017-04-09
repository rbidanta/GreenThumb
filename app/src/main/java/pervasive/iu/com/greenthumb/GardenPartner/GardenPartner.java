package pervasive.iu.com.greenthumb.GardenPartner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import pervasive.iu.com.greenthumb.DBHandler.GardenInfo;
import pervasive.iu.com.greenthumb.R;

/**
 *  GardenPartner: This is the screen when the user opens Garden Partner
 */





public class GardenPartner extends Fragment{

    private ListView lv;
    private List<String> gardenList;
    private DatabaseReference gardenReference;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Garden partner");

        gardenReference = FirebaseDatabase.getInstance().getReference("gardens");

        lv = (ListView) view.findViewById(R.id.gardenlist);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.garden_partner,container,false);

        FloatingActionButton addNewGarden = (FloatingActionButton) view.findViewById(R.id.addnewgarden);

        addNewGarden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RegisterGarden.class));
            }
        });



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        gardenReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                gardenList = new ArrayList<String>();

                for(DataSnapshot gardenSnapshot : dataSnapshot.getChildren()){
                    Map<String,Object> gInfoMap = (HashMap<String, Object>) gardenSnapshot.getValue();

                    //System.out.println("gInfoMap"+ gInfoMap);

                    //gardenSnapshot.getKey().

                    gardenList.add(gInfoMap.get("gName").toString());

                }


                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        getActivity(),
                        android.R.layout.simple_list_item_1,
                        gardenList);
                lv.setAdapter(arrayAdapter);


                lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                        String name = (String) parent.getItemAtPosition(position);

                        /*Intent gardenOverviewIntent = new Intent(getActivity(), GardenOverview.class);

                        gardenOverviewIntent.putExtra("gardenName", name);


                        startActivity(gardenOverviewIntent);*/


                        Fragment frag = new GardenOverview();
                        Bundle bundle = new Bundle();
                        bundle.putString("gardenName",name);
                        frag.setArguments(bundle);

                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.content_navigation, frag ); // give your fragment container id in first parameter
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
