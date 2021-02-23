package com.example.easydelivery.views.activities.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.easydelivery.R;
import com.example.easydelivery.helpers.InternalFile;
import com.example.easydelivery.menu.SettingUser;
import com.example.easydelivery.model.Business;
import com.example.easydelivery.model.Client;
import com.example.easydelivery.model.Delivery;
import com.example.easydelivery.module.CompanyInfo;
import com.example.easydelivery.views.activities.startup.SplashScreenActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;

import java.io.IOException;


public class Account extends Fragment {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private SharedPreferences prefs;

    private Button amButtonLogout;
    private Button amButtonInfoUser;
    private Button amButtonChangePassword;

    public Account() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        setupButtons(view);
        prefs = getActivity().getSharedPreferences("shared_login_data", Context.MODE_PRIVATE);
        InicializarFirebase();
        return view;
    }

    private void setupButtons(View view) {
        amButtonLogout = (Button) view.findViewById(R.id.amButtonLogout);
        amButtonInfoUser = (Button) view.findViewById(R.id.amButtonInfoUser);
        amButtonChangePassword = (Button) view.findViewById(R.id.amButtonChangePassword);
        amButtonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSession();
            }
        });
        amButtonInfoUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoUser();
            }
        });
        amButtonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }

    private void InicializarFirebase (){
        // firebaseDatabase.setPersistenceEnabled(true);
        firebaseDatabase = FirebaseDatabase.getInstance();
        // firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    public void closeSession()
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

        startActivity(new Intent(getActivity(), SplashScreenActivity.class));
        getActivity().finish();
    }

    public void changePassword() {
        String email = prefs.getString("email", "");

        mAuth.setLanguageCode("es");
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                { Toast.makeText(getActivity(), "Se a enviado un correo a su usuario: ", Toast.LENGTH_LONG).show(); }
                else
                { Toast.makeText(getActivity(), "Error al enviar el correo: ", Toast.LENGTH_LONG).show(); }
            }
        });
    }

    public void infoUser() {
        startActivity(new Intent(getActivity(), CompanyInfo.class));
    }
}
