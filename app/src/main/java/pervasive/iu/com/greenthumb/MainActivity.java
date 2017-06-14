package pervasive.iu.com.greenthumb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import pervasive.iu.com.greenthumb.Login.LoginActivity;
import pervasive.iu.com.greenthumb.Login.Profile;
import pervasive.iu.com.greenthumb.Login.my_profile;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button register_button;
    private EditText email_text;
    private EditText password_text;
    private TextView signin_view;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebase_auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebase_auth=FirebaseAuth.getInstance();

        if(firebase_auth.getCurrentUser()!=null)
        {
            finish();
            startActivity(new Intent(getApplicationContext(),Profile.class));
        }


        progressDialog=new ProgressDialog(this);

        register_button=(Button) findViewById(R.id.register);

        email_text=(EditText) findViewById(R.id.email_id);
        password_text=(EditText) findViewById(R.id.password);

        signin_view=(TextView) findViewById(R.id.signin);

        register_button.setOnClickListener(this);
        signin_view.setOnClickListener(this);

    }
    private void userRegistration()
    {
        String email=email_text.getText().toString().trim();
        String password=password_text.getText().toString().trim();
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please enter your email address", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("User is getting registered");
        progressDialog.show();

        firebase_auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful())
                {

                    String token = FirebaseInstanceId.getInstance().getToken();

                    DatabaseReference userref = FirebaseDatabase.getInstance().getReference(firebase_auth.getCurrentUser().getUid());

                    userref.child("token").setValue(token);

                    finish();
                    startActivity(new Intent(getApplicationContext(),Profile.class));

                }
                else
                {
                    Toast.makeText(MainActivity.this,"Registration failed. Please, try again.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v==register_button)
        {userRegistration();}
        if(v==signin_view)
        {//signin

            startActivity(new Intent(this, LoginActivity.class));
        }

    }
}
