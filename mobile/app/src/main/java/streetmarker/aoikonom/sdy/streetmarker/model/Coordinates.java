package streetmarker.aoikonom.sdy.streetmarker.model;

import com.google.android.gms.maps.model.LatLng;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by aoiko on 12/4/2018.
 */

public class Coordinates {
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

    private static String doubleToStr(double value) {
        Locale locale = Locale.US;
        NumberFormat  nb = NumberFormat.getNumberInstance(locale);
        return nb.format(value);
    }

    private static  double strToDouble(String value) throws ParseException {
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
}
