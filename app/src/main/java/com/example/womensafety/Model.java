package com.example.womensafety;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "contact")
public class Model {
    @ColumnInfo(name = "name")
    private  String name;
    @ColumnInfo(name = "number")
    private String number ;
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private  int id;

    @Ignore
    public Model(String name, String number, int id) {
        this.name = name;
        this.number = number;
        this.id = id;
    }

    public Model(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
