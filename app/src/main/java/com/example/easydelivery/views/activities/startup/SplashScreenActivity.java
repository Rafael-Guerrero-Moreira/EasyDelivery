package com.example.easydelivery.views.activities.startup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.easydelivery.MainActivity;
import com.example.easydelivery.R;
import com.example.easydelivery.ado.InternalFile;
import com.example.easydelivery.login;
import com.example.easydelivery.model.Client;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class SplashScreenActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Boolean authenticated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setupFirebase();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (verifyToken()) {
                        startActivity(new Intent( SplashScreenActivity.this, MainActivity.class));
                    } else {
                        startActivity(new Intent( SplashScreenActivity.this, WelcomeScreenActivity.class));
                    }
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                    finish();
                    System.exit(0);
                }
            }
        }, 3000);
    }
    private void setupFirebase(){
        // firebaseDatabase.setPersistenceEnabled(true);
        firebaseDatabase = FirebaseDatabase.getInstance();
        // firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }

    public Boolean verifyToken() throws JSONException {
        InternalFile internalFile = new InternalFile();
        JSONObject jsonObject; authenticated = false;
        try {
            jsonObject = internalFile.readerFile("data","datausers");
        } catch (IOException e) {
            e.printStackTrace();
            return authenticated;
        }

        databaseReference.child(jsonObject.getString("UserType")).addListenerForSingleValueEvent(new ValueEventListener() {
            Boolean band = false;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                    Client p = objSnaptshot.getValue(Client.class);
                    try {
                        if(jsonObject.getString("Token").equals(p.getToken())) {
                            band = true;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                if (band) {
                    try {
                        Toast.makeText(SplashScreenActivity.this, "Bienvenido: " +jsonObject.getString("User") , Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    authenticated = true;
                }
                else {
                    authenticated = false;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return authenticated;
    }
}