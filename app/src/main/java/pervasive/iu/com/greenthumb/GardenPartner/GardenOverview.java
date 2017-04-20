package pervasive.iu.com.greenthumb.GardenPartner;


import android.content.DialogInterface;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pervasive.iu.com.greenthumb.Adapter.MemberListViewAdapter;
import pervasive.iu.com.greenthumb.DBHandler.GardenInfo;
import pervasive.iu.com.greenthumb.DBHandler.MemberInfo;
import pervasive.iu.com.greenthumb.R;

public class GardenOverview extends Fragment {


    private EditText gName,gAddress,gOwnerName,gContactNumber;
    private Button reqMembership,updateInfo,viewRequests;
   // private ImageButton
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

    private FirebaseUser loggedUser;

    private String gardenId = "";

    private ListView mlistView;

    private ArrayList<MemberInfo> memberObjectList ;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.garden_overview);
        firebaseAuth = FirebaseAuth.getInstance();

        loggedUser = firebaseAuth.getCurrentUser();
        currentUser = firebaseAuth.getCurrentUser().getUid().toString();


    }


    DatabaseReference gardenReference;
    List<String> memberslist;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.activity_garden_overview,container,false);

        Bundle bundle = getArguments();

        final GardenInfo gInfo = (GardenInfo)bundle.getSerializable("gInfo");

        final String gardenName = gInfo.getgName().toString();

        String gardenOwner = gInfo.getgOwner().toString();

        String contactNumber = gInfo.getgOwnerPhone().toString();

        gardenId = gInfo.getgId().toString();

        String location = gInfo.getgAddress();

        String imagePath = gInfo.getgFireBasePath();

        gardenImagesRef = storage.getReference(imagePath);

        gName = (EditText) view.findViewById(R.id.g_name);
        gAddress = (EditText) view.findViewById(R.id.g_address);
        gOwnerName = (EditText) view.findViewById(R.id.g_owner);
        gContactNumber = (EditText) view.findViewById(R.id.g_contactNumber);
        gardenImage = (ImageView) view.findViewById(R.id.gardenOImage);
        reqMembership = (Button) view.findViewById(R.id.req_memship);
        updateInfo = (Button) view.findViewById(R.id.update_info);
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
            gOwnerName.setEnabled(false);
            gAddress.setEnabled(true);
            gContactNumber.setEnabled(true);
            reqMembership.setVisibility(View.GONE);
            updateInfo.setVisibility(View.VISIBLE);
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




        updateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateInfo(gardenId,gName.getText().toString(),gAddress.getText().toString(),gContactNumber.getText().toString());

            }

        });




        gardenReference = FirebaseDatabase.getInstance().getReference("gardens");

        gardenReference.child(gardenId).child("gMembers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                memberslist = new ArrayList<String>();

                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    System.out.println("ds.getValue:----"+ds.getValue().toString());

                    if(ds.getValue().toString().equalsIgnoreCase("false")){

                        memberslist.add(ds.getKey());
                    }else{
                        memberslist.remove(ds.getKey());
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        viewRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
                builderSingle.setIcon(R.mipmap.ic_new_member_req);

                if(!memberslist.isEmpty()) {
                    builderSingle.setTitle("Pending Requests:-");
                }else{
                    builderSingle.setTitle("No Pending Requests:-");
                }


                Bundle bundle = new Bundle();
                LayoutInflater inflater = getLayoutInflater(bundle);
                View memberListView = inflater.inflate(R.layout.view_membership_request, null);

                memberObjectList  = new ArrayList<MemberInfo>();

                for(String member: memberslist){

                    System.out.println("Member: "+member);

                    getMemberInfo(member.toString());


                }
                memberslist.clear();

                System.out.println("memberObjectList----------"+memberObjectList);

                mlistView = (ListView) memberListView.findViewById(R.id.newMemberList);


                final ArrayList<String> selectedMembers = new ArrayList<String>();

                final MemberListViewAdapter memberAdapter = new MemberListViewAdapter(memberObjectList,getContext(),selectedMembers);

                mlistView.setAdapter(memberAdapter);


                System.out.println("selectedMembers----------"+selectedMembers);


                builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setPositiveButton("Approve", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("Approved");

                        System.out.println("Selected Members: ======== "+selectedMembers);



                        for (String members : selectedMembers ) {

                            gardenReference.child(gardenId).child("gMembers").child(members).setValue(Boolean.TRUE);


                            DatabaseReference leaderBoard = FirebaseDatabase.getInstance().getReference("leaderboard");
                            leaderBoard.child(gardenId).child(members).setValue(Integer.parseInt("100"));

                        }

                    }
                });

                builderSingle.setAdapter(memberAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = memberAdapter.getItem(which).getFirstname()+ " " + memberAdapter.getItem(which).getFirstname();
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(getActivity());
                        builderInner.setMessage(strName);
                        builderInner.setTitle("Your Selected Item is");
                        builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int which) {
                                dialog.dismiss();
                            }
                        });
                        builderInner.show();
                    }
                });
                builderSingle.show();
            }
        });



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


   private void updateInfo(String gardenId , String gardenName, String gardenAddress, String contactPhone){

       DatabaseReference gardenReference = FirebaseDatabase.getInstance().getReference("gardens");

       if(isDataModified()) {
           /*gardenReference.child(gardenId).child("gName").setValue(gName.getText());
           gardenReference.child(gardenId).child("gAddress").setValue(gAddress.getText());
           gardenReference.child(gardenId).child("gOwnerPhone").setValue(gContactNumber.getText());*/
           Toast.makeText(getActivity(),"Information Updated Successfully!",Toast.LENGTH_LONG).show();
       }else {
           Toast.makeText(getActivity(),"Please provide valid information!",Toast.LENGTH_LONG).show();
       }

   }

    private boolean isDataModified(){

        if(gName.getText().toString().isEmpty() || gAddress.getText().toString().isEmpty() || gContactNumber.getText().toString().isEmpty()){

            return false;

        }
        return true;
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





    public void getMemberInfo(String memberId){

        final MemberInfo memberInfo = new MemberInfo();

        System.out.println("User GUID:"+memberId);

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference(memberId);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                memberInfo.setUserId(dataSnapshot.getKey());

                System.out.println("User Id:"+dataSnapshot.getKey());

                for(DataSnapshot memberds : dataSnapshot.getChildren()){

                    if(memberds.getKey().equalsIgnoreCase("firstname")){

                        memberInfo.setFirstname(memberds.getValue().toString());
                        System.out.println("First Name:"+memberds.getValue().toString());

                    }else if(memberds.getKey().equalsIgnoreCase("lastname")){

                        memberInfo.setLastname(memberds.getValue().toString());
                        System.out.println("Last Name:"+memberds.getValue().toString());

                    }

                }

                memberObjectList.add(memberInfo);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }













}
