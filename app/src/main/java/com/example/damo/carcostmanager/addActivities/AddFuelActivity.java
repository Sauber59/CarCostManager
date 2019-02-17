package com.example.damo.carcostmanager.addActivities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

    private Cost costInformation;

    //zainicjowanie obiektu wywolujacego okno ustawiania daty
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
        setContentView(R.layout.activity_add_fuel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //mapowanie elementów z layoutem
        dataFuetET = (EditText) findViewById(R.id.dataFuelET);
        quantityFuelET = (EditText) findViewById(R.id.quantityFuelET);
        costFuelET = (EditText) findViewById(R.id.costFuelET);
        distanceFuelET = (EditText) findViewById(R.id.distanceFuelET);
        fuelCancelBtn = (Button) findViewById(R.id.fuelCancelBtn);
        fuelSaveBtn = (Button) findViewById(R.id.fuelSaveBtn);
        progressDialog = new ProgressDialog(this);

        //uzyskanie połączenia z bazą dla zalogowanego uzytkownika
        firebaseAuth = FirebaseAuth.getInstance();
        //utworzenie odwołan do konkretnych tabel w bazie danych
        databaseCosts = FirebaseDatabase.getInstance().getReference("Costs");

        //ustawienie wartosci pola w odpowiednim formacie daty
        dataFuetET.setText(formattedDate);
    }


    //metoda dodajaca obiekt kosztu do bazy danych
    private void sendCostInformation(){
        //pobranie danych uzupełnionych przez uzytkownika
        String data = dataFuetET.getText().toString().trim();
        float cost = Float.parseFloat(costFuelET.getText().toString().trim());
        float quantity = Float.parseFloat(quantityFuelET.getText().toString().trim());
        float distance = Float.parseFloat(distanceFuelET.getText().toString().trim());

        //weryfikacja poprawnosci danych
        if (TextUtils.isEmpty(data) || TextUtils.isEmpty(costFuelET.getText())
                || TextUtils.isEmpty(quantityFuelET.getText()) || TextUtils.isEmpty(distanceFuelET.getText())){
            Toast.makeText(this, "Uzupełnij wszystkie dane!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Zapisywanie danych.. ");
        progressDialog.show();

        //zapisanie id wysylanego elementu
        String id = databaseCosts.push().getKey();
        //stworzenie i uzupelnieniewysylanego obeiktu
        costInformation = new Cost(id, data,cost,quantity,distance);
        //pobranie identyfikatora zalogowanego uzytkownika
        FirebaseUser user = firebaseAuth.getCurrentUser();

        //zapis danych do bazy w strukturze: idUzytkownika / wartość obiektu car
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

    //obsługa przycików
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

                //inicjacja  wraz z okresleniem parametrow okna obslugujacego wybieranie daty
                DatePickerDialog dialog = new DatePickerDialog(AddFuelActivity.this,
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
                        dataFuetET.setText(formattedDate);
                    }
                };
                break;
        }
    }
}
