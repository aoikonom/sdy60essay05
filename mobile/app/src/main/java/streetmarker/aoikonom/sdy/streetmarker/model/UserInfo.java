package streetmarker.aoikonom.sdy.streetmarker.model;

import java.io.Serializable;

/**
 * Created by aoiko on 10/3/2018.
 */

public class UserInfo implements Serializable {
    private String userName;
    private String key;
    private int points;


    public UserInfo(String key,String userName) {
        this.key = key;
        this.userName = userName;
    }

    public UserInfo() {

    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUserName() {
        return userName;
    }

    public String getKey() {
        return key;
    }


    public void addRating(float rating) {
        int addPoints = 10*((int)(rating * 2) - 5);
        points += addPoints;
    }

    public void copyPoints(UserInfo otherUser) {
        this.points = otherUser.points;
    }

    public int getPoints() {
        return points;
    }
}
