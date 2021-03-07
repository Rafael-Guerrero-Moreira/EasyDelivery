package com.example.easydelivery.module;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.easydelivery.Adapter.AdapterShopList;
import com.example.easydelivery.R;

import com.example.easydelivery.helpers.FireBaseRealtime;
import com.example.easydelivery.model.Delivery;
import com.example.easydelivery.model.MyCar;
import com.example.easydelivery.model.Order;
import com.example.easydelivery.model.OrderDatail;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class ShopActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private SharedPreferences prefs;
    private String id;
    private ArrayList<Delivery> deliveryArrayList = new ArrayList<Delivery>();
    private ArrayList<MyCar> myCarList = new ArrayList<MyCar>();
    private ArrayList<OrderDatail> orderDatailList = new ArrayList<OrderDatail>();
    private Spinner spinner;
    private FirebaseDatabase firebaseDatabase;
    String [] deliveryVector;
    private String  nameUser;
    private ListView listViewShop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        getVariableGlogal();
        starFirebase();
        starComponets();
        listDataForCar();
        seterSpinerCategory();
    }

    private void listDataForCar() {
        databaseReference.child("MyCar").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myCarList.clear();
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                    MyCar c = objSnaptshot.getValue(MyCar.class);
                    myCarList.add(c);
                    AdapterShopList adapterProducts = new AdapterShopList(ShopActivity.this, (ArrayList<MyCar>) myCarList);
                    listViewShop.setAdapter(adapterProducts);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void starFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void starComponets() {
        spinner = findViewById(R.id.ashSelectDeliver);
        listViewShop = findViewById(R.id.ashlproductList);
    }

    private void getVariableGlogal() {
        prefs = getSharedPreferences("shared_login_data",   Context.MODE_PRIVATE);
        id = prefs.getString("id", "");
        nameUser = prefs.getString("name", "");
    }

    private void seterSpinerCategory()
    {
        databaseReference.child("Delivery").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                deliveryArrayList.clear();
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                    Delivery delivery = objSnaptshot.getValue(Delivery.class);
                    deliveryArrayList.add(delivery);
                }
                deliveryVector = new String[deliveryArrayList.size()];
                for (int i=0; i<deliveryVector.length;i++){
                    deliveryVector[i] = String.valueOf(deliveryArrayList.get(i).getName());
                }
                spinner.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,deliveryVector));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void goToHomeActivity(View view) {
    }

    public void goToPreviousActivity(View view) {
    }

    public void addOrders(View view) {
        String key =  databaseReference.push().getKey();
        String nameDeliver = spinner.getSelectedItem().toString();
        String dateAndTime =  obtenerFechaConFormato("yyyy-MM-dd","GMT-5")+":"+ obtenerFechaConFormato("HH:mm:ss","GMT-5");
        String idDeliver = deliveryArrayList.get(spinner.getSelectedItemPosition()).getId();
        Order order = new Order();
        order.setIdOrder(key);
        order.setStatus("Pedido Sin Entregar");
        for (int i = 0;i<myCarList.size();i++)
        {
            OrderDatail orderDatail = new OrderDatail();
            orderDatail.setIdProduct(myCarList.get(i).getIdProduct());
            orderDatail.setProductname(myCarList.get(i).getProductname());
            orderDatail.setPhotoProduct(myCarList.get(i).getPhotoProduct());

            order.setIdBusiness(myCarList.get(i).getIdBusiness());
            order.setBusinessname(myCarList.get(i).getBusinessname());

            order.setIdClient(myCarList.get(i).getIdClient());
            order.setClientName(myCarList.get(i).getClientName());

            orderDatail.setPrice(myCarList.get(i).getPrice());
            orderDatail.setQuantity(myCarList.get(i).getQuantity());
            orderDatail.setSubTotal(myCarList.get(i).getSubTotal());


            orderDatail.setIdOrder(key);
            order.setIdOrder(key);
            order.setDeliveryname(nameDeliver);
            order.setIdDelivery(idDeliver);
            order.setDate(dateAndTime);
            orderDatailList.add(orderDatail);
        }
        FireBaseRealtime realtime = new FireBaseRealtime();

        for (int i = 0; i< orderDatailList.size(); i++) {
            realtime.registerShop(orderDatailList.get(i),order, id,this);
        }
    }
    @SuppressLint("SimpleDateFormat")
    public static String obtenerFechaConFormato(String formato, String zonaHoraria) {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat(formato);
        sdf.setTimeZone(TimeZone.getTimeZone(zonaHoraria));
        return sdf.format(date);
    }
}