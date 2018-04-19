package streetmarker.aoikonom.sdy.streetmarker.data;

import java.io.Serializable;

/**
 * Created by aoiko on 11/4/2018.
 */

public class PathFB implements Serializable {
    private String name;
    private String desciption;
    private String createdByUser;
    private String createdByUserId;
    private String coordinates;
    private String type;
    private int ratingsCount;
    private float totalRating;
    private int distance;
    private int duration;

    public PathFB(String name, String desciption, String createdByUser,String createdByUserId, String coordinates, String type,int ratingsCount,float totalRating,
                  int distance,int duration) {
        this.name = name;
        this.desciption = desciption;
        this.createdByUser = createdByUser;
        this.createdByUserId = createdByUserId;
        this.coordinates = coordinates;
        this.type = type;
        this.ratingsCount = ratingsCount;
        this.totalRating = totalRating;
        this.duration = duration;
        this.distance = distance;
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

    public String getCreatedByUserId() { return createdByUserId; }

    public String getCoordinates() {
        return coordinates;
    }

    public String getType() {
        return type;
    }

    public int getRatingsCount() {
        return ratingsCount;
    }

    public float getTotalRating() {
        return totalRating;
    }

    public int getDistance() { return distance; }

    public int getDuration() { return duration; }
}
