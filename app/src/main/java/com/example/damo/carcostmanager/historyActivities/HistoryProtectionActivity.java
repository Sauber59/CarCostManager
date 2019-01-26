package com.example.damo.carcostmanager.historyActivities;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.damo.carcostmanager.Adapters.CostList;
import com.example.damo.carcostmanager.Adapters.CostShortAdpater;
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

public class HistoryProtectionActivity extends AppCompatActivity {

    ListView listViewProtections;

    private DatabaseReference database;
    private FirebaseAuth firebaseAuth;

    List<Cost> costShortLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_protection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String databaseRef = "Protections/" + user.getUid().toString();
        database = FirebaseDatabase.getInstance().getReference(databaseRef);

        listViewProtections = (ListView) findViewById(R.id.listViewProtectionCosts);

        costShortLists = new ArrayList<>();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                costShortLists.clear();

                for (DataSnapshot costSnapshot : dataSnapshot.getChildren()){
                    Cost cost = costSnapshot.getValue(Cost.class);

                    costShortLists.add(cost);
                }


                Collections.reverse(costShortLists);  //odwrócenie kolejności
                CostShortAdpater adapter = new CostShortAdpater(HistoryProtectionActivity.this, costShortLists);
                listViewProtections.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
