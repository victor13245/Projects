package com.example.mini_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.mini_app.image_utilities.BackgroundRemover;
import com.example.mini_app.maps_utilities.PlacesTask;
import com.example.mini_app.maps_utilities.UserEventsHandler;
import com.example.mini_app.maps_utilities.PermissionUtils;
import com.example.mini_app.objects.ParkingSpot;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.AdvancedMarker;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.example.mini_app.databinding.ActivityMapsBinding;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.TravelMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.widget.Toast;


import java.util.List;
import java.util.Objects;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {


    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean permissionDenied = false;
    private static final String API_KEY = "AIzaSyDpFjUVqh6MNDP1zKrWAaLYblUtercge6o";
    private GeoApiContext geoApiContext;

    private boolean initialised = false;
    private boolean onlyFavourites = false;
    private boolean closerToDestination = false;
    private boolean placeDestinationMarker = false;

    ArrayList<ParkingSpot> parkingSpots = new ArrayList<>();
    ArrayList<ParkingSpot> favouriteParkingSpots = new ArrayList<>();

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private UserEventsHandler userEventsHandler = new UserEventsHandler(this);
    private FirebaseUser user;

    private FusedLocationProviderClient fusedLocationClient;
    private PlacesTask placesTask;
    private LocationCallback locationCallback;

    private Polyline previousPolyline = null;
    private Marker destinationMarker = null;
    private LatLng parkingDestination = null;
    private static boolean closeToParkingSpot = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Places.initialize(getApplicationContext(), API_KEY);
        //placesClient = Places.createClient(this);

        user = SplashActivity.getmAuth().getCurrentUser();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        geoApiContext = new GeoApiContext.Builder()
                .apiKey(API_KEY)
                .build();

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button buttonGetRoute = findViewById(R.id.button_get_route);
        buttonGetRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userEventsHandler.ShowRoutesDialog(view);
            }});

        Button buttonSetDestination = findViewById(R.id.button_set_destination);
        buttonSetDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeDestinationMarker=true;
            }});

        setupLocationCallback();

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
        mMap.setOnPoiClickListener(userEventsHandler);
        mMap.setOnMyLocationClickListener(userEventsHandler);
        mMap.setOnMarkerClickListener(userEventsHandler);
        mMap.setOnMapClickListener(userEventsHandler);
        enableMyLocation();


        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);


        parkingDestination = null;
        closeToParkingSpot = false;
        Bitmap markerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.parking_spot_marker);
        Bitmap parkingMarker = BackgroundRemover.removeBlackBackground(markerBitmap);
        Bitmap scaledParkingMarker = Bitmap.createScaledBitmap(parkingMarker, 90, 118, false);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://parkingspotmanager-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("parkingSpot");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dsp : snapshot.getChildren()) {
                    ParkingSpot aux = dsp.getValue(ParkingSpot.class);
                    parkingSpots.add(aux);
                }

                for (ParkingSpot p : parkingSpots) {
                    LatLng loc = new LatLng(p.getLat(), p.getLng());
                    mMap.addMarker(new MarkerOptions().position(loc).title(p.getName()).icon(BitmapDescriptorFactory.fromBitmap(scaledParkingMarker)));
                    //markerPositions.add(loc);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MapsActivity.this, "Could not retrieve parking spots from firebase", Toast.LENGTH_LONG).show();
            }
        });

        LatLng loc = new LatLng(45.43235772260022, 28.055607912220868);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        mMap.setTrafficEnabled(true);

        requestLocationUpdates();
    }

    @SuppressLint("MissingPermission")
    private void enableMyLocation() {
        // 1. Check if permissions are granted, if so, enable the my location layer
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

            return;
        }

        // 2. Otherwise, request location permissions from the user.
        PermissionUtils.requestLocationPermissions(this, LOCATION_PERMISSION_REQUEST_CODE, true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                android.Manifest.permission.ACCESS_FINE_LOCATION) || PermissionUtils
                .isPermissionGranted(permissions, grantResults,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Permission was denied. Display an error message
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (permissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            permissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    public void getClosestPOI(View view) {
        LatLng currentLocation = getCurrentLocation();
        if (currentLocation == null) {
            Toast.makeText(this, "Current location not available", Toast.LENGTH_SHORT).show();
            return;
        }

        LatLng closestPOI = getClosestPOILocation(currentLocation);
        if (closestPOI == null) {
            Toast.makeText(this, "No POI found", Toast.LENGTH_SHORT).show();
            return;
        }

        getRoute(currentLocation, closestPOI);
    }

    public void getRouteToLocation(LatLng location) {
        LatLng currentLocation = getCurrentLocation();
        if (currentLocation == null) {
            Toast.makeText(this, "Current location not available", Toast.LENGTH_SHORT).show();
            return;
        }
        if (location == null) {
            Toast.makeText(this, "No prior location found", Toast.LENGTH_SHORT).show();
            return;
        }

        getRoute(currentLocation, location);
    }


    private LatLng getCurrentLocation() {
        if (mMap.isMyLocationEnabled() && mMap.getMyLocation() != null) {
            return new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude());
        }
        return null;
    }

    private LatLng getClosestPOILocation(LatLng currentLocation) {

        float closestVal = 999999;
        LatLng closest = new LatLng(0, 0);

        ArrayList<ParkingSpot> parkingSpotArrayList = new ArrayList<>();
        if(onlyFavourites){
            parkingSpotArrayList = favouriteParkingSpots;
        }
        else {
            parkingSpotArrayList = parkingSpots;
        }
        LatLng startSearchLocation = null;
        if(closerToDestination){
            if(destinationMarker != null){
                startSearchLocation = destinationMarker.getPosition();
            }
            else{
                Toast.makeText(this, "No destination marker set", Toast.LENGTH_SHORT).show();
                startSearchLocation = currentLocation;
            }
        }
        else{
            startSearchLocation = currentLocation;
        }

        for (ParkingSpot p : parkingSpotArrayList) {
            LatLng marker = new LatLng(p.getLat(), p.getLng());
            float[] results = new float[5];
            Location.distanceBetween(startSearchLocation.latitude, startSearchLocation.longitude, marker.latitude, marker.longitude, results);
            if (!p.isOccupied()) {
                if (results[0] < closestVal) {
                    closestVal = results[0];
                    closest = marker;
                }
            }
        }

        parkingDestination = closest;
        return closest;
        //return new LatLng(45.43335772260022, 28.056607912220868);
    }

    private void getRoute(LatLng origin, LatLng destination) {

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://parkingspotmanager-default-rtdb.europe-west1.firebasedatabase.app/");
        String userPath = "users/"+ user.getEmail().replace(".","");
        DatabaseReference myRef = database.getReference(userPath);
        String poz = destination.latitude + "/" + destination.longitude;
        myRef.child("lastParkingSpot").setValue(poz);

        DirectionsApi.newRequest(geoApiContext)
                .mode(TravelMode.DRIVING)
                .origin(new com.google.maps.model.LatLng(origin.latitude, origin.longitude))
                .destination(new com.google.maps.model.LatLng(destination.latitude, destination.longitude))
                .setCallback(new PendingResult.Callback<DirectionsResult>() {
                    @Override
                    public void onResult(DirectionsResult result) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (result.routes != null && result.routes.length > 0) {
                                    DirectionsRoute route = result.routes[0];
                                    List<com.google.maps.model.LatLng> decodedPath = route.overviewPolyline.decodePath();
                                    List<LatLng> newDecodedPath = new java.util.ArrayList<>();

                                    for (com.google.maps.model.LatLng latLng : decodedPath) {
                                        newDecodedPath.add(new LatLng(latLng.lat, latLng.lng));
                                    }

                                    if(previousPolyline!= null){
                                        previousPolyline.remove();
                                    }

                                    previousPolyline = mMap.addPolyline(new PolylineOptions().addAll(newDecodedPath).color(Color.BLUE));
                                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                    builder.include(origin);
                                    builder.include(destination);
                                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));
                                } else {
                                    Toast.makeText(MapsActivity.this, "No route found", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MapsActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }

    private void fetchNearbyParkingSpots(LatLng location) {
        String locationString = location.latitude + "," + location.longitude;
        String radius = SettingsActivity.getRadius();
        if(radius == null){
            radius="10000";
        }
        String type = "parking";

        String placesUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                "location=" + locationString +
                "&radius=" + radius +
                "&type=" + type +
                "&key=" + API_KEY;

        placesTask = new PlacesTask(this);
        placesTask.execute(placesUrl);
    }

    private void setupLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                if(!initialised) {
                    for (Location location : locationResult.getLocations()) {
                        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                        fetchNearbyParkingSpots(currentLatLng);
                    }
                    initialised=true;
                }
                else {
                    placesTask.cancel(true);
                    if(parkingDestination!=null && SettingsActivity.isSendToHelper()){
                        for (Location location : locationResult.getLocations()) {
                            LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            float[] results = new float[5];
                            Location.distanceBetween(currentLatLng.latitude, currentLatLng.longitude, parkingDestination.latitude, parkingDestination.longitude, results);
                            if(results[0] < 200){
                                closeToParkingSpot = true;
                                Intent intent = new Intent();
                                intent.setClass(MapsActivity.this,BTActivity.class);
                                startActivity(intent);
                                break;
                            }
                        }
                    }
                }
            }
        };
    }

    private void requestLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest.Builder(
                LocationRequest.PRIORITY_HIGH_ACCURACY, 1000)
                .setMinUpdateIntervalMillis(2000)
                .setWaitForAccurateLocation(false)
                .setMaxUpdateDelayMillis(10000)
                .build();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("PRINTF","Fine location not enabled");
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }


    public ArrayList<ParkingSpot> getParkingSpots() {
        return parkingSpots;
    }

    public ArrayList<ParkingSpot> getFavouriteParkingSpots() {
        return favouriteParkingSpots;
    }

    public GoogleMap getmMap(){
        return this.mMap;
    }

    public FirebaseUser getUser() {
        return user;
    }

    public void setInitialised(boolean bool){this.initialised = bool;}

    public void setOnlyFavourites(boolean onlyFavourites) {
        this.onlyFavourites = onlyFavourites;
    }

    public Marker getDestinationMarker() {
        return destinationMarker;
    }

    public void setDestinationMarker(Marker destinationMarker) {
        this.destinationMarker = destinationMarker;
    }

    public void setCloserToDestination(boolean closerToDestination) {
        this.closerToDestination = closerToDestination;
    }

    public boolean isPlaceDestinationMarker() {
        return placeDestinationMarker;
    }

    public static boolean isCloseToParkingSpot() {
        return closeToParkingSpot;
    }

    public void setPlaceDestinationMarker(boolean placeDestinationMarker) {
        this.placeDestinationMarker = placeDestinationMarker;
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop location updates to save battery when the activity is not active
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Resume location updates when the activity is back in the foreground
        requestLocationUpdates();
    }

}