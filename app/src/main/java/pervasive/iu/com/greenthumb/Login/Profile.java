package pervasive.iu.com.greenthumb.Login;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import pervasive.iu.com.greenthumb.DBHandler.saveInfo;

import pervasive.iu.com.greenthumb.R;

public class Profile extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private TextView profileview;
    private Button logout_button;
    private DatabaseReference dbreference;
    //private TextView name_view,email_view,location_view,phone_view;
    private EditText firstname, lastname, email, location, address, phone;
    private Button buttonsave;

    private FirebaseUser user;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();

        String fname = "";
        String lname ="";
        String loc = "";
        String addr = "";
        String phno = "";




       user = firebaseAuth.getCurrentUser();

        if (user == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }else{

            DatabaseReference dbref = FirebaseDatabase.getInstance().getReference(user.getUid());
            dbref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for(DataSnapshot ds : dataSnapshot.getChildren()){

                        System.out.println("ds.getKey()-----"+ds.getKey());
                        System.out.println("ds.Value()-----"+ds.getValue());

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


        dbreference = FirebaseDatabase.getInstance().getReference();

        firstname = (EditText) findViewById(R.id.fName);
        lastname = (EditText) findViewById(R.id.lName);
        location = (EditText) findViewById(R.id.loc);
        address = (EditText) findViewById(R.id.address);
        phone = (EditText) findViewById(R.id.phone);
        buttonsave = (Button) findViewById(R.id.save);

        user = firebaseAuth.getCurrentUser();

        profileview = (TextView) findViewById(R.id.profile_view);
        profileview.setText("Welcome " + user.getEmail());
        logout_button = (Button) findViewById(R.id.logout_but);

        logout_button.setOnClickListener(this);
        buttonsave.setOnClickListener(this);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void saveInfo() {
        String first_name = firstname.getText().toString().trim();
        String last_name = lastname.getText().toString().trim();
        String location_val = location.getText().toString().trim();
        String address_val = address.getText().toString().trim();
        String phone_num = phone.getText().toString().trim();

        String token = FirebaseInstanceId.getInstance().getToken();


        if (first_name.isEmpty()) {
            Toast.makeText(this, "Please Enter First Name", Toast.LENGTH_LONG).show();
        } else if (last_name.isEmpty()) {
            Toast.makeText(this, "Please Enter Last Name", Toast.LENGTH_LONG).show();
        } else if (address_val.isEmpty()) {
            Toast.makeText(this, "Please Enter Last Name", Toast.LENGTH_LONG).show();
        } else if (phone_num.isEmpty()) {
            Toast.makeText(this, "Please Enter Phone Number", Toast.LENGTH_LONG).show();
        } else {


            saveInfo saveinf = new saveInfo(first_name, last_name, location_val, address_val, phone_num, user.getEmail(),token);
            FirebaseUser user = firebaseAuth.getCurrentUser();

            try {
                dbreference.child(user.getUid()).setValue(saveinf);
                Toast.makeText(this, "User information saved successfully", Toast.LENGTH_LONG).show();

                finish();
                startActivity(new Intent(getApplicationContext(),Navigation.class));

            } catch (Exception e) {
                Toast.makeText(this, "Error Occurred Saving User Details", Toast.LENGTH_LONG).show();
            }

        }

        /*saveInfo saveinf=new saveInfo(first_name,last_name,location_val,address_val,phone_num,user.getEmail());
        FirebaseUser user=firebaseAuth.getCurrentUser();

        dbreference.child(user.getUid()).setValue(saveinf);
        Toast.makeText(this,"User information saved successfully",Toast.LENGTH_LONG).show();*/

    }

    @Override
    public void onClick(View v) {
        if (v == logout_button) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        if (v == buttonsave) {
            saveInfo();
        }

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Profile Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
