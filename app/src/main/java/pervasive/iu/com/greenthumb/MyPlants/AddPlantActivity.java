package pervasive.iu.com.greenthumb.MyPlants;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.graphics.drawable.Drawable;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.view.View;
import android.view.View.OnClickListener;

import java.util.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import pervasive.iu.com.greenthumb.Login.LoginActivity;
import pervasive.iu.com.greenthumb.Model.Plants;
import pervasive.iu.com.greenthumb.R;

public class AddPlantActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;
    private DatabaseReference dbreference;
    private ImageButton buttonSave;
    private FirebaseStorage storage = FirebaseStorage.getInstance(); 
    private StorageReference plantStorageRef = storage.getReference("plants"); 
    private StorageReference plantImagesRef;  
    //private EditText gardenName, gardenAddress; 
    //private TextInputLayout inputLayoutgName, inputLayoutgAddress; 
    //private saveInfo loggedInUserInfo = new saveInfo();   
    //private ImageView mImageView;  
    private Uri gardenImageUri = null;   
    private String mCurrentPhotoPath;  
    //private static final String JPEG_FILE_PREFIX = "IMG_"; 
   // private static final String JPEG_FILE_SUFFIX = ".jpg";  
   // private AlbumStorageDirFactory mAlbumStorageDirFactory = null; 
    private EditText editTextPlantName;
    private EditText editTextTime;
    private RadioButton radioBtnIn;
    private RadioButton radioBtnOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant);
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser()==null)
        {
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }

        dbreference = FirebaseDatabase.getInstance().getReference();
        buttonSave = (ImageButton) findViewById(R.id.btnSave);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rgLocation);
        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

            }
        });
        buttonSave.setOnClickListener(this);
      /*  radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                View radioButton = radioGroup.findViewById(checkedId);
                int index = radioGroup.indexOfChild(radioButton);

                // Add logic here

                switch (index) {
                    case 0: // first button

                        Toast.makeText(getApplicationContext(), "Selected button number " + index, 500).show();
                        break;
                    case 1: // secondbutton

                        Toast.makeText(getApplicationContext(), "Selected button number " + index, 500).show();
                        break;
                }
            }*/
        //radioBtnIn = (RadioButton) findViewById(R.id.rbIndoor);
        //radioBtnIn.setOnClickListener();
        //radioBtnOut = (RadioButton) findViewById(R.id.rbOutdoor);

        //  rb.setOnClickListener(first_radio_listener);
        OnClickListener first_radio_listener = new OnClickListener() {
            public void onClick(View v) {
                if (radioBtnIn.isChecked()) {
                }
            }

            ;
        };
    }
    public void savePlant(){
        long kitId = 12434;
        String plantName = "Rose";
        String imageName ="sdha";
        String location = "Indoor";
        int notificationTime = 20;
        Date dNow = new Date( );
        Date lastModified = dNow ;
        HashMap<String, String> val = new HashMap<String, String>();
        val.put("moisture", "Twenty One");
        HashMap<String, String> thresholdValues = val;
        Plants plant = new Plants(kitId,plantName, imageName,location,notificationTime,lastModified,thresholdValues);
        FirebaseUser user=firebaseAuth.getCurrentUser();

        dbreference.child(user.getUid()).setValue(plant);
    }

    @Override
    public void onClick(View v) {
        if(v==buttonSave)
        {
            savePlant();
        }

    }
   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        Drawable drawablecart = menu.getItem(0).getIcon();
        drawablecart.mutate();
        drawablecart.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.shopping_cart){

            // code to open the shopping cart.
            Intent intent = new Intent(SearchActivity.this, ShoppingCartActivity.class);
            startActivity(intent);

            return true;

        }

        return super.onOptionsItemSelected(item);
    }*/
}
