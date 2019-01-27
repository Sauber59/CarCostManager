package com.example.damo.carcostmanager.historyActivities;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.damo.carcostmanager.Adapters.CostList;
import com.example.damo.carcostmanager.R;
import com.example.damo.carcostmanager.classes.Cost;
import com.example.damo.carcostmanager.interfaces.interface_delete;
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

public class HistoryFuelActivity extends AppCompatActivity implements interface_delete {

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

        listViewCosts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Cost cost = costLists.get(position);

                showUpdateDialog(cost.getIdCost());
                return false;
            }
        });
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
    @Override
    public void showUpdateDialog(final String costId){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.update_dialog, null);

        dialogBuilder.setView(dialogView);

        final Button deleteBTN = (Button)dialogView.findViewById(R.id.deleteBTN);

        dialogBuilder.setTitle("Akcje");

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        deleteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCost(costId);

                alertDialog.dismiss();

            }
        });

    }

    @Override
    public void deleteCost(String costId){
        DatabaseReference databaseReference = databaseCosts.child(costId);
        databaseReference.removeValue();

        Toast.makeText(this, "Usunieto", Toast.LENGTH_SHORT).show();
    }

}
