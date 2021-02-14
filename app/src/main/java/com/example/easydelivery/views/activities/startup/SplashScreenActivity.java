package com.example.easydelivery.views.activities.startup;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.easydelivery.MainActivity;
import com.example.easydelivery.R;
import com.example.easydelivery.helpers.InternalFile;
import com.example.easydelivery.helpers.PermissionsUtils;
import com.example.easydelivery.menu.Store;
import com.example.easydelivery.model.Buisnes;
import com.example.easydelivery.model.Client;
import com.example.easydelivery.helpers.GobalVarible;

import com.example.easydelivery.model.Delivery;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;


public class SplashScreenActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setupFirebase();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (PermissionsUtils.hasPermissions(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    try {
                        verifyToken();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        try {
                            goToActivity(false);
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
                    }
                } else {
                    try {
                        goToActivity(false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 2000);
    }
    private void setupFirebase(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    public void verifyToken() throws JSONException {
        InternalFile internalFile = new InternalFile();
        JSONObject jsonObject = internalFile.readUserFile();
        if (jsonObject == null) {
            goToActivity(false);
            return;
        }

        databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            Boolean band = false;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                    Client p = objSnaptshot.getValue(Client.class);
                    try {
                        if(jsonObject.getString("Token").equals(p.getToken())) {
                            jsonObject.put("flag","true");
                            internalFile.writeUserFile(jsonObject);
                           loginvar(p.getIduser(), p.getName() + " "+ p.getLastname(),p.getEmail());
                            band=true;
                            break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                if (band) {
                        Toast.makeText(SplashScreenActivity.this, "Bienvenido: " , Toast.LENGTH_LONG).show();
                    try {
                        goToActivity(true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    try {
                        goToActivity(false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        databaseReference.child("Buisnes").addListenerForSingleValueEvent(new ValueEventListener() {
            Boolean band = false;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                    Buisnes b = objSnaptshot.getValue(Buisnes.class);
                    try {
                        // se pregunta por el usuario en la bd esto por el email
                        Log.d("Token json",jsonObject.getString("Token") );
                        Log.d("Token BD",b.getToken() );
                        if (jsonObject.getString("Token").equals(b.getToken())) {
                            jsonObject.put("flag","true");
                            internalFile.writeUserFile(jsonObject);
                           loginvar(b.getId(), b.getBuisnesname() , b.getEmail());
                            band=true;
                            break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (band) {
                    Toast.makeText(SplashScreenActivity.this, "Bienvenido:  "  , Toast.LENGTH_LONG).show();
                    try {
                        goToActivity(true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    try {
                        goToActivity(false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
        databaseReference.child("Delivery").addListenerForSingleValueEvent(new ValueEventListener() {
            Boolean band = false;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                    Delivery d = objSnaptshot.getValue(Delivery.class);
                    try {
                        // se pregunta por el usuario en la bd esto por el email


                        if (jsonObject.getString("Token").equals(d.getToken())) {
                            //se asigna el nuevo token
                            jsonObject.put("flag","true");
                            internalFile.writeUserFile(jsonObject);
                           loginvar(d.getId(), d.getCompanyname() , d.getCorreo());
                            break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (band) {

                        Toast.makeText(SplashScreenActivity.this, "Bienvenido: "  , Toast.LENGTH_LONG).show();

                    try {
                        goToActivity(true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    try {
                        goToActivity(false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

    }

    private void goToActivity(Boolean value) throws JSONException {
        try {
            InternalFile internalFile = new InternalFile();
            JSONObject jsonObject = internalFile.readUserFile();
            if (value) {
                startActivity(new Intent( SplashScreenActivity.this, Store.class));
                Log.d("Token BD","Fue a Main" );
            } else if (!jsonObject.getString("flag").equals("true")) {
                startActivity(new Intent( SplashScreenActivity.this, WelcomeScreenActivity.class));
            }
            finish();
        }
        catch (Exception e)
        {
            startActivity(new Intent( SplashScreenActivity.this, WelcomeScreenActivity.class));
        }

    }
    public void loginvar(String id, String name, String email)
    {
        SharedPreferences prefs = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("id",id);
        editor.putString("name",name);
        editor.putString("email", email);
        editor.commit();
    }
}