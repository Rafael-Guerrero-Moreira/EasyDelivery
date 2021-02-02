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

public class CreateAucontforBuisnes extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    EditText TextEmail ;
    EditText TextPassword;
    EditText TextName;
    EditText Texbuinesname;
    EditText txtident;
    EditText TexConfirmpass;
    Boolean ConfimPass = false;
    Buisnes buisnes;
    Validation validation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_aucontfor_buisnes);
        TextEmail = (EditText) findViewById(R.id.txtemailbuisnees);
        TextPassword = (EditText) findViewById(R.id.txtpassbuisnes);
        TextName = (EditText) findViewById(R.id.txtnameprop);
        Texbuinesname = (EditText) findViewById(R.id.txtbuisnesname);
        txtident = (EditText) findViewById(R.id.txtident);
        TexConfirmpass = (EditText) findViewById(R.id.txtconfirmpassbuisnes);
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
    private void InicializarFirebase (){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        // firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    public void RegisterUser(){
      buisnes = new Buisnes();
      buisnes.setId(UUID.randomUUID().toString());
      buisnes.setName(TextName.getText().toString());
      buisnes.setBuisnesname(Texbuinesname.getText().toString());
      buisnes.setIdentification(txtident.getText().toString());
      buisnes.setEmail(TextEmail.getText().toString());
      buisnes.setToken(UUID.randomUUID().toString());
      buisnes.setType("Buisnes");
      databaseReference.child("Buisnes").child(buisnes.getId()).setValue(buisnes);

    }
    public void registrarUsuariob(View view){

        //Obtenemos el email y la contraseña desde las cajas de texto
        String email = TextEmail.getText().toString();
        String password  = TextPassword.getText().toString();
        String name = TextName.getText().toString();
        String buisnesname  = Texbuinesname.getText().toString();
        String identi = txtident.getText().toString();
        validation = new Validation();
         String   resultado = validation.ValidarCamposBuisnes(email,password,name,buisnesname,identi,ConfimPass);
        if(!TextUtils.isEmpty(resultado))
        {
            Toast.makeText(CreateAucontforBuisnes.this,"Le falta ingresar: "+ resultado,Toast.LENGTH_LONG).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            RegisterUser();
                            Toast.makeText(CreateAucontforBuisnes.this,"Se ha registrado el usuario con el email: "+ TextEmail.getText(),Toast.LENGTH_LONG).show();
                            try {
                                IniciarSesion();
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                            }
                        }else{

                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {//si se presenta una colisión
                                Toast.makeText(CreateAucontforBuisnes.this, "Ese usuario ya existe ", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(CreateAucontforBuisnes.this, "No se pudo registrar el usuario ", Toast.LENGTH_LONG).show();
                            }                        }
                    }
                });

    }
    private void IniciarSesion() throws JSONException, IOException {
        JSONObject object = new JSONObject();
        object.put("User",buisnes.getEmail());
        object.put("Token",buisnes.getToken());
        object.put("UserType","Buisnes");
        object.put("ID",buisnes.getId());
        Log.d("json",object.toString());
        Log.d("ruta", String.valueOf((Environment.getExternalStorageDirectory())));
        InternalFile i = new InternalFile();
        i.createFile("data","datausers");
        i.writerFile("data","datausers",object);

        Intent intent = new Intent( CreateAucontforBuisnes.this, MainActivity.class);
        startActivity(intent);
        finish();

    }


}