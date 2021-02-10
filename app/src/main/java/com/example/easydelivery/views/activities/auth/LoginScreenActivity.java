package com.example.easydelivery.views.activities.auth;

import android.Manifest;
import android.content.Intent;
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

import com.example.easydelivery.ChangePassword;
import com.example.easydelivery.MainActivity;
import com.example.easydelivery.R;
import com.example.easydelivery.UserType;
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

    public void register(View view)
    {
        startActivity(new Intent(this, UserType.class));
        finish();
    }

    public void changePassword(View view)
    {
        startActivity(new Intent(this, ChangePassword.class));
        finish();
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
        object.put("User", alTxtEmail.getText());
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
                        if (object.getString("User").equals(d.getCorreo())) {
                            //se asigna el nuevo token
                            d.setToken(UUID.randomUUID().toString());
                            InternalFile filei = new InternalFile();
                            object.put("Token",d.getToken());
                            object.put("UserType","Delivery");
                            object.put("ID",d.getId());
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
}