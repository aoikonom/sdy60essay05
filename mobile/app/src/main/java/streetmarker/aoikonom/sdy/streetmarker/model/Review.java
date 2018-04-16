package streetmarker.aoikonom.sdy.streetmarker.model;

public class Review {
    private String writtenByUserId;
    private String writtenByUserName;
    private float rating;
    private String comments;

    public Review() {

    }

    public Review(String writtenByUserId, String writtenByUserName, float rating, String comments) {
        this.writtenByUserName = writtenByUserName;
        this.writtenByUserId = writtenByUserId;
        this.rating = rating;
        this.comments = comments;
    }

    public String getWrittenByUserName() {
        return writtenByUserName;
    }

    public String getWrittenByUserId() { return writtenByUserId; }

    public float getRating() {
        return rating;
    }

    public String getComments() {
        return comments;
    }

    public void setWrittenByUserId(String writtenByUserId) {
        this.writtenByUserId = writtenByUserId;
    }

    public void setWrittenByUserName(String writtenByUserName) {
        this.writtenByUserName = writtenByUserName;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

}
