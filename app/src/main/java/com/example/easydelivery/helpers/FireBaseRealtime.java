package com.example.easydelivery.helpers;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;


import com.example.easydelivery.model.Category;
import com.example.easydelivery.module.ModuleProduct;
import com.example.easydelivery.views.activities.auth.LoginScreenActivity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class FireBaseRealtime extends LoginScreenActivity {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public boolean RegisterCategory(String idBussiness, String categoryName){
        setupFirebase();
        Category category = new Category();
        category.setIdBusiness(idBussiness);
        category.setName(categoryName);
        category.setId(UUID.randomUUID().toString());
        databaseReference.child("Category").child(category.getIdBusiness()).child(category.getId()).setValue(category);
       // verifiExistenceCategory(category);
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
