package com.example.easydelivery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.easydelivery.R;
import com.example.easydelivery.model.Business;

import java.util.ArrayList;

public class AdapterBusiness extends ArrayAdapter<Business> {

    public AdapterBusiness(Context context, ArrayList<Business> datos) {
        super(context, R.layout.business_list, datos);

    }
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.business_list, null);
        TextView Name;
        ImageView img;
        Name = item.findViewById(R.id.textnameBusiness);
        img = item.findViewById(R.id.imbusiness);
        Name.setText(getItem(position).getName());
        Glide.with(getContext()).load(getItem(position).getUrlLogo()).into(img);
        return(item);
    }
}

