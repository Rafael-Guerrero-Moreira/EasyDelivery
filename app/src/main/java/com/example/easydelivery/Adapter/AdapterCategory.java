package com.example.easydelivery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.easydelivery.R;
import com.example.easydelivery.model.Category;

import java.util.ArrayList;

public class AdapterCategory extends ArrayAdapter<Category> {
    public AdapterCategory(Context context, ArrayList<Category> datos) {
        super(context, R.layout.category_list, datos);
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.category_list, null);
        TextView Name;

        Name = item.findViewById(R.id.textViewCategoryName);
        Name.setText(getItem(position).getName());
        return(item);
    }
}