package com.example.mini_app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.HashMap;

public class MainPageActivity extends AppCompatActivity {

    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_page);

        user = SplashActivity.getmAuth().getCurrentUser();
        TextView titleText = findViewById(R.id.nameTextView);

        if(user!=null){
            String aux = user.getDisplayName() + "!";
            titleText.setText(aux);

            FirebaseDatabase database = FirebaseDatabase.getInstance("https://parkingspotmanager-default-rtdb.europe-west1.firebasedatabase.app/");
            DatabaseReference myRef = database.getReference("users");

            HashMap<String, Object> userHashMap = new HashMap<>();

            //String key = myRef.push().getKey();
            userHashMap.put("name",user.getDisplayName());
            userHashMap.put("email",user.getEmail());

            String userPath="";
            if(user.getEmail()!=null)
                userPath = user.getEmail().replace(".","");
            else
                Log.e("PRINTF","Email is null");

            String finalUserPath = userPath;
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!snapshot.hasChild(finalUserPath)) {
                        myRef.child(finalUserPath).setValue(userHashMap);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        ImageButton logoutImageButton = findViewById(R.id.logoutImageButton);
        logoutImageButton.setBackground(null);
        Bitmap logoutBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.logout_icon);
        logoutImageButton.setImageBitmap(logoutBitmap);
        logoutImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SplashActivity.getmAuth().signOut();
                SplashActivity.getmGoogleSignInClient().signOut().addOnCompleteListener(MainPageActivity.this,
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent intent = new Intent();
                                intent.setClass(MainPageActivity.this,SplashActivity.class);
                                startActivity(intent);

                            }
                        });
            }
        });

        ImageButton settingsImageButton = findViewById(R.id.settingsImageButton);
        settingsImageButton.setBackground(null);
        Bitmap settingsBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.settings_icon);
        settingsImageButton.setImageBitmap(settingsBitmap);
        settingsImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainPageActivity.this,SettingsActivity.class);
                startActivity(intent);
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void goToMap(View view) {
        Intent intent = new Intent();
        intent.setClass(this,MapsActivity.class);
        startActivity(intent);
    }

    public void goToParkingAssistant(View view){
        Intent intent = new Intent();
        intent.setClass(this, BTActivity.class);
        startActivity(intent);
    }


}