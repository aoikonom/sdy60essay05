package streetmarker.aoikonom.sdy.streetmarker.model;

import java.io.Serializable;

/**
 * Created by aoiko on 10/3/2018.
 */

public class UserInfo implements Serializable {
    private String userName;
    private String key;
    private float totalRating;
    private int ratingsCount;


    private UserInfo(String key,String userName) {
        this.key = key;
        this.userName = userName;
    }

    private static UserInfo mInstance;

    public static UserInfo getInstance() { return mInstance; }

    public static void setmInstance(UserInfo userInfo) {
        mInstance = userInfo;
    }

    public static UserInfo newInstance(String key,String name) {
        mInstance = new UserInfo(key, name);
        return mInstance;
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
        totalRating += rating;
        ratingsCount ++;
    }

    public float getTotalRating() {
        return totalRating;
    }

    public int getRatingsCount() {
        return ratingsCount;
    }
}
