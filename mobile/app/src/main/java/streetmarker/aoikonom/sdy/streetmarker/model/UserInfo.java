package streetmarker.aoikonom.sdy.streetmarker.model;

import java.io.Serializable;

/**
 * Created by aoiko on 10/3/2018.
 */

public class UserInfo implements Serializable {
    String userName;

    private UserInfo(String userName) {
        this.userName = userName;
    }

    private static UserInfo mInstance;

    public static UserInfo getInstance() { return mInstance; }

    public static UserInfo newInstance(String name) {
        mInstance = new UserInfo(name);
        return mInstance;
    }

    public UserInfo() {

    }

    public String getUserName() {
        return userName;
    }

}
