package com.example.easydelivery.module;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.easydelivery.MainActivity;
import com.example.easydelivery.R;
import com.example.easydelivery.ado.InternalFile;
import com.example.easydelivery.model.Buisnes;
import com.example.easydelivery.model.Delivery;
import com.example.easydelivery.val.Validation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.UUID;

public class CrateAcountDelivery extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    EditText TextCompanyname;
    EditText TextIdnet;
    EditText Textpass;
    EditText Textcorreo;
    EditText confirpass;
    Delivery delivery;
    Boolean ConfimPass = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crate_acount_delivery);
        delivery = new Delivery();
        InicializarFirebase ();
        TextCompanyname = findViewById(R.id.txtnamecompany);
        TextIdnet =  findViewById(R.id.txtidentdelyvery);
        Textcorreo =  findViewById(R.id.txtemaildelivery);
        Textpass = findViewById(R.id.txtpassdelivery);
        confirpass  = findViewById(R.id.txtconfirmpassdelyvery);
        confirpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(Textpass.getText().toString().equals(s.toString()))ConfimPass = true;
                else ConfimPass = false;
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    private void InicializarFirebase (){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        // firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
    }
    private void IniciarSesion() throws JSONException, IOException {
        JSONObject object = new JSONObject();
        object.put("User",delivery.getCorreo());
        object.put("Token",delivery.getToken());
        object.put("UserType",delivery.getType());

        Log.d("json",object.toString());
        Log.d("ruta", String.valueOf((Environment.getExternalStorageDirectory())));
        InternalFile i = new InternalFile();
        i.createFile("data","datausers");
        i.writerFile("data","datausers",object);

        Intent intent = new Intent( CrateAcountDelivery.this, MainActivity.class);
        startActivity(intent);
        finish();

    }
    public void RegisterUser(){
        delivery.setId(UUID.randomUUID().toString());
        delivery.setCompanyname(TextCompanyname.getText().toString());
        delivery.setIdent(TextIdnet.getText().toString());
        delivery.setCorreo(Textcorreo.getText().toString());
        delivery.setType("Delivery");
        delivery.setToken(UUID.randomUUID().toString());
        databaseReference.child("Delivery").child(delivery.getId()).setValue(delivery);
    }
    public void registrarUsuariob(View view){

        //Obtenemos el email y la contraseña desde las cajas de texto
        String email = Textcorreo.getText().toString();
        String password  = Textpass.getText().toString();
        String name = TextCompanyname.getText().toString();
        String identi = TextIdnet.getText().toString();
        Validation validation = new Validation();
        String   resultado = validation.ValidarCamposDelivery(email,password,name,identi,ConfimPass);
        if(!TextUtils.isEmpty(resultado))
        {
            Toast.makeText(CrateAcountDelivery.this,"Le falta ingresar: "+ resultado,Toast.LENGTH_LONG).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            RegisterUser();
                            Toast.makeText(CrateAcountDelivery.this,"Se ha registrado el usuario con el email: "+ Textcorreo.getText(),Toast.LENGTH_LONG).show();
                            try {
                                IniciarSesion();
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                            }
                        }else{

                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {//si se presenta una colisión
                                Toast.makeText(CrateAcountDelivery.this, "Ese usuario ya existe ", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(CrateAcountDelivery.this, "No se pudo registrar el usuario ", Toast.LENGTH_LONG).show();
                            }                        }
                    }
                });

    }

}