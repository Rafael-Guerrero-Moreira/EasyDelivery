package com.example.easydelivery.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.easydelivery.R;

import org.json.JSONArray;
import org.json.JSONException;

public class DashboardOptionsAdapter extends BaseAdapter {
    private Context context;
    private JSONArray optionsList;
    private LayoutInflater inflater;

    public DashboardOptionsAdapter(Context applicationContext, JSONArray list) {
        this.context = applicationContext;
        this.optionsList = list;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return optionsList.length();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.layout_dashboard_options, null);
        TextView txtTitle = view.findViewById(R.id.ldoTextViewTitle);
        ImageView image = (ImageView) view.findViewById(R.id.ldoImageView);
        try {
            Log.d("Taggggg", optionsList.getJSONObject(i).getString("title"));
            txtTitle.setText(optionsList.getJSONObject(i).getString("title"));
            Glide.with(context).load(optionsList.getJSONObject(i).getString("image")).into(image);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }
}
