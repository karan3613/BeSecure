package com.example.womensafety;

import android.content.Context;
import android.content.DialogInterface;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

public class listviewAdapter extends BaseAdapter {
  ArrayList<Model> arrcontacts ;
   Context context ;
   LayoutInflater layoutInflater;


    public listviewAdapter(ArrayList<Model> arrcontacts, Context context) {
        this.arrcontacts = arrcontacts;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arrcontacts.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = layoutInflater.inflate(R.layout.listview_design ,null);
        TextView  edit_name= (TextView)convertView.findViewById(R.id.edit_name);
        TextView  edit_number= (TextView)convertView.findViewById(R.id.edit_number);
        edit_name.setText(arrcontacts.get(position).getName());
        edit_number.setText(arrcontacts.get(position).getNumber());

        return convertView;
    }

}
