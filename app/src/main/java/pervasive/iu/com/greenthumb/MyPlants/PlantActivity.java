package pervasive.iu.com.greenthumb.MyPlants;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.firebase.auth.FirebaseAuth;

import pervasive.iu.com.greenthumb.R;

public class PlantActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private ImageButton buttonSave;
    private EditText editTextPlantName;
    private EditText editTextTime;
    private RadioButton radioBtnIn;
    private RadioButton radioBtnOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);
        firebaseAuth = FirebaseAuth.getInstance();

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rgLocation);
        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

            }
        });
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
