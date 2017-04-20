package pervasive.iu.com.greenthumb.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
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

import org.w3c.dom.Text;

import pervasive.iu.com.greenthumb.GardenPartner.GardenPartner;
import pervasive.iu.com.greenthumb.MyPlants.plant;
import pervasive.iu.com.greenthumb.R;

public class Navigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    private ImageView imgViewUser;
    private TextView txtUserName, txtUserEmail;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference userStorageRef = storage.getReference("user");
    private StorageReference userImageRef;
    private String userName="", mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        imgViewUser = (ImageView) header.findViewById(R.id.ivUser);
        txtUserName = (TextView) header.findViewById(R.id.txtUserName);
        txtUserEmail = (TextView) header.findViewById(R.id.txtEmailId);

        //txtUserEmail.setText();

        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getCurrentUser();

        String token = FirebaseInstanceId.getInstance().getToken();
        DatabaseReference userref = FirebaseDatabase.getInstance().getReference(user.getUid());

        LayoutInflater inflater ;
        ViewGroup container;
       // View view = inflater.inflate(R.layout.app_bar_navigation, container, false);
        userref.addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(DataSnapshot dataSnapshot) {

                     for(DataSnapshot ds : dataSnapshot.getChildren()){
                         if(ds.getKey().equalsIgnoreCase("firstname")){
                             userName +=  ds.getValue().toString()+" ";
                         }else if(ds.getKey().equalsIgnoreCase("lastname")){
                             userName +=   ds.getValue().toString();
                         }else if(ds.getKey().equalsIgnoreCase("emailId")){
                             txtUserEmail.setText(ds.getValue().toString());
                         }else if(ds.getKey().equalsIgnoreCase("UserImagePath")){
                             mCurrentPhotoPath = ds.getValue().toString();
                             userImageRef = storage.getReference(mCurrentPhotoPath);
                             Glide.with(getApplicationContext())
                                     .using(new FirebaseImageLoader())
                                     .load(userImageRef)
                                     .diskCacheStrategy(DiskCacheStrategy.NONE)
                                     .skipMemoryCache(true)
                                     .into(imgViewUser);
                         }

                         if(userName != null && !userName.isEmpty()){
                             txtUserName.setText("Welcome, " +userName);
                         }
                     }
         }

         @Override
         public void onCancelled(DatabaseError databaseError) {

         }
     });
        userref.child("token").setValue(token);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        displayScreen(R.id.my_plant);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void displayScreen(int id)
    {
        Fragment fragment=null;
        switch (id)
        {
            case R.id.my_plant:
                fragment=new plant();
                break;
            case R.id.garden_partner:
                fragment=new GardenPartner();
                break;
            case R.id.profile:
                fragment=new my_profile();
                break;
            case R.id.logout:
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(this,LoginActivity.class));
                break;
        }
        if(fragment!=null)
        {
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_navigation,fragment);
            ft.addToBackStack(null);
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        displayScreen(id);

        return true;
    }
}
