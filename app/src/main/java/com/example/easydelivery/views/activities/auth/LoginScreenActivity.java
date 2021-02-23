package com.example.easydelivery.views.activities.auth;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.easydelivery.views.activities.MainActivity;
import com.example.easydelivery.R;
import com.example.easydelivery.menu.StoreForBusinnes;
import com.example.easydelivery.model.Business;
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

import java.util.UUID;

import com.example.easydelivery.helpers.InternalFile;


public class LoginScreenActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText alTxtEmail;
    private EditText alTxtPassword;
    private CheckBox alChkRemember;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        alTxtEmail = findViewById(R.id.alTxtEmail);
        alTxtPassword = findViewById(R.id.alTxtPassword);
        alChkRemember = findViewById(R.id.alChkRemember);
        setupFirebase();
    }

    public void goToPreviousActivity(View view) {
        finish();
    }

    public void goToRegisterActivity(View view)
    {
        startActivity(new Intent(this, UserTypeScreenActivity.class));
        finish();
    }

    public void changePassword(View view)
    {
        startActivity(new Intent(this, ChangePasswordScreenActivity.class));
        //finish();
    }

    public void signIn(View view) {
        String email = alTxtEmail.getText().toString().trim();
        String password = alTxtPassword.getText().toString().trim();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Debe ingresar sus credenciales", Toast.LENGTH_LONG).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (alChkRemember.isChecked()) {
                                if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                    requestPermissions(new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, 3);
                                } else {
                                    createUserFile();
                                    Toast.makeText(LoginScreenActivity.this, "Bienvenido: " + alTxtEmail.getText(), Toast.LENGTH_LONG).show();
                                    startActivity(new Intent( LoginScreenActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
                                }
                            } else {
                                Toast.makeText(LoginScreenActivity.this, "Bienvenido: " + alTxtEmail.getText(), Toast.LENGTH_LONG).show();
                                obtenerID();
                                startActivity(new Intent( LoginScreenActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));

                            }
                        } else {
                           Toast.makeText(LoginScreenActivity.this, "Credenciales inválidas", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void setupFirebase(){
        // firebaseDatabase.setPersistenceEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        // firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }

    public JSONObject generateToken() throws JSONException {
        JSONObject object = new JSONObject();
        String user = alTxtEmail.getText().toString();
        databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                    Client p = objSnaptshot.getValue(Client.class);
                    try {
                        // se pregunta por el usuario en la bd esto por el email
                        if (user.equals(p.getEmail())) {
                            //se asigna el nuevo token
                            p.setToken(UUID.randomUUID().toString());
                            InternalFile filei = new InternalFile();
                            object.put("Token",p.getToken());
                            loginvar(p.getIduser(), p.getName() + " "+ p.getLastname(),p.getEmail(), p.getType());
                            databaseReference.child("Users").child(p.getIduser()).setValue(p);
                            filei.writeUserFile(object);
                            break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
        databaseReference.child("Bussines").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                    Business b = objSnaptshot.getValue(Business.class);
                    try {
                        // se pregunta por el usuario en la bd esto por el email
                        if (user.equals(b.getEmail())) {
                            //se asigna el nuevo token
                            b.setToken(UUID.randomUUID().toString());
                            Log.d("Token", b.getToken());
                            InternalFile filei = new InternalFile();
                            object.put("Token",b.getToken());
                           loginvar(b.getId(), b.getBussinesname() , b.getEmail(), b.getType());
                            databaseReference.child("Bussines").child(b.getId()).setValue(b);
                            filei.writeUserFile(object);
                            break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
        databaseReference.child("Delivery").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                    Delivery d = objSnaptshot.getValue(Delivery.class);
                    try {
                        // se pregunta por el usuario en la bd esto por el email
                        if (user.equals(d.getCorreo())) {
                            //se asigna el nuevo token
                            d.setToken(UUID.randomUUID().toString());
                            InternalFile filei = new InternalFile();
                            object.put("Token",d.getToken());
                           loginvar(d.getId(), d.getCompanyname() , d.getCorreo(), d.getType());

                            databaseReference.child("Delivery").child(d.getId()).setValue(d);
                            filei.writeUserFile(object);
                            break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
        return object;
    }
    public void obtenerID()  {
      String user = alTxtEmail.getText().toString();

        databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                    Client p = objSnaptshot.getValue(Client.class);
                        // se pregunta por el usuario en la bd esto por el email
                        if (user.equals(p.getEmail())) {
                           loginvar(p.getIduser(), p.getName() + " "+ p.getLastname(),p.getEmail(), p.getType());
                            break;
                        }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
        databaseReference.child("Bussines").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                    Business b = objSnaptshot.getValue(Business.class);
                        // se pregunta por el usuario en la bd esto por el email
                        if (user.equals(b.getEmail())) {
                           loginvar(b.getId(), b.getBussinesname() , b.getEmail(), b.getType());
                            break;
                        }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
        databaseReference.child("Delivery").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                    Delivery d = objSnaptshot.getValue(Delivery.class);

                        // se pregunta por el usuario en la bd esto por el email
                        if (user.equals(d.getCorreo())) {
                            loginvar(d.getId(), d.getCompanyname() , d.getCorreo(), d.getType());

                            break;
                        }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }


    public void createUserFile() {
        InternalFile filei = new InternalFile();
        filei.createUserFile();
        try {
            generateToken();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
            createUserFile();
        } else {
            //Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(LoginScreenActivity.this, "Bienvenido: " + alTxtEmail.getText(), Toast.LENGTH_LONG).show();
        startActivity(new Intent( LoginScreenActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
        return;
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