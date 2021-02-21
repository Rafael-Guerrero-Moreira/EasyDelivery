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

import com.bumptech.glide.Glide;
import com.example.easydelivery.R;
import com.example.easydelivery.menu.StoreForBusinnes;
import com.example.easydelivery.model.Product;
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

import java.util.UUID;

public class ModuleProduct extends AppCompatActivity {
////DataBase
    private  FirebaseDatabase firebaseDatabase;
    private  DatabaseReference databaseReference;
    private  StorageReference storage;
   ///Components
    private EditText nameproduct;
    private  EditText descripction;
    private  EditText price;
    private ImageView ivProduct;
    private FloatingActionButton buttongallery;
    //gallery
    private Uri uri;
    private static final int Gallery_Intent=1;
    //variable
    private SharedPreferences prefs;
    private String idUser="";
    private String idproduct = "";
    private String urlPhoto="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbarproduct);
        setSupportActionBar(myToolbar);
        nameproduct = findViewById(R.id.txtproductname);
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

            idproduct =  getIntent().getExtras().getString("idporduct","");
            urlPhoto =  getIntent().getExtras().getString("url","");
            if (!idproduct.isEmpty()){
                loaddata();
            }

    }

    private void loaddata() {
        databaseReference.child("Product").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                    Product p  = objSnaptshot.getValue(Product.class);

                    if (idproduct.equals(p.getIdproduct()))
                    {
                        nameproduct.setText(p.getProductname());
                        descripction.setText(p.getDescription());
                        price.setText(p.getPrice());
                        Glide.with(ModuleProduct.this).load(p.getUrlphoto()).into(ivProduct);
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

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
                try
                {
                    storage.child("Products").child(idUser+"/"+ uri.getLastPathSegment()).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                            while(!uri.isComplete());
                            Uri url = uri.getResult();
                            saveProduct(imageselect(url));
                        }
                    });
                }
                catch (Exception e )
                {
                    saveProduct(urlPhoto);
                }

                    break;
            }
            case R.id.icon_back: {
                startActivity(new Intent(this, StoreForBusinnes.class));
                finish();
            }
            }
            return true;
    }
    private void saveProduct(String url)
    {
        Product p = new Product();
        p.setProductname(nameproduct.getText().toString());
        p.setDescription(descripction.getText().toString());
        p.setPrice(price.getText().toString());
        p.setUrlphoto(url);
        p.setIdproduct(generateUid());
        p.setIdBuisnes(idUser);
        databaseReference.child("Product").child(p.getIdproduct()).setValue(p);
        Toast.makeText(ModuleProduct.this, "Datos Guardados", Toast.LENGTH_LONG).show();
        finish();
    }
    private  String generateUid()
    {
        if(idproduct.isEmpty())
            return UUID.randomUUID().toString();
        else
            return idproduct;
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