package com.example.easydelivery.views.activities.reports;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.easydelivery.Adapter.AdapterOrderDetail;
import com.example.easydelivery.R;
import com.example.easydelivery.generallist.OrderDetail;
import com.example.easydelivery.model.OrderDatail;
import com.example.easydelivery.model.ReportProduct;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReportPie extends AppCompatActivity {
    private PieChart pieChart;
    private ArrayList<PieEntry> pieEntries = new ArrayList<>();
    private ArrayList <ReportProduct> productIdList = new ArrayList<>();
    private ArrayList <ReportProduct> productListForReport = new ArrayList<>();
   // private ArrayList<OrderDatail> orderDatailArrayList = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_pie);
        pieChart = findViewById(R.id.PieChart);
       // createPieChartGrafit();
        setupFirebase();
        loadData();
    }

    private void createPieChartGrafit() {
        Description description = new Description();
        description.setText("Grafico Productos");
        pieChart.setDescription(description);
        PieDataSet pieDataSet = new PieDataSet(pieEntries,"Txto desc");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
    }
    private void loadData() {
        databaseReference.child("OrdersDetail").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
             //   orderDatailArrayList.clear();
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot snapshot : objSnaptshot.getChildren()) {
                        ReportProduct report = snapshot.getValue(ReportProduct.class);
                        productIdList.add(report);


                    }

                 //   orderDatailArrayList.add(orderid);

                }

                buieldDataForReport();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
    }

    private void buieldDataForReport() {
      String [] strings = new String[productIdList.size()];
        for (int i = 0; i<productIdList.size();i++)
        {
            int cant = 0;
            String name = "";
            for (int e = 0; e<productIdList.size();e++)
            {

                if(productIdList.get(i).getIdProduct().equals(productIdList.get(e).getIdProduct())
                        &&verfyExistproduc( strings,productIdList.get(e).getProductname()))
                {
                    name= productIdList.get(i).getProductname();
                    cant+= Integer.valueOf( productIdList.get(i).getQuantity());
                }

            }
            strings[i]=name;
            pieEntries.add( new PieEntry(cant,name));
        }
        createPieChartGrafit();
    }
    private boolean verfyExistproduc( String [] strings,String nameProduct ){
       try {
           for (int e = 0; e< strings.length;e++)
           {
               if(strings[e].equals(nameProduct))
               {
                   return false;
               }

           }
       }catch (Exception e){}
        return true;

    }

    private void setupFirebase(){
        // firebaseDatabase.setPersistenceEnabled(true);
        firebaseDatabase = FirebaseDatabase.getInstance();
        // firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }
}