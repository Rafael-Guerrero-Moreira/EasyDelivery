package com.example.easydelivery.module;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.easydelivery.R;
import com.example.easydelivery.helpers.FireBaseRealtime;
import com.example.easydelivery.model.MyCar;
import com.example.easydelivery.model.Product;
import com.example.easydelivery.views.activities.MainActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class SendToCar extends AppCompatActivity {

    private String idproduct;
    private String idBusiness;
    private SharedPreferences prefs;
    private String id;
    private String urlFoto;
    private TextView etname,etprice,etdescription,etcategory;
    private ImageView ivPoduct;
    private NumberPicker pikerQuantity;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_to_car);
        getVariable();
        instanceCompentsView();
        starUpFirebase ();
        setValueForProduct();

    }

    private void setValueForProduct() {
        databaseReference.child("Product").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                    Product product = objSnaptshot.getValue(Product.class);
                    if (idBusiness.equals(product.getIdBuisnes())&&idproduct.equals(product.getIdproduct())) {
                       etname.setText(product.getProductname());
                       etdescription.setText(product.getDescription());
                       etprice.setText(product.getPrice());
                       etcategory.setText(product.getCategory());
                       Glide.with(SendToCar.this).load(product.getUrlphoto()).into(ivPoduct);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void instanceCompentsView() {
        etname = findViewById(R.id.idEditableName);
        etdescription = findViewById(R.id.idEditableDescription);
        etprice = findViewById(R.id.idEditablePrice);
        etcategory = findViewById(R.id.idEditableCaregory);
        ivPoduct = findViewById(R.id.imvPhotoProduct);
        pikerQuantity = findViewById(R.id.npQuantity);
        pikerQuantity.setMaxValue(20);
        pikerQuantity.setMinValue(0);
    }

    private void getVariable(){
        prefs = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE);
        id = prefs.getString("id", "");
        name = prefs.getString("name", "");
        idproduct =  getIntent().getExtras().getString("idporduct","");
        idBusiness =  getIntent().getExtras().getString("idBusiness","");
        urlFoto = getIntent().getExtras().getString("ulrphoto","");
    }
    private void starUpFirebase (){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    public void onclickSendCar(View view) {
        MyCar myCar = new MyCar();
        myCar.setId(UUID.randomUUID().toString());
        myCar.setIdClient(id);
        myCar.setClientName(name);
        myCar.setIdBusiness(idBusiness);
        myCar.setBusinessname("StandBy");
        myCar.setIdProduct(idproduct);
        myCar.setProductname(etname.getText().toString());
        myCar.setPhotoProduct(urlFoto);
        myCar.setQuantity(String.valueOf(pikerQuantity.getValue()));
        myCar.setPrice(etprice.getText().toString());
        double sb = pikerQuantity.getValue() * Double.parseDouble(etprice.getText().toString());
        myCar.setSubTotal(String.valueOf(sb));
        FireBaseRealtime realtime = new FireBaseRealtime();
        realtime.RegisterMycar(myCar,SendToCar.this);
    }

    public void goToPreviousActivity(View view) {
        finish();
    }

    public void goToHomeActivity(View view) {
        startActivity(new Intent( this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));

    }
}