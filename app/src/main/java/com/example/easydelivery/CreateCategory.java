package com.example.easydelivery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.easydelivery.Adapter.AdapterBusiness;
import com.example.easydelivery.Adapter.AdapterCategory;
import com.example.easydelivery.generallist.ListBusinessforClient;
import com.example.easydelivery.helpers.FireBaseRealtime;
import com.example.easydelivery.model.Business;
import com.example.easydelivery.model.Category;
import com.example.easydelivery.model.InfoBusiness;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CreateCategory extends AppCompatActivity {
    private EditText categoryname;
    private SharedPreferences prefs;
    private String id;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private List<Category> categoryList = new ArrayList<Category>();
    ListView listViewCategorys;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_category);
        categoryname = findViewById(R.id.editTextCategoryName);
        listViewCategorys = findViewById(R.id.listViewCategory);
        prefs = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE);
        id = prefs.getString("id", "");
        setupFirebase();
        ListarDatos();
    }

    public void addCaterogy(View view) {
        FireBaseRealtime realtime = new FireBaseRealtime();
        realtime.RegisterCategory(id,categoryname.getText().toString());
    }
    private void setupFirebase(){
        // firebaseDatabase.setPersistenceEnabled(true);
        firebaseDatabase = FirebaseDatabase.getInstance();
        // firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }
    private void ListarDatos() {
        databaseReference.child("Category").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                categoryList.clear();
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                    Category category = objSnaptshot.getValue(Category.class);
                    categoryList.add(category);
                    AdapterCategory adapterCategory = new AdapterCategory(CreateCategory.this, (ArrayList<Category>) categoryList);
                    listViewCategorys.setAdapter(adapterCategory);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}