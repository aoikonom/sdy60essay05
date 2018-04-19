package streetmarker.aoikonom.sdy.streetmarker.model;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by aoiko on 12/4/2018.
 */

public class Coordinates implements Serializable {
    private List<LatLng> mPoints;

    public Coordinates(List<LatLng> mPoints) {
        this.mPoints = mPoints;
    }

    public Coordinates() {
        mPoints = new ArrayList<>();
    }

    public List<LatLng> getPoints() {
        return mPoints;
    }

    public void add(LatLng position) {
        mPoints.add(position);
    }

    public int size() {
        return mPoints.size();
    }

    public static String doubleToStr(double value) {
        Locale locale = Locale.US;
        NumberFormat  nb = NumberFormat.getNumberInstance(locale);
        nb.setMaximumFractionDigits(20);
        return nb.format(value);
    }

    public static  double strToDouble(String value) throws ParseException {
        Locale locale = Locale.US;
        NumberFormat  nb = NumberFormat.getNumberInstance(locale);
        return nb.parse(value).doubleValue();
    }

    @Override
    public String toString() {
        if (mPoints == null) return null;

        StringBuilder result = new StringBuilder();

        for (LatLng latLng : mPoints) {
            if (result.length() != 0)
                result.append(", ");
            result.append(Coordinates.doubleToStr(latLng.latitude));
            result.append(",");
            result.append(Coordinates.doubleToStr(latLng.longitude));
        }

        return result.toString();
    }

    public static Coordinates fromString(String coordinates) throws ParseException {
        List<LatLng> points = new ArrayList<>();

        String[] coords = coordinates.split(",");

        if (coords == null) return null;
        if ((coords.length % 2)!= 0)
            return null;

        for (int i = 0; i < coords.length / 2; i++) {
            String lat = coords[2*i].trim();
            String lng = coords[2*i + 1].trim();

            points.add(new LatLng(Coordinates.strToDouble(lat), Coordinates.strToDouble(lng)));
        }

        return new Coordinates(points);
    }

    public int distance() {
        double result = 0.0;
        for (int i = 1; i < mPoints.size(); i++) {
            LatLng s = mPoints.get(i - 1);
            LatLng e = mPoints.get(i);
            Location sl = new Location("");
            Location el = new Location("");
            sl.setLatitude(s.latitude);
            sl.setLongitude(s.longitude);
            el.setLatitude(e.latitude);
            el.setLongitude(e.longitude);
            result += Math.abs(sl.distanceTo(el));
        }
        return (int)result;
    }


}
