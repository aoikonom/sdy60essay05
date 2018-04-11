package streetmarker.aoikonom.sdy.streetmarker.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import streetmarker.aoikonom.sdy.streetmarker.data.PathFB;

/**
 * Created by aoiko on 11/4/2018.
 */

public class Path {
    private List<LatLng> mCoordinates = new ArrayList<>();
    private String mCreatedByUser;

    public Path(String createdByUser,List<LatLng> coordinates) {
        this.mCreatedByUser = createdByUser;
        this.mCoordinates = coordinates;
    }

    public Path(String mCreatedByUser) {
        this.mCreatedByUser = mCreatedByUser;
    }

    public static Path fromPathFB(PathFB pathFB) {
        throw new UnsupportedOperationException("Path.fromJSSON");
    }

    public void addCoordinate(LatLng coordinate) {
        mCoordinates.add(coordinate);
    }
}
