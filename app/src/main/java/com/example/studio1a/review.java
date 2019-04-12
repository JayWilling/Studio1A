class Review {
    private int reviewID;
    private int reviewerID;
    private int revieweeID;
    private String content;

    Review(int reviewerID, int revieweeID, String content) {
        reviewID = getNewReviewID();
        this.reviewerID = reviewerID;
        this.revieweeID = revieweeID;
        this.content = content;
    }

    /**
    Return an valid reviewID.
    
    Basic approach to always returning a valid (unused) reviewID would
    be to have this get the largest reviewID in the database and add to
    that. */
    private int getNewReviewID() {}

    // public void remove() {}

    // public void reply() {}
}
