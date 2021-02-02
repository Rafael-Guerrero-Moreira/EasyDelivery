package com.example.easydelivery;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.easydelivery.model.Buisnes;
import com.example.easydelivery.model.Client;
import com.example.easydelivery.model.Delivery;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.example.easydelivery.ado.InternalFile;


public class login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText TextEmail ;
    EditText TextPassword;
    private List<Client> listPerson = new ArrayList<Client>();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextEmail = (EditText) findViewById(R.id.txtuserlogin);
        TextPassword = (EditText) findViewById(R.id.txtpasslogin);
        InicializarFirebase();

    }
    public void Register(View view)
    {
        Intent intent = new Intent(this, UserType.class);
        startActivity(intent);
    }
    public void ChangePass(View view)
    {
        Intent intent = new Intent(this, com.example.easydelivery.ChangePassword.class);
        startActivity(intent);
    }

    public void loguearUsuario(View view) {
        //Obtenemos el email y la contraseña desde las cajas de texto
         String email = TextEmail.getText().toString().trim();
        String password = TextPassword.getText().toString().trim();
        //Verificamos que las cajas de texto no esten vacías
        if (TextUtils.isEmpty(email)) {//(precio.equals(""))
            Toast.makeText(this, "Se debe ingresar un email", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Falta ingresar la contraseña", Toast.LENGTH_LONG).show();
            return ;
        }
        //loguear usuario
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if (task.isSuccessful()) {
                            //Abrimos un nuevo fichero y doc si existe se sobre escribe en el
                            InternalFile filei = new InternalFile();
                            filei.createFile("data","datausers");
                            //funcion llenarJson llena el json con un nuevo token y el usuario para llenar el fichero
                            try {
                                llenarJson();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(login.this, "Bienvenido: " + TextEmail.getText(), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent( login.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                           Toast.makeText(login.this, "Usuario o comtrasenia Invalida", Toast.LENGTH_LONG).show();

                        }

                    }
                });

    }
    private void InicializarFirebase (){
        // firebaseDatabase.setPersistenceEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        // firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }
    public JSONObject llenarJson() throws JSONException {

        JSONObject object = new JSONObject();
        //LLenamso el objeto Json
        object.put("User",TextEmail.getText());
        Log.d("PUT",object.getString("User"));
        databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                    Client p = objSnaptshot.getValue(Client.class);
                    try {
                        // se pregunta por el usuario en la bd esto por el email
                        if (object.getString("User").equals(p.getEmail())) {
                            //se asigna el nuevo token
                            p.setToken(UUID.randomUUID().toString());
                            InternalFile filei = new InternalFile();
                            object.put("Token",p.getToken());
                            object.put("Type",p.getType());
                            object.put("ID",p.getIduser());

                            databaseReference.child("Users").child(p.getIduser()).setValue(p);
                            filei.writerFile("data","datausers",object);
                            break;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
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
                    Buisnes b = objSnaptshot.getValue(Buisnes.class);
                    try {
                        // se pregunta por el usuario en la bd esto por el email
                        if (object.getString("User").equals(b.getEmail())) {
                            //se asigna el nuevo token
                            b.setToken(UUID.randomUUID().toString());
                            InternalFile filei = new InternalFile();
                            object.put("Token",b.getToken());
                            object.put("UserType","Buisnes");
                            object.put("ID",b.getId());

                            databaseReference.child("Buisnes").child(b.getId()).setValue(b);
                            filei.writerFile("data","datausers",object);
                            break;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
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
                    Delivery d = objSnaptshot.getValue(Delivery.class);
                    try {
                        // se pregunta por el usuario en la bd esto por el email
                        if (object.getString("User").equals(d.getCorreo())) {
                            //se asigna el nuevo token
                            d.setToken(UUID.randomUUID().toString());
                            InternalFile filei = new InternalFile();
                            object.put("Token",d.getToken());
                            object.put("UserType","Delivery");
                            object.put("ID",d.getId());
                            databaseReference.child("Delivery").child(d.getId()).setValue(d);
                            filei.writerFile("data","datausers",object);
                            break;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return object;
    }



}