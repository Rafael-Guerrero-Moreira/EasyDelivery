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
import com.example.easydelivery.model.MyCar;

import com.example.easydelivery.module.ShopActivity;

import java.util.ArrayList;

public class AdapterShopList extends ArrayAdapter<MyCar> {

    public AdapterShopList(Context context, ArrayList<MyCar> datos) {
        super(context, R.layout.layout_shop_list, datos);

    }
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.layout_shop_list, null);
        TextView name, business, price,quantity,subtotal;

        name = item.findViewById(R.id.lshTxtProductNameInShop);
        business = item.findViewById(R.id.lshTxtBusiness);
        price = item.findViewById(R.id.lshTxtProductCoste);
        quantity = item.findViewById(R.id.lshTxtProductQuantity);
        subtotal = item.findViewById(R.id.lshTxtProductSubTotal);
        name.setText(getItem(position).getProductname());
        business.setText(getItem(position).getBusinessname());
        price.setText(getItem(position).getPrice());
        quantity.setText(getItem(position).getQuantity());
        subtotal.setText(getItem(position).getSubTotal());
        return(item);
    }
}