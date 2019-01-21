package com.example.damo.carcostmanager.historyActivities;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.ListView;

import com.example.damo.carcostmanager.CostList;
import com.example.damo.carcostmanager.R;
import com.example.damo.carcostmanager.classes.Cost;
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

public class HistoryFuelActivity extends AppCompatActivity {

    ListView listViewCosts;

    private DatabaseReference databaseCosts;
    private FirebaseAuth firebaseAuth;

    List<Cost> costLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_fuel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String databaseRef = "Costs/" + user.getUid().toString();
        databaseCosts = FirebaseDatabase.getInstance().getReference(databaseRef);

        listViewCosts = (ListView) findViewById(R.id.listViewFuelCosts);

        costLists = new ArrayList<>();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseCosts.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    costLists.clear();

                    for (DataSnapshot costSnapshot : dataSnapshot.getChildren()){
                        Cost cost = costSnapshot.getValue(Cost.class);

                        costLists.add(cost);
                    }


                    Collections.reverse(costLists);  //odwrócenie kolejności
                    CostList adapter = new CostList(HistoryFuelActivity.this, costLists);
                    listViewCosts.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
        });
    }
}
