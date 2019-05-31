package com.example.studiomerge;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import android.view.View;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import javax.annotation.Nullable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import com.example.studiomerge.lib.Constant;

public class ViewBookings extends AppCompatActivity {

    private static final String TAG = "view_Booking";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView data;
    ArrayAdapter<String> dataAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_bookings);


        final List<String> charityArray = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference charityRef = db.collection("bookings");

        Query query = charityRef.whereEqualTo("donorEmail", getIntent().getExtras().getString("currentEmail"));
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //ArrayList<String> allBookings = new ArrayList<>();
                if(task.isSuccessful()){
                    HashMap<String, String> userBookings = new HashMap<>();
                    for(QueryDocumentSnapshot document: task.getResult()){
                        //allBookings.add(document.get("organisation").toString());
                        userBookings.put(document.get("charityName").toString(), document.get("date").toString());

                        System.out.println("Charity added");
                    }

                    List<HashMap<String, String>> allBookings = new ArrayList<>();
                    SimpleAdapter adapter = new SimpleAdapter(
                            ViewBookings.this, allBookings, R.layout.list_item,
                            new String[]{"First Line", "Second Line"},
                            new int[]{R.id.text1, R.id.text2});

                    Iterator it = userBookings.entrySet().iterator();
                    while (it.hasNext()) {
                        HashMap<String, String> resultsMap = new HashMap<>();
                        Map.Entry pair = (Map.Entry)it.next();
                        resultsMap.put("First Line", pair.getKey().toString());
                        resultsMap.put("Second Line", pair.getValue().toString());
                        allBookings.add(resultsMap);
                    }
//                    dataAdapter = new ArrayAdapter<String>(
//                            ViewBookings.this,
//                            android.R.layout.simple_list_item_2,
//                            allCharities
//                    );
                    ListView bookingsLv = (ListView)findViewById(R.id.bookingsLv);

                    bookingsLv.setAdapter(adapter);
                    bookingsLv.setTextFilterEnabled(true);
                }
            }
        });


        ListView charitiesLv = (ListView)findViewById(R.id.charityLv);


        viewBooking();

    }

    protected void viewBooking() {
        super.onStart();

        // No clue what this is meant to be for, left here in case required
        //
        //data = findViewById(R.id.viewData);

        db.collection("bookings")
                .whereEqualTo("donorEmail", Constant.PROFILE_EMAIL)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        List<String> bookings = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : value) {
                            if (doc.get("charityName") != null) {
                                bookings.add(doc.getString("charityName"));
                            }
                            if (doc.get("date") != null) {
                                bookings.add(doc.getString("date"));
                            }
                            if (doc.get("time") != null) {
                                bookings.add(doc.getString("time"));
                            }

                        }
                        Log.d(TAG,"Bookings" + bookings);
                    }

                });


    }
}
