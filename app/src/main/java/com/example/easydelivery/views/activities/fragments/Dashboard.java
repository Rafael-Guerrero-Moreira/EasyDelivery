package com.example.easydelivery.views.activities.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.fragment.app.Fragment;

import com.example.easydelivery.Adapter.DashboardOptionsAdapter;
import com.example.easydelivery.R;
import com.example.easydelivery.menu.MenuOptions;

import org.json.JSONArray;


public class Dashboard extends Fragment {
    private GridView gridView;
    private SharedPreferences prefs;
    private JSONArray optionsList;
    private String usertype;
    public Dashboard() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        prefs = getActivity().getSharedPreferences("shared_login_data", getContext().MODE_PRIVATE);
        usertype = prefs.getString("usertype", "");
        Log.d("Type",usertype);
        optionsList = MenuOptions.getOptions(usertype);
        gridView = view.findViewById(R.id.amGridView);
        DashboardOptionsAdapter customAdapter = new DashboardOptionsAdapter(view.getContext(), optionsList);
        gridView.setAdapter(customAdapter);
        /*gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (usertype){
                    case "Business":
                        StarActivityforBusiness( position);
                        break;
                    case "Client":
                        StarActivityforClient(position);
                        break;
                }



            }
        });*/
        return view;
    }
   /*private void StarActivityforBusiness(int position){
        switch (position){
            case 0:
                startActivity(new Intent(getContext(), ProductsListScreenActivity.class));
                break;
            case 1:
                Toast.makeText(getContext(), String.valueOf(position) , Toast.LENGTH_LONG).show();
                break;
        }
   }
    private void StarActivityforClient(int position){
        switch (position){
            case 0:
                startActivity(new Intent(getContext(), ListBusinessforClient.class));
                break;
            case 1:
                Toast.makeText(getContext(), String.valueOf(position) , Toast.LENGTH_LONG).show();
                break;
        }
    }*/
}
