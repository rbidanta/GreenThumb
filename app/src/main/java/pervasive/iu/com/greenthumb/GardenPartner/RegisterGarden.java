package pervasive.iu.com.greenthumb.GardenPartner;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import pervasive.iu.com.greenthumb.DBHandler.GardenInfo;
import pervasive.iu.com.greenthumb.DBHandler.saveInfo;
import pervasive.iu.com.greenthumb.Login.LoginActivity;
import pervasive.iu.com.greenthumb.R;

public class RegisterGarden extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference dbreference;
    private DatabaseReference userReference ;
    private EditText gardenName, gardenAddress;
    private saveInfo loggedInUserInfo = new saveInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_garden);



        firebaseAuth=FirebaseAuth.getInstance();

        gardenName = (EditText) findViewById(R.id.gardenname);
        gardenAddress = (EditText) findViewById(R.id.gardenaddress);

        final String usrId = firebaseAuth.getCurrentUser().getUid();

        System.out.println("loggedInUserInfo"+usrId);
        userReference = FirebaseDatabase.getInstance().getReference(usrId);

        //userReference.


        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    // Retrieve More Data if Required
                    if(ds.getKey().equalsIgnoreCase("phone")){
                        loggedInUserInfo.setPhone(ds.getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }



    public void onRegister(View view) {



        //FirebaseUser user=firebaseAuth.getCurrentUser();

        saveGardenInfo();

        Toast.makeText(this,"Garden information saved successfully",Toast.LENGTH_LONG).show();

    }


    private void saveGardenInfo(){
        String gardenname = gardenName.getText().toString().trim();
        String gardenaddress = gardenAddress.getText().toString().trim();


        //saveInfo saveinf=new saveInfo(gardenname,gardenaddress);
        dbreference = FirebaseDatabase.getInstance().getReference("gardens");
        FirebaseUser user=firebaseAuth.getCurrentUser();




        String gId = dbreference.push().getKey();

        GardenInfo gInfo = new GardenInfo(gId, gardenname, gardenaddress, user.getUid(), loggedInUserInfo.getPhone(), null);

        dbreference.child(gId).setValue(gInfo);

        dbreference.child(gId).child("gMembers").child("User1").setValue(true);
        dbreference.child(gId).child("gMembers").child("User2").setValue(true);
        dbreference.child(gId).child("gMembers").child("User3").setValue(true);
        //Toast.makeText(getActivity(),"User information saved successfully",Toast.LENGTH_LONG).show();


    }


}
