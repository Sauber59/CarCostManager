package com.example.damo.carcostmanager.historyActivities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.damo.carcostmanager.Adapters.CostShortAdpater;
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

/**aktywnosc obslugujaca okno i funkcjonalnosc wyswietlania listy historii dodanych kosztow ubezpieczenia*/
public class HistoryProtectionActivity extends AppCompatActivity implements interface_delete {

    ListView listViewProtections;

    private DatabaseReference database;
    private FirebaseAuth firebaseAuth;

    List<Cost> costShortLists;

    /**
     * uzyskanie połączenia z bazą dla zalogowanego uzytkownika,
     * zapisanie informacji o zalogowanm uzytkowniku,
     * utworzenie odwołan do konkretnych tabel w bazie danych,
     * wywowałanie listenera reagujacego na dlugie klikniecie na obiekt listy,
     * wywoałanie okna update
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_protection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //uzyskanie połączenia z bazą dla zalogowanego uzytkownika
        firebaseAuth = FirebaseAuth.getInstance();
        //zapisanie informacji o zalogowanm uzytkowniku
        FirebaseUser user = firebaseAuth.getCurrentUser();
        //utworzenie odwołan do konkretnych tabel w bazie danych
        String databaseRef = "Protections/" + user.getUid().toString();
        database = FirebaseDatabase.getInstance().getReference(databaseRef);

        listViewProtections = (ListView) findViewById(R.id.listViewProtectionCosts);

        costShortLists = new ArrayList<>();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //wywowałanie listenera reagujacego na dlugie klikniecie na obiekt listy
        listViewProtections.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Cost cost = costShortLists.get(position);

                //wywoałanie okna update
                showUpdateDialog(cost.getIdCost());
                return false;
            }
        });
    }

    /**
     * pobranie danych z tabeli w celu wyswietlenia zarejestrowanych zapisów,
     * zapisanie elementow,
     * odwrócenie kolejności kolekcji,
     * zainicjowanie adaptera wyswietlajacego liste zapisow,
     * wybranie adaptera
     */
    @Override
    protected void onStart() {
        super.onStart();

        //pobranie danych z tabeli w celu wyswietlenia zarejestrowanych zapisów
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                costShortLists.clear();

                //zapisanie elementow
                for (DataSnapshot costSnapshot : dataSnapshot.getChildren()){
                    Cost cost = costSnapshot.getValue(Cost.class);

                    costShortLists.add(cost);
                }

                //odwrócenie kolejności kolekcji
                Collections.reverse(costShortLists);
                //zainicjowanie adaptera wyswietlajacego liste zapisow
                CostShortAdpater adapter = new CostShortAdpater(HistoryProtectionActivity.this, costShortLists);
                //wybranie adaptera
                listViewProtections.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    /**
     * metoda uruchamiajaca okno aktualizacji po przytrzymaniu pozycji na liscie kosztow metoda wynika z interfejsu
     * inicjalizacja buildera okna AlertDialog,
     * inicjacja wywoływanego okna, ustawienie layout z którego ma korzystać,
     * zmapowanie buttona,
     * wykreowanie obiektu alertDialog,
     * usuwanie obiektu po kliknieciu przycisku
     */
    @Override
    public void showUpdateDialog(final String costId){
        //inicjalizacja buildera okna AlertDialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        //ustawieinia parametrow wywoływanego okna, ustawienie layout z którego ma korzystać
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);
        //zmapowanie buttona
        final Button deleteBTN = (Button)dialogView.findViewById(R.id.deleteBTN);
        dialogBuilder.setTitle("Akcje");
        //wykreowanie obiektu alertDialog
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        //usuwanie elementu po kliknieciu przycisku
        deleteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCost(costId);

                alertDialog.dismiss();

            }
        });

    }

    @Override
    /**
     * metoda usuwająca zaznaczony na liscie koszt metoda wynika z interfejsu,
     * pobranie obiektu,
     * usuniecie obiektu
     */
    public void deleteCost(String costId){
        //pobranie obiektu
        DatabaseReference databaseReference = database.child(costId);
        //usuniecie obiektu
        databaseReference.removeValue();

        Toast.makeText(this, "Usunieto", Toast.LENGTH_SHORT).show();
    }
}
