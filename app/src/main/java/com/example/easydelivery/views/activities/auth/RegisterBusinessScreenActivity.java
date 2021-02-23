package com.example.easydelivery.views.activities.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.easydelivery.R;
import com.example.easydelivery.helpers.InternalFile;
import com.example.easydelivery.model.Business;
import com.example.easydelivery.views.activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.easydelivery.val.Validation;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.UUID;

public class RegisterBusinessScreenActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private EditText TextEmail ;
    private EditText TextPassword;
    private EditText TextName;
    private EditText TextBusiness;
    private EditText TextIdent;
    private EditText TextConfirmPassword;
    private Boolean ConfirmPassword = false;
    private Business business;
    private Validation validation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_business);
        TextEmail = findViewById(R.id.arbTxtEmail);
        TextPassword = findViewById(R.id.arbTxtPassword);
        TextName = findViewById(R.id.arbTxtName);
        TextBusiness = findViewById(R.id.arbTxtBusiness);
        TextIdent = findViewById(R.id.arbTxtIdent);
        TextConfirmPassword = findViewById(R.id.arbTxtConfirmPassword);
        TextConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextPassword.getText().toString().equals(s.toString())) ConfirmPassword = true;
                else ConfirmPassword = false;
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

    private void InicializarFirebase (){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        // firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    public void RegisterUser(){
      business = new Business();
      business.setId(UUID.randomUUID().toString());
      business.setName(TextName.getText().toString());
      business.setBussinesname(TextBusiness.getText().toString());
      business.setIdentification(TextIdent.getText().toString());
      business.setEmail(TextEmail.getText().toString());
      business.setToken(UUID.randomUUID().toString());
      business.setType("Business");
      databaseReference.child("Business").child(business.getId()).setValue(business);
      loginvar(business.getId(), business.getName(), business.getEmail(), business.getType());

    }
    public void registrarUsuariob(View view){

        //Obtenemos el email y la contraseña desde las cajas de texto
        String email = TextEmail.getText().toString();
        String password  = TextPassword.getText().toString();
        String name = TextName.getText().toString();
        String businessname  = TextBusiness.getText().toString();
        String identi = TextIdent.getText().toString();
        validation = new Validation();
        String resultado = validation.ValidarCamposBuisnes(email,password,name,businessname,identi,ConfirmPassword);
        if(!TextUtils.isEmpty(resultado))
        {
            Toast.makeText(RegisterBusinessScreenActivity.this,"Le falta ingresar: "+ resultado,Toast.LENGTH_LONG).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            RegisterUser();
                            Toast.makeText(RegisterBusinessScreenActivity.this,"Se ha registrado el usuario con el email: "+ TextEmail.getText(),Toast.LENGTH_LONG).show();
                            try {
                                IniciarSesion();
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                            }
                        }else{

                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {//si se presenta una colisión
                                Toast.makeText(RegisterBusinessScreenActivity.this, "Ese usuario ya existe", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegisterBusinessScreenActivity.this, "No se pudo registrar el usuario ", Toast.LENGTH_LONG).show();
                            }                        }
                    }
                });

    }
    private void IniciarSesion() throws JSONException, IOException {
        JSONObject object = new JSONObject();
        object.put("User", business.getEmail());
        object.put("Token", business.getToken());
        object.put("UserType","Business");
        object.put("ID", business.getId());
        Log.d("json",object.toString());
        Log.d("ruta", String.valueOf((Environment.getExternalStorageDirectory())));
        InternalFile i = new InternalFile();
        i.createUserFile();
        i.writeUserFile(object);

        Intent intent = new Intent( RegisterBusinessScreenActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }
    public void loginvar(String id, String name, String email, String usertype)
    {
        SharedPreferences prefs = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("id",id);
        Log.d("Id", id);
        editor.putString("name",name);
        editor.putString("email", email);
        editor.putString("usertype", usertype);
        Log.d("User", usertype);
        editor.commit();
    }

}