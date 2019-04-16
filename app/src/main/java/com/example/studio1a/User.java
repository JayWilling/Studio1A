package com.example.studio1a;
import android.content.Intent;

class User {
    private int userID;
    private String username;
    /**
    Encrypted. We must never know what the password is, but it should
    be safe to check if it contains one or two specific symbols. */
    private String password;
    private int phone;
    private String email;
    private String type;  // Can be user, donor or charity.

    public User(String username, String password, int phone, String email, String type) {
        userID = getNewUserID();
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.type = type;
    }

    /**
    Create a new User using values stored in an Intent.

    Use whenever user registration occurs outside of the registration
    activity. */


    /**
    User() {
        userID = getNewUserID();

        Not exactly sure how getIntent() works so this section may need
        to be modified later on.

        Intent intent = getIntent();
        username = intent.getStringExtra(RegistrationActivity.USERNAME);
        password = intent.getStringExtra(RegistrationActivity.PASSWORD);
        phone = intent.getIntExtra(RegistrationActivity.PHONE);
        email = intent.getStringExtra(RegistrationActivity.EMAIL);
        type = intent.getStringExtra(RegistrationActivity.TYPE);
    } */

    /**
    Return an valid userID.
    
    Basic approach to always returning a valid (unused) userID would
    be to have this get the largest userID in the database and add to
    that. */
    private int getNewUserID() {
        return 0;
    }

    /**
    Return a list of the Reviews made by the User.
    
    Search the database for entries (reviews) with a matching userID. */
    //public Review[] getReviews() {}

    /**
    Return a list of the Bookings made by the User.

    Search the database for entries (bookings) with a status that is
    not "completed" and a matching userID. */
    //public Booking getBookings() {
    //    return Booking(0, TimeSlot timeslot);
    //}

    /**
    Return a list of the Bookings completed by the User.

    Search the database for entries (bookings) with the "completed"
    status and a matching userID. */
    //public Booking[] getDonationHistory() {}
}
