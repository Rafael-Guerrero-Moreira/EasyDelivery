package com.example.easydelivery.module;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

import com.example.easydelivery.R;
import com.example.easydelivery.helpers.FireBaseRealtime;
import com.example.easydelivery.model.ReportProduct;
import com.example.easydelivery.views.activities.MainActivity;
import com.example.easydelivery.views.activities.auth.LoginScreenActivity;
import com.github.clans.fab.FloatingActionButton;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RatingsActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private String id;
    private String nameUser;
    private RatingBar ratingBar;
    private Button button;
    private DatabaseReference databaseReference;
    private ArrayList<String> numberStar = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase;
    private BarChart barChart;
    private ArrayList<BarEntry>  barEntry = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);
        getVariableGlogal();
        setupFirebase();
        ratingBar = findViewById(R.id.ratingBar2);
        button = findViewById(R.id.btnSendRatings);
        barChart = findViewById(R.id.barChart);
    }

    public void goToPreviousActivity(View view) {
        finish();
    }



    public void SendRatings(View view) {
        FireBaseRealtime realtime = new FireBaseRealtime();
        Float i = ratingBar.getRating();
        realtime.Ratings(id,String.valueOf(i));
        showBarChar();
    }
    private void showBarChar()
    {
        ratingBar.setVisibility(View.INVISIBLE);
        button.setVisibility(View.INVISIBLE);
        databaseReference.child("Ratings").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //   orderDatailArrayList.clear();
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                        String ratings = objSnaptshot.getValue().toString();
                    numberStar.add(ratings);
                }

                buieldDataForReport();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });

    }
    private void buieldDataForReport() {
        String [] strings = new String[numberStar.size()];
        String [] cantview = new String[numberStar.size()];
        for (int i = 0; i<numberStar.size();i++)
        {
            float cant = 0;
            String name = "";
            for (int e = 0; e<numberStar.size();e++)
            {

                if(numberStar.get(i).equals(numberStar.get(e))
                        &&verfyExistproduc( strings,numberStar.get(e)))
                {
                    name= numberStar.get(i);
                    cant++;
                }

            }
            if(!name.equals(""))
            {
                strings[i]=name;
                float v = Float.valueOf(name);
                barEntry.add(new BarEntry(v,cant));
            }

        }

        createPieChartGrafit();
    }
    private void createPieChartGrafit() {
        Description description = new Description();
        description.setText("Grafico Satisfaccion");
        barChart.setDescription(description);
        BarDataSet barDataSet = new BarDataSet(barEntry,"Text");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData barDatae = new BarData(barDataSet);
        barChart.setData(barDatae);
        barChart.invalidate();
        barChart.setVisibility(View.VISIBLE);
    }
    private boolean verfyExistproduc( String [] strings,String number ){
        try {
            for (int e = 0; e< strings.length;e++)
            {
                if(strings[e].equals(number))
                {
                    return false;
                }

            }
        }catch (Exception e){}
        return true;

    }
    private void getVariableGlogal() {
        prefs = getSharedPreferences("shared_login_data",   Context.MODE_PRIVATE);
        id = prefs.getString("id", "");
        nameUser = prefs.getString("name", "");
    }
    private void setupFirebase(){
        // firebaseDatabase.setPersistenceEnabled(true);
        firebaseDatabase = FirebaseDatabase.getInstance();
        // firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }

    public void goToHomeActivity(View view) {
        startActivity(new Intent( RatingsActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));

    }
}