package com.example.easydelivery.views.activities.products;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.example.easydelivery.Adapter.AdapterCategory;
import com.example.easydelivery.R;
import com.example.easydelivery.helpers.FireBaseRealtime;
import com.example.easydelivery.model.Category;
import com.example.easydelivery.views.activities.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CreateCategoryScreenActivity extends AppCompatActivity {
    private EditText categoryname;
    private SharedPreferences prefs;
    private String id;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private List<Category> categoryList = new ArrayList<Category>();
    private ListView listViewCategorys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_category);

        setupFirebase();
        getFirebaseData();

        categoryname = findViewById(R.id.accTxtCategoryName);
        listViewCategorys = findViewById(R.id.accListViewCategories);

        listData();
    }

    // GO TO ACTIVITIES

    public void goToPreviousActivity(View v) {
        finish();
    }

    public void goToHomeActivity(View v) {
        startActivity(new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    // FIREBASE SETTINGS

    private void setupFirebase(){
        // firebaseDatabase.setPersistenceEnabled(true);
        firebaseDatabase = FirebaseDatabase.getInstance();
        // firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }

    private void getFirebaseData() {
        prefs = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE);
        id = prefs.getString("id", "");
    }

    // LIST DATA

    private void listData() {
        databaseReference.child("Category").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                categoryList.clear();
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                    Category category = objSnaptshot.getValue(Category.class);
                    categoryList.add(category);
                    AdapterCategory adapterCategory = new AdapterCategory(CreateCategoryScreenActivity.this, (ArrayList<Category>) categoryList);
                    listViewCategorys.setAdapter(adapterCategory);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addCategory(View view) {
        FireBaseRealtime realtime = new FireBaseRealtime();
        realtime.RegisterCategory(id, categoryname.getText().toString());
        categoryname.setText("");
    }
}