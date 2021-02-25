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

import com.example.easydelivery.helpers.FireBaseRealtime;
import com.example.easydelivery.model.GenealLoginModel;
import com.example.easydelivery.views.activities.MainActivity;
import com.example.easydelivery.R;
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
    private int i =0;
    private String user;
    private String tablesUser;
    private SharedPreferences prefs;
    private String logeo="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        alTxtEmail = findViewById(R.id.alTxtEmail);
        alTxtPassword = findViewById(R.id.alTxtPassword);
        alChkRemember = findViewById(R.id.alChkRemember);
        mAuth = FirebaseAuth.getInstance();
        prefs = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE);

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
                                    Toast.makeText(LoginScreenActivity.this, "Bienvenido: " + alTxtEmail.getText(), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(LoginScreenActivity.this, "Bienvenido: " + alTxtEmail.getText(), Toast.LENGTH_LONG).show();
                                try {
                                       generateToken(false);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                           Toast.makeText(LoginScreenActivity.this, "Credenciales inválidas", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    public JSONObject generateToken(boolean createfile) throws JSONException {
        user = alTxtEmail.getText().toString();
        JSONObject object = new JSONObject();

            SharedPreferences prefs = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            sigin(createfile,user,"Client",editor);
            sigin(createfile,user,"Business",editor);
            sigin(createfile,user,"Delivery",editor);
        return object;
    }
    public void createUserFile() {
        InternalFile filei = new InternalFile();
        filei.createUserFile();
        try {
            generateToken(true);
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

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;



    public void sigin(Boolean createfile, String user, String tablesUser, SharedPreferences.Editor editor)
    {
        setupFirebase();
        JSONObject object = new JSONObject();
        databaseReference.child(tablesUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                    GenealLoginModel model = objSnaptshot.getValue(GenealLoginModel.class);
                    try {
                        // se pregunta por el usuario en la bd esto por el email
                        if (user.equals(model.getEmail())) {
                            //se asigna el nuevo token
                            if(createfile) {
                                model.setToken(UUID.randomUUID().toString());
                                InternalFile filei = new InternalFile();
                                object.put("Token", model.getToken());
                                object.put("UserType",model.getType());
                                databaseReference.child(tablesUser).child(model.getId()).child("token").setValue(model.getToken());
                                filei.writeUserFile(object);
                                loginvar(model.getId(), model.getName() ,model.getEmail(), model.getType(),editor);

                            }
                            else
                            {
                                loginvar(model.getId(), model.getName() ,model.getEmail(), model.getType(),editor);

                            }
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
    }
    private void setupFirebase(){
        // firebaseDatabase.setPersistenceEnabled(true);
        firebaseDatabase = FirebaseDatabase.getInstance();
        // firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }
    public void loginvar(String id, String name, String email, String usertype,SharedPreferences.Editor editor)
    {
        editor.putString("id",id);
        editor.putString("name",name);
        editor.putString("email", email);
        Log.d("h",usertype);
        editor.putString("usertype", usertype);
        editor.putString("logeo", "true");
        editor.commit();
        startActivity(new Intent( LoginScreenActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));

    }

}