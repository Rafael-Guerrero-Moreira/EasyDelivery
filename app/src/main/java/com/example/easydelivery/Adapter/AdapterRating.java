package com.example.easydelivery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.easydelivery.R;
import com.example.easydelivery.model.Category;
import com.example.easydelivery.model.RatingBusiness;

import java.util.ArrayList;

public class AdapterRating extends ArrayAdapter<RatingBusiness> {
    public AdapterRating(Context context, ArrayList<RatingBusiness> datos) {
        super(context, R.layout.layout_comentsclient, datos);
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.layout_comentsclient, null);
        TextView name,coments;
        RatingBar start;

        name = item.findViewById(R.id.lnameClient);
        coments = item.findViewById(R.id.lComents);
        start = item.findViewById(R.id.rBarView);
        name.setText(getItem(position).getClientName());
        coments.setText(getItem(position).getComents());
        start.setRating(Float.valueOf(getItem(position).getRating()));
        return(item);
    }
}
