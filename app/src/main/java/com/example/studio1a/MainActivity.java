package com.example.studio1a;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.Context;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db;
    EditText etName;
    Button regBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent myIntent = new Intent(MainActivity.this, RegistrationActivity.class);
        MainActivity.this.startActivity(myIntent);
        db = FirebaseFirestore.getInstance();
    }

    public void addDataOnClick(View view) {
        /** When the button in the main activity is clicked
         *  whatever was written in the EditText will be stored in a new document within
         *  the users collection (terminology for the firebase is a little different to databases)
         *  There is no restriction on what data may be stored within a single document.
         */


        etName = (EditText) findViewById(R.id.etUsername);
        Map<String, Object> user = new HashMap<>(); // New hashmap created containing  details for a new user to be stored
        user.put("username", etName.getText().toString()); // As many fields as desired may be 'put' into the hashmap

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
