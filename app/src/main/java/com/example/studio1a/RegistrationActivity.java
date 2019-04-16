package com.example.studio1a;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    FirebaseFirestore db;
    EditText currUsername;
    EditText currPassword;
    EditText currEmail;
    EditText currPhone;
    EditText currAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.db = FirebaseFirestore.getInstance();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void registerOnClick(View view) {
        //Make sure all UI elements are defined before using them to store data in the database
        // I believe this is what may have crashed the database last time
        currUsername = (EditText)findViewById(R.id.usernameEt);
        currPassword = (EditText)findViewById(R.id.passwordEt);
        currEmail = (EditText)findViewById(R.id.emailEt);
        currPhone = (EditText)findViewById(R.id.phoneEt);
        currAddress = (EditText)findViewById(R.id.addressEt);

        //add all data to append to the database into a hashmap
        final Map<String, Object> user = new HashMap<>();
        //user.put("Username", currUsername.getText().toString());
        user.put("Password", currPassword.getText().toString());
        user.put("Email", currEmail.getText().toString());
        user.put("Phone", currPhone.getText().toString());
        user.put("Address", currAddress.getText().toString());


        final Context context = this;
        final String cEmail = currEmail.getText().toString();
        CollectionReference userRef = db.collection("users");
        userRef.whereEqualTo("Email", cEmail);


        userRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    boolean emailExists = false;
                    for (DocumentSnapshot document : task.getResult()) {
                        String email = document.getString("Email");
                        if (email.equals(cEmail)) {
                            emailExists = true;
                        }
                    }
                        if (emailExists) {
                            AlertDialog dialog = new AlertDialog.Builder(context)
                                    .setTitle("Incorrect Information")
                                    .setMessage("A user with this email already exists. \n" +
                                            "Please enter a different email.")
                                    .setNegativeButton(android.R.string.ok, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }else {
                            db.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d("TAG", "Document snapshot added with ID:" + documentReference.getId());

                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("TAG","Failure adding new document", e);
                                        }
                                    });
                        }
                }
            }
        });
    }

    





}
