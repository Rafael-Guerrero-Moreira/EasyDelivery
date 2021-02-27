package com.example.easydelivery.generallist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.easydelivery.Adapter.AdapterProducts;
import com.example.easydelivery.R;
import com.example.easydelivery.model.Product;
import com.example.easydelivery.module.ModuleProduct;
import com.example.easydelivery.module.SendToCar;
import com.example.easydelivery.views.activities.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class ListProducts extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storage;
    ListView lisproducts;
    Product selectProduct;
    String id;
    String userType;
    private FloatingActionButton actionButtonAdd;

    private SharedPreferences prefs;

    private List<Product> productList = new ArrayList<Product>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_products);
        InicializarFirebase();
        selectProduct = new Product();
        lisproducts = findViewById(R.id.lisproductos);
        actionButtonAdd = findViewById(R.id.floatingBAddBusiness);
        prefs = getSharedPreferences("shared_login_data",Context.MODE_PRIVATE);
        id = prefs.getString("id", "");
        userType = prefs.getString("usertype", "");
        if(userType.equals("Client")){
            actionButtonAdd.hide(); actionButtonAdd.setEnabled(true);
            id = getIntent().getExtras().getString("idBusiness");
        }
        ListarDatos();
        lisproducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectProduct= (Product) parent.getItemAtPosition(position);
                if(userType.equals("Business"))
                startActivity(new Intent(ListProducts.this, ModuleProduct.class).putExtra("idporduct",selectProduct.getIdproduct()).putExtra("url",selectProduct.getUrlphoto()));
                else if (userType.equals("Client")) startActivity(new Intent(ListProducts.this, SendToCar.class).putExtra("idporduct",selectProduct.getIdproduct()).putExtra("url",selectProduct.getUrlphoto()));

            }
        });
        Toolbar myToolbar =  findViewById(R.id.toolbarStoreBusines);
        setSupportActionBar(myToolbar);
    }

    public void createProdcut(View view)
    {
        startActivity(new Intent(ListProducts.this, ModuleProduct.class).putExtra("idporduct","").putExtra("url",""));

    }

    private void ListarDatos() {
        databaseReference.child("Product").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productList.clear();
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                    Product c = objSnaptshot.getValue(Product.class);
                    if (id.equals(c.getIdBuisnes())) {
                        productList.add(c);
                        AdapterProducts adapterProducts = new AdapterProducts(ListProducts.this, (ArrayList<Product>) productList);
                        lisproducts.setAdapter(adapterProducts);
                    }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menugeneral,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemhome: {
                startActivity(new Intent(this, MainActivity.class));
                break;
            }
            case R.id.itemback: {
                finish();
            }
        }
        return true;
    }
}