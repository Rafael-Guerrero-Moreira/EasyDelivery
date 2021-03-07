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
import com.example.easydelivery.model.OrderDatail;

import java.util.ArrayList;

public class AdapterOrderDetail  extends ArrayAdapter<OrderDatail> {
        public AdapterOrderDetail(Context context, ArrayList<OrderDatail> datos) {
            super(context, R.layout.layout_order_detail_list, datos);

        }
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View item = inflater.inflate(R.layout.layout_order_detail_list, null);
            TextView name,price,quantity,subtotal;
            ImageView ivproduct;

            ivproduct = item.findViewById(R.id.lodlImageViewProduct);
            name = item.findViewById(R.id.lodTxtProductName);
            price = item.findViewById(R.id.lodTxtProductCoste);
            quantity = item.findViewById(R.id.lodTxtProductQuantity);
            subtotal = item.findViewById(R.id.lodTxtSbtotal);

            name.setText("Producto: "+getItem(position).getProductname());
            price.setText("P.U.: $ "+getItem(position).getPrice());
            quantity.setText("Cantidad: "+getItem(position).getQuantity());
            subtotal.setText("Subtotal: "+getItem(position).getSubTotal());
            Glide.with(getContext()).load(getItem(position).getPhotoProduct()).into(ivproduct);

            return(item);
        }
    }

