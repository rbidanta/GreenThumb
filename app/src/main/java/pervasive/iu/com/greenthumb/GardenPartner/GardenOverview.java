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

import java.util.HashMap;
import java.util.Map;

import pervasive.iu.com.greenthumb.DBHandler.GardenInfo;
import pervasive.iu.com.greenthumb.R;

public class GardenOverview extends Fragment {


    private EditText gName,gAddress,gOwnerName,gContactNumber;
    private Button reqMembership,cancelMembership,viewRequests;
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

    boolean hasMemebershipRequested = false;


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
        //cancelMembership = (Button) view.findViewById(R.id.cancel_memship);
        viewRequests = (Button) view.findViewById(R.id.view_requests);


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
            cancelMembership.setVisibility(View.INVISIBLE);
            viewRequests.setVisibility(View.VISIBLE);
        }else{

            gName.setEnabled(false);
            gOwnerName.setEnabled(false);
            gAddress.setEnabled(false);
            gContactNumber.setEnabled(false);
            // Check if the current user has requested membership
            reqMembership.setVisibility(View.VISIBLE);
            hasMembershipRequested(gInfo.getgId(),currentUser);

        }



        reqMembership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(reqMembership.getText().toString().equalsIgnoreCase("Request Membership")){

                    requestMembership(gInfo.getgId(),currentUser);
                    //reqMembership.setVisibility(View.INVISIBLE);
                    //cancelMembership.setVisibility(View.VISIBLE);
                    reqMembership.setText(R.string.cancel_membership);

                    Toast.makeText(getActivity(),"Membership Request Sent to Garden Owner",Toast.LENGTH_LONG).show();

                }else if (reqMembership.getText().toString().equalsIgnoreCase("Cancel Membership")){

                    cancelMembership(gInfo.getgId(),currentUser);
                    reqMembership.setText(R.string.request_membership);

                    Toast.makeText(getActivity(),"Membership Request has Been Cancelled",Toast.LENGTH_LONG).show();

                }



            }
        });

        /*cancelMembership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cancelMembership(gInfo.getgId(),currentUser);
                reqMembership.setVisibility(View.VISIBLE);
                cancelMembership.setVisibility(View.INVISIBLE);

                Toast.makeText(getActivity(),"Membership Request has Been Cancelled",Toast.LENGTH_LONG).show();

            }
        });*/

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

    private void cancelMembership(String gardenId, String requesterId){

        DatabaseReference gardenReference = FirebaseDatabase.getInstance().getReference("gardens");
        gardenReference.child(gardenId).child("gMembers").child(requesterId).setValue(null);

    }



    private void hasMembershipRequested(String gardenId, String requesterId){

        DatabaseReference gardenReference = FirebaseDatabase.getInstance().getReference("gardens").child(gardenId).child("gMembers");

        gardenReference.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

               for(DataSnapshot memberSnapshot : dataSnapshot.getChildren()){

                   if(null != memberSnapshot.getKey().toString() && memberSnapshot.getKey().toString().equalsIgnoreCase(currentUser)){
                       hasMemebershipRequested = true;
                       reqMembership.setText(R.string.cancel_membership);
                       //cancelMembership.setVisibility(View.VISIBLE);
                       break;
                   }else{
                       hasMemebershipRequested = false;
                       reqMembership.setText(R.string.request_membership);
                       //cancelMembership.setVisibility(View.INVISIBLE);
                   }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }






}
