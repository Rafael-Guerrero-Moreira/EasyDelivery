package com.example.easydelivery.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.easydelivery.Adapter.AdapterProducts;
import com.example.easydelivery.R;
import com.example.easydelivery.model.Product;
import com.example.easydelivery.module.CreateProduct;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class Store extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storage;
    ListView lista;
    private List<Product> productList = new ArrayList<Product>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        InicializarFirebase();
        BottomNavigationView bottomNavigationView;
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.BottonNavigation);
        bottomNavigationView.setSelectedItemId(R.id.fragmenStore);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        lista = findViewById(R.id.lisproductos);
        ListarDatos();
    }

    public void createProdcut(View view)
    {
        startActivity(new Intent(Store.this, CreateProduct.class));

    }
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.fragmenStore:
                    intent = new Intent(Store.this, Store.class);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.fragmenCategory:
                    intent = new Intent(Store.this, Category.class);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.fragmenSearch:
                    intent = new Intent(Store.this, Search.class);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.fragmenUser:
                    intent = new Intent(Store.this, SettingUser.class);
                    startActivity(intent);
                    finish();
                    return true;
            }


            return false;
        }
    };

    private void ListarDatos() {
        databaseReference.child("Product").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productList.clear();
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                    Product c = objSnaptshot.getValue(Product.class);
                    productList.add(c);
                    AdapterProducts adapterProducts = new AdapterProducts(Store.this,(ArrayList<Product>)productList );
                    lista.setAdapter(adapterProducts);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void InicializarFirebase (){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        storage  = FirebaseStorage.getInstance().getReference("images");
    }
}