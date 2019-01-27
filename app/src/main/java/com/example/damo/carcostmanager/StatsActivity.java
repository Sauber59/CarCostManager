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

    float seriveceCost;
    float reviewCost;
    float protectionCost;

    float startDistance;
    float endDistance;

    int numberOfDatabaseRequest = 4;
    AtomicInteger currentNumberOfDatabaseRequest;//zmienna weryfikująca kiedy ostatni listener pobieania danych z bazy się zakończy


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

        currentNumberOfDatabaseRequest = new AtomicInteger(0);

        databaseCosts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    costList.clear();

                    for (DataSnapshot costSnapshot : dataSnapshot.getChildren()) {
                        Cost cost = costSnapshot.getValue(Cost.class);

                        costList.add(cost);

                        fuelQuantity++;
                        fuelCost = fuelCost + cost.getCost();

                    }
                onDatabaseResult();
                }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(StatsActivity.this, "onStart, databaseCosts error", Toast.LENGTH_LONG).show();

            }
        });

        databaseServices.addValueEventListener(new ValueEventListener() {
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



     public void onDatabaseResult() {

        if (currentNumberOfDatabaseRequest.incrementAndGet() == numberOfDatabaseRequest) {

            distanse = calculateDistance(costList);
            averageFuelConsumption = calculateAverageFuelConsumption(costList);
            kilometerCost = calculateKilometerCost(costList, fuelCost);

            totalCostTV.setText(Float.toString(calculateAllCostSUM(fuelCost, seriveceCost, reviewCost, protectionCost)) + " zł");
            totalDistanceTV.setText(Float.toString(distanse) + " km");
            kilometerCostTV.setText(Float.toString(kilometerCost) + " zł");
            endDistanceTV.setText(Float.toString(costList.get(costList.size()-1).getDistance()) + " km");
            startDistanceTV.setText(Float.toString(costList.get(0).getDistance()) + " km");

            totalFuelCostTV.setText(Float.toString(fuelCost) + " zł");
            totalFuelQuantityTV.setText(Integer.toString(fuelQuantity) + " tankowań");
            totalAverageFuelConsumptionTV.setText(Float.toString(fuelQuantity) + " l");

            totalServiceCostTV.setText(Float.toString(seriveceCost) + " zł");
            totalCountServiceTV.setText(Integer.toString(serviceQuantity) + " serwisów");

            totalReviewCostTV.setText(Float.toString(reviewCost) + " zł");
            totalQuantityReviewTV.setText(Integer.toString(reviewQuantity) + " przeglądów");

            totalProtectionCostTV.setText(Float.toString(protectionCost) + " zł");
            totalCountProtectionTV.setText(Integer.toString(protectionQuantity) + " ubezpieczeń");
        }
    }


    public float calculateDistance(List<Cost> costList) {
        float distance = 0;
        if (costList.size() >= 2) {
            distance = costList.get(costList.size()-1).getDistance() - costList.get(0).getDistance();
        }

        return distance;
        }

    public float calculateAverageFuelConsumption(List<Cost> costList) {
        float fuelSum = 0;
        float fuelNumber = costList.size();
        float averageFuelConsumption = 0;
        
        if (costList.size() > 0) {
            for ( Cost cost : costList){
                fuelSum = fuelSum + cost.getQuantity();
            }
            averageFuelConsumption = fuelSum /fuelNumber;
        }
        return averageFuelConsumption;
    }

    public float calculateKilometerCost(List<Cost> costList, float fuelCost) {
        float kilometerCost = 0;

        if (costList.size() > 1) {
            float beginDistance = costList.get(0).getDistance();
            float endDistance = costList.get(costList.size() - 1).getDistance();

            for (int i = 1; i < costList.size(); i++) {
                if (costList.get(i).getDistance() < beginDistance)
                    beginDistance = costList.get(i).getDistance();

                if (costList.get(i).getDistance() > endDistance)
                    endDistance = costList.get(i).getDistance();
            }
            kilometerCost = fuelCost / (endDistance - beginDistance);
        }
        return kilometerCost;
    }
    public float calculateAllCostSUM(float a, float b, float c, float d) {
        return a + b + c + d;
    }
    }

