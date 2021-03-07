package com.example.easydelivery.module;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.easydelivery.R;
import com.example.easydelivery.helpers.FireBaseRealtime;
import com.example.easydelivery.views.activities.products.ProductsListScreenActivity;
import com.example.easydelivery.model.InfoBusiness;
import com.example.easydelivery.views.activities.MapsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CompanyInfo extends AppCompatActivity {
    private EditText textphone;
    private EditText textadress;
    private Spinner sptype;
    private ImageView ivlogo;
    private FloatingActionButton buttonlogo;
    public EditText cordBusiness;
    private Uri imageUri;
    private SharedPreferences prefs;
    private static final int Gallery_Intent = 1;
    private InfoBusiness buisnes;
    private String[] Opciones = {"Seleccione","Restaurante","Distribuidora","Zapateria","Venta ropa","Otros"};
    private String idUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private StorageReference storage;
    private String cordenadas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_info);
        inicializarFirebase ();
        instanceVariable();
        try {
            cordenadas = getIntent().getStringExtra("cordenadas");
            cordBusiness.setText(cordenadas);
        } catch (Exception e) {
            e.printStackTrace();
        }

        buttonlogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,Gallery_Intent);
            }
        });
        sptype.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,Opciones));
        verifydata();

    }

    private void instanceVariable() {
        prefs = getSharedPreferences("shared_login_data",   Context.MODE_PRIVATE);
        idUser = prefs.getString("id", "");
        textphone = findViewById(R.id.txtphone);
        sptype = findViewById(R.id.sptype);
        textadress = findViewById(R.id.txtadress);
        ivlogo = findViewById(R.id.ivlogonegocio);
        buttonlogo = findViewById(R.id.floatingbutoonlogo);
        cordBusiness = findViewById(R.id.txtcord);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolcompanyinf);
        setSupportActionBar(myToolbar);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Gallery_Intent) {
            imageUri = data.getData();
            ivlogo.setImageURI(imageUri);
        }
    }


    private void inicializarFirebase (){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        storage  = FirebaseStorage.getInstance().getReference("images");
    }
    private void verifydata()
    {
        idUser = prefs.getString("id", "");
        databaseReference.child("Comerce").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                    buisnes  = objSnaptshot.getValue(InfoBusiness.class);

                        if (idUser.equals(buisnes.getIdBuissnes()))
                        {
                            textphone.setText(buisnes.getPhone());
                            textadress.setText(buisnes.getAddress());
                            if(buisnes.getUrllogo().isEmpty()) return;
                            Glide.with(CompanyInfo.this).load(buisnes.getUrllogo()).into(ivlogo);
                            sptype.setSelection(valueselect(buisnes.getTypecomerce()));
                            cordBusiness.setText(buisnes.getCoordinates());
                            break;
                        }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private int valueselect (String texto)
    {
        for(int i =0;i<Opciones.length;i++ )
        {
            if(Opciones[i].equals(texto))
                return i;
        }
        return 0;
    }
    private String imageselect(Uri uri )
    {
        try {
            if(uri.equals(null)) return "null";
            else return uri.toString();
        } catch (Exception e)
        {
            return "null";
        }
    }
    public void goToMap(View view) {
        startActivity(new Intent(this, MapsActivity.class));
        finish();
    }

    public void goToPreviousActivity(View view) {
    }

    public void goToHomeActivity(View view) {
    }

    public void insertInfo(View view) {
        try {
            if (!imageselect(imageUri).equals("null"))
                storage.child("LogoBuissnes").child(idUser +"/"+imageUri.getLastPathSegment()).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uri.isComplete());
                        Uri url = uri.getResult();
                        InfoBusiness inf = new InfoBusiness();
                        inf.setPhone(textphone.getText().toString());
                        inf.setTypecomerce(sptype.getSelectedItem().toString());
                        inf.setAddress(textadress.getText().toString());
                        inf.setIdBuissnes(idUser);
                        inf.setUrllogo(imageselect(url));
                        FireBaseRealtime realtime = new FireBaseRealtime();
                        realtime.RegisterInfoBusiness(inf,CompanyInfo.this);
                    }
                });
            else
            {

                InfoBusiness inf = new InfoBusiness();
                inf.setPhone(textphone.getText().toString());
                inf.setTypecomerce(sptype.getSelectedItem().toString());
                inf.setAddress(textadress.getText().toString());
                inf.setIdBuissnes(idUser);
                inf.setUrllogo((buisnes.getUrllogo()));
                inf.setCoordinates(cordBusiness.getText().toString());
                FireBaseRealtime realtime = new FireBaseRealtime();
                realtime.RegisterInfoBusiness(inf,CompanyInfo.this);
            }
        } catch (Exception e)
        {

        }

    }
}