package com.example.damo.carcostmanager;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.TextView;
import android.widget.Toast;

import com.example.damo.carcostmanager.classes.Cost;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**aktywnosc obslugujaca okno i funkcjonalnosc obliczania na postawie zarejestrowanych kosztow w bazie wynikow statystycznych
 * klasa dostarcza informacji na m.in. temat calkowitego przejechanego dystansu, sredniego spalania w tym okresie, calkowitego ksoztu eksploatacji jak i sumy poszczegolnych rodzajow kosztów*/
public class StatsActivity extends AppCompatActivity {

    TextView totalCostTV;
    TextView totalDistanceTV;
    TextView kilometerCostTV;
    TextView totalFuelQuantityTV;
    TextView totalFuelCostTV;
    TextView totalAverageFuelConsumptionTV;
    TextView totalServiceCostTV;
    TextView totalCountServiceTV;
    TextView totalReviewCostTV;
    TextView totalQuantityReviewTV;
    TextView totalProtectionCostTV;
    TextView totalCountProtectionTV;
    TextView startDistanceTV;
    TextView endDistanceTV;

    private DatabaseReference databaseCosts;
    private DatabaseReference databaseServices;
    private DatabaseReference databaseReviews;
    private DatabaseReference databaseProtections;
    private FirebaseAuth firebaseAuth;

    List<Cost> costList;
    List<Cost> serivceList;
    List<Cost> reviewList;
    List<Cost> protectionList;

    int fuelQuantity;
    int serviceQuantity;
    int reviewQuantity;
    int protectionQuantity;

    float fuelCost;
    float averageFuelConsumption;
    float distanse;
    float kilometerCost;
    float allCostSum;

    float seriveceCost;
    float reviewCost;
    float protectionCost;


    int numberOfDatabaseRequest = 4; //zmienna określająca liczbę sekcji, która musi zostać pobrana zanim będą ustawiane wartości pól w oknie
    AtomicInteger currentNumberOfDatabaseRequest;//zmienna weryfikująca kiedy ostatni listener pobieania danych z bazy się zakończy
        /*związane jest to z tym, iż pobieranie danych działa asynchronicznie i nie wiadomo które pobieranie zakończy się jako ostatnie
        przez co nie jesteśmy w stanie przewidzieć miejsca w którym można ustawiać wartości pól.
        Stad stworzono licznik currentNumberOfDatabaseRequest, który zwiększa się wraz z zakończeniem pobrania danych dla każdej sekcji.
        Założono, że sekcje są 4 i kiedy licznik osiągnie taką liczbę to następuje uzupełnianie pól w oknie aplikacji.
         */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        totalCostTV = (TextView)findViewById(R.id.totalCostTV);
        totalDistanceTV = (TextView)findViewById(R.id.totalDistanceTV);
        kilometerCostTV = (TextView)findViewById(R.id.kilometerCostTV);
        totalFuelQuantityTV = (TextView)findViewById(R.id.totalFuelQuantityTV);
        totalFuelCostTV = (TextView)findViewById(R.id.totalFuelCostTV);
        totalAverageFuelConsumptionTV = (TextView)findViewById(R.id.totalAverageFuelConsumptionTV);
        totalServiceCostTV = (TextView)findViewById(R.id.totalServiceCostTV);
        totalCountServiceTV = (TextView)findViewById(R.id.totalCountServiceTV);
        totalReviewCostTV = (TextView)findViewById(R.id.totalReviewCostTV);
        totalQuantityReviewTV = (TextView)findViewById(R.id.totalQuantityReviewTV);
        totalProtectionCostTV = (TextView)findViewById(R.id.totalProtectionCostTV);
        totalCountProtectionTV = (TextView)findViewById(R.id.totalCountProtectionTV);
        startDistanceTV = (TextView)findViewById(R.id.startDistanceTV);
        endDistanceTV = (TextView)findViewById(R.id.endDistanceTV);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseCosts = FirebaseDatabase.getInstance().getReference("Costs/" + user.getUid().toString());
        databaseServices = FirebaseDatabase.getInstance().getReference("Services/" + user.getUid().toString());
        databaseReviews = FirebaseDatabase.getInstance().getReference("Reviews/" + user.getUid().toString());
        databaseProtections = FirebaseDatabase.getInstance().getReference("Protections/" + user.getUid().toString());

        costList = new ArrayList<>();
        serivceList = new ArrayList<>();
        reviewList = new ArrayList<>();
        protectionList = new ArrayList<>();

    }

    @Override
    protected void onStart() {
        super.onStart();

        /* inicjowanie zmiennej mówiącej o liczbie aktualnie pobranych wartości z bazy danych,
        zastosowano rozbudowany typ Integer z zabezpieczeniem wielowątkowości - > AtomicInteger */
        currentNumberOfDatabaseRequest = new AtomicInteger(0);

        //pobieranie danych dot. kosztuPaliw
        databaseCosts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    costList.clear();

                    for (DataSnapshot costSnapshot : dataSnapshot.getChildren()) {
                        Cost cost = costSnapshot.getValue(Cost.class);

                        costList.add(cost);

                        fuelQuantity++;
                        if (costList.size()>1) {
                            fuelCost = fuelCost + cost.getCost();
                        }
                    }
                onDatabaseResult();
                }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(StatsActivity.this, "onStart, databaseCosts error", Toast.LENGTH_LONG).show();

            }
        });

        //pobieranie danych dot. serwisów
        databaseServices.addValueEventListener(new ValueEventListener()  {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    serivceList.clear();

                    for (DataSnapshot costSnapshot : dataSnapshot.getChildren()) {
                        Cost cost = costSnapshot.getValue(Cost.class);

                        serivceList.add(cost);

                        serviceQuantity++;
                        seriveceCost = seriveceCost + cost.getCost();

                    }
                onDatabaseResult();
                }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(StatsActivity.this, "onStart, databaseServices error", Toast.LENGTH_LONG).show();

            }
        });

        //pobieranie danych dot. przeglądów
        databaseReviews.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    reviewList.clear();

                    for (DataSnapshot costSnapshot : dataSnapshot.getChildren()) {
                        Cost cost = costSnapshot.getValue(Cost.class);

                        reviewList.add(cost);

                        reviewQuantity++;
                        reviewCost = reviewCost+ cost.getCost();

                    }
                onDatabaseResult();
        }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(StatsActivity.this, "onStart, databaseReviews error", Toast.LENGTH_LONG).show();

            }
        });

        //pobieranie danych dot. ubezpieczeń
        databaseProtections.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    protectionList.clear();

                    for (DataSnapshot costSnapshot : dataSnapshot.getChildren()) {
                        Cost cost = costSnapshot.getValue(Cost.class);

                        protectionList.add(cost);

                        protectionQuantity++;
                        protectionCost = protectionCost+ cost.getCost();
                    }
                onDatabaseResult();
                }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(StatsActivity.this, "onStart, databaseProtections error", Toast.LENGTH_LONG).show();

            }
        });
    }



    /**ustawianie wartości pól w oknie aplikacji,
     wywoływana jest w momencie osiągnięcia odpowiedniej wartości przez licznik pobierania danych z bazy.
     Zzwiązane jest to z asynchronicznością pobierania danych z bazy danych i niemożliwości ustalenia w którym
     momencie pracy wszystkie dane zostaną pobrane*/
     public void onDatabaseResult() {

        if (currentNumberOfDatabaseRequest.incrementAndGet() == numberOfDatabaseRequest) {

            allCostSum = calculateAllCostSUM(fuelCost, seriveceCost, reviewCost, protectionCost);
            distanse = calculateDistance(costList);
            averageFuelConsumption = roundFloatTo2(calculateAverageFuelConsumption(costList, distanse));
            kilometerCost =roundFloatTo2(calculateKilometerCost(costList, allCostSum));

            totalCostTV.setText(Float.toString(allCostSum) + " zł");
            totalDistanceTV.setText(Float.toString(distanse) + " km");
            kilometerCostTV.setText(Float.toString(kilometerCost) + " zł");
            endDistanceTV.setText(Float.toString(costList.get(costList.size()-1).getDistance()) + " km");
            startDistanceTV.setText(Float.toString(costList.get(0).getDistance()) + " km");

            totalFuelCostTV.setText(Float.toString(fuelCost) + " zł");
            totalFuelQuantityTV.setText(Integer.toString(fuelQuantity) + " tankowań");
            totalAverageFuelConsumptionTV.setText(Float.toString(averageFuelConsumption) + " l");

            totalServiceCostTV.setText(Float.toString(seriveceCost) + " zł");
            totalCountServiceTV.setText(Integer.toString(serviceQuantity) + " serwisów");

            totalReviewCostTV.setText(Float.toString(reviewCost) + " zł");
            totalQuantityReviewTV.setText(Integer.toString(reviewQuantity) + " przeglądów");

            totalProtectionCostTV.setText(Float.toString(protectionCost) + " zł");
            totalCountProtectionTV.setText(Integer.toString(protectionQuantity) + " ubezpieczeń");
        }
    }

    // obliczanie przejechanego dystansu
    public float calculateDistance(List<Cost> costList) {
        float distance = 0;
        if (costList.size() >= 2) {
            distance = costList.get(costList.size()-1).getDistance() - costList.get(0).getDistance();
        }

        return distance;
        }

    //obliczanie sredniego zużycia paliwa
    public float calculateAverageFuelConsumption(List<Cost> costList, float distanse) {
        float fuelSum = 0;
        float averageFuelConsumption = 0;
        
        if (costList.size() > 1) {
            for ( int i = 1; i < costList.size(); i++){
                fuelSum = fuelSum + costList.get(i).getQuantity();
            }
            averageFuelConsumption = fuelSum / distanse * 100;
        }
        return averageFuelConsumption;
    }

    //obliczanie sredniego kosztu pojedynczego przejechanego kilemetra
    public float calculateKilometerCost(List<Cost> costList, float fuelCost) {
        float kilometerCost = 0;
        kilometerCost = fuelCost/ calculateDistance(costList);
        return kilometerCost;
    }
    public float calculateAllCostSUM(float a, float b, float c, float d) {
        return a + b + c + d;
    }

    //zaokrąglanie liczby w float do 2 miejsc po przecinku
    public float roundFloatTo2(float number){
        number = number * 100;
        number = Math.round(number);

        return number/100;
    }
    }




