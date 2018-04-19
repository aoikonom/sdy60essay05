package streetmarker.aoikonom.sdy.streetmarker.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;

import java.util.Date;

public class CurrentDesignPath {
    private Coordinates mCoordinates;
    private Polyline mPolyline;
    private Date mStartTime;
    private Date mEndTime;

    public Coordinates getCoordinates() {
        return mCoordinates;
    }

    public void setCoordinates(Coordinates mCoordinates) {
        this.mCoordinates = mCoordinates;
    }

    public Polyline getPolyline() {
        return mPolyline;
    }

    public void setPolyline(Polyline mPolyline) {
        this.mPolyline = mPolyline;
    }

    public Date getStartTime() {
        return mStartTime;
    }

    public void setStartTime(Date mStartTime) {
        this.mStartTime = mStartTime;
    }

    public Date getEndTime() {
        return mEndTime;
    }

    public void setEndTime(Date mEndTime) {
        this.mEndTime = mEndTime;
    }

    public void addPoint(LatLng latLng) {
        if (mCoordinates == null)
            mCoordinates = new Coordinates();
        mCoordinates.add(latLng);
        if (mPolyline != null)
            mPolyline.setPoints(mCoordinates.getPoints());
    }

    public void start() {
        mStartTime = new Date();
        mCoordinates = new Coordinates();
    }

    public void end() {
        mEndTime = new Date();
    }

    public Coordinates getOrCreateCoordinates() {
        if (mCoordinates == null)
            mCoordinates = new Coordinates();
        return mCoordinates;
    }

    public boolean hasCoordinates() {
        return mCoordinates != null && mCoordinates.size() > 0;
    }

    public void cancel() {
        mCoordinates = new Coordinates();
        if (mPolyline != null)
            mPolyline.remove();
    }

    public int distance() {
        if (mCoordinates == null) return 0;
        return mCoordinates.distance();
    }

    public int duration() {
        if (mStartTime == null || mEndTime == null) return 0;

        return (int) (mEndTime.getTime() - mStartTime.getTime()) / 1000;
    }
}
