package com.example.womensafety;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public  interface ModelDAO  {
@Insert
    public void addContact(Model model);

@Delete
    public Void deleteContact(Model model);

@Update
    public Void updateContact(Model model);


    @Query("select *from contact")
    public List<Model> getContacts();

}
