package com.example.easydelivery.views.activities.products;

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
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easydelivery.Adapter.AdapterProducts;
import com.example.easydelivery.R;
import com.example.easydelivery.model.Product;
import com.example.easydelivery.module.ModuleProduct;
import com.example.easydelivery.module.SendToCar;
import com.example.easydelivery.views.activities.MainActivity;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
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

public class ProductsListScreenActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private StorageReference storage;
    private GridView lisproducts;
    private Product selectProduct;
    private String id;
    private String userType;
    private FloatingActionMenu aplMenuSection;
    private FloatingActionButton aplAddProduct, aplAddProductCategory;
    private TextView aplTxtCategorySelected;
    private SharedPreferences prefs;

    private List<Product> productList = new ArrayList<Product>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);

        InicializarFirebase();
        getFirebaseData();
        setupButtons();
        hideButtons();
        setupToolbar();
        setupListView();
        listData();
    }

    // BUTTON SETTINGS (hay que configurar los botones flotantes para que presente diferentes opciones dependiendo del tipo de usuario)

    private void setupButtons() {
        aplTxtCategorySelected = findViewById(R.id.aplTxtCategorySelected);
        ////////////////////////////////////////////////
        aplMenuSection = findViewById(R.id.aplMenuSection);
        aplMenuSection.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if (opened) {
                    showButtons();
                } else {
                    hideButtons();
                }
            }
        });
        //
        aplAddProduct = findViewById(R.id.aplAddProduct);
        ////////////////////////////////////////////////
        if(userType.equals("Client")){
            aplAddProduct.hide(false); aplAddProduct.setEnabled(true);
            id = getIntent().getExtras().getString("idBusiness");
        }
        ////////////////////////////////////////////////
        aplAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCreateProduct();
            }
        });
        //
        aplAddProductCategory = findViewById(R.id.aplAddProductCategory);
        aplAddProductCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCreateCategory();
            }
        });
    }

    // TOOLBAR SETTINGS (hay que agregar entre las opciones las categorías automaticamente)

    private void setupToolbar() {
        Toolbar myToolbar =  findViewById(R.id.aplToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    // LISTVIEW SETTINGS

    private void setupListView() {
        selectProduct = new Product();
        lisproducts = findViewById(R.id.aplProductsList);
        lisproducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectProduct = (Product) parent.getItemAtPosition(position);
                if (userType.equals("Business")) {
                    startActivity(new Intent(ProductsListScreenActivity.this, ModuleProduct.class).putExtra("idporduct",selectProduct.getIdproduct()).putExtra("url",selectProduct.getUrlphoto()));
                }
                else if (userType.equals("Client")) {
                    startActivity(new Intent(ProductsListScreenActivity.this, SendToCar.class).putExtra("idporduct",selectProduct.getIdproduct()).putExtra("url",selectProduct.getUrlphoto()));
                }

            }
        });
    }

    private void hideButtons() {
        aplAddProduct.setVisibility(View.GONE);
        aplAddProductCategory.setVisibility(View.GONE);
    }

    private void showButtons() {
        aplAddProduct.setVisibility(View.VISIBLE);
        aplAddProductCategory.setVisibility(View.VISIBLE);
    }

    // GO TO ACTIVITIES

    public void goToPreviousActivity(View v) {
        finish();
    }

    public void goToHomeActivity(View v) {
        startActivity(new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public void goToCreateProduct()
    {
        aplMenuSection.toggle(true);
        startActivity(new Intent(ProductsListScreenActivity.this, ModuleProduct.class).putExtra("idporduct","").putExtra("url",""));
    }

    public void goToCreateCategory()
    {
        aplMenuSection.toggle(true);
        startActivity(new Intent(ProductsListScreenActivity.this, CreateCategoryScreenActivity.class));
    }

    // LIST DATA

    private void listData() {
        databaseReference.child("Product").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productList.clear();
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                    Product c = objSnaptshot.getValue(Product.class);
                    if (id.equals(c.getIdBuisnes())) {
                        productList.add(c);
                        AdapterProducts adapterProducts = new AdapterProducts(ProductsListScreenActivity.this, (ArrayList<Product>) productList);
                        lisproducts.setAdapter(adapterProducts);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // FIREBASE SETTINGS

    private void InicializarFirebase (){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        storage  = FirebaseStorage.getInstance().getReference("images");
    }

    private void getFirebaseData() {
        prefs = getSharedPreferences("shared_login_data",Context.MODE_PRIVATE);
        id = prefs.getString("id", "");
        userType = prefs.getString("usertype", "");
    }

    // OVERRIDE FUNCTIONS

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_products, menu);
        //MenuItem searchItem = menu.findItem(R.id.mspSearch);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getTitle().toString()){
            case "mspProductCategories":
                break;
            case "- Todas las categorías -":
                Toast.makeText(ProductsListScreenActivity.this, "Elegiste todas las categorías", Toast.LENGTH_SHORT).show();
                aplTxtCategorySelected.setText("Todas las categorías");
                break;
            default:
                aplTxtCategorySelected.setText("Categoría: " + item.getTitle().toString());
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}