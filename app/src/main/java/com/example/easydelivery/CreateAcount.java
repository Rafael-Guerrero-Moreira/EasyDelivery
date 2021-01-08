package com.example.easydelivery;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

import com.example.easydelivery.model.Person;

public class CreateAcount extends AppCompatActivity {
   private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    EditText TextEmail ;
    EditText TextPassword;
    EditText TextName;
    EditText TexLastName;
    EditText TextUser;
    EditText TexConfirmpass;
    Boolean ConfimPass = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acount);
        TextEmail = (EditText) findViewById(R.id.txtemail);
        TextPassword = (EditText) findViewById(R.id.txtpass);
        TextName = (EditText) findViewById(R.id.txtname);
        TexLastName = (EditText) findViewById(R.id.txtlastname);
        TextUser = (EditText) findViewById(R.id.txtuser);
        TexConfirmpass = (EditText) findViewById(R.id.txtconfirmpass);
//validacion de contrasenia
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

    private void registrarUsuario(){

        //Obtenemos el email y la contraseña desde las cajas de texto
        String email = TextEmail.getText().toString();
        String password  = TextPassword.getText().toString();
        String name = TextName.getText().toString();
        String lastname  = TexLastName.getText().toString();
        String user  = TextUser.getText().toString();
        String resultado =ValidarCampos(email,password,name,lastname,user);
        //creating a new user
        if(!TextUtils.isEmpty(resultado))
        {
            Toast.makeText(com.example.easydelivery.CreateAcount.this,"Le falta ingresar: "+ resultado,Toast.LENGTH_LONG).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
         .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                   @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
                       //checking if success
                       if(task.isSuccessful()){
                            RegisterUser();
                            Toast.makeText(com.example.easydelivery.CreateAcount.this,"Se ha registrado el usuario con el email: "+ TextEmail.getText(),Toast.LENGTH_LONG).show();
                            Intent intent = new Intent( com.example.easydelivery.CreateAcount.this, MainActivity.class);
                            startActivity(intent);
                        }else{

                           if (task.getException() instanceof FirebaseAuthUserCollisionException) {//si se presenta una colisión
                               Toast.makeText(CreateAcount.this, "Ese usuario ya existe ", Toast.LENGTH_SHORT).show();
                           } else {
                               Toast.makeText(CreateAcount.this, "No se pudo registrar el usuario ", Toast.LENGTH_LONG).show();
                           }                        }
                   }
               });

    }
    public void CreateUsers(View view) {
        //Invocamos al método:
        registrarUsuario();

    }
    public void RegisterUser(){
        Person p = new Person();
        p.setName(TextName.getText().toString());
        p.setLastname(TexLastName.getText().toString());
        p.setEmail(TextEmail.getText().toString());
        p.setUser(TextUser.getText().toString());
        p.setIduser(UUID.randomUUID().toString());

        databaseReference.child("Users").child(p.getIduser()).setValue(p);
    }
    public String ValidarCampos(String email, String password,String name,String apellido,String usuario)
    {
        String mensage;
        //Verificamos que las cajas de texto no esten vacías
        if(TextUtils.isEmpty(name)){
            return "Nombre";
        }
        if(TextUtils.isEmpty(apellido)){
            return "Apellido";
        }
        if(TextUtils.isEmpty(usuario)){
            return "Usuario";
        }
        if(TextUtils.isEmpty(email)){
            return "Email";
        }
        if(TextUtils.isEmpty(password)){
            return "Contraseña";
        }
        if (!ConfimPass){
            return "Las contraseñas no coinciden";
        }

        return "";
    }
    private void InicializarFirebase (){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        // firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
       mAuth = FirebaseAuth.getInstance();
    }


}
