package com.example.easydelivery.module;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.easydelivery.R;
import com.example.easydelivery.helpers.InternalFile;
import com.example.easydelivery.menu.Store;
import com.example.easydelivery.model.InfoBuisnes;
import com.example.easydelivery.model.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CompanyInfo extends AppCompatActivity {
    private EditText textphone;
    private EditText textadress;
    private Spinner sptype;
    private ImageView ivlogo;
    private FloatingActionButton buttonlogo;
    private Uri imageUri;
    private static final int Gallery_Intent = 1;
    private  InfoBuisnes buisnes;
    private String[] Opciones = {"Seleccione","Restaurante","Distribuidora","Zapateria","Venta ropa","Otros"};
    private List<InfoBuisnes> listBuissnes = new ArrayList<InfoBuisnes>();
    private ArrayAdapter<InfoBuisnes> arrayAdapterPersona;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private StorageReference storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_info);
        inicializarFirebase ();
        textphone = findViewById(R.id.txtphone);
        sptype = findViewById(R.id.sptype);
        textadress = findViewById(R.id.txtadress);
        ivlogo = findViewById(R.id.ivlogonegocio);
        buttonlogo = findViewById(R.id.floatingbutoonlogo);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolcompanyinf);
        setSupportActionBar(myToolbar);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Gallery_Intent) {
            imageUri = data.getData();
            ivlogo.setImageURI(imageUri);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuinfouser,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.icon_add: {
                try {
                    InternalFile f = new InternalFile();
                    JSONObject jsonObject =  f.readUserFile();
                    InfoBuisnes inf = new InfoBuisnes();
                    inf.setPhone(textphone.getText().toString());
                    inf.setTypecomerce(sptype.getSelectedItem().toString());
                    inf.setAddress(textadress.getText().toString());
                    inf.setIdBuissnes(jsonObject.getString("ID"));
                    inf.setUrllogo(imageselect(imageUri));
                    databaseReference.child("Comerce").child(inf.getIdBuissnes()).setValue(inf);
                    if (!imageselect(imageUri).equals("null"))
                    storage.child("LogoBuissnes").child(inf.getIdBuissnes()+"/"+imageUri.getLastPathSegment()+".jpeg").putFile(imageUri);

                    Toast.makeText(this, "Agregar", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(this, Store.class));
                    finish();
                    break;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            case R.id.icon_back: {
                startActivity(new Intent(this, Store.class));
                finish();
            }
        }
        return true;
    }


    private void inicializarFirebase (){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        storage  = FirebaseStorage.getInstance().getReference("images");
    }
    private void verifydata()
    {
        InternalFile i = new InternalFile();
        JSONObject object = i.readUserFile();
        databaseReference.child("Comerce").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listBuissnes.clear();
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                    buisnes  = objSnaptshot.getValue(InfoBuisnes.class);
                    try {
                        Log.d("JSON", object.getString("ID"));
                        Log.d("JSON", buisnes.getIdBuissnes());
                        if (object.getString("ID").equals(buisnes.getIdBuissnes()))
                        {
                            textphone.setText(buisnes.getPhone());
                            textadress.setText(buisnes.getAddress());
                            sptype.setSelection(valueselect(buisnes.getTypecomerce()));
                            Log.d("Error", "Aqui debe suceder algo");
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
            else return uri.getLastPathSegment();
        } catch (Exception e)
        {
            return "null";
        }

    }

}