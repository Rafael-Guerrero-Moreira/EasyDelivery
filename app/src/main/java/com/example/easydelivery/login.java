package com.example.easydelivery;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.database.FirebaseDatabase;

public class login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText TextEmail ;
    EditText TextPassword;
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
        Intent intent = new Intent(this, com.example.easydelivery.CreateAcount.class);
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
                            int pos = email.indexOf("@");
                            String user = email.substring(0, pos);
                            Toast.makeText(login.this, "Bienvenido: " + TextEmail.getText(), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent( login.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                           Toast.makeText(login.this, "Usuario o comtrasenia Invalida", Toast.LENGTH_LONG).show();

                        }

                    }
                });

    }
    private void InicializarFirebase (){
        // firebaseDatabase.setPersistenceEnabled(true);
        mAuth = FirebaseAuth.getInstance();
    }
}