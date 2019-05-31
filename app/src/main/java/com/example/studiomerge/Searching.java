package com.example.studiomerge;

import android.os.Bundle;

import com.example.studiomerge.lib.MultiObservable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Searching extends AppCompatActivity {
    private static final String TAG = "SEARCHING";
    ArrayAdapter<String> dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching);



        EditText charityEt = (EditText)findViewById(R.id.charityEt);
        final List<String> charityArray = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference charityRef = db.collection("users");

        Query query = charityRef.whereEqualTo("type", "charity");
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<String> allCharities = new ArrayList<>();
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document: task.getResult()){
                        allCharities.add(document.get("organisation").toString());

                        System.out.println("Charity added");
                    }
                     dataAdapter = new ArrayAdapter<String>(
                            Searching.this,
                            android.R.layout.simple_list_item_1,
                            allCharities
                    );
                    ListView searchCharities = (ListView)findViewById(R.id.charityLv);

                    searchCharities.setAdapter(dataAdapter);
                    searchCharities.setTextFilterEnabled(true);
                }
            }
        });


        ListView charitiesLv = (ListView)findViewById(R.id.charityLv);
        charitiesLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Open charity profile window
            }
        });

        charityEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dataAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public ArrayList<String> searchCharity(String name) {

        final String inputName = name;
        final ArrayList<String> allCharities = new ArrayList<>();
        final MultiObservable<Boolean> isSearched = new MultiObservable<>();
        Log.d(TAG, "Entering observer");
        System.out.println("getting there");
        isSearched.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                if ((Boolean) arg) {
                    Log.d(TAG, "In observer");
                    System.out.println("In there");
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    CollectionReference charityRef = db.collection("users");

                    Query query = charityRef.whereEqualTo("type", "charity")
                            .whereGreaterThanOrEqualTo("organisation", inputName);
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(QueryDocumentSnapshot document: task.getResult()){
                                    //if (document.get("type").toString().equals("charity")) {
                                    allCharities.add(document.get("organisation").toString());
                                    Log.d(TAG, "Charity added with name: " + document.get("organisation"));

                                    System.out.println("Charity added");
                                    //}
                                    //display result here
                                }
                                isSearched.setValue(Boolean.TRUE);
                            }
                        }
                    });
                }

            }
        });
        return allCharities;
    }

}
