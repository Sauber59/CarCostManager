package com.example.damo.carcostmanager;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.TextView;

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

public class StatsActivity extends AppCompatActivity {

    TextView totalCostTV;
    TextView totalDistanceTV;
    TextView kilometerCostTV;
    TextView averageKilometerCostTV;
    TextView totalFuelQuantityTV;
    TextView totalFuelCostTV;
    TextView totalAverageFuelConsumptionTV;
    TextView totalServiceCostTV;
    TextView totalCountServiceTV;
    TextView totalReviewCostTV;
    TextView totalQuantityReviewTV;
    TextView totalProtectionCostTV;
    TextView totalCountProtectionTV;

    private DatabaseReference databaseCosts;
    private FirebaseAuth firebaseAuth;

    List<Cost> costList;
    int fuelQuantity = 0;
    float fuelCost;
    float averageFuelConsumption;
    float distanse;
    float kilometerCost;

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
        averageKilometerCostTV = (TextView)findViewById(R.id.averageKilometerCostTV);
        totalFuelQuantityTV = (TextView)findViewById(R.id.totalFuelQuantityTV);
        totalFuelCostTV = (TextView)findViewById(R.id.totalFuelCostTV);
        totalAverageFuelConsumptionTV = (TextView)findViewById(R.id.totalAverageFuelConsumptionTV);
        totalServiceCostTV = (TextView)findViewById(R.id.totalServiceCostTV);
        totalCountServiceTV = (TextView)findViewById(R.id.totalCountServiceTV);
        totalReviewCostTV = (TextView)findViewById(R.id.totalReviewCostTV);
        totalQuantityReviewTV = (TextView)findViewById(R.id.totalQuantityReviewTV);
        totalProtectionCostTV = (TextView)findViewById(R.id.totalProtectionCostTV);
        totalCountProtectionTV = (TextView)findViewById(R.id.totalCountProtectionTV);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseCosts = FirebaseDatabase.getInstance().getReference("Costs/" + user.getUid().toString());

        costList = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseCosts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    costList.clear();

                    for (DataSnapshot costSnapshot : dataSnapshot.getChildren()) {
                        Cost cost = costSnapshot.getValue(Cost.class);

                        costList.add(cost);

                        fuelQuantity++;
                        fuelCost = fuelCost + cost.getCost();

                    }
                    distanse = calculateDistance(costList);
                    averageFuelConsumption = calculateAverageFuelConsumption(costList);
                    kilometerCost = calculateKilometerCost(costList, fuelCost);

                    totalDistanceTV.setText(Float.toString(distanse) + " km");

                    totalFuelCostTV.setText(Float.toString(fuelCost) + " zł");
                    totalFuelQuantityTV.setText(Integer.toString(fuelQuantity) + " tankowań");
                    totalAverageFuelConsumptionTV.setText(Float.toString(fuelQuantity) + " l");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public float calculateDistance(List<Cost> costList) {
        float distance = 0;
        if (costList.size() > 2) {
            distance = costList.get(costList.size()-1).getCost() - costList.get(0).getCost();
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
    }

