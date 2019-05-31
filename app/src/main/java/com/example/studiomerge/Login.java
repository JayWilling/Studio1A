package com.example.studiomerge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studiomerge.lib.Hash;
import com.example.studiomerge.lib.MultiObservable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Observable;
import java.util.Observer;

public class Login extends AppCompatActivity {

    private static final String TAG = "LOGIN";

    private FirebaseAuth mAuth;
    private TextView tvFailedLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mAuth = FirebaseAuth.getInstance();
        tvFailedLogin = findViewById(R.id.tvFailedLogin);

        setDefaultWidgetVisibility();
    }

    /**
     * Make a login attempt.
     *
     * Search the database for a matching set of credentials. The email
     * address must have been verified (via email verification) for the
     * attempt to be successful.
     *
     * The login attempt will fail if the users collection of the
     * database is empty. Ensure that there is at least one document
     * with email and password fields.
     *
     * If the attempt is successful, go to the main activity. If it is
     * not, display the appropriate error messages.
     *
     * @param v the View that received the onClick event
     */
    public void onLogin(View v) {
        EditText inputEmail = findViewById(R.id.inputEmail);
        EditText inputPassword = findViewById(R.id.inputPassword);
        final String email = inputEmail.getText().toString();
        final String password = new Hash().hash(
                inputPassword.getText().toString()
        );

        setDefaultWidgetVisibility();  // Reset widget visibility

        /**
         * Setup an observable to handle the results of the search
         * query.
         *
         * If a matching user was found, check that the email has been
         * verified and go to the main activity if it is. */
        final Activity currentActivity = this;
        final MultiObservable<Boolean> isMatched = new MultiObservable<>();
        isMatched.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                if ((Boolean) arg) {
                    // Check that the email has been verified
                    mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(currentActivity, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Log.d(TAG, "Current user: " + user.getEmail());
                                    Log.d(TAG, "Email verified: " + user.isEmailVerified());

                                    if (user.isEmailVerified()) {
                                        // Success; go to the main activity
                                        Log.d(TAG, "App login successful.");

                                        Intent intent = new Intent(
                                                getApplicationContext(),
                                                Dashboard.class);
                                        intent.putExtra("currentEmail", user.getEmail());
                                        startActivity(intent);
                                    } else {
                                        tvFailedLogin.setText(R.string.error_email_unverified);
                                        tvFailedLogin.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    Log.w(TAG, "Login failed.", task.getException());
                                }
                            }
                        });
                } else {
                    tvFailedLogin.setText(R.string.error_incorrect_credentials);
                    tvFailedLogin.setVisibility(View.VISIBLE);
                }
            }
        });

        // Search the database for a matching set of credentials
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = 0;
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                if (
                                        doc.get("email").equals(email)
                                        && doc.get("password").equals(password)
                                    ) {
                                    isMatched.setValue(Boolean.TRUE);
                                    Log.d(TAG, "Found user with matching credentials.");
                                } else if (++count == task.getResult().size()) {
                                    isMatched.setValue(Boolean.FALSE);
                                    Log.d(TAG, "Failed to find user with matching credentials.");
                                }
                            }
                        } else {
                            Log.w(
                                    TAG, "Error getting documents.",
                                    task.getException());
                        }
                    }
                });
    }

    /**
     * Go to the registration screen.
     *
     * @param v the View that received the onClick event
     */
    public void onNoAccount(View v) {
        Intent intent = new Intent(this, RegisterUser.class);
        startActivity(intent);
    }

    /**
     * Set the default visibility of on-screen elements.
     *
     * NB: This is done programmatically as doing so in the built-in
     *     editor does not seem to work.
     */
    private void setDefaultWidgetVisibility() {
        tvFailedLogin.setVisibility(View.GONE);
    }
}
