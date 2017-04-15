package pervasive.iu.com.greenthumb.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import pervasive.iu.com.greenthumb.DBHandler.saveInfo;
import pervasive.iu.com.greenthumb.R;

/**
 * Created by deepakasinathan on 3/25/17.
 */

public class my_profile extends Fragment implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;
    private TextView profileview;
    private Button logout_button;
    private DatabaseReference dbreference;
    //private TextView name_view,email_view,location_view,phone_view;
    private EditText firstname,lastname,email,location,address,phone;
    private Button buttonsave;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Garden partner");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_profile,container,false) ;
        perform(view);
        return view;
    }


    public void perform(View view)

    {
        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()==null)
        {
            getActivity().finish();
            startActivity(new Intent(getActivity(),LoginActivity.class));
        }

        dbreference = FirebaseDatabase.getInstance().getReference();

        firstname=(EditText) view.findViewById(R.id.fName);
        lastname=(EditText) view.findViewById(R.id.lName);
        location=(EditText) view.findViewById(R.id.loc);
        address=(EditText) view.findViewById(R.id.address);
        phone=(EditText) view.findViewById(R.id.phone);
        buttonsave=(Button) view.findViewById(R.id.save);

        FirebaseUser user=firebaseAuth.getCurrentUser();

        profileview=(TextView) view.findViewById(R.id.profile_view);
        profileview.setText("Welcome " + user.getEmail());
        logout_button=(Button) view.findViewById(R.id.logout_but);

        logout_button.setOnClickListener(this);
        buttonsave.setOnClickListener(this);

    }
    private void saveInfo(){
        String first_name=firstname.getText().toString().trim();
        String last_name=lastname.getText().toString().trim();
        String location_val=location.getText().toString().trim();
        String address_val=address.getText().toString().trim();
        String phone_num=phone.getText().toString().trim();
        System.out.println("phone_num:"+phone_num);

        saveInfo saveinf=new saveInfo(first_name,last_name,location_val,address_val,phone_num);
        FirebaseUser user=firebaseAuth.getCurrentUser();

        dbreference.child(user.getUid()).setValue(saveinf);
        Toast.makeText(getActivity(),"User information saved successfully",Toast.LENGTH_LONG).show();


    }

    @Override
    public void onClick(View v) {
        if(v==logout_button)
        {
            firebaseAuth.signOut();
            getActivity().finish();
            startActivity(new Intent(getActivity(),LoginActivity.class));
        }
        if(v==buttonsave)
        {
            saveInfo();
        }

    }
}
