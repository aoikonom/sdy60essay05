package streetmarker.aoikonom.sdy.streetmarker.model;

import com.google.android.gms.maps.model.LatLng;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import streetmarker.aoikonom.sdy.streetmarker.data.PathFB;
import streetmarker.aoikonom.sdy.streetmarker.utils.PathType;

/**
 * Created by aoiko on 11/4/2018.
 */

public class Path {
    private String mName;
    private String mDescription;
    private Coordinates mCoordinates;
    private String mCreatedByUser;
    private PathType mPathType;

    public Path(String name,String description,String createdByUser,Coordinates coordinates,PathType pathType) {
        this.mName = name;
        this.mDescription = description;
        this.mCreatedByUser = createdByUser;
        this.mCoordinates = coordinates;
        this.mPathType = pathType;
    }

    public static Path fromPathFB(PathFB pathFB) throws ParseException {
        return new Path(pathFB.getName(), pathFB.getDesciption(), pathFB.getCreatedByUser(), Coordinates.fromString(pathFB.getCoordinates()), PathType.valueOf(pathFB.getType()));
    }

    public PathFB toPathFB() {
        return new PathFB(mName, mDescription, mCreatedByUser, mCoordinates.toString(), mPathType.name());
    }

    public String getmName() {
        return mName;
    }

    public String getmDescription() {
        return mDescription;
    }

    public Coordinates getmCoordinates() {
        return mCoordinates;
    }

    public String getmCreatedByUser() {
        return mCreatedByUser;
    }

    public PathType getmPathType() {
        return mPathType;
    }
}
