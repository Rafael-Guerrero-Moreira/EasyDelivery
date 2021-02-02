package com.example.easydelivery.module;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.easydelivery.R;
import com.example.easydelivery.ado.InternalFile;
import com.example.easydelivery.menu.Store;
import com.example.easydelivery.model.Product;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.UUID;

public class CreateProduct extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    EditText Nameproduct;
    EditText descripction;
    EditText price;
    EditText urlimage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbarproduct);
        setSupportActionBar(myToolbar);
        Nameproduct = findViewById(R.id.txtproductname);
        descripction = findViewById(R.id.txtdescripcion);
        price = findViewById(R.id.txtprice);
        InicializarFirebase ();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuproductcreate,menu);
        return super.onCreateOptionsMenu(menu);

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.icon_add: {
                try {
                    InternalFile f = new InternalFile();
                    JSONObject jsonObject =  f.readerFile("data","datausers");
                    Product p = new Product();
                    p.setProductname(Nameproduct.getText().toString());
                    p.setDescription(descripction.getText().toString());
                    p.setPrice(price.getText().toString());
                    p.setUrlphoto("Null");
                    p.setIdproduct(UUID.randomUUID().toString());
                    p.setIdBuisnes(jsonObject.getString("ID"));

                    databaseReference.child("Product").child(p.getIdproduct()).setValue(p);
                    Toast.makeText(this, "Agregar", Toast.LENGTH_LONG).show();
                   startActivity(new Intent(this, Store.class));
                   finish();
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
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
    private void InicializarFirebase (){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        // firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }

}