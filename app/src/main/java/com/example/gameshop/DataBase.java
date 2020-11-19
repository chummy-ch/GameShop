package com.example.gameshop;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.gameshop.genrelActivity.Genres;

import java.util.function.BooleanSupplier;

public class DataBase extends SQLiteOpenHelper {
    public DataBase(@Nullable Context context, @Nullable String name) {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table users ("
                + "mail text PRIMARY KEY,"
                + "games text,"
                + "admin int,"
                + "age text,"
                + "password text,"
                + "regday text);");

        db.execSQL("create table games ("
                + "id integer PRIMARY KEY AUTOINCREMENT,"
                + "game text,"
                + "image text,"
                + "price int,"
                + "description text,"
                + "genres text,"
                + "sale int,"
                + "ageLimit int,"
                + "addedday text,"
                + "creater text,"
                + "addedby text"
                + ");");

        db.execSQL("create table transactions("
                +"id integer PRIMARY KEY AUTOINCREMENT,"
                + "game text,"
                + "user text,"
                + "price int,"
                + "date text);");

        db.execSQL("create table genres("
                    +"id integer primary key autoincrement,"
                    +"genre text,"
                    +"description text);");

        db.execSQL("create table views("
                    +"game text primary key,"
                    +"times integer);");

        db.execSQL("create table priceinfo("
                    + "id int primary key,"
                    + "fullprice int,"
                    + "sale int);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
