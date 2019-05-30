package com.example.studiomerge;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studiomerge.lib.Constant;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
    }

    /**
     * STUB. Exists purely to navigate to the profile activity.
     *
     * @param v the View that received the onClick event
     */
    public void onProfile(View v) {
        EditText inputEmail = findViewById(R.id.inputEmail);

        Intent intent = new Intent(this, Profile.class);
        intent.putExtra(Constant.PROFILE_EMAIL, inputEmail.getText().toString());

        startActivity(intent);
    }

    public void onBooking (View v) {
        Intent intent = new Intent(this, BookingTime.class);
        startActivity(intent);
    }

    public void openCalendar(View v) {
        Intent intent = new Intent(this, ViewCalendar.class);
        startActivity(intent);
    }

    public void viewBookings(View v) {
        Intent intent = new Intent(this, ViewBookings.class);
        startActivity(intent);
    }
}
