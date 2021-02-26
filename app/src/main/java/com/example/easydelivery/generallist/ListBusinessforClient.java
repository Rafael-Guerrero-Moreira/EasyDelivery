package com.example.easydelivery.generallist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.easydelivery.Adapter.AdapterBusiness;
import com.example.easydelivery.R;
import com.example.easydelivery.model.Business;
import com.example.easydelivery.model.InfoBusiness;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListBusinessforClient extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Business businessSelct;
    private List<Business> businessList = new ArrayList<Business>();
    ListView listViewbusiness;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_business_for_client);
        businessSelct = new Business();
        listViewbusiness = findViewById(R.id.listViewbusiness);
        InicializarFirebase();
        ListarDatos();
        listViewbusiness.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                businessSelct = (Business) parent.getItemAtPosition(position);
                startActivity(new Intent(ListBusinessforClient.this, ListProducts.class).putExtra("idBusiness",businessSelct.getId()));
            }
        });
    }


    private void ListarDatos() {
        databaseReference.child("Business").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                businessList.clear();
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                    Business business = objSnaptshot.getValue(Business.class);
                    businessList.add(business);
                    databaseReference.child("Comerce").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                                InfoBusiness infoBusiness = objSnaptshot.getValue(InfoBusiness.class);
                                AdapterBusiness adapterBusiness = new AdapterBusiness(ListBusinessforClient.this, (ArrayList<Business>) businessList,infoBusiness.getUrllogo());
                                listViewbusiness.setAdapter(adapterBusiness);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


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
    }

}