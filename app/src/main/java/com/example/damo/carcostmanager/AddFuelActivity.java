package com.example.damo.carcostmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import java.util.HashMap;
import java.util.Map;

public class AddFuelActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    private EditText dataFuetET;
    private EditText quantityFuelET;
    private EditText costFuelET;
    private EditText distanceFuelET;

    private Button fuelCancelBtn;
    private Button fuelSaveBtn;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fuel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dataFuetET = (EditText) findViewById(R.id.dataFuelET);
        quantityFuelET = (EditText) findViewById(R.id.quantityFuelET);
        costFuelET = (EditText) findViewById(R.id.costFuelET);
        distanceFuelET = (EditText) findViewById(R.id.distanceFuelET);

        fuelCancelBtn = (Button) findViewById(R.id.fuelCancelBtn);
        fuelSaveBtn = (Button) findViewById(R.id.fuelSaveBtn);

        firebaseAuth = FirebaseAuth.getInstance();

        /* if user no login
        if (firebaseAuth.getCurrentUser() == null){
            //zamknij aktywnosc
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }         */

        databaseReference = FirebaseDatabase.getInstance().getReference();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();


    }

    private void sendCostInformation(){
        String data = dataFuetET.getText().toString().trim();
        String cost = costFuelET.getText().toString().trim();
        String quantity = quantityFuelET.getText().toString().trim();
        String distance = distanceFuelET.getText().toString().trim();

        Toast.makeText(getApplicationContext(), "Zapisywanie danych..", Toast.LENGTH_SHORT).show();

        CostInformation costInformation = new CostInformation(data,cost,quantity,distance);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference.child(user.getUid()).push().setValue(costInformation).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    dataFuetET.setText(null);
                    costFuelET.setText(null);
                    quantityFuelET.setText(null);
                    distanceFuelET.setText(null);
                    Toast.makeText(getApplicationContext(), "Dodano", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddFuelActivity.this, MenuActivity.class));
                }else{
                    Toast.makeText(getApplicationContext(), "Dodawanie się nie powiodło", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void click(View view) {
        switch (view.getId()){
            case (R.id.fuelSaveBtn):
                sendCostInformation();

                //startActivity(new Intent(AddFuelActivity.this, MenuActivity.class));
                break;

            case (R.id.fuelCancelBtn):
                finish();
                startActivity(new Intent(AddFuelActivity.this, MenuActivity.class));
                break;
        }
    }
}
