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
    private String mKey;
    private String mName;
    private String mDescription;
    private Coordinates mCoordinates;
    private String mCreatedByUser;
    private PathType mPathType;
    private int mRatingsCount;
    private int mTotalRating;

    public Path(String key,String name,String description,String createdByUser,Coordinates coordinates,PathType pathType,int ratingsCount,int totalRating) {
        this.mKey = key;
        this.mName = name;
        this.mDescription = description;
        this.mCreatedByUser = createdByUser;
        this.mCoordinates = coordinates;
        this.mPathType = pathType;
        this.mRatingsCount = ratingsCount;
        this.mTotalRating = totalRating;
    }

    public static Path fromPathFB(String key,PathFB pathFB) throws ParseException {
        return new Path(key, pathFB.getName(), pathFB.getDesciption(), pathFB.getCreatedByUser(), Coordinates.fromString(pathFB.getCoordinates()),
                PathType.valueOf(pathFB.getType()), pathFB.getRatingsCount(), pathFB.getTotalRating());
    }

    public PathFB toPathFB() {
        return new PathFB(mName, mDescription, mCreatedByUser, mCoordinates.toString(), mPathType.name(), mRatingsCount, mTotalRating);
    }

    public String getName() {
        return mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public Coordinates getCoordinates() {
        return mCoordinates;
    }

    public String getCreatedByUser() {
        return mCreatedByUser;
    }

    public PathType getPathType() {
        return mPathType;
    }

    public int getRatingsCount() {
        return mRatingsCount;
    }

    public int getTotalRating() {
        return mTotalRating;
    }

    public float getAvgRating() {
        if (mRatingsCount == 0)
            return -1;
        else
            return Math.round(mTotalRating * 1.0 / mRatingsCount * 100) / 100;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        this.mKey = key;
    }
}
