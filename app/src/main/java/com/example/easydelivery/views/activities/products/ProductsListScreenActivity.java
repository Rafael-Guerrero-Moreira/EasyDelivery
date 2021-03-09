package com.example.easydelivery.views.activities.products;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easydelivery.Adapter.AdapterCategory;
import com.example.easydelivery.Adapter.AdapterProducts;
import com.example.easydelivery.R;
import com.example.easydelivery.generallist.MyCarListScreenActivity;
import com.example.easydelivery.model.Category;
import com.example.easydelivery.model.Product;
import com.example.easydelivery.module.ModuleProduct;
import com.example.easydelivery.module.SendToCar;
import com.example.easydelivery.views.activities.MainActivity;
import com.example.easydelivery.views.activities.reports.RatingForBusiness;
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
    private FloatingActionButton aplAddProduct, aplAddProductCategory,alpViewMyCar,alpViewRating ;
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
        aplAddProduct = findViewById(R.id.aplAddProduct);
        aplAddProductCategory = findViewById(R.id.aplAddProductCategory);
        alpViewRating = findViewById(R.id.aplviewRating);
        alpViewMyCar = findViewById(R.id.aplviewMyCar);
        if(userType.equals("Client")){
            aplAddProduct.hideButtonInMenu(true);
            aplAddProductCategory.hideButtonInMenu(true);
            id = getIntent().getExtras().getString("idBusiness");
        }
        else if(userType.equals("Business")) {
            alpViewMyCar.hideButtonInMenu(true);
        }
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
        ////////////////////////////////////////////////

        ////////////////////////////////////////////////
        aplAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCreateProduct();
            }
        });
        aplAddProductCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCreateCategory();
            }
        });

        alpViewMyCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMyCar();
            }
        });
        alpViewRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoRatings();
            }
        });

    }

    private void gotoRatings() {
        startActivity(new Intent(this, RatingForBusiness.class).putExtra("idBusiness",id).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
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
                    startActivity(new Intent(ProductsListScreenActivity.this, SendToCar.class).putExtra("idporduct",selectProduct.getIdproduct())
                            .putExtra("idBusiness",selectProduct.getIdBuisnes())
                            .putExtra("ulrphoto",selectProduct.getUrlphoto()));
                }

            }
        });
    }

    private void hideButtons() {
        if(userType.equals("Client")){
            alpViewMyCar.setVisibility(View.GONE);
        }
        else if (userType.equals("Business"))
        {
            aplAddProduct.setVisibility(View.GONE);
            aplAddProductCategory.setVisibility(View.GONE);
        }
        alpViewRating.setVisibility(View.GONE);


    }

    private void showButtons() {
        if(userType.equals("Client")){
            alpViewMyCar.setVisibility(View.VISIBLE);
        }
        else if (userType.equals("Business"))
        {
            aplAddProduct.setVisibility(View.VISIBLE);
            aplAddProductCategory.setVisibility(View.VISIBLE);
        }
        alpViewRating.setVisibility(View.VISIBLE);
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
    private void goToMyCar() {
        alpViewMyCar.toggle(true);
        startActivity(new Intent(ProductsListScreenActivity.this, MyCarListScreenActivity.class));

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
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_search_products, menu);
        MenuItem categoryItem = menu.findItem(R.id.mspProductCategories);
        databaseReference.child("Category").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                    Category category = objSnaptshot.getValue(Category.class);
                    categoryItem.getSubMenu().add(category.getId()).setTitle(category.getName());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        databaseReference.child("Product").removeEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        switch (item.getTitle().toString())
        {
            case "mspProductCategories":
                break;
            case "- Todas las categorías -":
                listData();
                break;
            default:
                aplTxtCategorySelected.setText(item.getTitle().toString());
                filterdata(item.getTitle().toString());
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void filterdata(String categoryselect) {
        databaseReference.child("Product").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productList.clear();

                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                    Product c = objSnaptshot.getValue(Product.class);
                    if (id.equals(c.getIdBuisnes())&&categoryselect.equals(c.getCategory())) {
                        productList.add(c);
                    }
                }
                AdapterProducts adapterProducts = new AdapterProducts(ProductsListScreenActivity.this, (ArrayList<Product>) productList);
                lisproducts.setAdapter(adapterProducts);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}