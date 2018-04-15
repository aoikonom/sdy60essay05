package streetmarker.aoikonom.sdy.streetmarker.model;

public class Review {
    private String writtenByUserName;
    private float rating;
    private String comments;

    public Review(String writtenByUserName, float rating, String comments) {
        this.writtenByUserName = writtenByUserName;
        this.rating = rating;
        this.comments = comments;
    }

    public String getWrittenByUserName() {
        return writtenByUserName;
    }

    public float getRating() {
        return rating;
    }

    public String getComments() {
        return comments;
    }
}
