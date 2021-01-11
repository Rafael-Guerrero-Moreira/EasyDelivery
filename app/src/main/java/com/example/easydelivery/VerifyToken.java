package com.example.easydelivery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.easydelivery.ado.InternalFile;
import com.example.easydelivery.model.Person;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class VerifyToken extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_token);
        InicializarFirebase ();
        try {
            VerificarToken();
        } catch (IOException e) {
            e.printStackTrace();
            Intent intent = new Intent( VerifyToken.this, login.class);
            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void InicializarFirebase (){
        // firebaseDatabase.setPersistenceEnabled(true);
        firebaseDatabase = FirebaseDatabase.getInstance();
        // firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }
    public void VerificarToken() throws IOException, JSONException {

        InternalFile internalFile = new InternalFile();
        JSONObject jsonObject = internalFile.readerFile("data","datausers");

        databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            Boolean band = false;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                    Person p = objSnaptshot.getValue(Person.class);
                    try {
                        if(jsonObject.getString("Token").equals(p.getToken()))
                        {
                           band = true;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                if(band)
                {
                    try {
                        Toast.makeText(VerifyToken.this, "Bienvenido: " +jsonObject.getString("User") , Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent( VerifyToken.this, MainActivity.class);
                startActivity(intent);
                }
                else{
                    Intent intent = new Intent( VerifyToken.this, login.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}