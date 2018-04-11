package streetmarker.aoikonom.sdy.streetmarker.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import streetmarker.aoikonom.sdy.streetmarker.services.GeofenceTrasitionService;

/**
 * Created by aoiko on 13/3/2018.
 */

public class GeofenceUtils {
    private static final String GEOTAG = "Geo";
    public static String GEOFENCE_NAME = "Geofence";
    public static final String GEOFENCE_ACTION = "ActionGeofence";
    public static final String GEOFENCE_EXTRA_TRANSITION = "TransitionStatus";
    public static final int GEOFENCE_REQ_CODE = 4;
    private Context mContext;

    private GeofencingClient mGeofencingClient;
    private GoogleMap mMap;

    public GeofenceUtils(Context context,GoogleMap map) {
        this.mContext = context;
        this.mMap = map;
        mGeofencingClient = LocationServices.getGeofencingClient(context);
    }

    Geofence createGeofence(Target target) {
        Geofence geofence = new Geofence.Builder()
                // Set the request ID of the geofence. This is a string to identify this
                // geofence.
                .setRequestId(GEOFENCE_NAME)

                .setCircularRegion(
                        target.getSearchLatitude(),
                        target.getSearchLongitude(),
                        target.getRadius()
                )
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();
        return geofence;
    }

    private GeofencingRequest createGeofenceRequest(Geofence geofence) {
        return new GeofencingRequest.Builder()
                .setInitialTrigger( GeofencingRequest.INITIAL_TRIGGER_ENTER )
                .addGeofence(geofence)
                .build();
    }

    private void drawGeofence(Target target) {
        Log.d(GEOTAG, "drawGeofence()");

        CircleOptions circleOptions = new CircleOptions()
                .center(target.getSearchPosition())
                .strokeColor(Color.argb(50, 70, 70, 70))
                .fillColor(Color.argb(100, 150, 150, 150))
                .radius(target.getRadius());
        mMap.addCircle( circleOptions );
    }

    public void startGeofence(final Target target) throws SecurityException {
        Log.i(GEOTAG, "startGeofence()");
        if( target != null ) {
            Geofence geofence = createGeofence(target);
            GeofencingRequest geofenceRequest = createGeofenceRequest(geofence);
            PendingIntent geofeceIntent = createGeofencePendingIntent();

            Log.d(GEOTAG, "addGeofence");
            mGeofencingClient.addGeofences(geofenceRequest, geofeceIntent).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    drawGeofence(target);
                }
            });

        } else {
            Log.e(GEOTAG, "Geofence marker is null");
        }
    }

    private PendingIntent createGeofencePendingIntent() {
        Log.d(GEOTAG, "createGeofencePendingIntent");
        Intent intent = new Intent( mContext, GeofenceTrasitionService.class);
        return PendingIntent.getService(mContext, GEOFENCE_REQ_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT );
    }


}
