package com.example.easydelivery.generallist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.example.easydelivery.Adapter.AdapterBusiness;
import com.example.easydelivery.Adapter.AdapterMyCar;
import com.example.easydelivery.Adapter.AdapterOrder;
import com.example.easydelivery.R;
import com.example.easydelivery.helpers.FireBaseRealtime;
import com.example.easydelivery.model.MyCar;
import com.example.easydelivery.model.OrderView;
import com.example.easydelivery.model.Orders;
import com.example.easydelivery.model.Product;
import com.example.easydelivery.module.ModuleProduct;
import com.example.easydelivery.views.activities.products.ProductsListScreenActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListOrders extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private   ArrayList<Product> productsList = new ArrayList<>();
    private List<String> orderList = new ArrayList<>();
    private List<OrderView> ordersListView = new ArrayList<>();
    private SharedPreferences prefs;
    private String id;
    private ListView listorder;
    private OrderView ordersSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_orders);
        listorder = findViewById(R.id.aplOrdersList);
        setupFirebase();
        getVariableGlogal();
        //resultQueryProduct();
        resultQueryOrders();
        listorder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ordersSelect = (OrderView) parent.getItemAtPosition(position);
                startActivity(new Intent(ListOrders.this, OrderDetail.class)
                        .putExtra("idorder",ordersSelect.getIdOrder()));

            }
        });
    }

    public void resultQueryProduct()
    {
        setupFirebase();
        databaseReference.child("Product").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                    Product product = objSnaptshot.getValue(Product.class);
                    if (product.getIdBuisnes().equals(id))
                        productsList.add(product);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void setupFirebase(){
        // firebaseDatabase.setPersistenceEnabled(true);
        firebaseDatabase = FirebaseDatabase.getInstance();
        // firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }
    public void resultQueryOrders()
    {
        databaseReference.child("Orders").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ordersListView.clear();
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                    OrderView order = objSnaptshot.getValue(OrderView.class);
                    ordersListView.add(order);
                    AdapterOrder adapterOrder =  new AdapterOrder(ListOrders.this, (ArrayList<OrderView>) ordersListView);
                    listorder.setAdapter(adapterOrder);
                    //String orderid = objSnaptshot.getKey();
                   // orderList.add(orderid);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void getVariableGlogal() {
        prefs = getSharedPreferences("shared_login_data",   Context.MODE_PRIVATE);
        id = prefs.getString("id", "");
    }

    public void refesh(View view) {
        for(String idor: orderList) {
            databaseReference.child("Orders/"+idor).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ordersListView.clear();
                    for (DataSnapshot objSnaptshot : snapshot.getChildren()) {
                        OrderView order = objSnaptshot.getValue(OrderView.class);
                        ordersListView.add(order);
                        AdapterOrder adapterOrder =  new AdapterOrder(ListOrders.this, (ArrayList<OrderView>) ordersListView);
                        listorder.setAdapter(adapterOrder);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            }

    }

    public void goToHomeActivity(View view) {
    }

    public void goToPreviousActivity(View view) {
    }
}