import java.util.Date;

class Booking {
    private int donorID;
    private TimeSlot timeslot;
    private String datetime;
    /**
    Can be pending, approved, declined or completed. Pending is the
    default state. */
    private String state = "pending";

    /**
    Create a new Booking using the given parameters.
    
    Should leave it to the controller to add the Booking to the
    database. */
    Booking(int donorID, TimeSlot timeslot) {
        this.donorID = donorID;
        this.timeslot = timeslot;

        Date curDate = new Date();
        datetime = curDate.toString();
    }

    /**
    Remove the Booking from the system.
    
    Don't really think this needs to be here since it doesn't need
    private access. Should be put into a controller instead. */
    public void remove() {
        // Remove from the database
        // Delete the Booking object
    }

    public void approve() { state = "approved"; }
    public void decline() { state = "declined"; }
    public void markAsCompleted() { state = "completed"; }
    public String getState() { return state; }
}
