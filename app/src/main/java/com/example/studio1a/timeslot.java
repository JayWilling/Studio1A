import java.util.Date;

class TimeSlot {
    private int userID;
    private Date datetime;
    /**
    Anything the user wants to write to describe the opportunity.
    
    Ex:
        Looking for 2-3 people to help me move into my new flat. */
    private String desc;

    TimeSlot(int userID) {
        this.userID = userID;

        Date curDate = new Date();
        datetime = curDate.toString();
    }

    /**
    Remove the TimeSlot from the system.
    
    Don't really think this needs to be here since it doesn't need
    private access. Should be put into a controller instead.
    
    Another question is if this should even exist - do we want to keep
    permanent records of what bookings have been made on which time
    slots? */
    public void remove() {
        // Remove from the database
        // Delete the TimeSlot object
    }

    /**
    Lock the TimeSlot against any further changes.
    
    Could do one of two things:
        1. Prevent any further Bookings being made on the TimeSlot
        2. Prevent the user from modifying the Booking */
    public void lock() {}

    /**
    Make a Booking on the current TimeSlot.
    
    Not really necessary since the goal could be accomplished by just
    using the Booking constructor. Should be a controller method (if
    used). */
    public void makeBooking() {}
}
