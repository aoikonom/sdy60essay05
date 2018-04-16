package streetmarker.aoikonom.sdy.streetmarker.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

import streetmarker.aoikonom.sdy.streetmarker.*;
import streetmarker.aoikonom.sdy.streetmarker.data.DB;
import streetmarker.aoikonom.sdy.streetmarker.data.IPathRetrieval;
import streetmarker.aoikonom.sdy.streetmarker.data.IUserRetrieval;
import streetmarker.aoikonom.sdy.streetmarker.model.Coordinates;
import streetmarker.aoikonom.sdy.streetmarker.model.Path;
import streetmarker.aoikonom.sdy.streetmarker.model.UserInfo;
import streetmarker.aoikonom.sdy.streetmarker.utils.GamePhase;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        IPathRetrieval, IUserRetrieval, View.OnClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnPolylineClickListener {

    private static final int REQUEST_LOCATION = 1;
    private static final int REQUEST_RESOLVE_ERROR = 2;
    private static final int REQUEST_CHECK_SETTINGS = 3;
    private static final float TRACK_DISTANCE_MORE_THAN = 5;
    private GoogleMap mMap;
    private ImageView mRecordImageView;
    private ImageView mSighoutImageView;
    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;
    private Location mAddedLcoation;
    private boolean cameraInitialized = false;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private FusedLocationProviderClient mFusedLocationClient;
    private GamePhase mGamePhase = GamePhase.NotRecording;

    private Marker mCurrentPosMarker;
    private boolean mResolvingError = false;

    private UserInfo mUserInfo;
    private Coordinates mCurrentCoordinates;
    private Polyline mCurrentPolyline;

    private Map<Marker, Path> mMarkerToPath = new HashMap<>();
    private Map<Polyline, Path> mPolylineToPath = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mRecordImageView = findViewById(R.id.record_icon);
        mRecordImageView.setOnClickListener(this);

        mSighoutImageView = findViewById(R.id.logout_icon);
        mSighoutImageView.setOnClickListener(this);

        //Instantiating the GoogleApiClient
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                    onLocationAquired(location);
                }
            };
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        cameraInitialized = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void startLocationUpdates() {
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null /* Looper */);
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        mMap.setOnPolylineClickListener(this);

    }

    //Callback invoked once the GoogleApiClient is connected successfully
    @Override
    public void onConnected(Bundle bundle) {
        requestLocationCheckSettings();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.requestPermission(this, REQUEST_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else {
            requestLocation();
        }

    }

    public void onLocationAquired(Location location) {
        if (mGamePhase == GamePhase.Recording && mCurrentLocation != null) {
            if (mAddedLcoation == null || mAddedLcoation.distanceTo(location) > TRACK_DISTANCE_MORE_THAN) {
                if (mCurrentCoordinates == null)
                    mCurrentCoordinates = new Coordinates();
                mCurrentCoordinates.add(new LatLng(location.getLatitude(), location.getLongitude()));
                if (mCurrentPolyline != null) {
                    mCurrentPolyline.setPoints(mCurrentCoordinates.getPoints());
                }
                mAddedLcoation = location;
            }
        }

        mCurrentLocation = location;
        if (mGamePhase == GamePhase.Recording || !cameraInitialized)
            initCamera(mCurrentLocation);
        if (mCurrentPosMarker == null)
            mCurrentPosMarker = mMap.addMarker(new MarkerOptions().
                    position(new LatLng(location.getLatitude(), location.getLongitude())).
                    title("Current Position").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pegman)));
        else
            mCurrentPosMarker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
    }

    public void requestLocation() {
        DB.retrieveUserInfo(FirebaseAuth.getInstance().getUid(), this);

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            onLocationAquired(location);
                        }
                    }
                });
        startLocationUpdates();
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if(grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // We can now safely use the API we requested access to
                requestLocation();
            } else {
                // Permission was denied or request was cancelled
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (result.hasResolution()) {
            try {
                mResolvingError = true;
                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else {
            // Show dialog using GooglePlayServicesUtil.getErrorDialog()
            Toast.makeText(this, result.getErrorCode(), Toast.LENGTH_LONG).show();
            mResolvingError = true;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void initCamera(Location location) {
        CameraPosition position = CameraPosition.builder()
                .target(new LatLng(location.getLatitude(),
                        location.getLongitude()))
                .zoom(16f)
                .bearing(0.0f)
                .tilt(0.0f)
                .build();

        mMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(position), null);

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled( true );

        cameraInitialized = true;
    }



    protected void requestLocationCheckSettings() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());



        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(MainActivity.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }


    @Override
    public void onUserRetrieved(UserInfo userInfo) {
        if (userInfo != null)
            mUserInfo = userInfo;
        else {
            mUserInfo = new UserInfo(FirebaseAuth.getInstance().getUid(), FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
//            DB.addUserInfo(FirebaseAuth.getInstance().getUid(), mUserInfo);
        }

        DB.retrievePaths(this);
    }

    private void onStartRecordingPath() {
        mRecordImageView.setImageResource(R.drawable.ic_stop_record);
        mCurrentCoordinates = new Coordinates();
        String msg = "Move arround to create a path \n\n";
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        mCurrentPolyline = empryPolyline();
    }

    private void onStopRecordingPath() {
        mRecordImageView.setImageResource(R.drawable.ic_record);
        onPathFinished(mCurrentCoordinates);
    }

    private void onGamePhaseChanged() {
        if (mGamePhase == GamePhase.Recording)
            onStartRecordingPath();
        else
            onStopRecordingPath();
    }

    @Override
    public void onClick(View v) {
        if (v == mRecordImageView) {
            if (mGamePhase == GamePhase.NotRecording) {
                mGamePhase = GamePhase.Recording;
            } else if (mGamePhase == GamePhase.Recording) {
                mGamePhase = GamePhase.NotRecording;
            }
            onGamePhaseChanged();
        }
        else if (v == mSighoutImageView) {
            signOut();
        }
    }

    void signOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(LoginActivity.SIGNIN_DIFFERENT_USER, true);
        startActivity(intent);
        finish();
    }

    void onPathFinished(Coordinates coordinates) {
        if (coordinates == null || coordinates.size() == 0) return;
        AddPathDialog dialog = AddPathDialog.newInstance(mUserInfo, coordinates);
        dialog.show(getFragmentManager(), "AddPathDialog");
    }

    @Override
    public void onPathAdded(Path path,boolean newPath) {
        if (newPath)
            DB.addPath(path);
        else
            drawPath(path);
    }

    public Polyline drawPath(final Path path) {
        PolylineOptions polylineOptions = new PolylineOptions();
        boolean createdByMe = path.getCreatedByUser().equals(mUserInfo.getUserName());
        polylineOptions.color(createdByMe ? Color.rgb(255, 153, 51) : Color.rgb(0, 0, 179));
        polylineOptions.add(path.getCoordinates().getPoints().toArray(new LatLng[path.getCoordinates().size()]));
        if (path.getCoordinates().size() > 0) {
            LatLng latLng = path.getCoordinates().getPoints().get(0);
            Marker marker = mMap.addMarker(new MarkerOptions().
                    position(latLng).
                    title(path.getName()).icon(BitmapDescriptorFactory.fromResource(path.getPathType().getIcon())));
            mMarkerToPath.put(marker, path);
        }

        Polyline result = mMap.addPolyline(polylineOptions);
        mPolylineToPath.put(result, path);
        result.setClickable(true);
        return result;

    }

    public Polyline empryPolyline() {
        PolylineOptions polylineOptions = new PolylineOptions();
        boolean createdByMe = true;
        polylineOptions.color(createdByMe ? Color.rgb(255, 153, 51) : Color.rgb(0, 0, 179));
        return mMap.addPolyline(polylineOptions);
    }

    public void showPathReviews(Path path) {
        if (path == null) return;
        ReviewsDialog dialog = ReviewsDialog.newInstance(mUserInfo, path);
        dialog.show(getFragmentManager(), "Reviews");
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Path path = mMarkerToPath.get(marker);
        if (path != null) {
            showPathReviews(path);
            return true;
        }
        return false;
    }

    @Override
    public void onPolylineClick(Polyline polyline) {
        Path path = mPolylineToPath.get(polyline);
        if (path != null) {
            showPathReviews(path);
        }
    }
}
