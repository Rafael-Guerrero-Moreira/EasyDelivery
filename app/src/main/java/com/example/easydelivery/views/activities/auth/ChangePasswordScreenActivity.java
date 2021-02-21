package com.example.easydelivery.views.activities.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.easydelivery.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ChangePasswordScreenActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        mAuth = FirebaseAuth.getInstance();
        email = (EditText) findViewById(R.id.acpTxtEmail);

    }

    public void goToPreviousActivity(View view) {
        finish();
    }

    public void changePassword(View view) {
        if (TextUtils.isEmpty(email.getText().toString())) {
            Toast.makeText(this, "Se debe ingresar un email", Toast.LENGTH_LONG).show();
            return;
        }
        mAuth.setLanguageCode("es");
        mAuth.sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                { Toast.makeText(ChangePasswordScreenActivity.this, "Se ha enviado un correo con un enlace para cambiar su contraseña", Toast.LENGTH_LONG).show();
                    finish();
                }
                else
                { Toast.makeText(ChangePasswordScreenActivity.this, "Error al enviar el correo", Toast.LENGTH_LONG).show(); }
            }
        });
    }
}