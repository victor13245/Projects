package com.example.mini_app;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SettingsActivity extends AppCompatActivity {

    private static boolean sendToHelper;
    private static String radius;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);

        Spinner spinnerRadius=findViewById(R.id.spinner_radius);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.range_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerRadius.setAdapter(adapter);
        spinnerRadius.setSelection(3);

        sendToHelper = false;
        radius = "10000";

        ImageButton radiusImageButton = findViewById(R.id.imageButtonRadius);
        radiusImageButton.setBackground(null);
        Bitmap infoBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.info_icon);
        radiusImageButton.setImageBitmap(infoBitmap);
        radiusImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(SettingsActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.info_dialog);

                TextView infoText = dialog.findViewById(R.id.textViewInfo);
                infoText.setText("Here you can set the range\n in which the app finds parking spots");

                dialog.show();
            }
        });

        ImageButton helperImageButton = findViewById(R.id.imageButtonHelper);
        helperImageButton.setBackground(null);
        helperImageButton.setImageBitmap(infoBitmap);
        helperImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(SettingsActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.info_dialog);

                TextView infoText = dialog.findViewById(R.id.textViewInfo);
                infoText.setText("The parking helper will assist you in finding a parking space inside the parking lot. It can also be connected to your car's parking sensor in order to help you park safely.");

                dialog.show();

            }
        });



        Button buttonConfirm = findViewById(R.id.buttonConfirm);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SettingsActivity.this, "New settings confirmed", Toast.LENGTH_SHORT).show();
                CheckBox helperCheckbox = findViewById(R.id.helperCheckBox);
                sendToHelper = helperCheckbox.isChecked();
                String aux = (String)spinnerRadius.getSelectedItem();
                radius = aux.replace("km","") + "000";
                Log.e("PRINTF","new options: " + radius + ", " + sendToHelper);
            }
        });

        /*Button backButton = findViewById(R.id.buttonBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(SettingsActivity.this,MainPageActivity.class);
                startActivity(intent);
            }
        });*/

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public static boolean isSendToHelper() {
        return sendToHelper;
    }

    public static String getRadius() {
        return radius;
    }
}