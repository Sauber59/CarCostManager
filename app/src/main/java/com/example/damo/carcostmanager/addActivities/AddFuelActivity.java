package com.example.damo.carcostmanager.addActivities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.damo.carcostmanager.MenuActivity;
import com.example.damo.carcostmanager.R;
import com.example.damo.carcostmanager.classes.Cost;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddFuelActivity extends AppCompatActivity {

    private EditText dataFuetET;
    private EditText quantityFuelET;
    private EditText costFuelET;
    private EditText distanceFuelET;

    private Button fuelCancelBtn;
    private Button fuelSaveBtn;

    private ProgressDialog progressDialog;

    private DatabaseReference databaseCosts;
    private FirebaseAuth firebaseAuth;

    private DatePickerDialog.OnDateSetListener mDateListener;
    Calendar cal = Calendar.getInstance();
    Date date = cal.getTime();
    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    String formattedDate = dateFormat.format(date);

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

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseCosts = FirebaseDatabase.getInstance().getReference("Costs");

        dataFuetET.setText(formattedDate);
    }

    private void sendCostInformation(){
        String data = dataFuetET.getText().toString().trim();
        float cost = Float.parseFloat(costFuelET.getText().toString().trim());
        float quantity = Float.parseFloat(quantityFuelET.getText().toString().trim());
        float distance = Float.parseFloat(distanceFuelET.getText().toString().trim());

        if (TextUtils.isEmpty(data) || TextUtils.isEmpty(costFuelET.getText())
                || TextUtils.isEmpty(quantityFuelET.getText()) || TextUtils.isEmpty(distanceFuelET.getText())){
            Toast.makeText(this, "Uzupełnij wszystkie dane!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Zapisywanie danych.. ");
        progressDialog.show();

        String id = databaseCosts.push().getKey();
        Cost costInformation = new Cost(id, data,cost,quantity,distance);
        FirebaseUser user = firebaseAuth.getCurrentUser();

        databaseCosts.child(user.getUid()).child(id).setValue(costInformation).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    progressDialog.hide();
                    Toast.makeText(getApplicationContext(), "Dodano", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddFuelActivity.this, MenuActivity.class));
                }else{
                    progressDialog.hide();
                    Toast.makeText(getApplicationContext(), "Dodawanie się nie powiodło", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void click(View view) {
        switch (view.getId()){
            case (R.id.fuelSaveBtn):
                sendCostInformation();
                break;

            case (R.id.fuelCancelBtn):
                finish();
                startActivity(new Intent(AddFuelActivity.this, MenuActivity.class));
                break;

            case (R.id.dataFuelET):
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AddFuelActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,mDateListener,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                mDateListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month +1;

                        cal.set(year, month, dayOfMonth);
                        date = cal.getTime();
                        formattedDate = dateFormat.format(date);
                        dataFuetET.setText(formattedDate);
                    }
                };
                break;
        }
    }
}
