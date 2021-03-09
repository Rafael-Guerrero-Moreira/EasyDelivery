package com.example.easydelivery.generallist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.easydelivery.Adapter.AdapterOrderDetail;
import com.example.easydelivery.R;
import com.example.easydelivery.helpers.FireBaseRealtime;
import com.example.easydelivery.model.Order;
import com.example.easydelivery.model.OrderDatail;
import com.example.easydelivery.views.activities.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderDetail extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private List<OrderDatail> orderDatailListView = new ArrayList<>();
    private String orderID;
    private ListView listView;
    private SharedPreferences prefs;
    private String id;
    private String usertype;
    private Button btnconfirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        orderID = getIntent().getExtras().getString("idorder");
        listView = findViewById(R.id.adtOrder);
        btnconfirm = findViewById(R.id.btnCondfir);
        setupFirebase();
        getVariableGlogal();
        userView();
        loadData();
    }


    private void loadData() {
             databaseReference.child("OrdersDetail/"+orderID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orderDatailListView.clear();
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                    OrderDatail orderid = objSnaptshot.getValue(OrderDatail.class);
                    orderDatailListView.add(orderid);
                    AdapterOrderDetail adapterOrderDetail = new AdapterOrderDetail(OrderDetail.this,(ArrayList<OrderDatail>) orderDatailListView);
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
        startActivity(new Intent( this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));

    }
    public void goToPreviousActivity(View view) {
        finish();
    }

    public void sendOrder(View view) {
       FireBaseRealtime realtime = new FireBaseRealtime();
        realtime.updateSatusOrder(orderID,usertype,OrderDetail.this);
        startActivity(new Intent( this, ListOrders.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));

    }
    private Boolean userView() {
       databaseReference.child("Orders/"+orderID+"/status").addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
             String status =  snapshot.getValue().toString();
              if(usertype.equals("Client")&&status.equals("Enviado"))
              {
                  btnconfirm.setText("Recibido");
                  btnconfirm.setEnabled(true);
              }
              else if(usertype.equals("Client")&&status.equals("Pedido Sin Entregar"))
              {
                  btnconfirm.setVisibility(View.INVISIBLE);
              }
              if (usertype.equals("Delivery"))
              {
                  btnconfirm.setVisibility(View.INVISIBLE);
              }
              if (usertype.equals("Business"))
              {
                  btnconfirm.setVisibility(View.VISIBLE);
              }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {

          }
      });


        return false;
    }
    private void getVariableGlogal() {
        prefs = getSharedPreferences("shared_login_data",   Context.MODE_PRIVATE);
        id = prefs.getString("id", "");
        usertype = prefs.getString("usertype", "");
    }
}