package pervasive.iu.com.greenthumb.MyPlants;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import pervasive.iu.com.greenthumb.R;

public class MainPlantActivity extends AppCompatActivity {

    private ImageButton btnAddPlant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_plant);
        btnAddPlant = (ImageButton) findViewById(R.id.btnAdd);

        //btnAddPlant.setOnClickListener(this);
    }

    public void addPlant(View v) {
        Toast toast = Toast.makeText(MainPlantActivity.this, "add plant",Toast.LENGTH_LONG);
        toast.show();
        startActivity(new Intent(this,PlantActivity.class));
    }
}
