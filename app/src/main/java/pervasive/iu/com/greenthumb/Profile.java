package pervasive.iu.com.greenthumb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Profile extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;
    private TextView profileview;
    private Button logout_button;
    private DatabaseReference dbreference;
    //private TextView name_view,email_view,location_view,phone_view;
    private EditText firstname,lastname,email,location,address,phone;
    private Button buttonsave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()==null)
        {
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }

        dbreference = FirebaseDatabase.getInstance().getReference();

        firstname=(EditText) findViewById(R.id.fName);
        lastname=(EditText) findViewById(R.id.lName);
        location=(EditText) findViewById(R.id.loc);
        address=(EditText) findViewById(R.id.address);
        phone=(EditText) findViewById(R.id.phone);
        buttonsave=(Button) findViewById(R.id.save);

        FirebaseUser user=firebaseAuth.getCurrentUser();

        profileview=(TextView) findViewById(R.id.profile_view);
        profileview.setText("Welcome " + user.getEmail());
        logout_button=(Button) findViewById(R.id.logout_but);

        logout_button.setOnClickListener(this);
        buttonsave.setOnClickListener(this);

    }
    private void saveInfo(){
        String first_name=firstname.getText().toString().trim();
        String last_name=lastname.getText().toString().trim();
        String location_val=location.getText().toString().trim();
        String address_val=address.getText().toString().trim();
        String phone_num=firstname.getText().toString().trim();

        saveInfo saveinf=new saveInfo(first_name,last_name,location_val,address_val,phone_num);
        FirebaseUser user=firebaseAuth.getCurrentUser();

        dbreference.child(user.getUid()).setValue(saveinf);
        Toast.makeText(this,"User information saved successfully",Toast.LENGTH_LONG).show();

    }

    @Override
    public void onClick(View v) {
        if(v==logout_button)
        {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }
        if(v==buttonsave)
        {
            saveInfo();
        }

    }
}
