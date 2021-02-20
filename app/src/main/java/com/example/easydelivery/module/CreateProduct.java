package com.example.easydelivery.module;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.easydelivery.menu.StoreForBusinnes;
import com.example.easydelivery.model.Product;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class CreateProduct extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storage;
    EditText Nameproduct;
    EditText descripction;
    EditText price;
    ImageView ivProduct;
    FloatingActionButton buttongallery;
    Uri uri;
    private static final int Gallery_Intent=1;
    private SharedPreferences prefs;
    private String idUser;

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
        prefs = getSharedPreferences("shared_login_data",   Context.MODE_PRIVATE);
        idUser = prefs.getString("id", "");
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
            uri = data.getData();
            ivProduct.setImageURI(uri);
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
                storage.child("Products").child(idUser+"/"+ uri.getLastPathSegment()).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uri.isComplete());
                        Uri url = uri.getResult();
                        Product p = new Product();
                        p.setProductname(Nameproduct.getText().toString());
                        p.setDescription(descripction.getText().toString());
                        p.setPrice(price.getText().toString());
                        p.setUrlphoto(imageselect(url));
                        p.setIdproduct(UUID.randomUUID().toString());
                        p.setIdBuisnes(idUser);
                        databaseReference.child("Product").child(p.getIdproduct()).setValue(p);
                        Toast.makeText(CreateProduct.this, "Producto Guardado", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
                    break;
            }
            case R.id.icon_back: {
                startActivity(new Intent(this, StoreForBusinnes.class));
                finish();
            }
            }
            return true;
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

    private void InicializarFirebase (){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        storage  = FirebaseStorage.getInstance().getReference("images");
    }

}