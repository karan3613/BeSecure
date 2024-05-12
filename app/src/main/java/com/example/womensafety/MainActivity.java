package com.example.womensafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.Manifest;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button cb, emergency;
    List<Model> calling;
    final int REQUEST_C0DE = 100;
    FusedLocationProviderClient locationProviderClient;
    TextView hold_call , help_needed ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cb = findViewById(R.id.contactbook);
        help_needed = findViewById(R.id.emergencytext);
        hold_call = findViewById(R.id.hold_call);
        // ACTION BAR
        ActionBar actionBar ;
        actionBar =getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#F03333"));
        actionBar.setBackgroundDrawable(colorDrawable);
        // DATABASE INTIALIZATION
        DATABASECLASS databaseclass = Room.databaseBuilder(
                        getApplicationContext(),
                        DATABASECLASS.class,
                        "ContactDB")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        // PERMISSIONS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.SEND_SMS, android.Manifest.permission.READ_CONTACTS}, 100);
            }
        }


        // CONTACT BOOK
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ContactBook.class);
                startActivity(i);
            }
        });

        //FUSED LO)CATION CLIENT MANAGER
        FusedLocationProviderClient locationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //  EMERGENCY BUTTON
        emergency = findViewById(R.id.emergency);
        emergency.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    AskPermissions();
                }

                Task<Location> location = locationProviderClient.getLastLocation();

                location.addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {

                            // get the SMSManager
                            SmsManager smsManager = SmsManager.getDefault();

                            // get the list of all the contacts in Database
                            calling = databaseclass.getDAO().getContacts();
                            // send SMS to each contact
                            for (Model c : calling) {
                                String message = "Hey, " + c.getName() + "I am in DANGER, i need help. Please urgently reach me out. Here are my coordinates.\n " + "http://maps.google.com/?q=" + location.getLatitude() + "," + location.getLongitude();
                                smsManager.sendTextMessage(c.getNumber(), null, message, null, null);
                            }
                        } else {
                            String message = "I am in DANGER, i need help. Please urgently reach me out.\n" + "GPS was turned off.Couldn't find location. Call your nearest Police Station.";
                            SmsManager smsManager = SmsManager.getDefault();
                            calling = databaseclass.getDAO().getContacts();
                            for (Model c : calling) {
                                smsManager.sendTextMessage(c.getNumber(), null, message, null, null);
                            }
                        }
                    }
                });

                location.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Check: ", "OnFailure");
                        String message = "I am in DANGER, i need help. Please urgently reach me out.\n" + "GPS was turned off.Couldn't find location. Call your nearest Police Station.";
                        SmsManager smsManager = SmsManager.getDefault();
                        calling = databaseclass.getDAO().getContacts();
                        for (Model c : calling) {
                            smsManager.sendTextMessage(c.getNumber(), null, message, null, null);
                        }
                    }
                });
                return false;
            }
        });

        SensorsService sensorsService = new SensorsService();
        Intent intent = new Intent(this, sensorsService.getClass());
        if (!isMyServiceRunning(sensorsService.getClass())){
            startService(intent);
        }
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (pm != null && !pm.isIgnoringBatteryOptimizations(getPackageName())) {
                askIgnoreOptimization();
            }
        }


    }



    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("Service status", "Running");
                return true;
            }
        }
        Log.i("Service status", "Not running");
        return false;
    }



    private void AskPermissions() {
        if(ContextCompat.checkSelfPermission(this , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this ,Manifest.permission.ACCESS_FINE_LOCATION)){
                new AlertDialog.Builder(this)
                        .setMessage("WE need permission for location")
                        .setTitle("PERMISSION")
                        .setCancelable(false).setPositiveButton(" OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MainActivity.this , new String[]{Manifest.permission.ACCESS_FINE_LOCATION} , REQUEST_C0DE);
                            }
                        })
                        .show();
            }
            else {
                ActivityCompat.requestPermissions(MainActivity.this , new String[]{Manifest.permission.ACCESS_FINE_LOCATION} , REQUEST_C0DE);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_C0DE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "this fine location permission is completely denied", Toast.LENGTH_SHORT).show();
            }
        }
}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, runServiceAgain.class);
        this.sendBroadcast(broadcastIntent);


    }
    private void askIgnoreOptimization() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            @SuppressLint("BatteryLife")
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + getPackageName()));
           startActivity(intent);
        }

    }
}
 