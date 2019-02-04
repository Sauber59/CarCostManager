package com.example.damo.carcostmanager.addActivities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

public class AddProtectionActivity extends AppCompatActivity {

    EditText dataProtectionET;
    EditText costProtectionET;
    EditText commentProtectionET;
    Button protectionCancelBtn;
    Button protectionSaveBtn;

    private ProgressDialog progressDialog;

    private DatabaseReference databaseProtections;
    private FirebaseAuth firebaseAuth;

    private DatePickerDialog.OnDateSetListener mDateListener;
    //pobranie daty aktualnej
    Calendar cal = Calendar.getInstance();
    Date date = cal.getTime();
    //okreslenie foramtu w jakim beda zapisywane  daty
    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    String formattedDate = dateFormat.format(date);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_protection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dataProtectionET = (EditText) findViewById(R.id.dataProtectionET);
        costProtectionET = (EditText) findViewById(R.id.costProtectionET);
        commentProtectionET = (EditText) findViewById(R.id.commentProtectionET);

        protectionCancelBtn = (Button) findViewById(R.id.protectionCancelBtn);
        protectionSaveBtn = (Button) findViewById(R.id.protectionSaveBtn);

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseProtections = FirebaseDatabase.getInstance().getReference("Protections");

        dataProtectionET.setText(formattedDate);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void sendProtectionInformation(){
        String data = dataProtectionET.getText().toString().trim();

        float cost = Float.parseFloat(costProtectionET.getText().toString().trim());
        String comment = commentProtectionET.getText().toString().trim();

        if (TextUtils.isEmpty(data) || TextUtils.isEmpty(costProtectionET.getText())){
            Toast.makeText(this, "Uzupełnij wszystkie dane!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Zapisywanie danych.. ");
        progressDialog.show();

        String id = databaseProtections.push().getKey();
        Cost protectionInformation = new Cost(id, data,cost,comment);
        FirebaseUser user = firebaseAuth.getCurrentUser();

        databaseProtections.child(user.getUid()).child(id).setValue(protectionInformation).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    progressDialog.hide();
                    Toast.makeText(getApplicationContext(), "Dodano", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddProtectionActivity.this, MenuActivity.class));
                }else{
                    progressDialog.hide();
                    Toast.makeText(getApplicationContext(), "Dodawanie się nie powiodło", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void click(View view) {
        switch (view.getId()){
            case (R.id.protectionSaveBtn):
                sendProtectionInformation();
                break;

            case (R.id.protectionCancelBtn):
                finish();
                startActivity(new Intent(AddProtectionActivity.this, MenuActivity.class));
                break;

            case (R.id.dataProtectionET):
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                //inicjacja  wraz z okresleniem parametrow okna obslugujacego wybieranie daty
                DatePickerDialog dialog = new DatePickerDialog(AddProtectionActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,mDateListener,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                //pobranie ustawionej przez uzytkownika daty
                mDateListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month +1;

                        //ustawienie wybranej oraz zapisanie w zmiennej celem dodania do bazy
                        cal.set(year, month, dayOfMonth);
                        date = cal.getTime();
                        formattedDate = dateFormat.format(date);
                        dataProtectionET.setText(formattedDate);
                    }
                };
                break;
        }
    }

}
