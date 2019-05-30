package com.example.studiomerge;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studiomerge.lib.Constant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterSuccess extends AppCompatActivity {

    private static final String TAG = "REG_SUCCESS";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_success);

        mAuth = FirebaseAuth.getInstance();
        createUser(getIntent().getStringExtra(Constant.REGISTER_EMAIL),
                   getIntent().getStringExtra(Constant.REGISTER_PASSWORD));
    }

    /**
     * Create a new Firebase user then login the user to setup the
     * email verification process.
     *
     * @param email    email address
     * @param password password
     */
    private void createUser(final String email, final String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(
                                    TAG, String.format(
                                            "Created new user.\n" +
                                            "Email: %s\n" +
                                            "Password: %s",
                                            email, password
                            ));
                            login(email, password);
                        } else {
                            Log.w(TAG, "Failed to create user.", task.getException());
                        }
                    }
                });
    }

    /**
     * Login to Firebase then send the user a verification email.
     *
     * @param email    email address
     * @param password password
     */
    private void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Login succeeded.");
                            sendVerificationEmail();
                        } else {
                            Log.w(TAG, "Login failed.", task.getException());
                        }
                    }
                });
    }

    /**
     * Send a verification email to the current user.
     */
    private void sendVerificationEmail() {
        FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Verification email sent.");
                        } else {
                            Log.w(TAG, "Failed to send verification email.", task.getException());
                        }
                    }
                });
    }

    /**
     * Wrapper for sendVerificationEmail().
     *
     * @param v the View that received the onClick event
     */
    public void onResendLink(View v) {
        sendVerificationEmail();
    }

    /**
     * Go to the login screen.
     *
     * @param v the View that received the onClick event
     */
    public void onContinue(View v) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}
