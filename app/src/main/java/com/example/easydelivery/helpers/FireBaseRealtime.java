package com.example.easydelivery.helpers;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;


import androidx.annotation.NonNull;

import com.example.easydelivery.model.Category;
import com.example.easydelivery.model.InfoBusiness;
import com.example.easydelivery.model.MyCar;
import com.example.easydelivery.model.Orders;
import com.example.easydelivery.model.Product;
import com.example.easydelivery.views.activities.auth.LoginScreenActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

public class FireBaseRealtime extends LoginScreenActivity {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public boolean RegisterCategory(String idBussiness, String categoryName) {
        setupFirebase();
        Category category = new Category();
        category.setIdBusiness(idBussiness);
        category.setName(categoryName);
        category.setId(UUID.randomUUID().toString());
        databaseReference.child("Category").child(category.getIdBusiness()).child(category.getId()).setValue(category);
        // verifiExistenceCategory(category);
        return true;
    }

    public boolean RegisterInfoBusiness(InfoBusiness infoBusiness, Context context) {
        setupFirebase();
        databaseReference.child("Comerce").child(infoBusiness.getIdBuissnes()).setValue(infoBusiness).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    databaseReference.child("Business").child(infoBusiness.getIdBuissnes()).child("urlLogo").setValue(infoBusiness.getUrllogo());
                    Toast.makeText(context, "Datos Guardados", Toast.LENGTH_LONG).show();
                }
            }
        });
        return true;
    }

    public boolean RegisterMycar(MyCar myCar , Activity activity) {
        setupFirebase();
        databaseReference.child("MyCar").child(myCar.getIdClient()).child(myCar.getId()).setValue(myCar).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(activity, "Agregado a mi carrito ", Toast.LENGTH_LONG).show();
                    activity.finish();
                } else {
                    Toast.makeText(activity, "Error al  Guardar datos", Toast.LENGTH_LONG).show();
                }
            }
        });
        return true;
    }
    public boolean registerShop(Orders orders , Activity activity) {
        setupFirebase();
        databaseReference.child("OrdersDetail").child(orders.getIdOrder()).child(orders.getIdProduct()).setValue(orders);
        databaseReference.child("Orders").child(orders.getIdOrder()).child("idOrder").setValue(orders.getIdOrder());
        databaseReference.child("Orders").child(orders.getIdOrder()).child("idClient").setValue(orders.getIdClient());
        databaseReference.child("Orders").child(orders.getIdOrder()).child("clientName").setValue(orders.getClientName());
        databaseReference.child("Orders").child(orders.getIdOrder()).child("idDelivery").setValue(orders.getIdDelivery());
        databaseReference.child("Orders").child(orders.getIdOrder()).child("deliveryname").setValue(orders.getBusinessname());
        databaseReference.child("Orders").child(orders.getIdOrder()).child("idBusiness").setValue(orders.getIdBusiness());
        databaseReference.child("Orders").child(orders.getIdOrder()).child("businessname").setValue(orders.getBusinessname());
        databaseReference.child("Orders").child(orders.getIdOrder()).child("date").setValue(orders.getDate());
        databaseReference.child("Orders").child(orders.getIdOrder()).child("status").setValue(orders.getEstado());
        return true;
    }



  /*  private void verifiExistenceCategory(Category categoryadd )
    {
        databaseReference.child("Category").child(categoryadd.getIdBusiness()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                        Category category = objSnaptshot.getValue(Category.class);
                       if(category.getName().equals(categoryadd.getName()))
                       {
                           Toast.makeText(FireBaseRealtime.this, "Categoria Ya existe", Toast.LENGTH_LONG).show();
                       }
                       else {
                           Toast.makeText(FireBaseRealtime.this, "Categoria Agregada" , Toast.LENGTH_LONG).show();
                       }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(FireBaseRealtime.this, "Error" , Toast.LENGTH_LONG).show();

                }
            });


    }*/
    private void setupFirebase(){
        // firebaseDatabase.setPersistenceEnabled(true);
        firebaseDatabase = FirebaseDatabase.getInstance();
        // firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }

}
