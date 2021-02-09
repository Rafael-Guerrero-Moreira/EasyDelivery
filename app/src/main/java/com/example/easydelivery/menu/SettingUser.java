package com.example.easydelivery.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.easydelivery.R;
import com.example.easydelivery.views.activities.startup.SplashScreenActivity;
import com.example.easydelivery.ado.InternalFile;
import com.example.easydelivery.model.Buisnes;
import com.example.easydelivery.model.Client;
import com.example.easydelivery.model.Delivery;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class SettingUser extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_user);
        BottomNavigationView bottomNavigationView;
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.BottonNavigation);
        bottomNavigationView.setSelectedItemId(R.id.fragmenUser);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        InicializarFirebase ();

    }
    private void InicializarFirebase (){
        // firebaseDatabase.setPersistenceEnabled(true);
        firebaseDatabase = FirebaseDatabase.getInstance();
        // firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    public void CloseSession(View view)
    {
        InternalFile internal = new InternalFile();
        JSONObject jsonObject = null;
        try {
            jsonObject = internal.readerFile("data", "datausers");
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("User", jsonObject.getString("User"));
            jsonObject2.put("Token", "Null");
            jsonObject2.put("UserType", jsonObject.getString("UserType"));
            if(!jsonObject.getString("UserType").equals("Buisnes"))
            databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                        Client p = objSnaptshot.getValue(Client.class);
                        try {
                            // se pregunta por el usuario en la bd esto por el email
                            if (jsonObject2.getString("User").equals(p.getEmail())) {
                                p.setToken("  ");
                                databaseReference.child("Users").child(p.getIduser()).setValue(p);
                                break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            else if (jsonObject.getString("UserType").equals("Buisnes"))
                databaseReference.child("Buisnes").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                            Buisnes p = objSnaptshot.getValue(Buisnes.class);
                            try {
                                // se pregunta por el usuario en la bd esto por el email
                                if (jsonObject2.getString("User").equals(p.getEmail())) {
                                    p.setToken("  ");
                                    databaseReference.child("Buisnes").child(p.getId()).setValue(p);
                                    break;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            else
            {
                databaseReference.child("Delivery").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                            Delivery p = objSnaptshot.getValue(Delivery.class);
                            try {
                                // se pregunta por el usuario en la bd esto por el email
                                if (jsonObject2.getString("User").equals(p.getCorreo())) {
                                    p.setToken("  ");
                                    databaseReference.child("Delivery").child(p.getId()).setValue(p);
                                    break;
                                }
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


            internal.writerFile("data", "datausers", jsonObject2);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(this, SplashScreenActivity.class);
        startActivity(intent);
    }
    public void Changepassword(View view) throws IOException, JSONException {
        InternalFile file = new InternalFile();
        JSONObject jsonObject = file.readerFile("data","datausers");
        mAuth.setLanguageCode("es");
        mAuth.sendPasswordResetEmail(jsonObject.getString("User")).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
              if(task.isSuccessful())
              { Toast.makeText(SettingUser.this, "Se a enviado un corro a su usuario: ", Toast.LENGTH_LONG).show(); }
              else
              { Toast.makeText(SettingUser.this, "Error al enviar el correo: ", Toast.LENGTH_LONG).show(); }
            }
        });
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.fragmenStore:
                    intent = new Intent(SettingUser.this, Store.class);
                    startActivity(intent);
                    finish();
                    return true;

                case R.id.fragmenCategory:
                    intent = new Intent(SettingUser.this, Category.class);
                    startActivity(intent);
                    finish();
                    return true;

                case R.id.fragmenSearch:
                    intent = new Intent(SettingUser.this, Search.class);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.fragmenUser:
                    intent = new Intent(SettingUser.this, SettingUser.class);
                    startActivity(intent);
                    finish();
                    return true;
            }


            return false;
        }
    };
}