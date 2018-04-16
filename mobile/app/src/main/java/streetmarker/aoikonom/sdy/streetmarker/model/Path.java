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
    private String mCreateByUserId;
    private String mCreatedByUser;
    private PathType mPathType;
    private int mRatingsCount;
    private float mTotalRating;

    public interface RatingChangedListner {
        void onRatingChanged(float newRating);
    }

    private List<RatingChangedListner> mListeners = new ArrayList<>();

    public Path(String key,String name,String description,String createdByUser,String createByUserId,Coordinates coordinates,PathType pathType,int ratingsCount,float totalRating) {
        this.mKey = key;
        this.mName = name;
        this.mDescription = description;
        this.mCreatedByUser = createdByUser;
        this.mCreateByUserId = createByUserId;
        this.mCoordinates = coordinates;
        this.mPathType = pathType;
        this.mRatingsCount = ratingsCount;
        this.mTotalRating = totalRating;
    }

    public static Path fromPathFB(String key,PathFB pathFB) throws ParseException {
        return new Path(key, pathFB.getName(), pathFB.getDesciption(), pathFB.getCreatedByUser(), pathFB.getCreatedByUserId(), Coordinates.fromString(pathFB.getCoordinates()),
                PathType.valueOf(pathFB.getType()), pathFB.getRatingsCount(), pathFB.getTotalRating());
    }

    public PathFB toPathFB() {
        return new PathFB(mName, mDescription, mCreatedByUser, mCreateByUserId, mCoordinates.toString(), mPathType.name(), mRatingsCount, mTotalRating);
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

    public String getCreateByUserId() { return mCreateByUserId; }

    public PathType getPathType() {
        return mPathType;
    }

    public int getRatingsCount() {
        return mRatingsCount;
    }

    public float getTotalRating() {
        return mTotalRating;
    }

    public float getAvgRating() {
        if (mRatingsCount == 0)
            return -1;
        else
            return (float) (Math.round(mTotalRating * 1.0 / mRatingsCount * 2) / 2.0);
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        this.mKey = key;
    }

    public void addRating(float rating) {
        mTotalRating += rating;
        mRatingsCount ++;
        for (RatingChangedListner listner : mListeners)
            listner.onRatingChanged(getAvgRating());
    }

    public void copyRating(Path otherPath) {
        this.mRatingsCount = otherPath.getRatingsCount();
        this.mTotalRating = otherPath.getTotalRating();
        for (RatingChangedListner listner : mListeners)
            listner.onRatingChanged(getAvgRating());
    }

    public void addListener(RatingChangedListner listener) {
        mListeners.add(listener);
    }

    public void removeListener(RatingChangedListner listener) {
        mListeners.remove(listener);
    }
}
