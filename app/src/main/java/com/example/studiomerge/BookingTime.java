package com.example.studiomerge;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class BookingTime extends AppCompatActivity {


    private EditText editTime, editDonorID, editCharity;
    private TextView editDate;
    private static final String TAG = "booking_Time";
    private Button button;
    private DatePickerDialog.OnDateSetListener mDateSetListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_time);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        editDate = findViewById(R.id.dateEt);
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        BookingTime.this,
                        android.R.style.Theme_DeviceDefault,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));
                dialog.show();

            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                editDate.setText(dayOfMonth + "/" + month + "/" + year);
            }
        };

        addbooking();


    }
    public void addbooking() {


        editDonorID = findViewById(R.id.emailEt);
        editDate = findViewById(R.id.dateEt);
        editTime = findViewById(R.id.timeEt);
        editCharity = findViewById(R.id.charityEt);
        button = findViewById(R.id.bookBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Map<String, Object> data = new HashMap<>();

                data.put("donorEmail", editDonorID.getText().toString());
                data.put("charityName", editCharity.getText().toString());
                data.put("date", editDate.getText().toString());
                data.put("time", editTime.getText().toString());


                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("bookings")
                        .add(data)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }

                        });

                //startActivity(new Intent(BookingTime.this,Profile.class));
                finish();
            }
        });


    }

    public void searchCharity(String name) {

        final String[] allCharities = {};
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference charityRef = db.collection("users");

        Query query = charityRef.whereEqualTo("type", "charity")
                .whereEqualTo("name", name)
                .orderBy("date", Query.Direction.DESCENDING)
                .orderBy("time", Query.Direction.DESCENDING);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    int index = 0;
                    for(QueryDocumentSnapshot document: task.getResult()){
                        allCharities[index] = document.getString("organisation");
                        index++;
                        //display result here
                    }
                }
            }
        });
    }
}
