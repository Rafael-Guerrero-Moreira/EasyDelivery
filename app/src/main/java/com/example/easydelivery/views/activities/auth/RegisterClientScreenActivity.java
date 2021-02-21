package com.example.easydelivery.views.activities.auth;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.easydelivery.R;
import com.example.easydelivery.helpers.InternalFile;
import com.example.easydelivery.menu.StoreForBusinnes;
import com.example.easydelivery.val.Validation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.UUID;

import com.example.easydelivery.model.Client;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterClientScreenActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private EditText TextEmail ;
    private EditText TextPassword;
    private EditText TextName;
    private EditText TexLastName;
    private Validation validation;
    private EditText TexConfirmpass;
    private Boolean ConfimPass = false;
    private Client p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_client);
        TextEmail = findViewById(R.id.arcTxtEmail);
        TextPassword = findViewById(R.id.arcTxtPassword);
        TextName = findViewById(R.id.arcTxtName);
        TexLastName = findViewById(R.id.arcTxtLastname);
        TexConfirmpass = findViewById(R.id.arcTxtConfirmPassword);
        TexConfirmpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextPassword.getText().toString().equals(s.toString()))ConfimPass = true;
                else ConfimPass = false;
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        InicializarFirebase ();

    }

    public void goToPreviousActivity(View view) {
        startActivity(new Intent(this, UserTypeScreenActivity.class));
        finish();
    }

    public void goToLoginActivity(View view)
    {
        startActivity(new Intent(this, LoginScreenActivity.class));
        finish();
    }

    private void registrarUsuario(){

        //Obtenemos el email y la contraseña desde las cajas de texto
        String email = TextEmail.getText().toString();
        String password  = TextPassword.getText().toString();
        String name = TextName.getText().toString();
        String lastname  = TexLastName.getText().toString();
        validation = new Validation();
        String   resultado = validation.ValidarCamposClient(email,password,name,lastname,ConfimPass);        //creating a new user
        if(!TextUtils.isEmpty(resultado))
        {
            Toast.makeText(RegisterClientScreenActivity.this,"Le falta ingresar: "+ resultado,Toast.LENGTH_LONG).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
         .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                   @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
                       //checking if success
                       if(task.isSuccessful()){
                            RegisterUser();
                            Toast.makeText(RegisterClientScreenActivity.this,"Se ha registrado el usuario con el email: "+ TextEmail.getText(),Toast.LENGTH_LONG).show();
                           try {
                               IniciarSesion();
                           } catch (JSONException | IOException e) {
                               e.printStackTrace();
                           }
                       }else{

                           if (task.getException() instanceof FirebaseAuthUserCollisionException) {//si se presenta una colisión
                               Toast.makeText(RegisterClientScreenActivity.this, "Ese usuario ya existe ", Toast.LENGTH_SHORT).show();
                           } else {
                               Toast.makeText(RegisterClientScreenActivity.this, "No se pudo registrar el usuario ", Toast.LENGTH_LONG).show();
                           }                        }
                   }
               });

    }
    public void CreateUsers(View view) {
        //Invocamos al método:
        registrarUsuario();

    }
    public void RegisterUser(){
        p = new Client();
        p.setName(TextName.getText().toString());
        p.setLastname(TexLastName.getText().toString());
        p.setEmail(TextEmail.getText().toString());
        p.setIduser(UUID.randomUUID().toString());
        p.setToken(UUID.randomUUID().toString());
        p.setType("Users");
        databaseReference.child("Users").child(p.getIduser()).setValue(p);


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
        object.put("User",p.getEmail());
        object.put("Token",p.getToken());
        object.put("UserType",p.getType());
        object.put("ID",p.getIduser());
        Log.d("json",object.toString());
        Log.d("ruta", String.valueOf((Environment.getExternalStorageDirectory())));
        InternalFile i = new InternalFile();
        i.createUserFile();
        i.writeUserFile(object);

        Intent intent = new Intent( RegisterClientScreenActivity.this, StoreForBusinnes.class);
        startActivity(intent);
        finish();

    }


}
