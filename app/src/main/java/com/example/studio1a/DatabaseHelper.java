package com.example.studio1a;

import android.support.annotation.NonNull;
import android.util.Log;

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


public class DatabaseHelper {
    FirebaseFirestore db;
    public DatabaseHelper() {
        db = FirebaseFirestore.getInstance();
    }

    public boolean findExistingUser(String email) {
        CollectionReference userRef = db.collection("users");
        userRef.whereEqualTo("Email", email);
        final boolean[] emailExists = new boolean[] {false};

        Query query = userRef.whereEqualTo("Email", email);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    emailExists[0] = false;
                    for (DocumentSnapshot document : task.getResult()) {
                        String email = document.getString("Email");
                        if (email.equals(email)) {
                            emailExists[0] = true;
                        }
                    }
                }
            }
        });
        return emailExists[0];
    }

    public void storeNewUser(Map<String, Object> user) {
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

    public DocumentSnapshot returnDocument(String collection, final String field, final String value) {
        CollectionReference userRef = db.collection(collection);
        userRef.whereEqualTo(field, value);
        final DocumentSnapshot[] data = {null};

        Query query = userRef.whereEqualTo(field, value);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        String currVal = document.getString(field);
                        if (currVal.equals(value)) {
                            data[0] = document;
                        }
                    }
                }
            }
        });
        return data[0];
    }
}