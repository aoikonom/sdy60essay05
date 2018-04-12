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

    public PathFB(String name, String desciption, String createdByUser, String coordinates, String type) {
        this.name = name;
        this.desciption = desciption;
        this.createdByUser = createdByUser;
        this.coordinates = coordinates;
        this.type = type;
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
}
