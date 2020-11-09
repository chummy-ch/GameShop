package com.example.gameshop;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

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
                + "password text);");

        db.execSQL("create table games ("
                + "game text PRIMARY KEY,"
                + "image text,"
                + "price int,"
                + "description text,"
                + "genres text,"
                + "sale int,"
                + "AgeLimit int"
                + ");");

        db.execSQL("create table transactions("
                +"id integer PRIMARY KEY AUTOINCREMENT,"
                + "game text,"
                + "user text,"
                + "price int,"
                + "date text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
