package com.example.easydelivery.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


import com.example.easydelivery.model.GenealLoginModel;
import com.example.easydelivery.views.activities.auth.LoginScreenActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class FireBaseRealtime extends LoginScreenActivity {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;



    public void sigin(Boolean createfile, String user, String tablesUser, SharedPreferences.Editor editor)
    {
        setupFirebase();
        JSONObject object = new JSONObject();
        databaseReference.child(tablesUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                    GenealLoginModel model = objSnaptshot.getValue(GenealLoginModel.class);
                    try {
                        // se pregunta por el usuario en la bd esto por el email
                        if (user.equals(model.getEmail())) {
                            //se asigna el nuevo token
                            if(createfile) {
                                model.setToken(UUID.randomUUID().toString());
                                InternalFile filei = new InternalFile();
                                object.put("Token", model.getToken());
                                object.put("UserType",model.getType());

                                databaseReference.child(tablesUser).child(model.getId()).child("token").setValue(model.getToken());
                                filei.writeUserFile(object);
                            }
                            Log.d("MS",model.getId());
                            loginvar(model.getId(), model.getName() ,model.getEmail(), model.getType(),editor);
                            break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }
    private void setupFirebase(){
        // firebaseDatabase.setPersistenceEnabled(true);
        firebaseDatabase = FirebaseDatabase.getInstance();
        // firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }
    public void loginvar(String id, String name, String email, String usertype,SharedPreferences.Editor editor)
    {

        editor.putString("id",id);
        editor.putString("name",name);
        editor.putString("email", email);
        editor.putString("usertype", usertype);
        editor.commit();
    }
}
