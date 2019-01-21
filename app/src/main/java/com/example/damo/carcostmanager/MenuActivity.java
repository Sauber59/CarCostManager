package com.example.damo.carcostmanager;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    TextView carBrandTV;
    TextView carModelTV;
    TextView carEngineInfoTV;

    TextView fuelCostTV;
    TextView fuelDistanceTV;
    TextView fuelQuantityTV;
    TextView fuelConsumptionTV;

    private DatabaseReference databaseCosts;
    private DatabaseReference databaseCar;
    private FirebaseAuth firebaseAuth;

    List<Cost> costList;
    Cost lastFuel;
    Cost beforeLastFuel;

    Car carInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        carBrandTV= (TextView) findViewById(R.id.carBrandTV);
        carModelTV= (TextView) findViewById(R.id.carModelTV);
        carEngineInfoTV= (TextView) findViewById(R.id.carEngineInfoTV);

        fuelCostTV = (TextView) findViewById(R.id.fuelCostTV);
        fuelDistanceTV= (TextView) findViewById(R.id.fuelDistanceTV);
        fuelConsumptionTV= (TextView) findViewById(R.id.fuelConsumptionTV);
        fuelQuantityTV= (TextView) findViewById(R.id.fuelQuantityTV);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseCosts = FirebaseDatabase.getInstance().getReference("Costs/" + user.getUid().toString());
        databaseCar = FirebaseDatabase.getInstance().getReference("Car/");

        costList = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();

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

        databaseCosts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    costList.clear();

                    for (DataSnapshot costSnapshot : dataSnapshot.getChildren()) {
                        Cost cost = costSnapshot.getValue(Cost.class);

                        costList.add(cost);
                    }

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

            }
        });
}



    public int test(int a, int b){
        return a+b;
    }

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
