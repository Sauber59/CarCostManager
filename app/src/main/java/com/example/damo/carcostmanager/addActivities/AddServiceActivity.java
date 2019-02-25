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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/**aktywnosc obslugujaca okno i funkcjonalnosc dodawania kosztow serwisow*/
public class AddServiceActivity extends AppCompatActivity {

    EditText dataServiceET;
    EditText costServiceET;
    EditText commentServiceET;
    Button serviceCancelBtn;
    Button serviceSaveBtn;

    private ProgressDialog progressDialog;

    private DatabaseReference databaseServices;
    private FirebaseAuth firebaseAuth;

    //zainicjowanie obiektu wywolujacego okno ustawiania daty
    private DatePickerDialog.OnDateSetListener mDateListener;
    //pobranie daty aktualnej
    Calendar cal = Calendar.getInstance();
    Date date = cal.getTime();
    //okreslenie foramtu w jakim beda zapisywane  daty
    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    String formattedDate = dateFormat.format(date);

    private Cost servicesInformation;

    /**mapowanie elementów z layoutem,
     * uzyskanie połączenia z bazą dla zalogowanego uzytkownika,
     * utworzenie odwołan do konkretnych tabel w bazie danych,
     * ustawienie wartosci pola w odpowiednim formacie daty
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //mapowanie elementów z layoutem
        dataServiceET = (EditText) findViewById(R.id.dataServiceET);
        costServiceET = (EditText) findViewById(R.id.costServiceET);
        commentServiceET = (EditText) findViewById(R.id.commentServiceET);
        serviceCancelBtn = (Button) findViewById(R.id.serviceCancelBtn);
        serviceSaveBtn = (Button) findViewById(R.id.serviceSaveBtn);
        progressDialog = new ProgressDialog(this);

        //uzyskanie połączenia z bazą dla zalogowanego uzytkownika
        firebaseAuth = FirebaseAuth.getInstance();
        //utworzenie odwołan do konkretnych tabel w bazie danych
        databaseServices = FirebaseDatabase.getInstance().getReference("Services");

        //ustawienie wartosci pola w odpowiednim formacie daty
        dataServiceET.setText(formattedDate);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**metoda dodajaca obiekt kosztu do bazy danych,
     * pobranie danych uzupełnionych przez uzytkownika,
     * weryfikacja poprawnosci danych,
     * zapisanie id wysylanego elementu,
     * stworzenie i uzupelnieniewysylanego obeiktu,
     * pobranie identyfikatora zalogowanego uzytkownika,
     * zapis danych do bazy w strukturze: idUzytkownika / wartość obiektu car
     * */
    private void sendServiceInformation(){
        //pobranie danych uzupełnionych przez uzytkownika
        String data = dataServiceET.getText().toString().trim();
        float cost = Float.parseFloat(costServiceET.getText().toString().trim());
        String comment =commentServiceET.getText().toString().trim();

        //weryfikacja poprawnosci danych
        if (TextUtils.isEmpty(data) || TextUtils.isEmpty(costServiceET.getText())){
            Toast.makeText(this, "Uzupełnij wszystkie dane!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Zapisywanie danych.. ");
        progressDialog.show();

        //zapisanie id wysylanego elementu
        String id = databaseServices.push().getKey();
        //stworzenie i uzupelnieniewysylanego obeiktu
        servicesInformation = new Cost(id,data,cost,comment);
        //pobranie identyfikatora zalogowanego uzytkownika
        FirebaseUser user = firebaseAuth.getCurrentUser();

        //zapis danych do bazy w strukturze: idUzytkownika / wartość obiektu car
        databaseServices.child(user.getUid()).child(id).setValue(servicesInformation).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    progressDialog.hide();
                    Toast.makeText(getApplicationContext(), "Dodano", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddServiceActivity.this, MenuActivity.class));
                }else{
                    progressDialog.hide();
                    Toast.makeText(getApplicationContext(), "Dodawanie się nie powiodło", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**obsluga przyciskow,
     *inicjacja  wraz z okresleniem parametrow okna obslugujacego wybieranie daty,
     *pobranie ustawionej przez uzytkownika daty,
     *ustawienie wybranej oraz zapisanie w zmiennej celem dodania do bazy*/
    public void click(View view) {
        switch (view.getId()){
            case (R.id.serviceSaveBtn):
                 sendServiceInformation();
                break;

            case (R.id.serviceCancelBtn):
                finish();
                startActivity(new Intent(AddServiceActivity.this, MenuActivity.class));
                break;

            case (R.id.dataServiceET):
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                //inicjacja  wraz z okresleniem parametrow okna obslugujacego wybieranie daty
                DatePickerDialog dialog = new DatePickerDialog(AddServiceActivity.this,
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
                        dataServiceET.setText(formattedDate);
                    }
                };
                break;
        }
    }

}
