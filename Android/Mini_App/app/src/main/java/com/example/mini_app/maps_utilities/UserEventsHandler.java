package com.example.mini_app.maps_utilities;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mini_app.MainPageActivity;
import com.example.mini_app.MapsActivity;
import com.example.mini_app.R;
import com.example.mini_app.image_utilities.BackgroundRemover;
import com.example.mini_app.objects.ParkingSpot;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;

public class UserEventsHandler implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,GoogleMap.OnPoiClickListener,GoogleMap.OnMarkerClickListener,GoogleMap.OnMapClickListener {

    private MapsActivity mapsActivity;
    public UserEventsHandler(MapsActivity mapsActivity) {
        this.mapsActivity = mapsActivity;
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        if(mapsActivity.isPlaceDestinationMarker()){
            mapsActivity.setPlaceDestinationMarker(false);
            if(mapsActivity.getDestinationMarker() != null){
                mapsActivity.getDestinationMarker().setPosition(latLng);
            }
            else{
                MarkerOptions options = new MarkerOptions().position(latLng).title("Destination").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                Marker aux = mapsActivity.getmMap().addMarker(options);
                mapsActivity.setDestinationMarker(aux);
            }
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(mapsActivity, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(mapsActivity, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPoiClick(@NonNull PointOfInterest poi) {
        Toast.makeText(mapsActivity, "Name: " +
                        poi.name + "\nPlace ID:" + poi.placeId +
                        "\nLatitude:" + poi.latLng.latitude +
                        " Longitude:" + poi.latLng.longitude,
                Toast.LENGTH_SHORT).show();
        mapsActivity.getmMap().animateCamera(CameraUpdateFactory.newLatLngZoom(poi.latLng, 20),2000,null);
        //mMap.moveCamera(CameraUpdateFactory.scrollBy(100, 100));
    }

    public static void getRouteToNearestPOI(View view) {
        
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {

        ArrayList<ParkingSpot> parkingSpots = mapsActivity.getParkingSpots();
        ParkingSpot myParkingSpot = null;
        for(ParkingSpot p : parkingSpots){
            LatLng latLng = new LatLng(p.getLat(),p.getLng());
            if(marker.getPosition().equals(latLng))
            {
                myParkingSpot = p;
                break;
            }
        }
        showMarkerDialogue(myParkingSpot,marker);
        return false;
    }

    private void showMarkerDialogue(ParkingSpot myParkingSpot, Marker marker) {

        final Dialog dialog = new Dialog(mapsActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.marker_dialogue);

        TextView textViewName = dialog.findViewById(R.id.textViewName);
        TextView textViewStatus = dialog.findViewById(R.id.textViewStatus);
        textViewName.setText(marker.getTitle());
        if(myParkingSpot!=null){
            String status = "not occupied";
            if(myParkingSpot.isOccupied())
                status="occupied";
            String aux = "Status: " + status + "\nNumber of spaces: " + myParkingSpot.getNrSpaces();
            textViewStatus.setText(aux);
        }
        else{
            String aux = "No available data for this parking space";
            textViewStatus.setText(aux);
        }

        Button favouritesButton = dialog.findViewById(R.id.buttonFavourite);
        favouritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance("https://parkingspotmanager-default-rtdb.europe-west1.firebasedatabase.app/");
                String userPath = "users/"+ mapsActivity.getUser().getEmail().replace(".","");
                DatabaseReference myRef = database.getReference(userPath);
                String poz = marker.getPosition().latitude + "/" + marker.getPosition().longitude;
                myRef.child("favouriteParkingSpot").child(marker.getTitle()).setValue(poz);

                Bitmap markerBitmap = BitmapFactory.decodeResource(mapsActivity.getResources(), R.drawable.favourite_parking_spot_marker);
                Bitmap parkingMarker = BackgroundRemover.removeBlackBackground(markerBitmap);
                Bitmap scaledParkingMarker = Bitmap.createScaledBitmap(parkingMarker, 90, 118, false);

                marker.setIcon(BitmapDescriptorFactory.fromBitmap(scaledParkingMarker));

                ParkingSpot aux = new ParkingSpot();
                aux.setKey(marker.getTitle());
                aux.setName(marker.getTitle());
                aux.setLat((float) marker.getPosition().latitude);
                aux.setLng((float) marker.getPosition().longitude);
                aux.setOccupied(false);
                aux.setNrSpaces(99);
                mapsActivity.getFavouriteParkingSpots().add(aux);
            }
        });

        Button removeFavouritesButton = dialog.findViewById(R.id.buttonRemoveFavourite);

        removeFavouritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance("https://parkingspotmanager-default-rtdb.europe-west1.firebasedatabase.app/");
                String userPath = "users/"+ mapsActivity.getUser().getEmail().replace(".","");
                DatabaseReference myRef = database.getReference(userPath);
                myRef.child("favouriteParkingSpot").child(marker.getTitle()).removeValue();

                Bitmap markerBitmap = BitmapFactory.decodeResource(mapsActivity.getResources(), R.drawable.parking_spot_marker);
                Bitmap parkingMarker = BackgroundRemover.removeBlackBackground(markerBitmap);
                Bitmap scaledParkingMarker = Bitmap.createScaledBitmap(parkingMarker, 90, 118, false);

                marker.setIcon(BitmapDescriptorFactory.fromBitmap(scaledParkingMarker));


                mapsActivity.getFavouriteParkingSpots().removeIf(p -> p.getLat() == marker.getPosition().latitude && p.getLng() == marker.getPosition().longitude);

            }
        });

        Button getDirectionsButton = dialog.findViewById(R.id.buttonGetDirection);
        getDirectionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapsActivity.getRouteToLocation(marker.getPosition());
            }
        });

        dialog.show();

    }

    public void ShowRoutesDialog(View view){
        final Dialog dialog = new Dialog(mapsActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.routes_dialogue);

        Button buttonRoutes = dialog.findViewById(R.id.buttonRoutes);
        buttonRoutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox checkBoxFavourites = dialog.findViewById(R.id.checkboxFavourites);
                mapsActivity.setOnlyFavourites(checkBoxFavourites.isChecked());
                CheckBox checkBoxDestination = dialog.findViewById(R.id.checkboxDestination);
                mapsActivity.setCloserToDestination(checkBoxDestination.isChecked());
                mapsActivity.getClosestPOI(view);
            }});

        Button buttonLast = dialog.findViewById(R.id.buttonLastRoute);

        buttonLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase database = FirebaseDatabase.getInstance("https://parkingspotmanager-default-rtdb.europe-west1.firebasedatabase.app/");
                String userPath = "users/"+ mapsActivity.getUser().getEmail().replace(".","") + "/lastParkingSpot";
                DatabaseReference myRef = database.getReference(userPath);

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String locationString = snapshot.getValue(String.class);
                        if(locationString == null)
                        {
                            Log.e("PRINTF","Last location string is null");
                            mapsActivity.getRouteToLocation(null);
                        }
                        else
                        {
                        String[] splitString = locationString.split("/");
                        LatLng location = new LatLng(Double.parseDouble(splitString[0]),Double.parseDouble(splitString[1]));
                        mapsActivity.getRouteToLocation(location);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

            }});

        dialog.show();
    }



}
