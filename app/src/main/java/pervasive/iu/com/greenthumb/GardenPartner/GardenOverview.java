package pervasive.iu.com.greenthumb.GardenPartner;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import pervasive.iu.com.greenthumb.DBHandler.GardenInfo;
import pervasive.iu.com.greenthumb.R;

public class GardenOverview extends Fragment {


    private EditText gName,gAddress,gOwnerName,gContactNumber;
    private Button reqMembership;
    private ImageView gardenImage;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference dbreference;
    private DatabaseReference userReference ;
    // Create a storage reference from our app
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference gardenStorageRef = storage.getReference("gardens");
    private StorageReference gardenImagesRef;

    private String currentUser ="";
    private String firstname = "";
    private String lastname = "";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.garden_overview);
        firebaseAuth = FirebaseAuth.getInstance();

        currentUser = firebaseAuth.getCurrentUser().getUid().toString();


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.activity_garden_overview,container,false);

        Bundle bundle = getArguments();

        final GardenInfo gInfo = (GardenInfo)bundle.getSerializable("gInfo");

        final String gardenName = gInfo.getgName().toString();

        String gardenOwner = gInfo.getgOwner().toString();

        String contactNumber = gInfo.getgOwnerPhone().toString();

        String location = gInfo.getgAddress();

        String imagePath = gInfo.getgFireBasePath();



        gardenImagesRef = storage.getReference(imagePath);


        gName = (EditText) view.findViewById(R.id.g_name);
        gAddress = (EditText) view.findViewById(R.id.g_address);
        gOwnerName = (EditText) view.findViewById(R.id.g_owner);
        gContactNumber = (EditText) view.findViewById(R.id.g_contactNumber);
        gardenImage = (ImageView) view.findViewById(R.id.gardenOImage);
        reqMembership = (Button) view.findViewById(R.id.req_memship);


        Glide.with(view.getContext())
                .using(new FirebaseImageLoader())
                .load(gardenImagesRef)
                .into(gardenImage);

        gardenImage.setVisibility(View.VISIBLE);
       // gardenImage.setVisibility(View.INVISIBLE);

        // For fetching the name of Owner

        userReference = FirebaseDatabase.getInstance().getReference(gardenOwner);

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    if(ds.getKey().equalsIgnoreCase("firstname"))
                        firstname = ds.getValue().toString();
                    else if(ds.getKey().equalsIgnoreCase("lastname"))
                        lastname = ds.getValue().toString();

                    while(!firstname.isEmpty() && !lastname.isEmpty()){
                        gOwnerName.setText(firstname + " " + lastname);
                        break;
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        gName.setText(gardenName);
        gAddress.setText(location);
        gContactNumber.setText(contactNumber);
        if(currentUser.equalsIgnoreCase(gardenOwner)){

            gName.setEnabled(true);
            gOwnerName.setEnabled(true);
            gAddress.setEnabled(true);
            gContactNumber.setEnabled(true);
            reqMembership.setVisibility(View.INVISIBLE);
        }else{

            gName.setEnabled(false);
            gOwnerName.setEnabled(false);
            gAddress.setEnabled(false);
            gContactNumber.setEnabled(false);
            reqMembership.setVisibility(View.VISIBLE);
        }



        reqMembership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                requestMembership(gInfo.getgId(),currentUser);

                Toast.makeText(getActivity(),"Membership Request Sent to Garden Owner",Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }



    @Override
    public void onStart() {
        super.onStart();
    }



    private void requestMembership(String gardenId , String requesterId){

        DatabaseReference gardenReference = FirebaseDatabase.getInstance().getReference("gardens");
        gardenReference.child(gardenId).child("gMembers").child(requesterId).setValue(Boolean.FALSE);

    }






}
