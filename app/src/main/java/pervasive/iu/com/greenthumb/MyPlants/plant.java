package pervasive.iu.com.greenthumb.MyPlants;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pervasive.iu.com.greenthumb.Adapter.GardenListViewAdapter;
import pervasive.iu.com.greenthumb.Adapter.PlantListViewAdapter;
import pervasive.iu.com.greenthumb.GardenPartner.GardenOverview;
import pervasive.iu.com.greenthumb.Model.Plants;
import pervasive.iu.com.greenthumb.R;

/**
 * Created by deepakasinathan on 3/25/17.
 */

public class plant extends Fragment {
    private ImageButton btnAddPlant;
    private ListView lv;
    private ArrayList<Plants> plantList;
    private DatabaseReference plantReference;
    private Toolbar toolbar;
    private MenuItem searchMenu;
    public static boolean searchActive = false;
    private float addButtonBaseYCoordinate;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("My Plants");
        DatabaseReference def = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        plantReference = def.child(user.getUid()).child("Plants");
        if (toolbar != null){
            initToolbar();
        }
        lv = (ListView) view.findViewById(R.id.plantList);
        addButtonBaseYCoordinate = btnAddPlant.getY();
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
    }


    protected void initToolbar() {
        toolbar.setTitle("My Plants");

        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });

        Menu menu = toolbar.getMenu();

        if (menu != null) {
            searchMenu = menu.findItem(R.id.action_search);

            if (searchMenu != null) {

                SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenu);

                if (searchView != null) {
                    searchView.setQueryHint("Search");
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            return false;
                        }
                    });

                    MenuItemCompat.setOnActionExpandListener(searchMenu,
                            new MenuItemCompat.OnActionExpandListener() {

                                @Override
                                public boolean onMenuItemActionExpand(MenuItem item) {
                                    searchActive = true;
                                    addButtonVisibility(false);
                                    lv.setLongClickable(false);

                                   // realIndexesOfSearchResults = new ArrayList<Integer>();
                                   // for (int i = 0; i < notes.length(); i++)
                                  //      realIndexesOfSearchResults.add(i);

                                  //  adapter.notifyDataSetChanged();

                                    return true;
                                }

                                @Override
                                public boolean onMenuItemActionCollapse(MenuItem item) {
                                    searchEnded();
                                    return true;
                                }
                            });
                }
            }
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_main_plant, container, false);

        btnAddPlant = (ImageButton) view.findViewById(R.id.fabAdd);

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

                plantList = new ArrayList<Plants>();

                for (DataSnapshot plantSnapShot : dataSnapshot.getChildren()) {
                    Map<String, Object> plantInfoMap = (HashMap<String, Object>) plantSnapShot.getValue();

                    Plants plantInfo = new Plants();
                    plantInfo.setPlantName(plantInfoMap.get("plantName").toString());
                    plantInfo.setKitId(plantInfoMap.get("kitId").toString());
                    plantInfo.setNotificationTime(plantInfoMap.get("notificationTime").toString());
                    plantInfo.setLocation(plantInfoMap.get("location").toString());
                    plantInfo.setPlantImagePath(plantInfoMap.get("plantImagePath").toString());
                    plantInfo.setPlantId(plantInfoMap.get("plantId").toString());
                    HashMap<String, String> values = (HashMap<String, String>) plantInfoMap.get("thresholdValues");
                    plantInfo.setThresholdValues(values);
                    plantList.add(plantInfo);
                }

                PlantListViewAdapter plantAdapter = new PlantListViewAdapter(plantList,getContext());

                lv.setAdapter(plantAdapter);


                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Plants plantInfo = (Plants) parent.getItemAtPosition(position);
                        Intent myIntent = new Intent(getActivity(), AddPlantActivity.class);
                        myIntent.putExtra("plantInfo", plantInfo);
                        startActivity(myIntent);
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

    protected void searchEnded() {
        searchActive = false;
        //adapter = new Adapter(getApplicationContext(), notes, date);
      //  listView.setAdapter(adapter);
      //  listView.setLongClickable(true);
        addButtonVisibility(true);
    }

    protected void addButtonVisibility(boolean isVisible) {
        if (isVisible) {
            btnAddPlant.animate().cancel();
            btnAddPlant.animate().translationY(addButtonBaseYCoordinate);
        } else {
            btnAddPlant.animate().cancel();
            btnAddPlant.animate().translationY(addButtonBaseYCoordinate + 500);
        }
    }
}