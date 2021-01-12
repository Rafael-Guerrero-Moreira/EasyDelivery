package com.example.easydelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.easydelivery.ado.InternalFile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ChangePassword extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        mAuth = FirebaseAuth.getInstance();
        email = (EditText) findViewById(R.id.txtemailchangepass);

    }
    public void ChangepasswordLogin(View view) throws IOException, JSONException {
        if (TextUtils.isEmpty(email.getText().toString())) {//(precio.equals(""))
            Toast.makeText(this, "Se debe ingresar un email", Toast.LENGTH_LONG).show();
            return;
        }
        mAuth.setLanguageCode("es");
        mAuth.sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                { Toast.makeText(ChangePassword.this, "Se a enviado un corro a su usuario: ", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ChangePassword.this, com.example.easydelivery.VerifyToken.class);
                    startActivity(intent);
                }
                else
                { Toast.makeText(ChangePassword.this, "Error al enviar el correo: ", Toast.LENGTH_LONG).show(); }
            }
        });
    }
}