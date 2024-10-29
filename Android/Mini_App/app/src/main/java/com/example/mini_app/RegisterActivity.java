package com.example.mini_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private final static String TAG = "registerLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        mAuth = SplashActivity.getmAuth();

        Button registerButton = findViewById(R.id.buttonRegister);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText usernameEditText = findViewById(R.id.editTextUsername);
                String username = usernameEditText.getText().toString();
                if(username.matches("")){
                    Toast.makeText(RegisterActivity.this, "Username is mandatory",
                            Toast.LENGTH_SHORT).show();

                    return;
                }
                EditText emailEditText = findViewById(R.id.editTextEmailAddress);
                String email = emailEditText.getText().toString();
                if(email.matches("")){
                    Toast.makeText(RegisterActivity.this, "Email is mandatory",
                            Toast.LENGTH_SHORT).show();

                    return;
                }
                EditText passwordEditText = findViewById(R.id.editTextPassword);
                String password = passwordEditText.getText().toString();
                if(email.matches("")){
                    Toast.makeText(RegisterActivity.this, "Password is mandatory",
                            Toast.LENGTH_SHORT).show();

                    return;
                }
                EditText passwordConfirmEditText = findViewById(R.id.editTextConfirmPassword);
                String passwordConfirm = passwordConfirmEditText.getText().toString();

                if(password.equals(passwordConfirm)) {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(username)
                                                .build();
                                        if(user != null) {
                                            user.updateProfile(profileUpdates);
                                            SplashActivity.setmAuth(mAuth);
                                            Intent intent = new Intent();
                                            intent.setClass(RegisterActivity.this, SplashActivity.class);
                                            startActivity(intent);
                                        }
                                        else
                                            Log.d(TAG,"Username is null");



                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        if(task.getException().getMessage().contains("email address is badly formatted")) {
                                            Toast.makeText(RegisterActivity.this, "Authentication failed: Invalid email format.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                        else if(task.getException().getMessage().contains("The given password is invalid")){
                                            Toast.makeText(RegisterActivity.this, "Authentication failed: Invalid password format. The password should have at least 6 characters",
                                                    Toast.LENGTH_SHORT).show();
                                        } else if(task.getException().getMessage().contains("email address is already in use")){
                                            Toast.makeText(RegisterActivity.this, "Authentication failed: Email already in use",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "Password and Password confirmation mismatch.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
}