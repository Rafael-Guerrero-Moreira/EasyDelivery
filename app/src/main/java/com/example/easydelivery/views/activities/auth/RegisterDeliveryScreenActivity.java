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
import com.example.easydelivery.model.Delivery;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.UUID;

public class RegisterDeliveryScreenActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private EditText TextCompanyname;
    private EditText TextIdnet;
    private EditText Textpass;
    private EditText Textcorreo;
    private EditText confirpass;
    private Delivery delivery;
    private Boolean ConfimPass = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_delivery);
        delivery = new Delivery();
        InicializarFirebase ();
        TextCompanyname = findViewById(R.id.ardTxtCompany);
        TextIdnet = findViewById(R.id.ardTxtIdent);
        Textcorreo = findViewById(R.id.ardTxtEmail);
        Textpass = findViewById(R.id.ardTxtPassword);
        confirpass = findViewById(R.id.ardTxtConfirmPassword);
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
    private void IniciarSesion() throws JSONException, IOException {
        JSONObject object = new JSONObject();
        object.put("User",delivery.getEmail());
        object.put("Token",delivery.getToken());
        object.put("UserType",delivery.getType());
        object.put("ID",delivery.getId());
        Log.d("json",object.toString());
        Log.d("ruta", String.valueOf((Environment.getExternalStorageDirectory())));
        InternalFile i = new InternalFile();
        i.createUserFile();
        i.writeUserFile(object);
        loginvar(delivery.getId(),delivery.getName(),delivery.getEmail(),delivery.getType());
        Intent intent = new Intent( RegisterDeliveryScreenActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }
    public void RegisterUser(String Uid){
        delivery.setId(Uid);
        delivery.setName(TextCompanyname.getText().toString());
        delivery.setIdent(TextIdnet.getText().toString());
        delivery.setEmail(Textcorreo.getText().toString());
        delivery.setType("Delivery");
        delivery.setToken(UUID.randomUUID().toString());
        databaseReference.child("Delivery").child(delivery.getId()).setValue(delivery);
    }
    public void registerinAuth(View view){

        //Obtenemos el email y la contraseña desde las cajas de texto
        String email = Textcorreo.getText().toString();
        String password  = Textpass.getText().toString();
        String name = TextCompanyname.getText().toString();
        String identi = TextIdnet.getText().toString();
        Validation validation = new Validation();
        String   resultado = validation.ValidarCamposDelivery(email,password,name,identi,ConfimPass);
        if(!TextUtils.isEmpty(resultado))
        {
            Toast.makeText(RegisterDeliveryScreenActivity.this,"Le falta ingresar: "+ resultado,Toast.LENGTH_LONG).show();
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
                            Toast.makeText(RegisterDeliveryScreenActivity.this,"Se ha registrado el usuario con el email: "+ Textcorreo.getText(),Toast.LENGTH_LONG).show();
                            try {
                                IniciarSesion();
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                            }
                        }else{

                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {//si se presenta una colisión
                                Toast.makeText(RegisterDeliveryScreenActivity.this, "Ese usuario ya existe ", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegisterDeliveryScreenActivity.this, "No se pudo registrar el usuario ", Toast.LENGTH_LONG).show();
                            }                        }
                    }
                });

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