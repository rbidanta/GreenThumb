package pervasive.iu.com.greenthumb.Login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pervasive.iu.com.greenthumb.DBHandler.saveInfo;
import pervasive.iu.com.greenthumb.Model.Plants;
import pervasive.iu.com.greenthumb.R;

/**
 * Created by deepakasinathan on 3/25/17.
 */

public class my_profile extends Fragment implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;
    private TextView profileview;
    private Button logout_button;
    private DatabaseReference dbreference;
    private EditText firstname,lastname,email,location,address,phone;
    private Button buttonsave;
    private FirebaseUser user;

    private ImageView userImg;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference userStorageRef = storage.getReference("user");
    private StorageReference userImageRef;
    private Uri userImageUri = null;
    private String mCurrentPhotoPath;
    private Bitmap bitmap;
    private static final int PICK_IMAGE_REQUEST = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    private static ImageButton btnGallery, btnCamera;

    private String fname = "";
    private String lname ="";
    private String loc = "";
    private String addr = "";
    private String phno = "";

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
        user=firebaseAuth.getCurrentUser();

        dbreference = FirebaseDatabase.getInstance().getReference();

        firstname=(EditText) view.findViewById(R.id.fName);
        lastname=(EditText) view.findViewById(R.id.lName);
        location=(EditText) view.findViewById(R.id.loc);
        address=(EditText) view.findViewById(R.id.address);
        phone=(EditText) view.findViewById(R.id.phone);
        buttonsave=(Button) view.findViewById(R.id.save);
        userImg = (ImageView) view.findViewById(R.id.imgProfile);
        btnGallery = (ImageButton) view.findViewById(R.id.btnAddGalleryImage);
        btnCamera = (ImageButton) view.findViewById(R.id.btnAddImage);
        btnCamera.setVisibility(view.GONE);
        btnGallery.setVisibility(view.GONE);
        if(firebaseAuth.getCurrentUser()==null)
        {
            getActivity().finish();
            startActivity(new Intent(getActivity(),LoginActivity.class));
        } else{

            DatabaseReference dbref = FirebaseDatabase.getInstance().getReference(user.getUid());
            dbref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        if(ds.getKey().equalsIgnoreCase("firstname")){
                            firstname.setText(ds.getValue().toString());
                        }else if(ds.getKey().equalsIgnoreCase("lastname")){
                            lastname.setText(ds.getValue().toString());
                        }else if(ds.getKey().equalsIgnoreCase("location")){
                            location.setText(ds.getValue().toString());
                        }else if(ds.getKey().equalsIgnoreCase("address")){
                            address.setText(ds.getValue().toString());
                        }else if(ds.getKey().equalsIgnoreCase("phone")){
                            phone.setText(ds.getValue().toString());
                        }else if(ds.getKey().equalsIgnoreCase("UserImagePath")){
                            mCurrentPhotoPath = ds.getValue().toString();
                            userImageRef = storage.getReference(mCurrentPhotoPath);
                            Glide.with(getContext())
                                    .using(new FirebaseImageLoader())
                                    .load(userImageRef)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(userImg);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        profileview=(TextView) view.findViewById(R.id.profile_view);
        profileview.setText(user.getEmail());
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

        String token = FirebaseInstanceId.getInstance().getToken();


        if(first_name.isEmpty()){
            Toast.makeText(getActivity(),"Please Enter First Name",Toast.LENGTH_LONG).show();
        }else if(last_name.isEmpty()){
            Toast.makeText(getActivity(),"Please Enter Last Name",Toast.LENGTH_LONG).show();
        }else if(address_val.isEmpty()){
            Toast.makeText(getActivity(),"Please Enter Last Name",Toast.LENGTH_LONG).show();
        }else if(phone_num.isEmpty()){
            Toast.makeText(getActivity(),"Please Enter Phone Number",Toast.LENGTH_LONG).show();
        }else{

            FirebaseUser user=firebaseAuth.getCurrentUser();
            String userId = user.getUid();
            userImageRef = userStorageRef.child(userId+"/"+first_name.replaceAll(" ","")+".jpg");
            saveInfo saveinf = new saveInfo(first_name, last_name,  address_val, location_val, phone_num, user.getEmail(),token, userImageRef.getPath());

            try {
               dbreference.child(user.getUid()).setValue(saveinf);
               Toast.makeText(getActivity(),"User information saved successfully",Toast.LENGTH_LONG).show();
             } catch (Exception e) {
               Toast.makeText(getActivity(),"Error Occurred Saving User Details",Toast.LENGTH_LONG).show();
             }
       }
    }
    public void onClickCamera(View v){
    }

    public void imagePicker(View v){
    }
    @Override
    public void onClick(View v) {
        if(v == logout_button)
        {
            firebaseAuth.signOut();
            getActivity().finish();
            startActivity(new Intent(getActivity(),LoginActivity.class));
        }
        if(v == buttonsave)
        {
            saveInfo();
        }
    }
}
