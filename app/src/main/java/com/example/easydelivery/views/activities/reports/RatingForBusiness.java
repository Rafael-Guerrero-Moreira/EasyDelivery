package com.example.easydelivery.views.activities.reports;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Rating;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.easydelivery.Adapter.AdapterOrder;
import com.example.easydelivery.Adapter.AdapterRating;
import com.example.easydelivery.R;
import com.example.easydelivery.helpers.FireBaseRealtime;
import com.example.easydelivery.model.Business;
import com.example.easydelivery.model.RatingBusiness;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RatingForBusiness extends AppCompatActivity {

    private SharedPreferences prefs;
    private String idUser;
    private String userType;
    private EditText coments;
    private RatingBar ratingBar;
    private String idBusiness;
    private String name;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<RatingBusiness> businessArrayList = new ArrayList<>();
    private ListView listViewComets;
    private TextInputLayout inputLayout;
    private Button btnSendRatingsBusiness;
    private PieChart pieChartRatings;
    private TextView textViewmsg;
    private ArrayList<PieEntry> pieEntries = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_for_business);
        instanceVariable();
        setupFirebase();
        instanceComponents();
        datacoments();
    }
    private void datacoments() {
        databaseReference.child("RatingsBusiness").child(idBusiness).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                businessArrayList.clear();
                for (DataSnapshot objSnaptshot : snapshot.getChildren())
                {
                    RatingBusiness business = objSnaptshot.getValue(RatingBusiness.class);
                    businessArrayList.add(business);
                    AdapterRating adapterRating = new AdapterRating(RatingForBusiness.this,(ArrayList<RatingBusiness>)businessArrayList);
                    listViewComets.setAdapter(adapterRating);

                }
                buildDataForReport();
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
    private void instanceComponents() {
            coments = findViewById(R.id.txtcoment);
            inputLayout = findViewById(R.id.txtcontentComents);
            ratingBar = findViewById(R.id.rBarBusiness);
            btnSendRatingsBusiness = findViewById(R.id.btnSendRatingsBusiness);
            listViewComets = findViewById(R.id.listComents);
            textViewmsg = findViewById(R.id.tvMessage);
        pieChartRatings = findViewById(R.id.PieChartRatings);
        if(userType.equals("Client"))
        {
            inputLayout.setVisibility(View.VISIBLE);
            ratingBar.setVisibility(View.VISIBLE);
            btnSendRatingsBusiness.setVisibility(View.VISIBLE);
        }
        else if(userType.equals("Business"))
        {
            coments.setVisibility(View.GONE);
            ratingBar.setVisibility(View.GONE);
            btnSendRatingsBusiness.setVisibility(View.GONE);
            textViewmsg.setText("Aceptacion ante el publico");
        }

    }


    private void instanceVariable() {
        prefs = getSharedPreferences("shared_login_data",   Context.MODE_PRIVATE);
        idUser = prefs.getString("id", "");
        userType = prefs.getString("usertype", "");
        name = prefs.getString("name", "");
        idBusiness = getIntent().getExtras().getString("idBusiness");
    }


    public void goToHomeActivity(View view) {
    }

    public void goToPreviousActivity(View view) {
    }
    private void buildDataForReport() {
        String [] strings = new String[businessArrayList.size()];
        for (int i = 0; i<businessArrayList.size();i++)
        {
            int cant = 0;
            String name = "";
            for (int e = 0; e<businessArrayList.size();e++)
            {

                if(businessArrayList.get(i).getRating().equals(businessArrayList.get(e).getRating())
                        &&verfyExistproduc( strings,businessArrayList.get(e).getRating()))
                {
                    name= businessArrayList.get(i).getRating();
                    cant++;
                }

            }
            strings[i]=name;
            pieEntries.add( new PieEntry(cant,name));
        }
        createPieChartGrafit();
    }
    private boolean verfyExistproduc( String [] strings,String ratings ){
        try {
            for (int e = 0; e< strings.length;e++)
            {
                if(strings[e].equals(ratings))
                {
                    return false;
                }

            }
        }catch (Exception e){}
        return true;

    }
    private void createPieChartGrafit() {
        Description description = new Description();
        description.setText("Estadisticas de Aceptacion");
        pieChartRatings.setDescription(description);
        PieDataSet pieDataSet = new PieDataSet(pieEntries,"Valores");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData pieData = new PieData(pieDataSet);
        pieChartRatings.setData(pieData);
    }

    public void SendRatings(View view) {
        RatingBusiness business = new RatingBusiness();
        business.setIdClient(idUser);
        business.setIdBusiness(idBusiness);
        business.setClientName(name);
        business.setComents(coments.getText().toString());
        business.setRating(String.valueOf(ratingBar.getRating()));
        FireBaseRealtime realtime = new FireBaseRealtime();
        realtime.RatingForBusiness(business,this);
    }
}