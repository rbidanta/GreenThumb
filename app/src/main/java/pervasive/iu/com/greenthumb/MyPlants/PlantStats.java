package pervasive.iu.com.greenthumb.MyPlants;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

import pervasive.iu.com.greenthumb.DBHandler.GardenInfo;
import pervasive.iu.com.greenthumb.Login.LoginActivity;
import pervasive.iu.com.greenthumb.Model.Plants;
import pervasive.iu.com.greenthumb.R;

/**
 * Created by madrinathapa on 4/16/17.
 */

public class PlantStats extends AppCompatActivity{
    private ImageView ivPlant;
    private TextView tvPlantName;
    private FirebaseAuth firebaseAuth;
    private String currentUser;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference plantStorageRef = storage.getReference("plants");
    private StorageReference plantImagesRef;
    private Plants plantDetails;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        plantDetails = (Plants) intent.getSerializableExtra("plantInfo");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_stats);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser().getUid().toString();

        if(firebaseAuth.getCurrentUser() == null)
        {
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }

        final String plantName = plantDetails.getPlantName().toString();
        String plantImagePath = plantDetails.getPlantImagePath().toString();

        HashMap<String, String> thresholdValues = plantDetails.getThresholdValues();

        tvPlantName = (TextView) findViewById(R.id.txtPlantName);
        ivPlant = (ImageView) findViewById(R.id.imgViewPlant);
        plantImagesRef = storage.getReference(plantImagePath);

        tvPlantName.setText(plantName);

        Glide.with(getApplicationContext())
                .using(new FirebaseImageLoader())
                .load(plantImagesRef)
                .into(ivPlant);
    }

   /* @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("On create of plant stats");

        View view = inflater.inflate(R.layout.activity_plant_stats,container,false);

        Bundle bundle = getArguments();

        final Plants plantInfo = (Plants) bundle.getSerializable("plantInfo");

        final String plantName = plantInfo.getPlantName().toString();

        String plantImagePath = plantInfo.getPlantImagePath().toString();

        HashMap<String, String> thresholdValues = plantInfo.getThresholdValues();

        tvPlantName = (TextView) view.findViewById(R.id.txtPlantName);
        ivPlant = (ImageView) view.findViewById(R.id.imgViewPlant);
        plantImagesRef = storage.getReference(plantImagePath);

        tvPlantName.setText(plantName);

        Glide.with(view.getContext())
                .using(new FirebaseImageLoader())
                .load(plantImagesRef)
                .into(ivPlant);


        return view;
    }

*/

}
