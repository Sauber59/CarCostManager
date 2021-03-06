package com.example.damo.carcostmanager;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.damo.carcostmanager.addActivities.AddFuelActivity;
import com.example.damo.carcostmanager.addActivities.AddProtectionActivity;
import com.example.damo.carcostmanager.addActivities.AddReviewActivity;
import com.example.damo.carcostmanager.addActivities.AddServiceActivity;
import com.example.damo.carcostmanager.classes.Car;
import com.example.damo.carcostmanager.classes.Cost;
import com.example.damo.carcostmanager.historyActivities.HistoryFuelActivity;
import com.example.damo.carcostmanager.historyActivities.HistoryProtectionActivity;
import com.example.damo.carcostmanager.historyActivities.HistoryReviewActivity;
import com.example.damo.carcostmanager.historyActivities.HistoryServiceActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**aktywnosc obslugujaca okno i funkcjonalnosc jednegoz najwaznejszych okien w aplikacji - menu glowne programu
 * z tego poziomu mozliwe jest uruchamianie wszelkich pozostalych aktywnosci, dodawanie kosztow, przegladnie historii i edycja
 * ponadto wyswietla ostatnie dane zarejestrowanych kosztow ja i pozwala przejsc do klasy zwracajacej calosciowe statyski eksploatacji pojazdu*/
public class MenuActivity extends AppCompatActivity {

    TextView carBrandTV;
    TextView carModelTV;
    TextView carEngineInfoTV;

    TextView fuelCostTV;
    TextView fuelDistanceTV;
    TextView fuelQuantityTV;
    TextView fuelConsumptionTV;

    TextView serviceDateTV;
    TextView serviceCostTV;

    TextView reviewCostTV;
    TextView reviewDateTV;

    TextView protectionCostTV;
    TextView protectionDateTV;

    private DatabaseReference databaseCosts;
    private DatabaseReference databaseCar;
    private DatabaseReference databaseServices;
    private DatabaseReference databaseProtections;
    private DatabaseReference databaseReviews;
    private FirebaseAuth firebaseAuth;

    List<Cost> costList;
    List<Cost> protectionList;
    List<Cost> servicesList;
    List<Cost> reviewsList;
    Cost lastFuel;
    Cost lastService;
    Cost lastProtection;
    Cost lastReview;
    Cost beforeLastFuel;

    Car carInfo;

    //okreslenie foramtu w jakim beda obslugiwane daty
    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");



    @Override
    /**
     * mapowanie obiektów,
     * uzyskanie połączenia z bazą dla zalogowanego uzytkownika,
     * zapisanie informacji o zalogowanm uzytkowniku,
     * utworzenie odwołan do konkretnych tabel w bazie danych,
     * zainicjowanie list przechowujących różne rodzaje kosztów
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //mapowanie obiektów
        carBrandTV= (TextView) findViewById(R.id.carBrandTV);
        carModelTV= (TextView) findViewById(R.id.carModelTV);
        carEngineInfoTV= (TextView) findViewById(R.id.carEngineInfoTV);
        fuelCostTV = (TextView) findViewById(R.id.fuelCostTV);
        fuelDistanceTV= (TextView) findViewById(R.id.fuelDistanceTV);
        fuelConsumptionTV= (TextView) findViewById(R.id.fuelConsumptionTV);
        fuelQuantityTV= (TextView) findViewById(R.id.fuelQuantityTV);
        serviceCostTV= (TextView) findViewById(R.id.serviceCostTV);
        serviceDateTV= (TextView) findViewById(R.id.serviceDateTV);
        reviewCostTV= (TextView) findViewById(R.id.reviewCostTV);
        reviewDateTV= (TextView) findViewById(R.id.reviewDateTV);
        protectionCostTV= (TextView) findViewById(R.id.protectionCostTV);
        protectionDateTV= (TextView) findViewById(R.id.protectionDateTV);

        //uzyskanie połączenia z bazą dla zalogowanego uzytkownika
        firebaseAuth = FirebaseAuth.getInstance();
        //zapisanie informacji o zalogowanm uzytkowniku
        FirebaseUser user = firebaseAuth.getCurrentUser();
        //utworzenie odwołan do konkretnych tabel w bazie danych
        databaseCosts = FirebaseDatabase.getInstance().getReference("Costs/" + user.getUid().toString());
        databaseCar = FirebaseDatabase.getInstance().getReference("Car/");
        databaseProtections = FirebaseDatabase.getInstance().getReference("Protections/" + user.getUid().toString());
        databaseReviews = FirebaseDatabase.getInstance().getReference("Reviews/" + user.getUid().toString());
        databaseServices = FirebaseDatabase.getInstance().getReference("Services/" + user.getUid().toString());

        //zainicjowanie list przechowujących różne rodzaje kosztów
        costList = new ArrayList<>();
        protectionList = new ArrayList<>();
        reviewsList= new ArrayList<>();
        servicesList= new ArrayList<>();
    }

    @Override
    /**
     * pobieranie z bazy danych informacji o pojezdzie,
     * pobieranie z bazy danych odstatniego kosztu, wykorzystanie odpowiedniego listenera,
     * iteracja po zapisach w bazie danych,
     * ustawienie wartości pól w oknie aplikacji
     */
    protected void onStart() {
        super.onStart();

        //pobieranie z bazy danych informacji o pojezdzie
        databaseCar.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {

                    for (DataSnapshot carSnapshot : dataSnapshot.getChildren()){
                        carInfo = carSnapshot.getValue(Car.class);
                        carBrandTV.setText(carInfo.getBrand());
                        carModelTV.setText(carInfo.getModel());
                        carEngineInfoTV.setText(carInfo.getEngine());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        //pobieranie z bazy danych odstatniego tankowania, wykorzystanie odpowiedniego listenera
       databaseCosts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    costList.clear();

                    //iteracja po zapisach w bazie danych
                    for (DataSnapshot costSnapshot : dataSnapshot.getChildren()) {
                        //zapis pobranego obiektu
                        Cost cost = costSnapshot.getValue(Cost.class);
                        costList.add(cost);
                    }

                    //ustawienie wartości pól w oknie aplikacji
                    if (costList.size() > 0) {
                        lastFuel = costList.get(costList.size() - 1);
                        fuelCostTV.setText(Float.toString(lastFuel.getCost()) + " zł");
                        fuelQuantityTV.setText(Float.toString(lastFuel.getQuantity()) + " l");
                    }

                    if (costList.size() > 1) {
                        beforeLastFuel = costList.get(costList.size() - 2);
                        float distance = lastFuel.getDistance() - beforeLastFuel.getDistance();
                        fuelDistanceTV.setText(Float.toString(distance) + " km");
                        fuelConsumptionTV.setText(Float.toString(lastFuel.getQuantity() / distance * 100) + " l");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Bład", Toast.LENGTH_SHORT).show();
            }
        });

        //pobieranie z bazy danych  odstatniego serwisu
        databaseServices.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {

                    for (DataSnapshot costSnapshot : dataSnapshot.getChildren()) {
                        Cost cost = costSnapshot.getValue(Cost.class);

                        servicesList.add(cost);

                    }

                    if (servicesList.size() > 0) {
                        lastService = servicesList.get(servicesList.size() - 1);
                        serviceCostTV.setText(Float.toString(lastService.getCost()) + " zł");
                        serviceDateTV.setText(lastService.getData());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Bład", Toast.LENGTH_SHORT).show();
            }
        });

        //pobieranie odstatniego przegladu
        databaseReviews.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {

                    for (DataSnapshot costSnapshot : dataSnapshot.getChildren()) {
                        Cost cost = costSnapshot.getValue(Cost.class);

                        reviewsList.add(cost);

                    }

                    if (reviewsList.size() > 0) {
                        lastReview = reviewsList.get(reviewsList.size() - 1);
                        reviewCostTV.setText(Float.toString(lastReview.getCost()) + " zł");
                        try {
                            reviewDateTV.setText(addDate(lastReview.getData()));
                        } catch (ParseException e) {
                            displayExceptionMessage(e.getMessage());
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Bład", Toast.LENGTH_SHORT).show();
            }
        });

        //pobieranie odstatniego ubezpieczenia
        databaseProtections.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    protectionList.clear();

                    for (DataSnapshot costSnapshot : dataSnapshot.getChildren()) {
                        Cost protection = costSnapshot.getValue(Cost.class);

                        protectionList.add(protection);
                    }

                    if (protectionList.size() > 0) {
                        lastProtection = protectionList.get(protectionList.size() - 1);
                        protectionCostTV.setText(Float.toString(lastProtection.getCost()) + " zł");

                        try {
                            protectionDateTV.setText(addDate(lastProtection.getData()));
                        } catch (ParseException e) {
                            displayExceptionMessage(e.getMessage());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Bład", Toast.LENGTH_SHORT).show();
            }
        });

}

    /**
     * metoda dodająca rok ważności do daty (np. przegląu, ubezpieczenia)
     */

    public String addDate (String dateStr) throws ParseException {
        Date date = dateFormat.parse(dateStr);
        date.setYear(date.getYear() + 1);
        return dateFormat.format(date);
    }

    /**
     * metoda przechwytujaca i wyswietlajaca tresc bledu
     */

    public void displayExceptionMessage(String msg)
    {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * obsluga klikniec w przyciski rozwijące ukryte menu z Buttonami
     */

    public void click(View view) {
        LinearLayout extraContainer = null;

        switch (view.getId()){

            case R.id.fuelBtn:
                extraContainer = (LinearLayout)findViewById(R.id.extraContainerFuel);

                if (extraContainer.getVisibility() == View.GONE) {
                    extraContainer.setVisibility(View.VISIBLE);
                    return;
                }

                if (extraContainer.getVisibility() == View.VISIBLE) {
                    extraContainer.setVisibility(View.GONE);
                }
            break;

            case R.id.serviceBtn:
                extraContainer = (LinearLayout)findViewById(R.id.extraContainerService);

                if (extraContainer.getVisibility() == View.GONE) {
                    extraContainer.setVisibility(View.VISIBLE);
                    return;
                }

                if (extraContainer.getVisibility() == View.VISIBLE) {
                    extraContainer.setVisibility(View.GONE);
                }
                break;

            case R.id.reviewBtn:
                extraContainer = (LinearLayout)findViewById(R.id.extraContainerReview);

                if (extraContainer.getVisibility() == View.GONE) {
                    extraContainer.setVisibility(View.VISIBLE);
                    return;
                }

                if (extraContainer.getVisibility() == View.VISIBLE) {
                    extraContainer.setVisibility(View.GONE);
                }
                break;

            case R.id.protectionBtn:
                extraContainer = (LinearLayout)findViewById(R.id.extraContainerProtection);

                if (extraContainer.getVisibility() == View.GONE) {
                    extraContainer.setVisibility(View.VISIBLE);
                    return;
                }

                if (extraContainer.getVisibility() == View.VISIBLE) {
                    extraContainer.setVisibility(View.GONE);
                }
                break;

            case R.id.editCarBtn:
                extraContainer = (LinearLayout)findViewById(R.id.extraContainerCarInfo);

                if (extraContainer.getVisibility() == View.GONE) {
                    extraContainer.setVisibility(View.VISIBLE);
                    return;
                }

                if (extraContainer.getVisibility() == View.VISIBLE) {
                    extraContainer.setVisibility(View.GONE);
                }
                break;
        }
    }

    /**
     * obsluga klikniec przycisków dodawania kosztów
     */

    public void clickAdd(View view) {
        Intent intent = null;

        switch (view.getId()){
            case (R.id.addFuelBtn):
                intent = new Intent(MenuActivity.this, AddFuelActivity.class);
                startActivity(intent);
                break;
        }

        switch (view.getId()){
            case (R.id.addSeriveBtn):
                intent = new Intent(MenuActivity.this, AddServiceActivity.class);
                startActivity(intent);
                break;
        }

        switch (view.getId()){
            case (R.id.addProtectionBtn):
                intent = new Intent(MenuActivity.this, AddProtectionActivity.class);
                startActivity(intent);
                break;
        }

        switch (view.getId()){
            case (R.id.addReviewbtn):
                intent = new Intent(MenuActivity.this, AddReviewActivity.class);
                startActivity(intent);
                break;
        }

        switch (view.getId()){
            case (R.id.editInfoCarBtn):
                intent = new Intent(MenuActivity.this, CarInfoActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * obsluga klikniec przycisków uruchamiajacych okna z listami zarejestrowanych juz kosztów
     */

    public void clickHistory(View view) {
        Intent intent = null;

        switch (view.getId()){
            case (R.id.hisoryFuelBtn):
                intent = new Intent(MenuActivity.this, HistoryFuelActivity.class);
                startActivity(intent);
                break;
        }

        switch (view.getId()){
            case (R.id.historyServiceBtn):
                intent = new Intent(MenuActivity.this, HistoryServiceActivity.class);
                startActivity(intent);
                break;
        }

        switch (view.getId()){
            case (R.id.historyProtectionBtn):
                intent = new Intent(MenuActivity.this, HistoryProtectionActivity.class);
                startActivity(intent);
                break;
        }

        switch (view.getId()){
            case (R.id.hisoryReviewBtn):
                intent = new Intent(MenuActivity.this, HistoryReviewActivity.class);
                startActivity(intent);
                break;
        }
        switch (view.getId()){
            case (R.id.statsCarBtn):
                intent = new Intent(MenuActivity.this, StatsActivity.class);
                startActivity(intent);
                break;
        }
    }
}
