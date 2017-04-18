package pervasive.iu.com.greenthumb.MyPlants;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import pervasive.iu.com.greenthumb.Adapter.GardenListViewAdapter;
import pervasive.iu.com.greenthumb.Adapter.VitalStatViewAdapter;
import pervasive.iu.com.greenthumb.DBHandler.GardenInfo;
import pervasive.iu.com.greenthumb.GardenPartner.GardenOverview;
import pervasive.iu.com.greenthumb.Login.LoginActivity;
import pervasive.iu.com.greenthumb.MainActivity;
import pervasive.iu.com.greenthumb.Model.Plants;
import pervasive.iu.com.greenthumb.R;

public class AddPlantActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;
    private DatabaseReference dbreference;
    private ImageView plantImg;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference plantStorageRef = storage.getReference("plants");
    private StorageReference plantImagesRef;
    private Uri plantImageUri = null;
    private String mCurrentPhotoPath;
    private EditText editTextPlantName, editTextTime, editTextKitId;
    private RadioGroup radioGroupLoc;
    private RadioButton radioBtnSelected, radioBtnIn, radioBtnOut;
    private TextInputLayout inputLayoutPlantName, inputLayoutKitNum, inputLayoutNotTime;
    private ListView lvStats;
    private Button buttonSave, buttonUpdate, buttonShowStats;
    private Plants plantDetails;
    private String plantId;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        plantDetails = (Plants) intent.getSerializableExtra("plantInfo");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null)
        {
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }

        //Hide keyboard
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        dbreference = FirebaseDatabase.getInstance().getReference();
        buttonSave = (Button) findViewById(R.id.btnSavePlant);
        buttonShowStats = (Button) findViewById(R.id.btnShowStats);
        plantImg = (ImageView) findViewById(R.id.imgViewPlant);
        editTextPlantName = (EditText) findViewById(R.id.txtPlantName);
        editTextTime = (EditText) findViewById(R.id.txtNotiTime);
        editTextKitId = (EditText) findViewById(R.id.txtKitNum);
        radioGroupLoc = (RadioGroup) findViewById(R.id.rgLocation);
        radioBtnIn = (RadioButton) findViewById(R.id.rbIndoor);
        radioBtnOut = (RadioButton) findViewById(R.id.rbOutdoor);
        inputLayoutPlantName = (TextInputLayout) findViewById(R.id.input_layout_pname);
        inputLayoutKitNum = (TextInputLayout) findViewById(R.id.input_layout_KitNum);
        inputLayoutNotTime = (TextInputLayout) findViewById(R.id.input_layout_NotTime);
        buttonUpdate = (Button) findViewById(R.id.btnUpdatePlant);

        View v = findViewById(android.R.id.content);
        if(plantDetails != null){
            buttonSave.setVisibility(View.GONE);
            buttonUpdate.setVisibility(View.VISIBLE);
            buttonShowStats.setVisibility(View.VISIBLE);
            bindPlantDetails(plantDetails, v);
        }
        buttonSave.setOnClickListener(this);
        buttonUpdate.setOnClickListener(this);
        buttonShowStats.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v == buttonSave)
        {
            if(!validatePlantName()){
                return;
            }

            if(!validateKitNum()){
                return;
            }

            if(!validateNotTime()){
                return;
            }

            if(!validatePlantImage()) {
                Toast.makeText(this,R.string.Err_Plant_Image,Toast.LENGTH_LONG).show();
                return;
            }

            savePlant();

            Toast.makeText(this,"Plant information has been saved successfully",Toast.LENGTH_LONG).show();

            finish();

        }else if(v == buttonUpdate){
            if(!validatePlantName()){
                return;
            }

            if(!validateKitNum()){
                return;
            }

            if(!validateNotTime()){
                return;
            }

            if(!validatePlantImage()) {
                Toast.makeText(this,R.string.Err_Plant_Image,Toast.LENGTH_LONG).show();
                return;
            }

            savePlant();

            Toast.makeText(this,"Plant information has been updated successfully",Toast.LENGTH_LONG).show();

            finish();
        }else if(v == buttonShowStats){
            if(plantDetails != null) {
                showStats();
            }
        }
    }

    public void bindPlantDetails(Plants plantInfo, View v){

        plantId = plantInfo.getPlantId();
        String kitId = plantInfo.getKitId();
        String plantName = plantInfo.getPlantName();
        String notificationTime = plantInfo.getNotificationTime();
        String location = plantInfo.getLocation();
        String plantImagePath = plantInfo.getPlantImagePath();
        editTextKitId.setText(kitId);
        editTextPlantName.setText(plantName);
        editTextTime.setText(notificationTime);
        plantImagesRef = storage.getReference(plantImagePath);

        Toast.makeText(this,"Plant image path value inside bind method "+plantImagePath,Toast.LENGTH_LONG).show();

        if(location.trim().equals("Indoor")){
            radioBtnIn.setChecked(true);
        }else{
            radioBtnOut.setChecked(true);
        }

        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(plantImagesRef)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(plantImg);
    }

    public void savePlant(){

        FirebaseUser user = firebaseAuth.getCurrentUser();
        DatabaseReference def = dbreference.child(user.getUid()).child("Plants");

        if(plantId == null){
            plantId = def.push().getKey();
        }
        String userId = user.getUid();
        String kitId = editTextKitId.getText().toString().trim();
        String plantName = editTextPlantName.getText().toString().trim();
        String notificationTime = editTextTime.getText().toString().trim();
        String plantImagePath = userId+"/"+plantId+"/"+plantName.replaceAll(" ","")+".jpg";

        // get selected radio button from radioGroup
        int selectedId = radioGroupLoc.getCheckedRadioButtonId();

        String location;

        radioBtnSelected = (RadioButton) findViewById(selectedId);

        if(radioBtnSelected == radioBtnIn){
            location = "Indoor";
        }else{
            location = "Outdoor";
        }

        Date lastModified = new Date();

        plantImagesRef = plantStorageRef.child(userId+"/"+plantId+"/"+plantName.replaceAll(" ","")+".jpg");
        HashMap<String, String> val = new HashMap<String, String>();
        val.put("Sunlight", "21");
        val.put("Soil Moisture", "5.5");
        val.put("Air Temperature","18.5");
        val.put("PH", "5");

        HashMap<String, String> thresholdValues = val;

        Plants plant = new Plants(plantId, kitId, userId, plantName, plantImagesRef.getPath(), location, notificationTime, lastModified, thresholdValues);

        def.child(plantId).setValue(plant);
        putImagetoFireBase();
    }

    public void showStats() {

        final String plantName = plantDetails.getPlantName().toString();
        String plantImagePath = plantDetails.getPlantImagePath().toString();

        HashMap<String, String> thresholdValues = plantDetails.getThresholdValues();
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.activity_plant_stats, null);
        TextView tvPlantName = (TextView) convertView.findViewById(R.id.txtPlantName);
        de.hdodenhof.circleimageview.CircleImageView ivPlant = (de.hdodenhof.circleimageview.CircleImageView) convertView.findViewById(R.id.imgViewPlant);
        lvStats = (ListView) convertView.findViewById(R.id.statList);
        plantImagesRef = storage.getReference(plantImagePath);

        tvPlantName.setText(plantName);
        VitalStatViewAdapter adapter = new VitalStatViewAdapter(thresholdValues);
        lvStats.setAdapter(adapter);

        Glide.with(getApplicationContext())
                .using(new FirebaseImageLoader())
                .load(plantImagesRef)
                .into(ivPlant);

        AlertDialog.Builder statDialog = new AlertDialog.Builder(this);
        statDialog.setView(convertView);
        statDialog.setTitle("Vital Stats");
        statDialog.show();
    }

    private boolean validatePlantName() {
        if (editTextPlantName.getText().toString().trim().isEmpty()) {
            inputLayoutPlantName.setError(getString(R.string.Err_Plant_Name));
            requestFocus(editTextPlantName);
            return false;
        } else {
            inputLayoutPlantName.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateKitNum() {
        if (editTextKitId.getText().toString().trim().isEmpty()) {
            inputLayoutKitNum.setError(getString(R.string.Err_Kit_Num));
            requestFocus(editTextKitId);
            return false;
        } else {
            inputLayoutKitNum.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateNotTime() {
        if (editTextTime.getText().toString().trim().isEmpty()) {
            inputLayoutNotTime.setError(getString(R.string.Err_Not_Time));
            requestFocus(editTextTime);
            return false;
        } else {
            inputLayoutNotTime.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validatePlantImage() {

        if (plantImageUri == null) {
            return false;
        } else {
            return true;
        }

    }
    static final int REQUEST_TAKE_PHOTO = 1;

    public void onClickCamera(View v){

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.greenthumb.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }

    }

    private void setPic() {

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
        int targetW = plantImg.getWidth();
        int targetH = plantImg.getHeight();

		/* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        }

		/* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        //bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
        bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

		/* Associate the Bitmap to the ImageView */
        plantImg.setImageBitmap(bitmap);
        //mVideoUri = null;
        plantImg.setVisibility(View.VISIBLE);
        //mVideoView.setVisibility(View.INVISIBLE);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void putImagetoFireBase(){
        plantImg.setDrawingCacheEnabled(true);
        plantImg.buildDrawingCache();
        //Bitmap bitmap = plantImg.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBdata = baos.toByteArray();

        UploadTask uploadTask = plantImagesRef.putBytes(imageBdata);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });
    }

    private void handleBigCameraPhoto() {
        if (mCurrentPhotoPath != null) {
            setPic();
            galleryAddPic();
            mCurrentPhotoPath = null;
        }

    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        plantImageUri = contentUri;
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            handleBigCameraPhoto();
        }

    }
}
