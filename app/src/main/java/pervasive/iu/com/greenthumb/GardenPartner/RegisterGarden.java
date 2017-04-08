package pervasive.iu.com.greenthumb.GardenPartner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pervasive.iu.com.greenthumb.DBHandler.GardenInfo;
import pervasive.iu.com.greenthumb.DBHandler.saveInfo;
import pervasive.iu.com.greenthumb.Login.LoginActivity;
import pervasive.iu.com.greenthumb.R;
import pervasive.iu.com.greenthumb.Utility.AlbumStorageDirFactory;

public class RegisterGarden extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference dbreference;
    private DatabaseReference userReference ;
    private EditText gardenName, gardenAddress;
    private TextInputLayout inputLayoutgName, inputLayoutgAddress;
    private saveInfo loggedInUserInfo = new saveInfo();

    private static final int ACTION_TAKE_PHOTO_B = 1;
    private static final int ACTION_TAKE_PHOTO_S = 2;
    private static final int ACTION_TAKE_VIDEO = 3;

    private static final String BITMAP_STORAGE_KEY = "viewbitmap";
    private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";
    private ImageView mImageView;
    private Bitmap mImageBitmap;

    private String mCurrentPhotoPath;

    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";

    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_garden);



        firebaseAuth=FirebaseAuth.getInstance();

        gardenName = (EditText) findViewById(R.id.gardenname);
        gardenAddress = (EditText) findViewById(R.id.gardenaddress);
        inputLayoutgName = (TextInputLayout) findViewById(R.id.input_layout_gname);
        inputLayoutgAddress = (TextInputLayout) findViewById(R.id.input_layout_gaddress);

        mImageView = (ImageView) findViewById(R.id.gardenImage);
        mImageView.setVisibility(View.INVISIBLE);

        mImageBitmap = null;

        final String usrId = firebaseAuth.getCurrentUser().getUid();

        System.out.println("loggedInUserInfo"+usrId);
        userReference = FirebaseDatabase.getInstance().getReference(usrId);

        //userReference.


        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    // Retrieve More Data if Required
                    if(ds.getKey().equalsIgnoreCase("phone")){
                        loggedInUserInfo.setPhone(ds.getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        gardenName.addTextChangedListener(new MyTextWatcher(gardenName));
        gardenAddress.addTextChangedListener(new MyTextWatcher(gardenAddress));


    }



    public void onRegister(View view) {

        if (!validategName()) {
            return;
        }

        if (!validategAddress()) {
            return;
        }

        saveGardenInfo();

        Toast.makeText(this,"Garden information saved successfully",Toast.LENGTH_LONG).show();

        finish();

    }

    private boolean validategName() {
        if (gardenName.getText().toString().trim().isEmpty()) {
            inputLayoutgName.setError(getString(R.string.err_msg_gname));
            requestFocus(gardenName);
            return false;
        } else {
            inputLayoutgName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validategAddress() {
        String address = gardenAddress.getText().toString().trim();

        if (address.isEmpty()) {
            inputLayoutgAddress.setError(getString(R.string.err_msg_address));
            requestFocus(gardenAddress);
            return false;
        } else {
            inputLayoutgAddress.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    private void saveGardenInfo(){
        String gardenname = gardenName.getText().toString().trim();
        String gardenaddress = gardenAddress.getText().toString().trim();

        //saveInfo saveinf=new saveInfo(gardenname,gardenaddress);
        dbreference = FirebaseDatabase.getInstance().getReference("gardens");
        FirebaseUser user=firebaseAuth.getCurrentUser();

        String gId = dbreference.push().getKey();

        GardenInfo gInfo = new GardenInfo(gId, gardenname, gardenaddress, user.getUid(), loggedInUserInfo.getPhone(), null);

        dbreference.child(gId).setValue(gInfo);

        dbreference.child(gId).child("gMembers").child("User1").setValue(true);
        dbreference.child(gId).child("gMembers").child("User2").setValue(true);
        dbreference.child(gId).child("gMembers").child("User3").setValue(true);
        //Toast.makeText(getActivity(),"User information saved successfully",Toast.LENGTH_LONG).show();


    }


    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.gardenname:
                    validategName();
                    break;
                case R.id.gardenaddress:
                    validategAddress();
                    break;

            }
        }
    }



    /* Photo album for this application */
    private String getAlbumName() {
        return getString(R.string.galbum_name);
    }


    private File getAlbumDir() {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());

            if (storageDir != null) {
                if (! storageDir.mkdirs()) {
                    if (! storageDir.exists()){
                        Log.d("CameraSample", "failed to create directory");
                        return null;
                    }
                }
            }

        } else {
            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
        }

        return storageDir;
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
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

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
        mImageView.setImageBitmap(bitmap);
        //mVideoUri = null;
        mImageView.setVisibility(View.VISIBLE);
        //mVideoView.setVisibility(View.INVISIBLE);
    }

    private void galleryAddPic() {

        System.out.println("Inside galleryAddPic");
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void handleBigCameraPhoto() {
        System.out.println("handleBigCameraPhoto");

        if (mCurrentPhotoPath != null) {
            setPic();
            galleryAddPic();
            mCurrentPhotoPath = null;
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
