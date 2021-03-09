package com.example.easydelivery.generallist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.easydelivery.Adapter.AdapterMyCar;
import com.example.easydelivery.Adapter.AdapterProducts;
import com.example.easydelivery.R;
import com.example.easydelivery.model.MyCar;
import com.example.easydelivery.model.Product;
import com.example.easydelivery.module.ModuleProduct;
import com.example.easydelivery.module.ShopActivity;
import com.example.easydelivery.views.activities.MainActivity;
import com.example.easydelivery.views.activities.products.ProductsListScreenActivity;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class MyCarListScreenActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    private ListView mycarList;

    private List<MyCar> myCarList = new ArrayList<MyCar>();
    private SharedPreferences prefs;
    private String id;
    private FloatingActionButton amclAddShop;
    private FloatingActionMenu aplMenuSection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_car_list_screen);
        starUpVariable();
        setupButtons();
        hideButtons();
        getFirebaseData();
        InicializarFirebase();
        listData();

    }

    private void setupButtons() {

        amclAddShop = findViewById(R.id.amclstore);
        ////////////////////////////////////////////////
        aplMenuSection = findViewById(R.id.amclMenuSection);
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
        amclAddShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToShop();
            }
        });


    }

    private void goToShop() {
        aplMenuSection.toggle(true);
        startActivity(new Intent(MyCarListScreenActivity.this, ShopActivity.class));
    }

    private void showButtons() {
        amclAddShop.setVisibility(View.VISIBLE);
    }
    private void hideButtons() {
        amclAddShop.setVisibility(View.GONE);
    }
    private void starUpVariable() {
        mycarList = findViewById(R.id.amclmycarList);
    }

    private void listData() {
        databaseReference.child("MyCar").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myCarList.clear();
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                    MyCar c = objSnaptshot.getValue(MyCar.class);
                    myCarList.add(c);
                    AdapterMyCar adapterProducts = new AdapterMyCar(MyCarListScreenActivity.this, (ArrayList<MyCar>) myCarList);
                    mycarList.setAdapter(adapterProducts);
                    }
                }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void goToHomeActivity(View view) {
        startActivity(new Intent( this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));

    }
    public void goToPreviousActivity(View view) {
        finish();
    }
    private void InicializarFirebase (){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
    private void getFirebaseData() {
        prefs = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE);
        id = prefs.getString("id", "");
    }


}