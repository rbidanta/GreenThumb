package pervasive.iu.com.greenthumb.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
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

import pervasive.iu.com.greenthumb.MainActivity;
import pervasive.iu.com.greenthumb.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private Button login_button;
    private EditText login_email;
    private EditText login_password;
    private TextView register_link;
    private TextInputLayout inputLayoutEmail, inputLayoutPassword;
    private FirebaseAuth firebase_auth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebase_auth=FirebaseAuth.getInstance();

        if(firebase_auth.getCurrentUser()!=null)
        {

            String token = FirebaseInstanceId.getInstance().getToken();

            DatabaseReference userref = FirebaseDatabase.getInstance().getReference(firebase_auth.getCurrentUser().getUid());

            userref.child("token").child(token);

            finish();
            startActivity(new Intent(getApplicationContext(),Navigation.class));
        }



        progressDialog=new ProgressDialog(this);

        login_email=(EditText) findViewById(R.id.email_id);
        login_password=(EditText) findViewById(R.id.password);
        login_button=(Button) findViewById(R.id.login);
        register_link=(TextView) findViewById(R.id.login_here);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_pass);
        login_button.setOnClickListener(this);
        register_link.setOnClickListener(this);
    }

    private void userLogin() {
        String email = login_email.getText().toString().trim();

        String password = login_password.getText().toString().trim();

        progressDialog.setMessage("Logging in......");
        progressDialog.show();

        firebase_auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful())
                {

                    String token = FirebaseInstanceId.getInstance().getToken();

                    DatabaseReference userref = FirebaseDatabase.getInstance().getReference(firebase_auth.getCurrentUser().getUid());

                    userref.child("token").child(token);
                    finish();
                    startActivity(new Intent(getApplicationContext(),Navigation.class));
                }else{
                    Toast.makeText(getApplicationContext(), "Invalid email id and password. Please, try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v==login_button)
        {
           if(!validateEmail()){
               return;
           }

           if(!validatePassword()){
               return;
           }
           userLogin();
        }
        if(v==register_link)
        {
           finish();
            startActivity(new Intent(this,MainActivity.class));
        }
    }

    private boolean validateEmail() {
        if (login_email.getText().toString().trim().isEmpty()) {
            inputLayoutEmail.setError(getString(R.string.Err_Email));
            requestFocus(login_email);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePassword() {
        if (login_password.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.Err_Password));
            requestFocus(login_password);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
