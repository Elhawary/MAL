package com.example.bassemsarhan.mal;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.net.URI;

public class CustomGridAdapter extends BaseAdapter {

    private Context context;
    private String[] URLS;
    LayoutInflater inflater;
    private ArrayAdapter<String> movietAdapter;
    public CustomGridAdapter(Context context, String[] URLS ) {

        this.context = context;
        this.URLS = URLS;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.cell,  parent , false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
           String ss =  "https://image.tmdb.org/t/p/w500"+URLS[position];
           DisplayMetrics metrics = context.getResources().getDisplayMetrics();
           int width = metrics.widthPixels/2;
          int height = metrics.heightPixels/2;
          Picasso.with(context).load(ss).noFade().resize(width, height).into(imageView);

        return convertView;
    }

    @Override
    public int getCount() {
        return URLS.length;
    }

    @Override
    public Object getItem(int position) {
        return URLS[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}