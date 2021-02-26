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
import com.example.easydelivery.model.Business;
import com.example.easydelivery.model.Client;
import com.example.easydelivery.model.Delivery;
import com.example.easydelivery.module.ClientInfo;
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
        String id = prefs.getString("id", "");

        databaseReference.child("Client").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                    Client p = objSnaptshot.getValue(Client.class);

                    // se pregunta por el usuario en la bd esto por el email
                    if (id.equals(p.getId())) {
                        p.setToken("  ");
                        databaseReference.child("Client").child(p.getId()).setValue(p);
                        break;
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        databaseReference.child("Business").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                    Business p = objSnaptshot.getValue(Business.class);

                    // se pregunta por el usuario en la bd esto por el email
                    if (id.equals(p.getId())) {
                        p.setToken("  ");
                        databaseReference.child("Business").child(p.getId()).setValue(p);
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
                    if (id.equals(p.getId())) {
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
        startActivity(new Intent(getActivity(), SplashScreenActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));;
        getActivity().finish();
        loginvar();
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
        String type = prefs.getString("usertype", "");
        if(type.equals("Business")) startActivity(new Intent(getActivity(), CompanyInfo.class));
        if(type.equals("Client")) startActivity(new Intent(getActivity(), ClientInfo.class));
    }
    public void loginvar()
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("usertype","Null");
        editor.commit();
    }
}
