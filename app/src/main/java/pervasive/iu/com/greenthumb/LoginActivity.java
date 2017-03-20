package pervasive.iu.com.greenthumb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
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

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private Button login_button;
    private EditText login_email;
    private EditText login_password;
    private TextView register_link;

    private FirebaseAuth firebase_auth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebase_auth=FirebaseAuth.getInstance();

        if(firebase_auth.getCurrentUser()!=null)
        {
            finish();
            startActivity(new Intent(getApplicationContext(),Profile.class));
        }

        progressDialog=new ProgressDialog(this);

        login_email=(EditText) findViewById(R.id.email_id);
        login_password=(EditText) findViewById(R.id.password);
        login_button=(Button) findViewById(R.id.login);
        register_link=(TextView) findViewById(R.id.login_here);

        login_button.setOnClickListener(this);
        register_link.setOnClickListener(this);
    }
    private void userLogin() {
        String email = login_email.getText().toString().trim();

        String password = login_password.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter your email address", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
        }

        progressDialog.setMessage("Logging in......");
        progressDialog.show();

        firebase_auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful())
                {
                    finish();
                    startActivity(new Intent(getApplicationContext(),Profile.class));
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        if(v==login_button)
        {
           userLogin();
        }
        if(v==register_link)
        {
           finish();
            startActivity(new Intent(this,MainActivity.class));
        }
    }
}
