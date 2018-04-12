package streetmarker.aoikonom.sdy.streetmarker.model;

import java.io.Serializable;

/**
 * Created by aoiko on 10/3/2018.
 */

public class UserInfo implements Serializable {
    String userName;

    int targetsCompleted;

    public UserInfo(String userName) {
        this.userName = userName;
    }

    public UserInfo() {

    }

    public String getUserName() {
        return userName;
    }

}
