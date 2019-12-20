package com.example.navigation;

import android.animation.AnimatorSet;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.widget.Toast;

// classes needed to initialize map
import com.mapbox.api.directions.v5.models.LegStep;
import com.mapbox.api.directions.v5.models.VoiceInstructions;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

// classes needed to add the location component
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;

import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import java.lang.ref.WeakReference;
import android.os.Handler;



// classes needed to add a marker
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

// classes to calculate a route
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.NavigationView;
import com.mapbox.services.android.navigation.ui.v5.NavigationViewOptions;
import com.mapbox.services.android.navigation.ui.v5.listeners.NavigationListener;
import com.mapbox.services.android.navigation.ui.v5.listeners.SpeechAnnouncementListener;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.ui.v5.voice.SpeechAnnouncement;
import com.mapbox.services.android.navigation.v5.instruction.Instruction;
import com.mapbox.services.android.navigation.v5.milestone.Milestone;
import com.mapbox.services.android.navigation.v5.milestone.MilestoneEventListener;
import com.mapbox.services.android.navigation.v5.navigation.MapboxNavigation;
import com.mapbox.services.android.navigation.v5.navigation.MapboxNavigationOptions;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
//import com.mapbox.services.api.directions.v5.models.StepManeuver;

// classes needed to launch navigation UI
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.v5.routeprogress.RouteProgress;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, MapboxMap.OnMapClickListener, PermissionsListener, SpeechAnnouncementListener, NavigationListener , MilestoneEventListener ,LocationEngineCallback<LocationEngineResult>{
    // variables for adding location layer
    private MapView mapView;
    private MapboxMap mapboxMap;
    static int i = 0;
    String MY_INSTRUCTION ;
    // variables for adding location layer
    private PermissionsManager permissionsManager;
    private LocationComponent locationComponent;
    // variables for calculating and drawing a route
    private DirectionsRoute currentRoute;
    private DirectionsRoute currentRoute2;
    private DirectionsResponse directionsResponse;
    private static final String TAG = "DirectionsActivity";
    private NavigationMapRoute navigationMapRoute;
    // variables needed to initialize navigation
    private Button button;
    private List<Milestone> milestones;
    // Variables needed to add the location engine
    private LocationEngine locationEngine;
    private long DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L;
    private long DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5;
    //private MainActivityLocationCallback callback = new MainActivityLocationCallback(this);
    private Handler handler = new Handler();
    private int value2= 0 ;
    List<LegStep> steps;
    ArrayList sarr = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_main);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }





    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(getString(R.string.navigation_guidance_day), new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                enableLocationComponent(style);

                addDestinationIconSymbolLayer(style);

                mapboxMap.addOnMapClickListener(MainActivity.this);
                button = findViewById(R.id.startButton);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //boolean simulateRoute = true;
                       NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                                .directionsRoute(currentRoute)
                                .shouldSimulateRoute(true)
                                .build();

                   // Log.d("location"  , String.valueOf((locationComponent.getLastKnownLocation().getLongitude())));

                    //PrimeThread p =new PrimeThread();
                    //p.start();








//                        NavigationViewOptions.Builder builder = NavigationViewOptions.builder();
//                        builder.navigationListener(MainActivity.this);
//                        builder.directionsRoute(currentRoute);
//                        builder.shouldSimulateRoute(true);
//
//                        NavigationViewOptions options2 = builder.build();


                        //Log.d("option.tostring()", options.toString())
                        // Call this method with Context from within an Activity
                       NavigationLauncher.startNavigation(MainActivity.this, options);
                        Thread t1 = new Thread(new Runnable() {

                            @Override
                            public void run() {
                                String demo ="";
                                int num =sarr.size();
                                int counter = 0;
                                Log.d("numm", (String) sarr.get(num-1));
                                int value=0;

                                while(value !=num){
                                    Process logcat;
                                    final StringBuilder log = new StringBuilder();
                                    try {
                                        logcat = Runtime.getRuntime().exec("/system/bin/logcat -d | tail -n 1");//Runtime.getRuntime().exec(new String[]{"logcat", "-d"});
                                        BufferedReader br = new BufferedReader(new InputStreamReader(logcat.getInputStream()),4*1024);
                                        String line;



                                        while ((line = br.readLine()) != null) {



                                            if(line.contains(" mediaplayer went away with unhandled events")) {
                                                if(!demo.equals(line)) {

                                                    //Log.d("value2", String.valueOf(value2));
                                                    //counter ++;
                                                    // Log.d("counter", String.valueOf(counter));
                                                    //                                          if(counter %2 != 0) {

                                                    String val = (String) sarr.get(value);

                                                    Log.d("final", (String) sarr.get(value));
                                                    value++;
                                                    demo = line;
                                                    //Thread.sleep(2000);


                                               if( !val.matches(".*\\d.*")){
                                                    if (val.contains("left")) {
                                                        //Log.d("Left", "1");
                                                        value2 =+1 ;

                                                  } else if (val.contains("right")) {
                                                       // Log.d("right", "2");
                                                    }
                                              }//end of regex


                                                    //break;
                                                    //}

                                                    //Thread.sleep(8000);


                                                }
                                                else{
                                                    Log.v("else ","part");
                                                }
                                            }
                                               /* else {
                                                    try {

                                                        Runtime.getRuntime().exec(new String[]{"logcat", "-c"});
                                                    } catch (Exception e1) {
                                                        e1.printStackTrace();
                                                    }

                                                }*/

                                            try {

                                                Runtime.getRuntime().exec("logcat -b all -c");
                                            } catch (Exception e1) {
                                                e1.printStackTrace();
                                            }

                                        }
                        } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    //LogsUtil.readLogs();

                                }}});
                        t1.start();

                        Log.d("value2 " , String.valueOf(value2));

                                //Log.d("TESTTTTT", "test");





                    /*    int num =sarr.size();
                        Log.d("numm", (String) sarr.get(num-1));
                        int value=0;
                    while(value !=num){
                        Process logcat;
                        final StringBuilder log = new StringBuilder();
                        try {
                            logcat = Runtime.getRuntime().exec(new String[]{"logcat", "-d"});
                            BufferedReader br = new BufferedReader(new InputStreamReader(logcat.getInputStream()),4*1024);
                            String line;

                            String separator = System.getProperty("line.separator");
                            while ((line = br.readLine()) != null) {

                                Log.d("logcat", "hello");
                                if(line.contains("mediaplayer")){

                                    Log.d("logcat", "hmmm");
                                    String val = (String) sarr.get(value);
                                    Log.d("final",val);
                                    value ++;

                                }
                                else{
                                    try {
                                        Log.d("logcat2","In Here");
                                        Runtime.getRuntime().exec(new String[]{"logcat", "-c"});
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                    }

                                }

                    */
                       /* Process logcat;
                        final StringBuilder log = new StringBuilder();
                        try {
                            logcat = Runtime.getRuntime().exec(new String[]{"logcat", "-d"});
                            BufferedReader br = new BufferedReader(new InputStreamReader(logcat.getInputStream()),4*1024);
                            String line;
                            String separator = System.getProperty("line.separator");
                            while ((line = br.readLine()) != null) {
                                Log.d("logcat2",line);

                                if(line.contains("MediaPlayer")){

                                //Log.d("logcat2", "heyyy");
                                }
                                else{
                                    try {
                                        Runtime.getRuntime().exec(new String[]{"logcat", "-c"});
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                    }

                                }



                                log.append(line);
                                log.append(separator);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

*/

                    /* if(((sarr.get(value).toString()).contains("destination"))){

                                    try {
                                        Runtime.getRuntime().exec(new String[]{"logcat", "-c"});
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                    }
                                }*/
/*

                               log.append(line);
                                log.append(separator);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

*/


                                //callme2();


                                /* for(long i = 0;i< 8000 ; i++){


                                 *//*if(i > 100000){
                               i = 0;
                           }*//*

                           Log.d("Longitude", String.valueOf(locationComponent.getLastKnownLocation().getLongitude()));
                           Log.d("Latitude", String.valueOf(locationComponent.getLastKnownLocation().getLatitude()));
                        }*/
                                //new NavigationView(MainActivity.this.getBaseContext()).startNavigation(options2);
                            }
                        });




            }
        });
    }

    private void addDestinationIconSymbolLayer(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addImage("destination-icon-id",
                BitmapFactory.decodeResource(this.getResources(), R.drawable.mapbox_marker_icon_default));
        GeoJsonSource geoJsonSource = new GeoJsonSource("destination-source-id");
        loadedMapStyle.addSource(geoJsonSource);
        SymbolLayer destinationSymbolLayer = new SymbolLayer("destination-symbol-layer-id", "destination-source-id");
        destinationSymbolLayer.withProperties(
                iconImage("destination-icon-id"),
                iconAllowOverlap(true),
                iconIgnorePlacement(true)
        );
        loadedMapStyle.addLayer(destinationSymbolLayer);
    }

    @SuppressWarnings({"MissingPermission"})
    @Override
    public boolean onMapClick(@NonNull LatLng point) {

        Point destinationPoint = Point.fromLngLat(point.getLongitude(), point.getLatitude());
        Point originPoint = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(),
                locationComponent.getLastKnownLocation().getLatitude());

        GeoJsonSource source = mapboxMap.getStyle().getSourceAs("destination-source-id");
        if (source != null) {
            source.setGeoJson(Feature.fromGeometry(destinationPoint));
        }

        getRoute(originPoint, destinationPoint);

        button.setEnabled(true);
        button.setBackgroundResource(R.color.mapboxBlue);
        return true;
    }

    public void getDirection(DirectionsRoute dr) {

        String[] instruction;
        Log.d("man", dr.voiceLanguage());
    }

    private void getRoute(Point origin, Point destination) {
        NavigationRoute.builder(this)
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        // You can get the generic HTTP info about the response
                        Log.d(TAG, "Response code: " + response.code());
                        if (response.body() == null) {
                            Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                            return;
                        } else if (response.body().routes().size() < 1) {
                            Log.e(TAG, "No routes found");
                            return;
                        }

                        currentRoute = response.body().routes().get(0);

                        Log.d("", currentRoute.toString());
                       // Log.d("manuever", currentRoute.legs().get(0).steps().get(0).maneuver().instruction());
                        //Log.d("manueverList", currentRoute.legs().get(0).steps().toString());




                        steps = response.body().routes().get(0).legs().get(0).steps();

                        Log.d("voiceInstruction", String.valueOf(steps.get(1).voiceInstructions().get(1).announcement()));

                        for(LegStep l:steps){

                            for( VoiceInstructions i : l.voiceInstructions()) {
                                if (i.announcement() != null) {
                                    Log.d("announsemnent", i.announcement().toString());
                                    sarr.add(i.announcement().toString());
                                }
                            }

                        }





                        for (LegStep l : steps) {
                            if (l.maneuver() != null) {
                                      //  Log.d("==>", l.maneuver().instruction().toString());

                               //sarr.add(l.maneuver().instruction().toString());

                            }
                        }


                        // Draw the route on the map
                        if (navigationMapRoute != null) {
                            navigationMapRoute.removeRoute();
                        } else {
                            navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap, R.style.NavigationMapRoute);
                        }
                        navigationMapRoute.addRoute(currentRoute);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                        Log.e(TAG, "Error: " + throwable.getMessage());
                    }
                });
    }


    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            // Activate the MapboxMap LocationComponent to show user location
            // Adding in LocationComponentOptions is also an optional parameter
            locationComponent = mapboxMap.getLocationComponent();

            locationComponent.activateLocationComponent(this, loadedMapStyle);
            locationComponent.setLocationComponentEnabled(true);
            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocationComponent(mapboxMap.getStyle());
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    @Override
    public SpeechAnnouncement willVoice(SpeechAnnouncement announcement) {


        //Log.d("inside " , "hello Will voice");
        return  null;
    }

    @Override
    public void onCancelNavigation() {

    }

    @Override
    public void onNavigationFinished() {

    }

    @Override
    public void onNavigationRunning() {

    }

    @Override
    public void onMilestoneEvent(RouteProgress routeProgress, String instruction, Milestone milestone) {

        //Log.d("hello","from milestone");



    }


    Instruction myInstruction = new Instruction() {
        @Override
        public String buildInstruction(RouteProgress routeProgress) {
            return routeProgress.currentLegProgress().upComingStep().maneuver().instruction();
        }
    };


    public void callme2(){

        //Log.d("Longitude", String.valueOf(locationComponent.getLastKnownLocation().getLongitude()));
       // Log.d("Latitude", String.valueOf(locationComponent.getLastKnownLocation().getLatitude()));

        callme2();

    }

    @Override
    public void onSuccess(LocationEngineResult result) {


        //Log.d("locatiionChange","i am working");

    }

    @Override
    public void onFailure(@NonNull Exception exception) {



    }




}
