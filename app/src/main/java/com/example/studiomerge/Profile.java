package com.example.studiomerge;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.studiomerge.lib.Constant;
import com.example.studiomerge.lib.MultiObservable;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Profile extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "PROFILE";

    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        // Initialise MapView widget
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.googleMap);
        mapFragment.getMapAsync(this);
    }

    /**
     * Extract the email of the charity from the calling Intent and
     * search the database for its physical address (combination of
     * address, state and postcode fields).
     *
     * Fails if there is no charity with the extracted email in the
     * database.
     *
     * The email is extracted using the Constant.PROFILE_EMAIL String.
     *
     * @param googleMap instance of GoogleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        final String email = getIntent().getStringExtra(Constant.PROFILE_EMAIL);

        /**
         * Setup an observable to handle the results of the search
         * query.
         *
         * If a charity was found with the extracted email, attempt to
         * convert their address to a specific latitude and longitude. */
        final MultiObservable<String> charityAddress = new MultiObservable<>();
        charityAddress.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                getLocationFromAddress(getApplicationContext(), (String) arg);
            }
        });

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
                                        doc.get("type").equals("charity")
                                        && doc.get("email").equals(email)
                                    ) {
                                    String address = String.format(
                                            "%s, %s, %s",
                                            doc.get("address"), doc.get("state"),
                                            doc.get("postcode")
                                    );
                                    charityAddress.setValue(address);

                                    Log.d(TAG, "Found charity with email: " + email);
                                    Log.d(TAG, "Charity address: " + address);
                                } else if (++count == task.getResult().size()) {
                                    Log.d(TAG, "Failed to find charity with email: " + email);
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
     * Convert the given address to a specific latitude and longitude
     * using Geocoder.
     *
     * If conversion is successful, call initialiseMap().
     *
     * @param context current application context
     * @param address address to convert
     */
    private void getLocationFromAddress(Context context, String address) {
        Geocoder coder = new Geocoder(context);
        List<Address> coderResults;

        LatLng result = null;
        try {
            coderResults = coder.getFromLocationName(address, 2);
            if (coderResults == null) {
                Log.d(TAG, "Failed to convert address to LatLng.");
                Log.d(TAG, "Address: " + address);
                return;
            }

            Address location = coderResults.get(0);
            initialiseMap(
                    new LatLng(location.getLatitude(), location.getLongitude()),
                    address
            );
        } catch (IOException e) {
            //
        }
    }

    /**
     * Pan the camera to and set a marker at the given location, then
     * zoom in to an appropriate field of view.
     *
     * @param location latitude and longitude of the given address
     * @param address  address description
     */
    private void initialiseMap(LatLng location, String address) {
        try {
            this.googleMap.addMarker(
                    new MarkerOptions().position(location).title(address));
            this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
            this.googleMap.moveCamera(CameraUpdateFactory.zoomTo(16));

            Log.d(TAG, "Map initialised successfully.");
        } catch (IllegalArgumentException e) {
            Log.w(TAG, "Cannot initialise map on null location.", e);
        }
    }
}
