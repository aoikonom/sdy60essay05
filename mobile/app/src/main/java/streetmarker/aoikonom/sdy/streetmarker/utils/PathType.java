package streetmarker.aoikonom.sdy.streetmarker.utils;

import streetmarker.aoikonom.sdy.streetmarker.R;

/**
 * Created by aoiko on 11/4/2018.
 */

public enum PathType {
    Pedestrian(R.drawable.ic_pedestrian),
    Bicyclist(R.drawable.ic_bicyclist),
    Stroller(R.drawable.ic_stroller);

    int mIcon;

    PathType(int icon) {
        mIcon = icon;
    }

    public int getIcon() {
        return mIcon;
    }

}
