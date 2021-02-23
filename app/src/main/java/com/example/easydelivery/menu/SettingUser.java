package com.example.easydelivery.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.easydelivery.R;
import com.example.easydelivery.module.CompanyInfo;
import com.example.easydelivery.views.activities.startup.SplashScreenActivity;
import com.example.easydelivery.helpers.InternalFile;
import com.example.easydelivery.model.Business;
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

import java.io.IOException;

public class SettingUser extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_user);
        BottomNavigationView bottomNavigationView;
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.BottonNavigation);
        bottomNavigationView.setSelectedItemId(R.id.fragmenSettingUser);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        prefs = getSharedPreferences("shared_login_data",   Context.MODE_PRIVATE);
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
        String idcomerce = prefs.getString("id", "");

        databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                        Client p = objSnaptshot.getValue(Client.class);

                            // se pregunta por el usuario en la bd esto por el email
                            if (idcomerce.equals(p.getIduser())) {
                                p.setToken("  ");
                                databaseReference.child("Users").child(p.getIduser()).setValue(p);
                                break;
                            }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            databaseReference.child("Buisnes").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                            Business p = objSnaptshot.getValue(Business.class);

                                // se pregunta por el usuario en la bd esto por el email
                                if (idcomerce.equals(p.getId())) {
                                    p.setToken("  ");
                                    databaseReference.child("Buisnes").child(p.getId()).setValue(p);
                                    break;
                                }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            databaseReference.child("Delivery").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                            Delivery p = objSnaptshot.getValue(Delivery.class);
                                // se pregunta por el usuario en la bd esto por el email
                                if (idcomerce.equals(p.getId())) {
                                    p.setToken("  ");
                                    databaseReference.child("Delivery").child(p.getId()).setValue(p);
                                    break;
                                }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            InternalFile file = new InternalFile();
            file.deleteUserFile();

        Intent intent = new Intent(this, SplashScreenActivity.class);
        startActivity(intent);
    }
    public void Changepassword(View view) throws IOException, JSONException {
        String email = prefs.getString("email", "");

        mAuth.setLanguageCode("es");
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
              if(task.isSuccessful())
              { Toast.makeText(SettingUser.this, "Se a enviado un correo a su usuario: ", Toast.LENGTH_LONG).show(); }
              else
              { Toast.makeText(SettingUser.this, "Error al enviar el correo: ", Toast.LENGTH_LONG).show(); }
            }
        });
    }
    public void infoUser(View view) throws IOException, JSONException {
       startActivity(new Intent(this, CompanyInfo.class));
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.fragmenProductsBusiness:
                    intent = new Intent(SettingUser.this, StoreForBusiness.class);
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
                case R.id.fragmenSettingUser:
                    intent = new Intent(SettingUser.this, SettingUser.class);
                    startActivity(intent);
                    finish();
                    return true;
            }


            return false;
        }
    };
}