package com.example.easydelivery.views.activities.auth;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.easydelivery.R;
import com.example.easydelivery.helpers.InternalFile;
import com.example.easydelivery.val.Validation;
import com.example.easydelivery.views.activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
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
    private Client client;

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
        setupFirebase();
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
        String  resulValidation= validation.ValidarCamposClient(email,password,name,lastname,ConfimPass);        //creating a new user
        if(!TextUtils.isEmpty(resulValidation))
        {
            Toast.makeText(RegisterClientScreenActivity.this,"Le falta ingresar: "+ resulValidation,Toast.LENGTH_LONG).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
         .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                   @Override
             public void onComplete(@NonNull Task<AuthResult> task) {
                       //checking if success
                       if(task.isSuccessful()){
                           FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                           RegisterUser(user.getUid());
                            Toast.makeText(RegisterClientScreenActivity.this,"Se ha registrado el usuario con el email: "+ TextEmail.getText(),Toast.LENGTH_LONG).show();
                           try {
                               signIn();
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
    public void RegisterUser(String uid){
        client = new Client();
        client.setName(TextName.getText().toString());
        client.setLastname(TexLastName.getText().toString());
        client.setEmail(TextEmail.getText().toString());
        client.setId(uid);
        client.setToken(UUID.randomUUID().toString());
        client.setType("Client");
        databaseReference.child("Client").child(client.getId()).setValue(client);
    }
    private void setupFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        // firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
       mAuth = FirebaseAuth.getInstance();
    }
    private void signIn() throws JSONException, IOException {
        JSONObject object = new JSONObject();
        object.put("User", client.getEmail());
        object.put("Token", client.getToken());
        object.put("UserType", client.getType());
        object.put("ID", client.getId());
        Log.d("json",object.toString());
        Log.d("ruta", String.valueOf((Environment.getExternalStorageDirectory())));
        InternalFile i = new InternalFile();
        i.createUserFile();
        i.writeUserFile(object);
        loginvar(client.getId(), client.getName() ,client.getEmail(), client.getType());
        startActivity(new Intent( RegisterClientScreenActivity.this, MainActivity.class));
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
