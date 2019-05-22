package com.example.studiomerge;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

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
}
