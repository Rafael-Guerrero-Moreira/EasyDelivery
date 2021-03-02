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
import com.example.easydelivery.model.MyCar;

import java.util.ArrayList;

public class AdapterMyCar extends ArrayAdapter<MyCar> {
    public AdapterMyCar(Context context, ArrayList<MyCar> datos) {
        super(context, R.layout.layout_my_car_list, datos);
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.layout_my_car_list, null);
        TextView Name, business,price;
        ImageView img;
        Name = item.findViewById(R.id.lplTxtProductNameInCar);
        img = item.findViewById(R.id.lmclImageViewProductInCar);
        business = item.findViewById(R.id.lmcTxtBusiness );
        price = item.findViewById(R.id.lmcTxtProductCoste);
        Name.setText(getItem(position).getProductname());
        business.setText(getItem(position).getBusinessname());
        price.setText("$ " + getItem(position).getPrice());
        Glide.with(getContext()).load(getItem(position).getPhotoProduct()).into(img);
        return(item);
    }
}
