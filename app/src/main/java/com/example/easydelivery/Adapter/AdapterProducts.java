package com.example.easydelivery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.easydelivery.model.Product;
import com.example.easydelivery.R;

import java.util.ArrayList;

public class AdapterProducts extends ArrayAdapter<Product> {
    public AdapterProducts(Context context, ArrayList<Product> datos) {
        super(context, R.layout.layout_products_list, datos);
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.layout_products_list, null);
        TextView Name, description,price;
         ImageView img;
        Name = item.findViewById(R.id.lplTxtProductName);
        img = item.findViewById(R.id.lplImageViewProduct);
        description = item.findViewById(R.id.lplTxtProductDescription);
        price = item.findViewById(R.id.lplTxtProductPrice);
        Name.setText(getItem(position).getProductname());
        description.setText(getItem(position).getDescription());
        price.setText("$ " + getItem(position).getPrice());
        Glide.with(getContext()).load(getItem(position).getUrlphoto()).into(img);
        return(item);
    }
}

