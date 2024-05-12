package com.example.womensafety;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ContactBook extends AppCompatActivity {
ListView listView;
Button btn ;
ArrayList<Model> arrcontacts = new ArrayList<>() ;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_bokk);
        listView = findViewById( R.id.listView);

        // ACTION BAR
        ActionBar actionBar ;
        actionBar =getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#F03333"));

        actionBar.setBackgroundDrawable(colorDrawable);

        btn = findViewById(R.id.add);
        DATABASECLASS databaseclass = Room.databaseBuilder(
                        getApplicationContext(),
                        DATABASECLASS.class,
                        "ContactDB")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
// ADD button DIALOG BOX

        arrcontacts.addAll(databaseclass.getDAO().getContacts());
        listviewAdapter listviewAdapter = new listviewAdapter(arrcontacts ,this);
        listView.setAdapter(listviewAdapter);
        // LISTVIEW SET ON ITEM CLICK LISTENER
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position , long id) {
                Model m = arrcontacts.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(ContactBook.this );
                builder.setTitle("DELETE Contacts");
                builder.setMessage("DO you want to delete the contacts");
                builder.setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ContactBook.this, "YES", Toast.LENGTH_SHORT).show();
                        databaseclass.getDAO().deleteContact(m);
                        arrcontacts.remove(position);
                        listviewAdapter.notifyDataSetChanged();
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ContactBook.this, "NO", Toast.LENGTH_SHORT).show();
                    }
                });
                final  AlertDialog alertDialog = builder.create();
                alertDialog.show();

                return false;
            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Toast.makeText(ContactBook.this, "runnung", Toast.LENGTH_SHORT).show();

               View view = LayoutInflater.from(ContactBook.this).inflate(R.layout.alert_box ,null);

               AlertDialog.Builder builder = new AlertDialog.Builder(ContactBook.this);
               builder.setView(view);
               EditText Update_name = view.findViewById(R.id.edit_name);
               EditText Update_number = view.findViewById(R.id.edit_number);

               builder.setTitle("Add Name/No");
               builder.setIcon(R.drawable.add_contact_icon);
               builder.setCancelable(false).setPositiveButton("save", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       if(arrcontacts.size() != 5) {
                           String update_name = Update_name.getText().toString();
                           String update_number = Update_number.getText().toString();
                           databaseclass.getDAO().addContact(new Model(update_name, update_number));
                           arrcontacts.add(new Model(update_name, update_number));
                           listviewAdapter.notifyDataSetChanged();

                           Toast.makeText(ContactBook.this, "save", Toast.LENGTH_SHORT).show();
                       }
                       else{
                           Toast.makeText(ContactBook.this, "YOU CAN ADD ONLY FIVE CONTACTS ", Toast.LENGTH_SHORT).show();
                       }
                       }
               }).setNegativeButton("back", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       Toast.makeText(ContactBook.this, "BACk", Toast.LENGTH_SHORT).show();
                   }
               });
               final AlertDialog alertDialog = builder.create();
               alertDialog.show();


           }
       });





       }


    }


