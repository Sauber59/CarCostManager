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

public class AddReviewActivity extends AppCompatActivity {

    EditText dataReviewET;
    EditText costReviewET;
    EditText commenReviewET;
    Button reviewCancelBtn;
    Button reviewSaveBtn;

    private ProgressDialog progressDialog;

    private DatabaseReference databaseReviews;
    private FirebaseAuth firebaseAuth;

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
        setContentView(R.layout.activity_add_review);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //mapowanie elementów z layoutem
        dataReviewET = (EditText) findViewById(R.id.dataReviewET);
        costReviewET = (EditText) findViewById(R.id.costReviewET);
        commenReviewET = (EditText) findViewById(R.id.commenReviewET);
        reviewCancelBtn = (Button) findViewById(R.id.reviewCancelBtn);
        reviewSaveBtn = (Button) findViewById(R.id.reviewSaveBtn);
        progressDialog = new ProgressDialog(this);

        //uzyskanie połączenia z bazą dla zalogowanego uzytkownika
        firebaseAuth = FirebaseAuth.getInstance();
        //utworzenie odwołan do konkretnych tabel w bazie danych
        databaseReviews = FirebaseDatabase.getInstance().getReference("Reviews");

        //ustawienie wartosci pola w odpowiednim formacie daty
        dataReviewET.setText(formattedDate);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //metoda dodajaca obiekt kosztu do bazy danych
    private void sendReviewInformation(){
        //pobranie danych uzupełnionych przez uzytkownika
        String data = dataReviewET.getText().toString().trim();
        float cost = Float.parseFloat(costReviewET.getText().toString().trim());
        String comment = commenReviewET.getText().toString().trim();

        //weryfikacja poprawnosci danych
        if (TextUtils.isEmpty(data) || TextUtils.isEmpty(costReviewET.getText())){
            Toast.makeText(this, "Uzupełnij wszystkie dane!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Zapisywanie danych.. ");
        progressDialog.show();

        //zapisanie id wysylanego elementu
        String id = databaseReviews.push().getKey();
        //stworzenie i uzupelnieniewysylanego obeiktu
        Cost servicesInformation = new Cost(id, data,cost,comment);
        //pobranie identyfikatora zalogowanego uzytkownika
        FirebaseUser user = firebaseAuth.getCurrentUser();

        //zapis danych do bazy w strukturze: idUzytkownika / wartość obiektu car
        databaseReviews.child(user.getUid()).child(id).setValue(servicesInformation).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    progressDialog.hide();
                    Toast.makeText(getApplicationContext(), "Dodano", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddReviewActivity.this, MenuActivity.class));
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
            case (R.id.reviewSaveBtn):
                sendReviewInformation();
                break;

            case (R.id.reviewCancelBtn):
                finish();
                startActivity(new Intent(AddReviewActivity.this, MenuActivity.class));
                break;

            case (R.id.dataReviewET):
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                //inicjacja  wraz z okresleniem parametrow okna obslugujacego wybieranie daty
                DatePickerDialog dialog = new DatePickerDialog(AddReviewActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,mDateListener,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                //pobranie ustawionej przez uzytkownika daty
                mDateListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month +1;

                        cal.set(year, month, dayOfMonth);
                        date = cal.getTime();
                        formattedDate = dateFormat.format(date);
                        dataReviewET.setText(formattedDate);
                    }
                };
                break;
        }
    }

}
