package com.example.easydelivery.module;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.easydelivery.R;
import com.example.easydelivery.helpers.InternalFile;
import com.example.easydelivery.menu.Store;
import com.example.easydelivery.model.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class CreateProduct extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storage;
    EditText Nameproduct;
    EditText descripction;
    EditText price;
    EditText urlimage;
    ImageView ivProduct;
    FloatingActionButton buttongallery;
    Uri imageUri;
    private static final int Gallery_Intent=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbarproduct);
        setSupportActionBar(myToolbar);
        Nameproduct = findViewById(R.id.txtproductname);
        descripction = findViewById(R.id.txtdescripcion);
        price = findViewById(R.id.txtprice);
        ivProduct = findViewById(R.id.ivProduct);
        buttongallery = findViewById(R.id.floatingbutoonphoto);
        InicializarFirebase ();
        buttongallery.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,Gallery_Intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Gallery_Intent) {
            imageUri = data.getData();
            ivProduct.setImageURI(imageUri);
        }
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
                    JSONObject jsonObject =  f.readUserFile();
                    Product p = new Product();
                    p.setProductname(Nameproduct.getText().toString());
                    p.setDescription(descripction.getText().toString());
                    p.setPrice(price.getText().toString());
                    p.setUrlphoto(imageUri.getLastPathSegment());
                    p.setIdproduct(UUID.randomUUID().toString());
                    p.setIdBuisnes(jsonObject.getString("ID"));
                    databaseReference.child("Product").child(p.getIdproduct()).setValue(p);
                    storage.child("Products").child(p.getIdBuisnes()+"/"+imageUri.getLastPathSegment()+".jpeg").putFile(imageUri);
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


    private void InicializarFirebase (){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        // firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
        storage  = FirebaseStorage.getInstance().getReference("images");
    }

}