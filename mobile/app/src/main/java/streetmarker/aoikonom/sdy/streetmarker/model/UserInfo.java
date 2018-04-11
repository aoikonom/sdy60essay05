package streetmarker.aoikonom.sdy.streetmarker.model;

import android.app.IntentService;

import java.io.Serializable;

/**
 * Created by aoiko on 10/3/2018.
 */

public class UserInfo implements Serializable {
    long currentMission;
    String currentTarget;

    int targetsCompleted;

    public UserInfo(long currentMission, String currentTarget) {
        this.currentMission = currentMission;
        this.currentTarget = currentTarget;
        this.targetsCompleted = 0;

        if (currentTarget != null) {
            String[] tokens = currentTarget.split("_");
            if (tokens.length > 1)
                targetsCompleted = Integer.valueOf(tokens[1]);
        };
    }

    public UserInfo() {

    }

    public long getCurrentMission() {
        return currentMission;
    }

    public String getCurrentTarget() {
        return currentTarget;
    }

    public int getTargetsCompleted() { return targetsCompleted; }

    public void setTargetsCompleted(int targetsCompleted) {
        this.targetsCompleted = targetsCompleted;
    }
}
