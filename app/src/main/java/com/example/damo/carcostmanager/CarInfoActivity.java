package com.example.damo.carcostmanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CarInfoActivity extends AppCompatActivity {


    private EditText carBrandET;
    private EditText carModelET;
    private EditText carEngineInfoET;

    private Button fuelCancelBtn;
    private Button fuelSaveBtn;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        carBrandET = (EditText) findViewById(R.id.carBrandET);
        carModelET= (EditText) findViewById(R.id.carModelET);
        carEngineInfoET= (EditText) findViewById(R.id.carEngineInfoET);

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseCar = FirebaseDatabase.getInstance().getReference("Car");
    }

    private void sendCarInformation(){
        String brand = carBrandET.getText().toString().trim();
        String model = carModelET.getText().toString().trim();
        String engine = carEngineInfoET.getText().toString().trim();

        if (TextUtils.isEmpty(brand) || TextUtils.isEmpty(model)
                || TextUtils.isEmpty(engine)) {
            Toast.makeText(this, "Uzupełnij wszystkie dane!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Zapisywanie danych.. ");
        progressDialog.show();

        Car car= new Car(brand, model, engine);
        FirebaseUser user = firebaseAuth.getCurrentUser();

        databaseCar.child(user.getUid()).setValue(car).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    progressDialog.hide();
                    Toast.makeText(getApplicationContext(), "Dodano", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CarInfoActivity.this, MenuActivity.class));
                }else{
                    progressDialog.hide();
                    Toast.makeText(getApplicationContext(), "Dodawanie się nie powiodło", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void click(View view) {
        Intent intent = null;

        switch (view.getId()){
            case (R.id.carInfoCancelBtn):
                intent = new Intent(CarInfoActivity.this, MenuActivity.class);
                startActivity(intent);
                break;
        }

        switch (view.getId()){
            case (R.id.carInfoSaveBtn):
                sendCarInformation();
                break;
        }
    }
}
