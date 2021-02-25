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

import com.example.easydelivery.R;
import com.example.easydelivery.helpers.InternalFile;
import com.example.easydelivery.helpers.PermissionsUtils;

import com.example.easydelivery.model.GenealLoginModel;
import com.example.easydelivery.views.activities.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;


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
        String table = jsonObject.getString("UserType");
        databaseReference.child(table).addListenerForSingleValueEvent(new ValueEventListener() {
            Boolean band = false;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                    GenealLoginModel model = objSnaptshot.getValue(GenealLoginModel.class);
                    try {
                        if(jsonObject.getString("Token").equals(model.getToken())) {
                            jsonObject.put("flag","true");
                            internalFile.writeUserFile(jsonObject);
                            loginvar(model.getId(), model.getName(),model.getEmail(),model.getType());
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


    }

    private void goToActivity(Boolean value) throws JSONException {
        try {
            InternalFile internalFile = new InternalFile();
            JSONObject jsonObject = internalFile.readUserFile();
            if (value) {
                startActivity(new Intent( SplashScreenActivity.this, MainActivity.class));
                Log.d("Token BD","Fue a Main" );
            } else if (!value) {
                startActivity(new Intent( SplashScreenActivity.this, WelcomeScreenActivity.class));
            }
            finish();
        }
        catch (Exception e)
        {
            startActivity(new Intent( SplashScreenActivity.this, WelcomeScreenActivity.class));
            finish();
        }
    }
    public void loginvar(String id, String name, String email, String usertype)
    {
        SharedPreferences prefs = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("id",id);
        editor.putString("name",name);
        editor.putString("email", email);
        editor.putString("usertype", usertype);
        editor.commit();
    }
}