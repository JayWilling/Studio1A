package com.example.studiomerge;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.studiomerge.lib.Constant;
import com.example.studiomerge.lib.Hash;
import com.example.studiomerge.lib.MultiObservable;
import com.example.studiomerge.lib.Validator;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class RegisterDonor extends AppCompatActivity {

    private static final String TAG = "REG_DONOR";

    private TextView tvFirstNameRequirements;
    private TextView tvLastNameRequirements;
    private TextView tvEmailRequirements;
    private TextView tvPhoneRequirements;
    private TextView tvPasswordRequirements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_donor);

        tvFirstNameRequirements = findViewById(R.id.tvFirstNameRequirements);
        tvLastNameRequirements = findViewById(R.id.tvLastNameRequirements);
        tvEmailRequirements = findViewById(R.id.tvEmailRequirements);
        tvPhoneRequirements = findViewById(R.id.tvPhoneRequirements);
        tvPasswordRequirements = findViewById(R.id.tvPasswordRequirements);

        /**
         * Setup an observable to handle checking for email
         * availability. */
        final MultiObservable<Boolean> isEmailAvailable = new MultiObservable<>();
        isEmailAvailable.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                if ((Boolean) arg) {
                    tvEmailRequirements.setVisibility(View.GONE);
                } else {
                    tvEmailRequirements.setText(R.string.error_email_unavailable);
                    tvEmailRequirements.setVisibility(View.VISIBLE);
                }
            }
        });

        /**
         * Set the value of the observable to change whenever the email
         * field loses focus. */
        final EditText inputEmail = findViewById(R.id.inputEmail);
        inputEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    new Validator().isEmailAvailable(
                            inputEmail.getText().toString(), isEmailAvailable
                    );
                }
            }
        });

        tvEmailRequirements.setVisibility(View.GONE);  // Widgets default to VISIBLE in the IDE
        setDefaultWidgetVisibility();
    }

    /**
     * Check each field for completion, then create a new account and
     * advance to the next activity.
     *
     * The completion of each field is performed by using the relevant
     * function in the Validator class.
     *
     * The availability of the given email is defined using the
     * visibility of the tvEmailRequirements object:
     *
     *     GONE      -- available
     *     VISIBLE   -- unavailable
     *
     * @param v the View that received the onClick event
     */
    public void onCreateAccount(View v) {
        EditText inputFirstName = findViewById(R.id.inputFirstName);
        EditText inputLastName = findViewById(R.id.inputLastName);
        EditText inputEmail = findViewById(R.id.inputEmail);
        EditText inputPhone = findViewById(R.id.inputPhone);
        EditText inputPassword = findViewById(R.id.inputPassword);

        // Check the completion of each field
        Validator validator = new Validator();
        boolean isValidFirstName = validator.isValidName(
                inputFirstName.getText().toString());
        boolean isValidLastName = validator.isValidName(
                inputLastName.getText().toString());
        boolean isValidEmail = validator.isValidEmail(
                inputEmail.getText().toString());
        boolean isValidPhone = validator.isValidPhone(
                inputPhone.getText().toString());
        boolean isValidPassword = validator.isValidPassword(inputPassword);

        if (
                isValidFirstName && isValidLastName && isValidEmail
                && tvEmailRequirements.getVisibility() == View.GONE
                && isValidPhone && isValidPassword
            ) {
            // Create a new user
            Map<String, String> donor = new HashMap<>();
            donor.put("type", "donor");
            donor.put("firstName", inputFirstName.getText().toString());
            donor.put("lastName", inputLastName.getText().toString());
            donor.put("email", inputEmail.getText().toString());
            donor.put("phone", inputPhone.getText().toString());
            donor.put("password", new Hash().hash(
                    inputPassword.getText().toString()));

            // Add the user to the database
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users")
                    .add(donor)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(
                                DocumentReference documentReference) {
                            Log.d(
                                    TAG,
                                    "User added with ID: " + documentReference.getId()
                            );
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Error adding new user.", e);
                        }
                    });

            // Advance to the next activity
            Intent intent = new Intent(
                    this, RegisterSuccess.class);
            intent.putExtra(Constant.REGISTER_EMAIL, inputEmail.getText().toString());
            intent.putExtra(Constant.REGISTER_PASSWORD, new Hash().hash(
                    inputPassword.getText().toString()
            ));
            startActivity(intent);
        } else {
            setDefaultWidgetVisibility();

            // Display the appropriate error messages
            if (!isValidFirstName) {
                tvFirstNameRequirements.setVisibility(View.VISIBLE);
            }
            if (!isValidLastName) {
                tvLastNameRequirements.setVisibility(View.VISIBLE);
            }
            if (!isValidEmail) {
                tvEmailRequirements.setText(R.string.error_invalid_email);
                tvEmailRequirements.setVisibility(View.VISIBLE);
            }
            if (!isValidPhone) {
                tvPhoneRequirements.setVisibility(View.VISIBLE);
            }
            if (!isValidPassword) {
                tvPasswordRequirements.setVisibility(View.VISIBLE);
            }
        }
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

    /**
     * Set the default visibility of on-screen elements.
     *
     * Does not modify the visibility of tvEmailRequirements if the
     * widget is already visible.
     *
     * NB: This is done programmatically as doing so in the built-in
     *     editor does not seem to work.
     */
    private void setDefaultWidgetVisibility() {
        tvFirstNameRequirements.setVisibility(View.GONE);
        tvLastNameRequirements.setVisibility(View.GONE);
        tvPhoneRequirements.setVisibility(View.GONE);
        tvPasswordRequirements.setVisibility(View.GONE);

        if (!(tvEmailRequirements.getVisibility() == View.VISIBLE)) {
            tvEmailRequirements.setVisibility(View.GONE);
        }
    }
}
