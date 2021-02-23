package com.example.easydelivery.views.activities.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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

    public Dashboard() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        prefs = getActivity().getSharedPreferences("shared_login_data", getContext().MODE_PRIVATE);
        String usertype = prefs.getString("usertype", "");
        optionsList = MenuOptions.getOptions(usertype);
        gridView = view.findViewById(R.id.amGridView);
        DashboardOptionsAdapter customAdapter = new DashboardOptionsAdapter(view.getContext(), optionsList);
        gridView.setAdapter(customAdapter);
        return view;
    }
}
