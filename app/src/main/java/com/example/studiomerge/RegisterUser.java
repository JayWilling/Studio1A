package com.example.studiomerge;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user);
    }

    /**
     * Go to the donor registration screen.
     *
     * @param v the View that received the onClick event
     */
    public void onDonor(View v) {
        Intent intent = new Intent(this, RegisterDonor.class);
        startActivity(intent);
    }

    /**
     * Go to the charity registration screen.
     *
     * @param v the View that received the onClick event
     */
    public void onCharity(View v) {
        Intent intent = new Intent(this, RegisterCharity.class);
        startActivity(intent);
    }

    /**
     * Go to the login screen.
     *
     * @param v the View that received the onClick event
     */
    public void onAlreadyMember(View v) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}
