package com.example.womensafety;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database( entities = {Model.class} , exportSchema = false , version =  1)
public abstract class DATABASECLASS extends RoomDatabase {
    public abstract ModelDAO getDAO() ;

}