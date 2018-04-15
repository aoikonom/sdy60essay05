package streetmarker.aoikonom.sdy.streetmarker.data;

/**
 * Created by aoiko on 11/4/2018.
 */

public class PathFB {
    private String name;
    private String desciption;
    private String createdByUser;
    private String coordinates;
    private String type;
    private int ratingsCount;
    private int totalRating;

    public PathFB(String name, String desciption, String createdByUser, String coordinates, String type,int ratingsCount,int totalRating) {
        this.name = name;
        this.desciption = desciption;
        this.createdByUser = createdByUser;
        this.coordinates = coordinates;
        this.type = type;
        this.ratingsCount = ratingsCount;
        this.totalRating = totalRating;
    }

    public PathFB() {

    }

    public String getName() {
        return name;
    }

    public String getDesciption() {
        return desciption;
    }

    public String getCreatedByUser() {
        return createdByUser;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public String getType() {
        return type;
    }

    public int getRatingsCount() {
        return ratingsCount;
    }

    public int getTotalRating() {
        return totalRating;
    }
}
