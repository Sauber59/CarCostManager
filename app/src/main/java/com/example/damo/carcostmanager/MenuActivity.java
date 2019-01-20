package com.example.damo.carcostmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    TextView fuelCostTV;
    TextView fuelDistanceTV;
    TextView fuelQuantityTV;
    TextView fuelConsumptionTV;

    private DatabaseReference databaseCosts;
    private FirebaseAuth firebaseAuth;

    List<Cost> costList;
    Cost lastFuel;
    Cost beforeLastFuel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        costList = new ArrayList<>();

        fuelCostTV = (TextView) findViewById(R.id.fuelCostTV);
        fuelDistanceTV= (TextView) findViewById(R.id.fuelDistanceTV);
        fuelConsumptionTV= (TextView) findViewById(R.id.fuelConsumptionTV);
        fuelQuantityTV= (TextView) findViewById(R.id.fuelQuantityTV);



        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String databaseRef = "Costs/" + user.getUid().toString();
        databaseCosts = FirebaseDatabase.getInstance().getReference(databaseRef);

        //getCostList(costList, databaseCosts);
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseCosts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                costList.clear();

                for (DataSnapshot costSnapshot : dataSnapshot.getChildren()){
                    Cost cost = costSnapshot.getValue(Cost.class);

                    costList.add(cost);
                }

                lastFuel = costList.get(costList.size() - 1);
                beforeLastFuel = costList.get(costList.size() - 2);
                float distance = lastFuel.getDistance() - beforeLastFuel.getDistance();
                fuelCostTV.setText(Float.toString(lastFuel.getCost()) + " zł");
                fuelDistanceTV.setText(Float.toString(distance) + " km");
                fuelQuantityTV.setText(Float.toString(lastFuel.getQuantity()) + " l");
                fuelConsumptionTV.setText(Float.toString(lastFuel.getQuantity() / distance * 100) + " l");
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
        }

        switch (view.getId()){
            case (R.id.addSeriveBtn):
                intent = new Intent(MenuActivity.this, AddServiceActivity.class);
                startActivity(intent);
        }

        switch (view.getId()){
            case (R.id.addProtectionBtn):
                intent = new Intent(MenuActivity.this, AddProtectionActivity.class);
                startActivity(intent);
        }

        switch (view.getId()){
            case (R.id.addReviewbtn):
                intent = new Intent(MenuActivity.this, AddReviewActivity.class);
                startActivity(intent);
        }


    }

    public void clickHistory(View view) {
        Intent intent = null;

        switch (view.getId()){
            case (R.id.hisoryFuelBtn):
                intent = new Intent(MenuActivity.this, HistoryFuelActivity.class);
                startActivity(intent);
        }

        switch (view.getId()){
            case (R.id.historyServiceBtn):
                intent = new Intent(MenuActivity.this, HistoryServiceActivity.class);
                startActivity(intent);
        }

        switch (view.getId()){
            case (R.id.historyProtectionBtn):
                intent = new Intent(MenuActivity.this, HistoryProtectionActivity.class);
                startActivity(intent);
        }

        switch (view.getId()){
            case (R.id.hisoryReviewBtn):
                intent = new Intent(MenuActivity.this, HistoryReviewActivity.class);
                startActivity(intent);
        }
    }
}
