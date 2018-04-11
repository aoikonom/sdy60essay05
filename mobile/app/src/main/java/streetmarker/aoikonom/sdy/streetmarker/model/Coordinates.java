package streetmarker.aoikonom.sdy.streetmarker.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by aoiko on 12/4/2018.
 */

public class Coordinates {
    private List<LatLng> mPoints;

    public Coordinates(List<LatLng> mPoints) {
        this.mPoints = mPoints;
    }

    public List<LatLng> getPoints() {
        return mPoints;
    }
}
