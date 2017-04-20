package pervasive.iu.com.greenthumb.Login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
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
import java.util.Date;

import pervasive.iu.com.greenthumb.DBHandler.saveInfo;

import pervasive.iu.com.greenthumb.R;

public class Profile extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private TextView profileview;
    private Button logout_button;
    private DatabaseReference dbreference;
    private EditText firstname, lastname, email, location, address, phone;
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

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();

        String fname = "";
        String lname ="";
        String loc = "";
        String addr = "";
        String phno = "";

       user = firebaseAuth.getCurrentUser();

        if (user == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }else{

            DatabaseReference dbref = FirebaseDatabase.getInstance().getReference(user.getUid());
            dbref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for(DataSnapshot ds : dataSnapshot.getChildren()){

                        System.out.println("ds.getKey()-----"+ds.getKey());
                        System.out.println("ds.Value()-----"+ds.getValue());

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


        dbreference = FirebaseDatabase.getInstance().getReference();

        firstname = (EditText) findViewById(R.id.fName);
        lastname = (EditText) findViewById(R.id.lName);
        location = (EditText) findViewById(R.id.loc);
        address = (EditText) findViewById(R.id.address);
        phone = (EditText) findViewById(R.id.phone);
        buttonsave = (Button) findViewById(R.id.save);
        userImg = (ImageView) findViewById(R.id.imgProfile);
        user = firebaseAuth.getCurrentUser();

        profileview = (TextView) findViewById(R.id.profile_view);
        profileview.setText(user.getEmail());
        logout_button = (Button) findViewById(R.id.logout_but);

        logout_button.setOnClickListener(this);
        buttonsave.setOnClickListener(this);
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void saveInfo() {
        String first_name = firstname.getText().toString().trim();
        String last_name = lastname.getText().toString().trim();
        String location_val = location.getText().toString().trim();
        String address_val = address.getText().toString().trim();
        String phone_num = phone.getText().toString().trim();

        String token = FirebaseInstanceId.getInstance().getToken();

        if (first_name.isEmpty()) {
            Toast.makeText(this, "Please Enter First Name", Toast.LENGTH_LONG).show();
        } else if (last_name.isEmpty()) {
            Toast.makeText(this, "Please Enter Last Name", Toast.LENGTH_LONG).show();
        } else if (address_val.isEmpty()) {
            Toast.makeText(this, "Please Enter Last Name", Toast.LENGTH_LONG).show();
        } else if (phone_num.isEmpty()) {
            Toast.makeText(this, "Please Enter Phone Number", Toast.LENGTH_LONG).show();
        } else {
            String userId = user.getUid();
            userImageRef = userStorageRef.child(userId+"/"+first_name.replaceAll(" ","")+".jpg");
            saveInfo saveinf = new saveInfo(first_name, last_name, location_val, address_val, phone_num, user.getEmail(),token, userImageRef.getPath());
            FirebaseUser user = firebaseAuth.getCurrentUser();

            try {
                dbreference.child(userId).setValue(saveinf);
                putImagetoFireBase();
                Toast.makeText(this, "User information saved successfully", Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(getApplicationContext(),Navigation.class));

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error Occurred Saving User Details", Toast.LENGTH_LONG).show();
            }

        }


    }

    @Override
    public void onClick(View v) {
        if (v == logout_button) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        if (v == buttonsave) {
            saveInfo();
        }

    }

    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Profile Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

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

    public void imagePicker(View v){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void setPic() {
        int targetW = userImg.getWidth();
        int targetH = userImg.getHeight();

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

		/* Decode the JPEG file into a Bitmap */
        bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

		/* Associate the Bitmap to the ImageView */
        userImg.setImageBitmap(bitmap);
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
        userImg.setDrawingCacheEnabled(true);
        userImg.buildDrawingCache();
        //Bitmap bitmap = plantImg.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBdata = baos.toByteArray();

        UploadTask uploadTask = userImageRef.putBytes(imageBdata);
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
        userImageUri = contentUri;
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            handleBigCameraPhoto();
        }
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                userImg.setImageBitmap(bitmap);
                userImageUri = uri;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
