package com.example.mini_app.maps_utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mini_app.MapsActivity;
import com.example.mini_app.R;
import com.example.mini_app.image_utilities.BackgroundRemover;
import com.example.mini_app.objects.ParkingSpot;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class PlacesTask extends AsyncTask<String, Void, String> {

    private MapsActivity mapsActivity;


    public PlacesTask(MapsActivity mapsActivity){
        this.mapsActivity = mapsActivity;
    }

    @Override
    protected String doInBackground(String... strings) {
        String response = "";
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }

                in.close();
                response = content.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        return response;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        parsePlacesData(s);
    }

    private void parsePlacesData(String jsonData) {
        try {
            // Initialize the markers
            Bitmap markerBitmap = BitmapFactory.decodeResource(mapsActivity.getResources(), R.drawable.parking_spot_marker);
            Bitmap parkingMarker = BackgroundRemover.removeBlackBackground(markerBitmap);
            Bitmap scaledParkingMarker = Bitmap.createScaledBitmap(parkingMarker, 90, 118, false);

            Bitmap favouriteMarkerBitmap = BitmapFactory.decodeResource(mapsActivity.getResources(), R.drawable.favourite_parking_spot_marker);
            Bitmap favouriteParkingMarker = BackgroundRemover.removeBlackBackground(favouriteMarkerBitmap);
            Bitmap favouriteScaledParkingMarker = Bitmap.createScaledBitmap(favouriteParkingMarker, 90, 118, false);

            mapsActivity.setInitialised(true);


            // Parse the places data from JSON
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray results = jsonObject.getJSONArray("results");

            FirebaseDatabase database = FirebaseDatabase.getInstance("https://parkingspotmanager-default-rtdb.europe-west1.firebasedatabase.app/");
            String userPath = "users/" + mapsActivity.getUser().getEmail().replace(".", "") + "/favouriteParkingSpot";
            DatabaseReference myRef = database.getReference(userPath);

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Collect all favorite parking spots into a map
                    Map<String, String> favoriteSpots = new HashMap<>();
                    for (DataSnapshot d : snapshot.getChildren()) {
                        favoriteSpots.put(d.getKey(), (String) d.getValue());
                    }

                    mapsActivity.getFavouriteParkingSpots().clear();
                    for (int i = 0; i < results.length(); i++) {
                        try {
                            JSONObject place = results.getJSONObject(i);
                            String name = place.getString("name");
                            JSONObject geometry = place.getJSONObject("geometry");
                            JSONObject location = geometry.getJSONObject("location");
                            double lat = location.getDouble("lat");
                            double lng = location.getDouble("lng");
                            LatLng placeLatLng = new LatLng(lat, lng);

                        if (!name.contains("bike") && !name.contains("biciclete")) {
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(placeLatLng);
                            markerOptions.title(name);

                            if (favoriteSpots.containsKey(name)) {
                                String[] latLong = favoriteSpots.get(name).split("/");
                                if (lat == Double.parseDouble(latLong[0]) && lng == Double.parseDouble(latLong[1])) {
                                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(favouriteScaledParkingMarker));

                                    ParkingSpot aux = new ParkingSpot();
                                    aux.setKey(name);
                                    aux.setName(name);
                                    aux.setLat((float) lat);
                                    aux.setLng((float) lng);
                                    aux.setOccupied(false);
                                    aux.setNrSpaces(99);
                                    mapsActivity.getFavouriteParkingSpots().add(aux);
                                } else {
                                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(scaledParkingMarker));
                                    mapsActivity.getFavouriteParkingSpots().removeIf(p -> p.getLat() == lat && p.getLng() == lng);

                                }
                            } else {
                                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(scaledParkingMarker));
                                mapsActivity.getFavouriteParkingSpots().removeIf(p -> p.getLat() == lat && p.getLng() == lng);

                            }

                            mapsActivity.getmMap().addMarker(markerOptions);

                            ParkingSpot aux = new ParkingSpot();
                            aux.setKey(name);
                            aux.setName(name);
                            aux.setLat((float) lat);
                            aux.setLng((float) lng);
                            aux.setOccupied(false);
                            aux.setNrSpaces(99);

                            mapsActivity.getParkingSpots().add(aux);
                        }
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
