package com.example.easydelivery.generallist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.easydelivery.Adapter.AdapterOrderDetail;
import com.example.easydelivery.R;
import com.example.easydelivery.model.Orders;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderDetail extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private List<Orders> ordersListView = new ArrayList<>();
    private String orderID;
    private ListView listView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        orderID = getIntent().getExtras().getString("idorder");
        listView = findViewById(R.id.adtOrder);
        setupFirebase();
        loadData();
    }

    private void loadData() {
             databaseReference.child("OrdersDetail/"+orderID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ordersListView.clear();
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                    Orders orderid = objSnaptshot.getValue(Orders.class);
                    ordersListView.add(orderid);
                    AdapterOrderDetail adapterOrderDetail = new AdapterOrderDetail(OrderDetail.this,(ArrayList<Orders>) ordersListView);
                    listView.setAdapter(adapterOrderDetail);

                }
            }

                 @Override
                 public void onCancelled(@NonNull DatabaseError error) {

                 }


        });
    }

    private void setupFirebase(){
        // firebaseDatabase.setPersistenceEnabled(true);
        firebaseDatabase = FirebaseDatabase.getInstance();
        // firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }
    public void goToHomeActivity(View view) {
    }

    public void goToPreviousActivity(View view) {
    }
}