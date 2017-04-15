package pervasive.iu.com.greenthumb.MyPlants;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import pervasive.iu.com.greenthumb.Login.LoginActivity;
import pervasive.iu.com.greenthumb.Model.Plants;
import pervasive.iu.com.greenthumb.R;

public class AddPlantActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;
    private DatabaseReference dbreference;
    private Button buttonSave;
    private ImageView plantImg;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference plantStorageRef = storage.getReference("plants");
    private StorageReference plantImagesRef;
    private Uri gardenImageUri = null;
    private String mCurrentPhotoPath;
    /*private static final String JPEG_FILE_PREFIX = "IMG_"; 
    private static final String JPEG_FILE_SUFFIX = ".jpg";  
   private AlbumStorageDirFactory mAlbumStorageDirFactory = null; */
    private EditText editTextPlantName;
    private EditText editTextTime;
    private EditText editTextKitId;
    private RadioGroup radioGroupLoc;
    private RadioButton radioBtnSelected;
    private RadioButton radioBtnIn;
    private RadioButton radioBtnOut;
    private TextInputLayout inputLayoutPlantName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser()==null)
        {
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }

        dbreference = FirebaseDatabase.getInstance().getReference();
        buttonSave = (Button) findViewById(R.id.btnSavePlant);
        plantImg = (ImageView) findViewById(R.id.imgViewPlant);
        editTextPlantName = (EditText) findViewById(R.id.txtPlantName);
        editTextTime = (EditText) findViewById(R.id.txtNotiTime);
        editTextKitId = (EditText) findViewById(R.id.txtKitNum);
        radioGroupLoc = (RadioGroup) findViewById(R.id.rgLocation);
        radioBtnIn = (RadioButton) findViewById(R.id.rbIndoor);
        radioBtnOut = (RadioButton) findViewById(R.id.rbOutdoor);
        buttonSave.setOnClickListener(this);
        /*OnClickListener first_radio_listener = new OnClickListener() {
            public void onClick(View v) {
                if (radioBtnIn.isChecked()) {
                }
            }
        };*/
    }
    public void savePlant(){
        System.out.println("Inside savePlant");
        FirebaseUser user = firebaseAuth.getCurrentUser();
        DatabaseReference def = dbreference.child(user.getUid()).child("Plants");
        String plantId = def.push().getKey();
        String userId = user.getUid();
        String kitId = editTextKitId.getText().toString().trim();
        String plantName = editTextPlantName.getText().toString().trim();
        String notificationTime = editTextTime.getText().toString().trim();

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
        plantImagesRef = plantStorageRef.child(plantId+"/"+plantName.replaceAll(" ","")+".jpg");
        HashMap<String, String> val = new HashMap<String, String>();
        val.put("moisture", "Twenty One");
        val.put("temp","18.5");
        val.put("ph", "5.5");

        HashMap<String, String> thresholdValues = val;

        Plants plant = new Plants(plantId, kitId, userId, plantName, location, notificationTime, lastModified, thresholdValues);
        def.child(plantId).setValue(plant);
        putImagetoFireBase();
    }

    @Override
    public void onClick(View v) {
        System.out.println("Inside on click");
        if(v==buttonSave)
        {
            savePlant();

            Toast.makeText(this,"Plant information has been saved successfully",Toast.LENGTH_LONG).show();

            finish();

        }
    }

    private boolean validatePlantName() {
        if (editTextPlantName.getText().toString().trim().isEmpty()) {
            inputLayoutPlantName.setError(getString(R.string.err_msg_gname));
            requestFocus(editTextPlantName);
            return false;
        } else {
            inputLayoutPlantName.setErrorEnabled(false);
        }

        return true;
    }

    static final int REQUEST_TAKE_PHOTO = 1;

    public void onClickCamera(View v){

        System.out.println("Inside onClickCamera");

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            System.out.println("Inside onClickCamera 1");
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

        System.out.println("Inside Set Pic");

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
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

		/* Associate the Bitmap to the ImageView */
        plantImg.setImageBitmap(bitmap);
        //mVideoUri = null;
        plantImg.setVisibility(View.VISIBLE);
        //mVideoView.setVisibility(View.INVISIBLE);
    }

    private File createImageFile() throws IOException {

        System.out.println("Inside createImageFile");
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
        System.out.println("mCurrentPhotoPath"+mCurrentPhotoPath);
        return image;
    }

    private void putImagetoFireBase(){
        plantImg.setDrawingCacheEnabled(true);
        plantImg.buildDrawingCache();
        Bitmap bitmap = plantImg.getDrawingCache();
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
        System.out.println("handleBigCameraPhoto");

        if (mCurrentPhotoPath != null) {
            setPic();
            //galleryAddPic();
            mCurrentPhotoPath = null;
        }

    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            System.out.println("Inside Activity Result");
            handleBigCameraPhoto();
        }

    }
}
