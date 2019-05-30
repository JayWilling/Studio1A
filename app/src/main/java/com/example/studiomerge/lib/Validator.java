package com.example.studiomerge.lib;

import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Validator {

    private static final String TAG = "VALIDATOR";

    public Validator() {}

    /**
     * Check that the given String contains at least one character, no
     * digits and does not start with a space.
     *
     * @param name the String to be checked
     * @return     true if the contents of the field meet the
     *             requirements; false otherwise.
     */
    public boolean isValidName(String name) {
        return (
                name.length() > 0 && name.matches("[^0-9]+")
                && !Character.isWhitespace(name.charAt(0))
        );
    }

    /**
     * Check that the given String contains the @ symbol and has no
     * spaces.
     *
     * @param email the String to be checked
     * @return      true if the contents of the field meet the
     *              requirements; false otherwise.
     */
    public boolean isValidEmail(String email) {
        return (!email.matches("[ ]+") && email.matches(".*[@].*"));
    }

    /**
     * Check that the given email is available by searching for it in
     * the database.
     *
     * The results of the database query must be handled by the given
     * observable due to the query being asynchronous. The observable
     * is set to true if no match is found; false otherwise.
     *
     * @param email      email to search for
     * @param observable Boolean instance of MultiObservable that will
     *                   handle the results of the query
     */
    public void isEmailAvailable(final String email,
                                 final MultiObservable<Boolean> observable) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = 0;
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                if (doc.get("email").equals(email)) {
                                    observable.setValue(false);
                                    Log.d(TAG, "Email unavailable: " + email);
                                } else if (++count == task.getResult().size()) {
                                    observable.setValue(true);
                                    Log.d(TAG, "Email available: " + email);
                                }
                            }
                        } else {
                            Log.w(
                                    TAG, "Error getting documents.",
                                    task.getException()
                            );
                        }
                    }
                });
    }

    /**
     * Check that the given String is 10 characters long and contains
     * only digits.
     *
     * @param phone the String to be checked
     * @return      true if the contents of the field meet the
     *              requirements; false otherwise.
     */
    public boolean isValidPhone(String phone) {
        return (
                phone.isEmpty()
                || (phone.length() == 10 && phone.matches("[0-9]+"))
        );
    }

    /**
     * Check that the contents of the given field are 4-16 characters
     * long and contains at least one number, symbol, lowercase and
     * uppercase letter.
     *
     * Does not save the contents of the field: it is only read.
     *
     * @param field the field to check the contents of
     * @return      true if the contents of the field meet the
     *              requirements; false otherwise.
     */
    public boolean isValidPassword(EditText field) {
        return (
                field.getText().toString().length() >= 4
                && field.getText().toString().length() <= 16
                && field.getText().toString().matches(".*[a-z].*")
                && field.getText().toString().matches(".*[A-Z].*")
                && field.getText().toString().matches(".*[0-9].*")
                && field.getText().toString().matches(".*[^a-zA-Z0-9 ].*")
        );
    }

}
