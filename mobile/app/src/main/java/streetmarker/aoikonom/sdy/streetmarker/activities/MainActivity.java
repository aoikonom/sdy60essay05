package streetmarker.aoikonom.sdy.streetmarker.activities;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;


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

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import streetmarker.aoikonom.sdy.streetmarker.*;
import streetmarker.aoikonom.sdy.streetmarker.data.DB;
import streetmarker.aoikonom.sdy.streetmarker.data.IPathRetrieval;
import streetmarker.aoikonom.sdy.streetmarker.data.IUserRetrieval;
import streetmarker.aoikonom.sdy.streetmarker.data.PathFB;
import streetmarker.aoikonom.sdy.streetmarker.model.CurrentDesignPath;
import streetmarker.aoikonom.sdy.streetmarker.model.Path;
import streetmarker.aoikonom.sdy.streetmarker.model.UserInfo;
import streetmarker.aoikonom.sdy.streetmarker.utils.GamePhase;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        IPathRetrieval, IUserRetrieval, View.OnClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnPolylineClickListener, RecognitionListener  {

    private static final int PERMISSION_REQUEST_LOCATION = 1;
    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 2;
    private static final int REQUEST_RESOLVE_ERROR = 2;
    private static final int REQUEST_CHECK_SETTINGS = 3;
    private static final float TRACK_DISTANCE_MORE_THAN = 1;
    private static final int RC_PATH = 1;
    private static final String VOICE_MODE_START = "ModeStart";
    private static final String VOICE_MODE_STOP = "ModeStop";
    private static final String VOICE_MODE_MENU = "ModeMenu";
    private static final String VOICE_MODE_KEYWORDS = "ModeKeywords";
    private static final String VOICE_COMMAND_START = "start path";
    private static final String VOICE_COMMAND_STOP = "stop path";

    /* Used to handle permission request */

    private GoogleMap mMap;
    private ImageView mRecordImageView;
    private ImageView mSighoutImageView;
    private ImageView mPathListImageView;
    private ImageView mGotoMyLocationImageView;
    private ImageView mLeaderBoardImageView;
    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;
    private Location mAddedLcoation;
    private boolean returnedFromPathList = false;
    private boolean cameraInitialized = false;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private FusedLocationProviderClient mFusedLocationClient;
    private GamePhase mGamePhase = GamePhase.None;

    private Marker mCurrentPosMarker;
    private boolean mResolvingError = false;

    private UserInfo mUserInfo;
    private CurrentDesignPath mCurrentDesignPath;

    private ArrayList<Path> mPaths = new ArrayList<>();
    private Map<Marker, Path> mMarkerToPath = new HashMap<>();
    private Map<Polyline, Path> mPolylineToPath = new HashMap<>();

    private SpeechRecognizer recognizer;
    private String mCurrentVoiceMode = VOICE_MODE_START;


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

        mPathListImageView = findViewById(R.id.path_list_icon);
        mPathListImageView.setOnClickListener(this);

        mGotoMyLocationImageView = findViewById(R.id.goto_current_location);
        mGotoMyLocationImageView.setOnClickListener(this);

        mLeaderBoardImageView = findViewById(R.id.leader_board_icon);
        mLeaderBoardImageView.setOnClickListener(this);

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

        Toast.makeText(this, "Press red button or say '" + VOICE_COMMAND_START + "' to start recording path", Toast.LENGTH_LONG).show();

    }

    void askRecordingPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.requestPermission(this, PERMISSIONS_REQUEST_RECORD_AUDIO, Manifest.permission.RECORD_AUDIO, true);
            return;
        }
        // Recognizer initialization is a time-consuming and it involves IO,
        // so we execute it in async task
        new SetupRecognizerTask(this).execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        if (!returnedFromPathList)
            cameraInitialized = false;
        returnedFromPathList = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    if (recognizer != null) {
        recognizer.cancel();
        recognizer.shutdown();
        }
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

        mGamePhase = GamePhase.NotRecording;

    }

    //Callback invoked once the GoogleApiClient is connected successfully
    @Override
    public void onConnected(Bundle bundle) {
        requestLocationCheckSettings();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.requestPermission(this, PERMISSION_REQUEST_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else {
            requestLocation();
        }

    }

    public void onLocationAquired(Location location) {
        if (mGamePhase == GamePhase.Recording && mCurrentLocation != null) {
            if (mAddedLcoation == null || mAddedLcoation.distanceTo(location) > TRACK_DISTANCE_MORE_THAN) {
                if (mCurrentDesignPath == null)
                    mCurrentDesignPath = new CurrentDesignPath();
                mCurrentDesignPath.addPoint(new LatLng(location.getLatitude(), location.getLongitude()));

                mAddedLcoation = location;
            }
        }

        mCurrentLocation = location;
        if (mGamePhase == GamePhase.Recording || !cameraInitialized)
            moveCamera(mCurrentLocation);
        if (mCurrentPosMarker == null)
            mCurrentPosMarker = mMap.addMarker(new MarkerOptions().
                    position(new LatLng(location.getLatitude(), location.getLongitude())).
                    title("Current Position").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pegman)));
        else
            mCurrentPosMarker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
    }

    public void requestLocation() {
        askRecordingPermission();

        if (mUserInfo == null)
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
        switch (requestCode) {
            case PERMISSION_REQUEST_LOCATION:
                if(grantResults.length == 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // We can now safely use the API we requested access to
                    requestLocation();
                } else {
                    // Permission was denied or request was cancelled
                }
            break;
            case PERMISSIONS_REQUEST_RECORD_AUDIO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Recognizer initialization is a time-consuming and it involves IO,
                    // so we execute it in async task
                    new SetupRecognizerTask(this).execute();
                }
                break;
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

    private void moveCamera(Location location) {
        moveCamera(new LatLng(location.getLatitude(), location.getLongitude()));
    }

    private void moveCamera(LatLng location) {
        cameraInitialized = true;

        CameraPosition position = CameraPosition.builder()
                .target(location)
                .zoom(16f)
                .bearing(0.0f)
                .tilt(0.0f)
                .build();

        mMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(position), null);

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled( true );
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
        mCurrentDesignPath = new CurrentDesignPath();
        mCurrentDesignPath.start();
        mRecordImageView.setImageResource(R.drawable.ic_stop_record);
        String msg = "Move arround to create a path \n\n";
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        mCurrentDesignPath.setPolyline(empryPolyline());
        mCurrentVoiceMode = VOICE_MODE_STOP;
        listenToVoiceOmmands(VOICE_MODE_STOP);
        Toast.makeText(this, "Press red button or say '" + VOICE_COMMAND_STOP + "' to finish recording path", Toast.LENGTH_LONG).show();
    }

    private void onStopRecordingPath() {
        mRecordImageView.setImageResource(R.drawable.ic_record);
        onPathFinished();
        mCurrentVoiceMode = VOICE_MODE_STOP;
        listenToVoiceOmmands(VOICE_MODE_START);

    }

    private void onGamePhaseChanged() {
        if (mGamePhase == GamePhase.Recording)
            onStartRecordingPath();
        else
            onStopRecordingPath();
    }

    private void onRecordIconClicked() {
        if (mGamePhase == GamePhase.NotRecording) {
            mGamePhase = GamePhase.Recording;
        } else if (mGamePhase == GamePhase.Recording) {
            mGamePhase = GamePhase.NotRecording;
        }
        onGamePhaseChanged();
    }

    private void onPathListIconClicked() {
        Intent intent = new Intent(this, PathListActivity.class);
        Bundle bundle = new Bundle();
        ArrayList<PathFB> pathsFB = new ArrayList<>();
        for (Path path : mPaths)
            pathsFB.add(path.toPathFB());
        bundle.putSerializable(PathListActivity.PATHS_ARGUMENT, pathsFB);
        intent.putExtras(bundle);
        startActivityForResult(intent, RC_PATH);
    }

    void onGotoMyLocation() {
        if (mCurrentLocation != null)
            moveCamera(mCurrentLocation);
    }

    void onLeaderBoardClicked() {
        Intent intent = new Intent(this, LeaderBoardActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (v == mRecordImageView) {
            onRecordIconClicked();
        }
        else if (v == mSighoutImageView) {
            signOut();
        }
        else if (v == mPathListImageView) {
            onPathListIconClicked();
        }
        else if (v == mGotoMyLocationImageView) {
            onGotoMyLocation();
        }
        else if (v == mLeaderBoardImageView) {
            onLeaderBoardClicked();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_PATH:
                if (resultCode == RESULT_OK) {
                    int position = data.getIntExtra(PathListActivity.PATH_POSITION, -1);
                    if (position < 0 || position >= mPaths.size())
                        return;
                    Path path = mPaths.get(position);
                    if (path != null)
                        if (path.getCoordinates() != null)
                            if (path.getCoordinates().size() > 0) {
                                LatLng location = path.getCoordinates().getPoints().get(0);
                                returnedFromPathList = true;
                                moveCamera(location);
                            }
                }
        }
    }

    void signOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(LoginActivity.SIGNIN_DIFFERENT_USER, true);
        startActivity(intent);
        finish();
    }

    void onPathFinished() {
        mAddedLcoation = null;
        if (mCurrentDesignPath == null || !mCurrentDesignPath.hasCoordinates()) return;

        mCurrentDesignPath.end();

        AddPathDialog dialog = AddPathDialog.newInstance(mUserInfo, mCurrentDesignPath);
        dialog.show(getFragmentManager(), "AddPathDialog");
    }

    boolean pathExists(String key) {
        for (Path path : mPaths)
            if (path.getKey().equals(key))
                return true;
        return  false;
    }

    @Override
    public void onPathAdded(Path path,boolean newPath) {
        boolean pathExists = pathExists(path.getKey());

        if (newPath)
            DB.addPath(path);
        else if (!pathExists)
            drawPath(path);

        if (!pathExists)
            mPaths.add(path);

    }

    @Override
    public void onPathCanceled() {
        if (mCurrentDesignPath != null)
            mCurrentDesignPath.cancel();
    }

    public Polyline drawPath(final Path path) {
        PolylineOptions polylineOptions = new PolylineOptions();
        boolean createdByMe = path.getCreatedByUser().equals(mUserInfo.getUserName());
        float review = path.getAvgRating();
        int ratings = path.getRatingsCount();

        int pathColor;

        if (ratings == 0)
            pathColor = Color.BLACK;
        else {
            if (review >= 4)
                pathColor = Color.rgb(0, 0, 179);
            else if (review >= 2.5)
                pathColor = Color.rgb(255, 153, 51);
            else
                pathColor = Color.RED;
        }

        polylineOptions.color(pathColor);
        LatLng[] coords = path.getCoordinates().getPoints().toArray(new LatLng[path.getCoordinates().size()]);
        polylineOptions.add(coords);
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

    void onVoiceCommand(String command) {
        if (command.equals(VOICE_COMMAND_START) && mGamePhase == GamePhase.NotRecording) {
            mGamePhase = GamePhase.Recording;
            onGamePhaseChanged();
        }
        else if (command.equals(VOICE_COMMAND_STOP) && mGamePhase == GamePhase.Recording) {
            mGamePhase = GamePhase.NotRecording;
            onGamePhaseChanged();
        }
    }

    private static class SetupRecognizerTask extends AsyncTask<Void, Void, Exception> {
        WeakReference<MainActivity> activityReference;
        SetupRecognizerTask(MainActivity activity) {
            this.activityReference = new WeakReference<>(activity);
        }
        @Override
        protected Exception doInBackground(Void... params) {
            try {
                Assets assets = new Assets(activityReference.get());
                File assetDir = assets.syncAssets();
                activityReference.get().setupRecognizer(assetDir);
            } catch (IOException e) {
                return e;
            }
            return null;
        }
        @Override
        protected void onPostExecute(Exception result) {
            if (result == null)
                activityReference.get().listenToVoiceOmmands(VOICE_MODE_START);
        }
    }

    /**
     * In partial result we get quick updates about current hypothesis. In
     * keyword spotting mode we can react here, in other modes we need to wait
     * for final result in onResult.
     */
    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        if (hypothesis == null)
            return;

        String text = hypothesis.getHypstr();
        onVoiceCommand(text);
    }

    /**
     * This callback is called when we stop the recognizer.
     */
    @Override
    public void onResult(Hypothesis hypothesis) {
        if (hypothesis != null) {
            String text = hypothesis.getHypstr();
             onVoiceCommand(text);
        }
    }

    @Override
    public void onBeginningOfSpeech() {
    }

    /**
     * We stop recognizer here to get a final result
     */
    @Override
    public void onEndOfSpeech() {
//        if (!recognizer.getSearchName().equals(KWS_SEARCH))
//            listenToVoiceOmmands();
    }

    private void listenToVoiceOmmands(String voiceMode) {
        recognizer.stop();

        recognizer.startListening(voiceMode);

    }

    private void setupRecognizer(File assetsDir) throws IOException {
        // The recognizer can be configured to perform multiple searches
        // of different kind and switch between them

        recognizer = SpeechRecognizerSetup.defaultSetup()
                .setAcousticModel(new File(assetsDir, "en-us-ptm"))
                .setDictionary(new File(assetsDir, "cmudict-en-us.dict"))

                .setRawLogDir(assetsDir) // To disable logging of raw audio comment out this call (takes a lot of space on the device)

                .getRecognizer();
        recognizer.addListener(this);

        /* In your application you might not need to add all those searches.
          They are added here for demonstration. You can leave just one.
         */

        // Create keyword-activation search.
        recognizer.addKeyphraseSearch(VOICE_MODE_START, VOICE_COMMAND_START);
        recognizer.addKeyphraseSearch(VOICE_MODE_STOP, VOICE_COMMAND_STOP);

        File KeyPhrasesGrammar = new File(assetsDir, "keyphrases.gram");
        recognizer.addKeywordSearch(VOICE_MODE_KEYWORDS, KeyPhrasesGrammar);

        // Create grammar-based search for selection between demos
        File menuGrammar = new File(assetsDir, "menu.gram");
        recognizer.addGrammarSearch(VOICE_MODE_MENU, menuGrammar);

        // Create grammar-based search for digit recognition
//        File digitsGrammar = new File(assetsDir, "digits.gram");
//        recognizer.addGrammarSearch(DIGITS_SEARCH, digitsGrammar);
//
//        // Create language model search
//        File languageModel = new File(assetsDir, "weather.dmp");
//        recognizer.addNgramSearch(FORECAST_SEARCH, languageModel);
//
//        // Phonetic search
//        File phoneticModel = new File(assetsDir, "en-phone.dmp");
//        recognizer.addAllphoneSearch(PHONE_SEARCH, phoneticModel);
    }

    @Override
    public void onError(Exception error) {
    }

    @Override
    public void onTimeout() {
        listenToVoiceOmmands(mCurrentVoiceMode);
    }

}
