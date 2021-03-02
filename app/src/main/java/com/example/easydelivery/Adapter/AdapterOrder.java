package com.example.easydelivery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.easydelivery.R;
import com.example.easydelivery.model.OrderView;

import java.util.ArrayList;

public class AdapterOrder  extends ArrayAdapter<OrderView>  {
    public AdapterOrder(Context context, ArrayList<OrderView> datos) {
        super(context, R.layout.layout_orders, datos);

    }
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.layout_orders, null);
        TextView name, deliver,date,status;

        name = item.findViewById(R.id.lorTxtClientorder);
        deliver = item.findViewById(R.id.lorTxtDeliver);
       // price = item.findViewById(R.id.);
        date = item.findViewById(R.id.lorTxtDate);
        status = item.findViewById(R.id.lorTxtStatus);
        name.setText(getItem(position).getClientName());
        deliver.setText(getItem(position).getDeliveryname());
        date.setText(getItem(position).getDate());
     //   price.setText(getItem(position).getPrice());
        status.setText(getItem(position).getStatus());
        return(item);
    }
}
